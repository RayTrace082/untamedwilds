package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ai.AmphibiousTransition;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartMeleeAttackGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.unique.HippoTerritorialityTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityHippo extends ComplexMobAmphibious {

    private static final ResourceLocation TEXTURE = new ResourceLocation("untamedwilds:textures/entity/hippo/common.png");
    private static final float SIZE = 1.1f;
    private static final String BREEDING = "EARLY_SUMMER";
    private static final int GESTATION = 8 * ConfigGamerules.cycleLength.get();
    private static final int GROWING = 12 * ConfigGamerules.cycleLength.get();
    private static final int RARITY = 5;

    public static Animation EAT;
    public static Animation IDLE_YAWN;
    public static Animation IDLE_LOOK;
    public static Animation ATTACK;
    public static Animation IDLE_TALK;
    public int angryProgress;

    public EntityHippo(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        IDLE_YAWN = Animation.create(36);
        IDLE_LOOK = Animation.create(128);
        IDLE_TALK = Animation.create(20);
        EAT = Animation.create(48);
        ATTACK = Animation.create(24);
        this.stepHeight = 1F;
        this.experienceValue = 10;
        this.ecoLevel = 8;
        this.isAmphibious = true;
        this.buoyancy = 0.998F;
        this.dexterity = 0.01F;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(2, new SmartMeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 0.8D));
        //this.goalSelector.addTask(5, new EntityAIGraze(this, 100));
        this.goalSelector.addGoal(4, new AmphibiousTransition(this, 1.1D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, 300, 0, false));
        this.goalSelector.addGoal(6, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(3, new HippoTerritorialityTargetGoal<>(this, LivingEntity.class, true, false, input -> !(input instanceof EntityHippo && getEcoLevel(input) > 5)));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(9.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
    }

    public boolean isActive() {
        if (this.forceSleep < 0) {
            return false;
        }
        float f = this.world.getCelestialAngle(0F);
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
            if (i <= 8 && !this.isInWater() && !this.isAngry() && !this.isSleeping() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(IDLE_YAWN);
            }
            if ((i > 8 && i <= 12) && !this.isAngry() && !this.isSleeping() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(IDLE_LOOK);
            }
            if (i == 13 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                this.setSitting(true);
            }
            if (i == 14 && this.isSitting()) {
                this.setSitting(false);
            }
            if (i == 15 && !this.isActive() && !this.isSleeping() && this.isInWater()){
                this.setAnimation(IDLE_YAWN);
                this.setSleeping(true);
                this.forceSleep = -800 - this.rand.nextInt(1200);
            }
            if (i > 2980 && !this.isChild()) {
                this.setAnimation(IDLE_TALK);
            }
            if (this.getAnimation() == ATTACK && this.getAttackTarget() != null && this.getBoundingBox().grow(1.2F, 1.0F, 1.2F).contains(this.getAttackTarget().getPositionVector()) && (this.getAnimationTick() > 8)) {
                LivingEntity target = this.getAttackTarget();
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue() * 1F);
                EntityUtils.destroyBoat(this.world, target);
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.getAnimation() != NO_ANIMATION) {
            if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
                this.playSound(ModSounds.ENTITY_HIPPO_AMBIENT, 1.5F, 0.8F);
            }
        }
        if (this.world.isRemote && this.isAngry() && this.angryProgress < 40) {
            this.angryProgress += 4;
        } else if (this.world.isRemote && !this.isAngry() && this.angryProgress > 0) {
            this.angryProgress -= 4;
        }
        super.livingTick();
    }

    @Override
    public boolean wantsToLeaveWater(){
        return this.isActive();
    }

    @Override
    public boolean wantsToEnterWater(){
        return !this.isActive();
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
        }
        return flag;
    }

    private Animation chooseAttackAnimation() {
        return ATTACK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (this.isChild()) {
            return ModSounds.ENTITY_BEAR_BABY_CRY;
        }
        return ModSounds.ENTITY_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() { return ModSounds.ENTITY_BEAR_DEATH; }

    public void breed() {
        for (int i = 0; i <= 1 + this.rand.nextInt(1); i++) {
            EntityHippo child = this.createChild(this);
            if (child != null) {
                child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
                if (this.getOwner() != null) {
                    child.setTamedBy((PlayerEntity) this.getOwner());
                }
                this.world.addEntity(child);
            }
        }
    }

    @Nullable
    public EntityHippo createChild(AgeableEntity ageable) {
        EntityHippo bear = new EntityHippo(ModEntity.HIPPO, this.world);
        bear.setGender(this.rand.nextInt(2));
        bear.setMobSize(this.rand.nextFloat());
        bear.setGrowingAge(this.getAdulthoodTime() * -2);
        bear.registerGoals();
        return bear;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return (stack.getItem() == Items.MELON);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, EAT, IDLE_YAWN, IDLE_LOOK, IDLE_TALK, ATTACK};
    }

    public Animation getAnimationEat() { return EAT; }

    public boolean isFavouriteFood(ItemStack stack) { return stack.getItem() == Items.MELON_SLICE; }
    public String getBreedingSeason() { return BREEDING; }
    public static int getRarity() { return RARITY; }
    public int getAdulthoodTime() { return GROWING; }
    public int getPregnancyTime() { return GESTATION; }
    public float getModelScale() { return SIZE; }
    public ResourceLocation getTexture() { return TEXTURE; }

    // Species available, referenced to properly distribute Bears in the world
    public enum SpeciesHippo implements IStringSerializable {

        COMMON		(ModEntity.HIPPO, 1, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.SAVANNA);

        public EntityType<? extends EntityHippo> type;
        public int rarity;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesHippo(EntityType<? extends EntityHippo> type, int rolls, BiomeDictionary.Type... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getName() {
            return "why would you do this?";
        }

        public static EntityType<? extends EntityHippo> getSpeciesByBiome(Biome biome) {
            List<EntityHippo.SpeciesHippo> types = new ArrayList<>();
            for (EntityHippo.SpeciesHippo type : values()) {
                for(BiomeDictionary.Type biomeTypes : type.spawnBiomes) {
                    if(BiomeDictionary.hasType(biome, biomeTypes)){
                        for (int i = 0; i < type.rarity; i++) {
                            types.add(type);
                        }
                    }
                }
            }
            if (types.isEmpty()) {
                return null;
            }
            return types.get(new Random().nextInt(types.size())).type;
        }
    }
}
