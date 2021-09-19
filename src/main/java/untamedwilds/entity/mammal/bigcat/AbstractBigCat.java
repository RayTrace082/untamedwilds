package untamedwilds.entity.mammal.bigcat;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ai.SmartFollowOwnerGoal;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractBigCat extends ComplexMobTerrestrial {

    public static Animation ATTACK_BITE;
    public static Animation ATTACK_MAUL;
    public static Animation ATTACK_POUNCE;
    public static Animation ANIMATION_ROAR;
    public static Animation ANIMATION_EAT;
    public static Animation IDLE_TALK;
    public static Animation IDLE_STRETCH;
    public int aggroProgress;

    public AbstractBigCat(EntityType<? extends AbstractBigCat> type, World worldIn) {
        super(type, worldIn);
        ATTACK_POUNCE = Animation.create(42);
        ATTACK_MAUL = Animation.create(22);
        IDLE_TALK = Animation.create(20);
        IDLE_STRETCH = Animation.create(110);
        this.stepHeight = 1;
        this.experienceValue = 10;
        this.turn_speed = 0.1F;
    }

    public boolean isPushedByWater() {
        return false;
    }

    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);
    }

    public void livingTick() {
        if (!this.world.isRemote) {
            if (this.ticksExisted % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-3);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }
            // Angry Sleepers
            if (ConfigGamerules.angrySleepers.get() && !this.isTamed() && this.isSleeping() && this.forceSleep == 0) {
                List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                if (!list.isEmpty()) {
                    PlayerEntity player = list.get(0);
                    if (!player.isSneaking() && !player.isCreative()) {
                        this.setSleeping(false);
                        this.setAttackTarget(player);
                        this.forceSleep = -300;
                    }
                }
            }
            if (this.ticksExisted % 200 == 0) {
                if (!this.isActive() && this.getNavigator().noPath()) {
                    this.tiredCounter++;
                    if (this.getDistanceSq(this.getHomeAsVec()) <= 6) {
                        this.setSleeping(true);
                        this.tiredCounter = 0;
                    }
                    else if (tiredCounter >= 3) {
                        this.setHome(BlockPos.ZERO);
                        this.tiredCounter = 0;
                    }
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
                    if (i == 2 && this.canMove() && !this.isInWater() && !this.isChild()) {
                        this.getNavigator().clearPath();
                        this.setAnimation(IDLE_STRETCH);
                    }
                    if (i > 2980 && !this.isInWater() && !this.isChild()) {
                        this.setAnimation(IDLE_TALK);
                    }
                }
            }
            if (this.ticksExisted % 80 == 2 && this.getAttackTarget() != null && this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_ROAR);
            }
            if (this.getAnimation() == ATTACK_POUNCE && this.getAnimationTick() == 10) {
                this.getMoveHelper().strafe(2F, 0);
                this.getJumpController().setJumping();
            }
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
            this.playSound(SoundEvents.ENTITY_HORSE_EAT,1.5F, 0.8F);
        }
        if (this.getAnimation() == ATTACK_MAUL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,1.5F, 0.8F);
        }
        if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
            SoundEvent sound = this instanceof EntityPuma ? SoundEvents.ENTITY_OCELOT_AMBIENT : ModSounds.ENTITY_BIG_CAT_AMBIENT;
            this.playSound(sound, 1F, 1);
        }
        if (this.world.isRemote && this.isAngry() && this.aggroProgress < 40) {
            this.aggroProgress++;
        } else if (this.world.isRemote && !this.isAngry() && this.aggroProgress > 0) {
            this.aggroProgress--;
        }
        super.livingTick();
    }

    public double getMountedYOffset() { return this.getModelScale() + 0.5f * this.getMobSize(); }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void setupTamedAI() {
        if (this.isTamed()) {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Updating AI tasks for tamed mob");
            }
            this.goalSelector.addGoal(3, new SmartFollowOwnerGoal(this, 2.3D, 12.0F, 3.0F));
            this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new SmartOwnerHurtTargetGoal(this));
        }
    }

    protected SoundEvent getAmbientSound() {
        return !this.isChild() ? null : SoundEvents.ENTITY_OCELOT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isChild() ?  SoundEvents.ENTITY_OCELOT_HURT : ModSounds.ENTITY_BIG_CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isChild() ? SoundEvents.ENTITY_OCELOT_DEATH : ModSounds.ENTITY_BIG_CAT_DEATH;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {

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

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation(entityIn);
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    private Animation chooseAttackAnimation(Entity target) {
        if (target.getHeight() < this.getHeight()) {
            return ATTACK_MAUL;
        }
        return ATTACK_POUNCE;
    }

    //public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_POUNCE, ATTACK_MAUL, IDLE_TALK, IDLE_STRETCH};
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.9F;
    }

    public enum SpeciesBigCat implements IStringSerializable {

        CAVE_LION	(ModEntity.CAVE_LION, EntityCaveLion.getRarity(), Biome.Category.TAIGA, Biome.Category.ICY),
        JAGUAR		(ModEntity.JAGUAR, EntityJaguar.getRarity(), Biome.Category.JUNGLE),
        LEOPARD		(ModEntity.LEOPARD, EntityLeopard.getRarity(), Biome.Category.SAVANNA, Biome.Category.TAIGA),
        LION		(ModEntity.LION, EntityLion.getRarity(), Biome.Category.SAVANNA),
        MARSUPIAL_LION(ModEntity.MARSUPIAL_LION, EntityMarsupialLion.getRarity(), Biome.Category.JUNGLE),
        PUMA		(ModEntity.PUMA, EntityPuma.getRarity(), Biome.Category.MESA, Biome.Category.FOREST, Biome.Category.TAIGA),
        SABERTOOTH  (ModEntity.SABERTOOTH, EntitySabertooth.getRarity(), Biome.Category.TAIGA),
        SNOW_LEOPARD(ModEntity.SNOW_LEOPARD, EntitySnowLeopard.getRarity(), Biome.Category.ICY, Biome.Category.TAIGA),
        TIGER		(ModEntity.TIGER, EntityTiger.getRarity(), Biome.Category.JUNGLE, Biome.Category.TAIGA);

        public EntityType<? extends AbstractBigCat> type;
        public int rarity;
        public Biome.Category[] spawnBiomes;

        SpeciesBigCat(EntityType<? extends AbstractBigCat> type, int rolls, Biome.Category... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getString() { return "why would you do this?"; }

        public static EntityType<? extends AbstractBigCat> getSpeciesByBiome(Biome biome) {
            List<AbstractBigCat.SpeciesBigCat> types = new ArrayList<>();
            for (AbstractBigCat.SpeciesBigCat type : values()) {
                for(Biome.Category biomeTypes : type.spawnBiomes) {
                    if(biome.getCategory() == biomeTypes){
                        for (int i=0; i < type.rarity; i++) {
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