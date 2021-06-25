package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import untamedwilds.entity.ai.target.HuntPackMobTarget;
import untamedwilds.entity.ai.target.HurtPackByTargetGoal;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.entity.mammal.bigcat.EntityLion;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityHyena extends ComplexMobTerrestrial implements INewSkins, ISpecies, IPackEntity, INeedsPostUpdate {

    public static Animation ATTACK_POUNCE;
    public static Animation IDLE_TALK;
    public static Animation ATTACK_BITE;

    private static final String BREEDING = "MID_WET";
    private static final int GESTATION = 4 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();

    public EntityHyena(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        IDLE_TALK = Animation.create(20);
        ATTACK_POUNCE = Animation.create(42);
        ATTACK_BITE = Animation.create(15);
        this.stepHeight = 1F;
        this.experienceValue = 10;
        this.turn_speed = 0.1F;
    }

    protected void registerData() {
        super.registerData();
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesHyena.values().length; i++) {
            EntityUtils.buildSkinArrays("hyena", SpeciesHyena.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new SmartSwimGoal(this));
        this.goalSelector.addGoal(2, new FindItemsGoal(this, 12, true));
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.8D, false, 1, false));
        this.goalSelector.addGoal(3, new SmartAvoidGoal<>(this, LivingEntity.class, 16, 1.2D, 1.6D, input -> getEcoLevel(input) > 6));
        this.goalSelector.addGoal(4, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new GotoSleepGoal(this, 1D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new SmartLookAtGoal(this, LivingEntity.class, 10.0F));
        this.targetSelector.addGoal(1, new HurtPackByTargetGoal(this).setCallsForHelp(EntityHyena.class));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof EntityLion)));
        this.targetSelector.addGoal(3, new HuntPackMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 6));
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
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2);
    }

    public boolean wantsToBreed() {
        if (ConfigGamerules.naturalBreeding.get() && this.growingAge == 0) {
            return this.getHunger() >= 80;
        }
        return false;
    }

    @Override
    public void livingTick() {
        if (this.herd == null) {
            IPackEntity.initPack(this);
        }
        else {
            this.herd.tick();
        }
        if (!this.world.isRemote) {
            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-10);
                if (!this.isStarving()) {
                    this.heal(1.0F);
                }
            }
            // Random idle animations
            if (this.getAnimation() == NO_ANIMATION && this.getAttackTarget() == null && !this.isSleeping()) {
                if (this.getCommandInt() == 0) {
                    int i = this.rand.nextInt(3000);
                    if (i == 0 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.isActive()) {
                        this.getNavigator().clearPath();
                        this.setSitting(true);
                    }
                    if ((i == 1 || this.isInWater()) && this.isSitting() && this.getCommandInt() < 2) {
                        this.setSitting(false);
                    }
                    if (i > 2980 && !this.isInWater() && !this.isChild()) {
                        this.setAnimation(IDLE_TALK);
                    }
                }
            }
            this.setAngry(this.getAttackTarget() != null);
            if (this.getAnimation() == ATTACK_POUNCE && this.getAnimationTick() == 10) {
                this.getMoveHelper().strafe(2F, 0);
                this.getJumpController().setJumping();
            }
            if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
                this.playSound(ModSounds.ENTITY_HYENA_AMBIENT, 1, 1);
            }
            if (this.getAttackTarget() != null && this.ticksExisted % 120 == 0) {
                if (this.getVariant() == 2) {
                    this.playSound(ModSounds.ENTITY_HYENA_LAUGHING, 1.5F, 1);
                }
                else {
                    this.playSound(ModSounds.ENTITY_HYENA_GROWL, 1F, 1);
                }
            }
        }
        if (this.getAnimation() != NO_ANIMATION) {
            if (this.getAnimation() == ATTACK_BITE && this.getAnimationTick() == 6) {
                this.playSound(ModSounds.ENTITY_ATTACK_BITE, 1.5F, 0.8F);
            }
        }
        super.livingTick();
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
        }
        return flag;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    private Animation chooseAttackAnimation() {
        switch (this.rand.nextInt(4)) {
            case 0: return ATTACK_POUNCE;
            case 1: return ATTACK_POUNCE;
            default: return ATTACK_BITE;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_HYENA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() { return ModSounds.ENTITY_HYENA_DEATH; }

    @Nullable
    public EntityHyena func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
        return create_offspring(new EntityHyena(ModEntity.HYENA, this.world));
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {
            if (itemstack.getItem() == Items.BLAZE_ROD) { // DEBUG
                this.setAnimation(ATTACK_BITE);
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

    public boolean isBreedingItem(ItemStack stack) {
        return (stack.getItem() == Items.ROTTEN_FLESH);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_POUNCE, ATTACK_BITE, IDLE_TALK};
    }

    public Animation getAnimationEat() { return NO_ANIMATION; }

    protected activityType getActivityType() { return activityType.NOCTURNAL; }
    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.ROTTEN_FLESH; }
    public String getBreedingSeason() { return BREEDING; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SpeciesHyena.values()[this.getVariant()].size; }
    protected int getOffspring() { return 2; }
    public int getMaxPackSize() { return SpeciesHyena.values()[this.getVariant()].packSize; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || reason == SpawnReason.SPAWN_EGG) {
            return this.rand.nextInt(SpeciesHyena.values().length);
        }
        return SpeciesHyena.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.hyena_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesHyena.values()[i].name().toLowerCase(); }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(SpeciesHyena.values()[this.getVariant()].attack);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(SpeciesHyena.values()[this.getVariant()].health);
        this.setHealth(this.getMaxHealth());
    }

    // Species available, referenced to properly distribute Hyenas in the world
    public enum SpeciesHyena implements IStringSerializable {

        AARDWOlF	(0, 0.8F,3, 1, 4, 10, Biome.Category.SAVANNA),
        BROWN		(1, 0.8F, 2, 6, 4, 14, Biome.Category.SAVANNA),
        SPOTTED		(2, 1.0F, 5, 20, 5, 20, Biome.Category.SAVANNA, Biome.Category.MESA),
        STRIPED 	(3, 0.9F, 3, 1, 4, 16, Biome.Category.SAVANNA, Biome.Category.MESA),
        SHORTFACE   (4, 1.2F, 1, 4, 5, 24, Biome.Category.SAVANNA);

        public int species;
        public float size;
        public int rarity;
        public float attack;
        public float health;
        public int packSize;
        public Biome.Category[] spawnBiomes;

        SpeciesHyena(int species, float size, int rolls, int packSize, int attack, int health, Biome.Category... biomes) {
            this.species = species;
            this.size = size;
            this.rarity = rolls;
            this.packSize = packSize;
            this.attack = (float)attack;
            this.health = (float)health;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.hyena." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesHyena> types = new ArrayList<>();
            for (SpeciesHyena type : values()) {
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
