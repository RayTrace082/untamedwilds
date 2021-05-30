package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityRhino extends ComplexMobTerrestrial implements INewSkins, ISpecies, INeedsPostUpdate {

    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GESTATION = 14 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 20 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 5;
    private static final DataParameter<Boolean> CHARGING = EntityDataManager.createKey(EntityRhino.class, DataSerializers.BOOLEAN);

    public static Animation ATTACK_THREATEN;
    public static Animation ATTACK_GORE;

    public EntityRhino(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        ATTACK_THREATEN = Animation.create(50);
        ATTACK_GORE = Animation.create(14);
        this.stepHeight = 1F;
        this.experienceValue = 10;
        this.turn_speed = 0.2F;
    }

    protected void registerData() {
        super.registerData();
        dataManager.register(CHARGING, false);
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesRhino.values().length; i++) {
            EntityUtils.buildSkinArrays("rhino", SpeciesRhino.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackCharger(this, 1.4F, 3));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.6D, false));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new GrazeGoal(this, 10));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 120, 0, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityRhino) && getEcoLevel(input) > 5));
    }

    @Override
    protected void setupTamedAI() {
        if (this.isTamed()) {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Updating AI tasks for tamed mob");
            }
            this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 1.3D, 12.0F, 3.0F));
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        }
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 60.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 6D);
    }

    public boolean isActive() {
        super.isActive();
        float f = this.world.getCelestialAngleRadians(0F);
        return (f > 0.21F && f < 0.78F);
    }

    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.growingAge == 0) {
            return this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void livingTick() {
        if (!this.world.isRemote) {
            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            int i = this.rand.nextInt(3000);
            if (i == 13 && !this.isInWater() && this.getAttackTarget() == null && this.isNotMoving() && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                this.setSitting(true);
            }
            if (i == 14 && this.isSitting()) {
                this.setSitting(false);
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        else {
            if (this.getAnimation() == ATTACK_THREATEN) {
                this.setSprinting(this.getAnimationTick() % 18 < 6);
            }
        }
        super.livingTick();
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
            if (!this.isCharging()) {
                this.playSound(SoundEvents.ENTITY_ZOGLIN_ATTACK, 1.0F, this.getSoundPitch());
                IFlinging.func_234403_a_(this, (LivingEntity)entityIn);
            }
        }
        return flag;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_RAVAGER_STEP, 0.15F, 1.0F);
    }

    private Animation chooseAttackAnimation() {
        return ATTACK_GORE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_COW_DEATH; }

    @Nullable
    public EntityRhino func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityRhino(ModEntity.RHINO, this.world));
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {
            if (this.isTamed() && this.getOwner() == player) {
                if (itemstack.isEmpty()) {
                    this.setCommandInt(this.getCommandInt() + 1);
                    player.sendMessage(new TranslationTextComponent("entity.untamedwilds.command." + this.getCommandInt()), Util.DUMMY_UUID);
                    if (this.getCommandInt() > 1) {
                        this.getNavigator().clearPath();
                        this.setSitting(true);
                    } else if (this.getCommandInt() <= 1 && this.isSitting()) {
                        this.setSitting(false);
                    }
                }
                EntityUtils.consumeItemStack(this, itemstack);
            }
            if (!this.isTamed() && this.isChild() && EntityUtils.hasFullHealth(this) && this.isFavouriteFood(itemstack)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
                if (this.getRNG().nextInt(3) == 0) {
                    this.setTamedBy(player);
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
        }

        return super.func_230254_b_(player, hand);
    }

    public boolean isCharging() {
        return dataManager.get(CHARGING);
    }

    public void setCharging(boolean bool) {
        dataManager.set(CHARGING, bool);
    }

    public boolean isBreedingItem(ItemStack stack) {
        return (stack.getItem() == Items.MELON);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_THREATEN, ATTACK_GORE};
    }

    public Animation getAnimationEat() { return NO_ANIMATION; }

    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.MELON_SLICE; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SpeciesRhino.values()[this.getVariant()].size; }
    protected int getOffspring() { return 1; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG) {
            return this.rand.nextInt(SpeciesRhino.values().length);
        }
        return SpeciesRhino.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.rhino_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesRhino.values()[i].name().toLowerCase(); }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(SpeciesRhino.values()[this.getVariant()].attack);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(SpeciesRhino.values()[this.getVariant()].health);
        this.setHealth(this.getMaxHealth());
    }

    // Species available, referenced to properly distribute Rhinoceroses in the world
    public enum SpeciesRhino implements IStringSerializable {

        BLACK		(0, 0.9F,5, 8, 50, Biome.Category.SAVANNA),
        INDIAN		(1, 1.1F, 4, 8, 60, Biome.Category.JUNGLE, Biome.Category.EXTREME_HILLS),
        JAVAN		(2, 0.8F, 1, 6, 45, Biome.Category.JUNGLE),
        SUMATRAN	(3, 0.8F, 1, 5, 30, Biome.Category.JUNGLE),
        WHITE		(4, 1.0F, 2, 10, 60, Biome.Category.SWAMP, Biome.Category.SAVANNA),
        WOOLY		(5, 1.2F, ConfigGamerules.extinctMobs.get() ? 1 : 0, 10, 60, Biome.Category.ICY, Biome.Category.TAIGA);

        public int species;
        public float size;
        public int rarity;
        public float attack;
        public float health;
        public Biome.Category[] spawnBiomes;

        SpeciesRhino(int species, float size, int rolls, int attack, int health, Biome.Category... biomes) {
            this.species = species;
            this.size = size;
            this.rarity = rolls;
            this.attack = (float)attack;
            this.health = (float)health;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.rhino." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<EntityRhino.SpeciesRhino> types = new ArrayList<>();
            for (EntityRhino.SpeciesRhino type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
                        for (int i=0; i < type.rarity; i++) {
                            types.add(type);
                        }
                    }
                }
            }
            if (types.isEmpty()) {
                return 99;
            } else {
                return types.get(new Random().nextInt(types.size())).getSpecies();
            }
        }
    }
}
