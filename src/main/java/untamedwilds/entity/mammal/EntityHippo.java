package untamedwilds.entity.mammal;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAmphibious;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.target.ProtectChildrenTarget;
import untamedwilds.entity.mammal.bear.BlackBear;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;

public class EntityHippo extends ComplexMobAmphibious {

    public static Animation EAT;
    public static Animation IDLE_YAWN;
    public static Animation IDLE_LOOK;
    public static Animation ATTACK;
    public int angryProgress;

    public EntityHippo(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        IDLE_YAWN = Animation.create(36);
        IDLE_LOOK = Animation.create(128);
        EAT = Animation.create(48);
        ATTACK = Animation.create(24);
        this.stepHeight = 1F;
        this.experienceValue = 10;
        this.ecoLevel = 8;
        this.isAmphibious = true;
        this.buoyancy = 0.98F;
    }

    public void initEntityAI() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.3D, false));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 0.8D));
        //this.goalSelector.addTask(5, new EntityAIGraze(this, 100));
        //this.goalSelector.addTask(6, new EntityAIGetInWater(this, 1.1D));
        //this.goalSelector.addTask(6, new EntityAIGetInLand(this, 1.1D));
        this.goalSelector.addGoal(5, new SmartWanderGoal(this, 1D, true));
        this.goalSelector.addGoal(6, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(2, new ProtectChildrenTarget<>(this, LivingEntity.class, 0, true, true, input -> !(input instanceof BlackBear)));
        //this.targetSelector.addTask(3, new EntityAITargetHippoTerritoriality<>(this, EntityLivingBase.class, true, false, input -> !(input instanceof EntityHippo)));
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
            /*if (CompatBridge.SereneSeasons) {
                return (CompatSereneSeasons.isCurrentSeason(this.world, SpeciesHippo.values()[this.getSpecies()].breedingSeason));
            }*/
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
            if (i <= 5 && !this.isInWater() && !this.isAngry() && !this.isSleeping() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(IDLE_YAWN);
            }
            if ((i > 5 && i <= 10) && !this.isAngry() && !this.isSleeping() && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(IDLE_LOOK);
            }
            if (i == 11 && !this.isInWater() && this.isNotMoving() && this.canMove() && this.getAnimation() == NO_ANIMATION) {
                this.setSitting(true);
            }
            if (i == 12 && this.isSitting()) {
                this.setSitting(false);
            }
            if (i == 13 && !this.isActive() && !this.isSleeping() && this.isInWater()){
                this.setAnimation(IDLE_YAWN);
                this.setSleeping(true);
                this.forceSleep = -800 - this.rand.nextInt(1200);
            }
            if (this.getAnimation() == ATTACK && this.getAttackTarget() != null && this.getBoundingBox().grow(1.2F, 1.0F, 1.2F).contains(this.getAttackTarget().getPositionVector()) && (this.getAnimationTick() > 8)) {
                LivingEntity target = this.getAttackTarget();
                //if (this.getAttackTarget().isActiveItemStackBlocking()) this.getAttackTarget().getActiveItemStack().damageItem(40, this.getAttackTarget());
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getValue() * 1F);
                if (target.getRidingEntity() != null && target.getRidingEntity() instanceof BoatEntity) {
                    BoatEntity boat = (BoatEntity) target.getRidingEntity();
                    boat.remove();
                    if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                        for(int j = 0; j < 3; ++j) {
                            boat.entityDropItem(boat.getBoatType().asPlank());
                        }
                        for(int k = 0; k < 2; ++k) {
                            boat.entityDropItem(Items.STICK);
                        }
                    }
                }
            }
            this.setAngry(this.getAttackTarget() != null);
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

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            this.setAnimation(ATTACK);
            return true;
        }
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isChild()) {
            return ModSounds.ENTITY_BEAR_BABY_AMBIENT;
        }
        return ModSounds.ENTITY_BEAR_AMBIENT;
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

    //public int getPregnancyTime() { return Config.HCBreeding ? SpeciesHippo.values()[this.getSpecies()].pregnancyDuration * EntityHelper.getTicksInMonth(world) : 84000; }

    public void breed() {
        for (int i = 0; i <= 1; i++) {
            BlackBear child = this.createChild(this);
            child.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
            this.world.addEntity(child);
        }
    }

    public BlackBear createChild(AgeableEntity ageable) {
        BlackBear hippo = ModEntity.BLACK_BEAR.create(this.world);
        hippo.setGender(this.rand.nextInt(2));
        hippo.setMobSize(this.rand.nextFloat());
        hippo.setGrowingAge(this.getAdulthoodTime() * -1);
        return hippo;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return (stack.getItem() == Items.GLISTERING_MELON_SLICE);
    }

    /*public int setSpeciesByBiome(Biome biome) {
        if (Config.doRandom) {
            return this.rand.nextInt(SpeciesHippo.values().length);
        }
        return SpeciesHippo.getSpeciesByBiome(biome);
    }*/

    /*@Override
    public double swimSpeed() {
        if (this.getAttackTarget() != null) {
            return 0.3;
        }
        return 0.15;
    }*/

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, EAT, IDLE_YAWN, IDLE_LOOK, ATTACK};
    }

    public Animation getAnimationEat() { return EAT; }
}
