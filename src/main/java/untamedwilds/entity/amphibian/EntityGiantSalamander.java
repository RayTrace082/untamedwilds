package untamedwilds.entity.amphibian;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.*;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModItems;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityGiantSalamander extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins {

    public static Animation ATTACK_SWALLOW;
    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GROWING = 6 * ConfigGamerules.cycleLength.get();
    public int swimProgress;

    public EntityGiantSalamander(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new ComplexMobAquatic.MoveHelperController(this, 0.6F);
        ATTACK_SWALLOW = Animation.create(15);
        this.experienceValue = 1;
        this.swimSpeedMult = 3;
        this.buoyancy = 0.992F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.16D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 8.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 0.7D));
        this.goalSelector.addGoal(2, new SmartAvoidGoal<>(this, LivingEntity.class, (float)this.getAttributeValue(Attributes.FOLLOW_RANGE), 1D, 1.1D, input -> getEcoLevel(input) > 6));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 600));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, false, input -> getEcoLevel(input) < 6));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesGiantSalamander.values().length; i++) {
            EntityUtils.buildSkinArrays("giant_salamander", SpeciesGiantSalamander.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public boolean wantsToLeaveWater() { return this.world.isRainingAt(this.getPosition()); }

    public boolean wantsToEnterWater() { return true; }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        super.livingTick();

        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.wantsToBreed() && !this.isMale()) {
                    this.breed();
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }
        else {
            if (this.isInWater() && !this.isOnGround() && this.swimProgress < 20) {
                this.swimProgress++;
            } else if ((!this.isInWater() || this.isOnGround()) && this.swimProgress > 0) {
                this.swimProgress--;
            }
        }
    }

    /* Breeding conditions for the giant salamander are:
     * A nearby giant salamander of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityGiantSalamander> list = this.world.getEntitiesWithinAABB(EntityGiantSalamander.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setGrowingAge(GROWING);
                    list.get(0).setGrowingAge(GROWING);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (entityIn instanceof LivingEntity && entityIn.getWidth() * entityIn.getHeight() < 0.4F && (entityIn instanceof TameableEntity && !((TameableEntity) entityIn).isTamed())) {
                this.setAnimation(ATTACK_SWALLOW);
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
                EntityUtils.spawnParticlesOnEntity(this.world, (LivingEntity)entityIn, ParticleTypes.POOF, 6, 2);
                this.setMotion(new Vector3d(entityIn.getPosX() - this.getPosX(), entityIn.getPosY() - this.getPosY(), entityIn.getPosZ() - this.getPosZ()).scale(0.15F));
                this.peacefulTicks = 12000;
                entityIn.remove();
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        EntityUtils.dropEggs(this, "egg_giant_salamander_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 5);
        return null;
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND) {
            if (itemstack.getItem().equals(Items.WATER_BUCKET) && this.isAlive()) {
                EntityUtils.mutateEntityIntoItem(this, player, hand, "bucket_giant_salamander_" + getRawSpeciesName(this.getVariant()).toLowerCase(), itemstack);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return super.func_230254_b_(player, hand);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        SoundEvent soundevent = this.isChild() ? SoundEvents.ENTITY_TURTLE_SHAMBLE_BABY : SoundEvents.ENTITY_TURTLE_SHAMBLE;
        this.playSound(soundevent, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_TURTLE_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_TURTLE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_TURTLE_DEATH;
    }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == ModItems.MEAT_TURTLE_RAW.get(); }

    public Animation[] getAnimations() { return new Animation[]{NO_ANIMATION, ATTACK_SWALLOW}; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomeKey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(SpeciesGiantSalamander.values().length);
        }
        return SpeciesGiantSalamander.getSpeciesByBiome(biome);
    }

    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.giant_salamander" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesGiantSalamander.values()[i].name().toLowerCase(); }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(SpeciesGiantSalamander.values()[this.getVariant()].attack);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(SpeciesGiantSalamander.values()[this.getVariant()].health);
        this.setHealth(this.getMaxHealth());
    }

    public enum SpeciesGiantSalamander implements IStringSerializable {

        CHINESE			(0, 1.2F, 2, 3, 14, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS),
        HELLBENDER		(1, 0.8F, 5, 1, 6, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS),
        JAPANESE	    (2, 1.0F, 3, 2, 10, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS);

        public Float scale;
        public int species;
        public int rolls;
        public float attack;
        public float health;
        public Biome.Category[] spawnBiomes;

        SpeciesGiantSalamander(int species, Float scale, int rolls, int attack, int health, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.attack = (float)attack;
            this.health = (float)health;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.giant_salamander." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesGiantSalamander> types = new ArrayList<>();
            for (SpeciesGiantSalamander type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
                        for (int i=0; i < type.rolls; i++) {
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
