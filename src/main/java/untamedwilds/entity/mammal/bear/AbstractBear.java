package untamedwilds.entity.mammal.bear;

import com.github.alexthe666.citadel.animation.Animation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import java.util.*;

public abstract class AbstractBear extends ComplexMobTerrestrial {

    public static Animation ATTACK_BITE;
    public static Animation ATTACK_MAUL;
    public static Animation ATTACK_SWIPE;
    public static Animation ATTACK_POUND;
    public static Animation ANIMATION_ROAR;
    public static Animation IDLE_STAND;
    public static Animation IDLE_TALK;
    public static Animation ANIMATION_EAT;

    AbstractBear(EntityType<? extends AbstractBear> type, World worldIn) {
        super(type, worldIn);
        ANIMATION_ROAR = Animation.create(50);
        IDLE_TALK = Animation.create(20);
        IDLE_STAND = Animation.create(148);
        ANIMATION_EAT = Animation.create(104);
        ATTACK_BITE = Animation.create(18);
        ATTACK_MAUL = Animation.create(76);
        ATTACK_SWIPE = Animation.create(26);
        ATTACK_POUND = Animation.create(28);
        this.stepHeight = 1;
        this.turn_speed = 0.3F;
        this.experienceValue = 10;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        if (!this.world.isRemote) {
            if (this.ticksExisted % 600 == 0) {
                if (this.wantsToBreed()) {
                    this.setInLove(null);
                }
            }
            if (this.ticksExisted % 1000 == 0) {
                this.addHunger(-2);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }
            // Bearserk
            if (this.getHealth() < this.getMaxHealth() / 2) {
                this.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1200, 0, true, true));
                this.forceSleep = -1200;
            }
            // Angry sleepers
            if (!this.isTamed() && this.isSleeping() && this.forceSleep == 0) {
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
            /*// Crop tramplers
            if (!this.isTamed() && this.collidedHorizontally && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                boolean flag = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(0.2D);

                for(BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.world.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock) {
                        flag = this.world.func_225521_a_(blockpos, true, this) || flag;
                    }
                }

                if (!flag && this.onGround) {
                    this.jump();
                }
            }*/
            this.setAngry(this.getAttackTarget() != null);
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
                        if (!this.isSitting()) {
                            this.setAnimation(IDLE_STAND);
                        }
                    }
                    if (i > 2980 && !this.isInWater() && !this.isChild()) {
                        this.setAnimation(IDLE_TALK);
                    }
                }
            }
        }
        if (this.ticksExisted % 100 == 0 && this.getAttackTarget() != null && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() != NO_ANIMATION) {
            if (this.getAnimation() == ATTACK_BITE && this.getAnimationTick() == 1) {
                this.playSound(ModSounds.ENTITY_ATTACK_BITE, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_SWIPE && this.getAnimationTick() == 8) {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_POUND && this.getAnimationTick() == 10) {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 7) {
                this.playSound(ModSounds.ENTITY_BEAR_WARNING, 1.5F, 1);
            }
            if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1) {
                this.playSound(ModSounds.ENTITY_BEAR_AMBIENT, 1.5F, 1);
            }
            if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
            }
        }
        super.livingTick();
    }

    public double getMountedYOffset() { return getModelScale() + 0.5f * this.getMobSize(); }

    protected SoundEvent getAmbientSound() {
        return !this.isChild() ? null : ModSounds.ENTITY_BEAR_BABY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return !this.isChild() ? ModSounds.ENTITY_BEAR_HURT : ModSounds.ENTITY_BEAR_BABY_CRY;
    }

    protected SoundEvent getDeathSound() { return ModSounds.ENTITY_BEAR_DEATH; }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation();
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source == DamageSource.SWEET_BERRY_BUSH;
    }

    private Animation chooseAttackAnimation() {
        switch (this.rand.nextInt(3)) {
            case 0: return ATTACK_SWIPE;
            case 1: return ATTACK_BITE;
            default: return ATTACK_POUND;
        }
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.85F;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) {
            if (this.isTamed() && this.getOwner() == player) {
                if (itemstack.isEmpty()) {
                    this.setCommandInt(this.getCommandInt() + 1);
                    player.sendMessage(new TranslationTextComponent("entity.untamedwilds.command." + this.getCommandInt()), null);
                    if (this.getCommandInt() > 1) {
                        this.getNavigator().clearPath();
                        this.setSitting(true);
                    } else if (this.getCommandInt() <= 1 && this.isSitting()) {
                        this.setSitting(false);
                    }
                }
                if (itemstack.isFood()) {
                    this.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1, 1);
                    this.addHunger((itemstack.getItem().getFood().getHealing() * 10 * itemstack.getCount()));
                    for (Pair<EffectInstance, Float> pair : itemstack.getItem().getFood().getEffects()) {
                        if (pair.getFirst() != null && this.world.rand.nextFloat() < pair.getSecond()) {
                            this.addPotionEffect(new EffectInstance(pair.getFirst()));
                        }
                    }
                }
                else if (itemstack.hasEffect()) {
                    this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1, 1);
                    this.addHunger(10);
                    for(EffectInstance effectinstance : PotionUtils.getEffectsFromStack(itemstack)) {
                        if (effectinstance.getPotion().isInstant()) {
                            effectinstance.getPotion().affectEntity(this.getOwner(), this.getOwner(), this, effectinstance.getAmplifier(), 1.0D);
                        } else {
                            this.addPotionEffect(new EffectInstance(effectinstance));
                        }
                    }
                }
            }
            if (!this.isTamed() && this.isChild() && this.getHealth() == this.getMaxHealth() && this.isFavouriteFood(itemstack)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
                if (this.getRNG().nextInt(3) == 0) {
                    // Advancement Trigger: "Bear Force One"
                    this.setTamedBy(player);
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
        }

        return super.func_230254_b_(player, hand);
    }

    public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_ROAR, IDLE_STAND, IDLE_TALK, ANIMATION_EAT, ATTACK_MAUL, ATTACK_BITE, ATTACK_SWIPE, ATTACK_POUND};
    }

    // Model Parameters
    public boolean hasHump() { return false; }
    public boolean hasShortSnout() { return false; }

    // Species available, referenced to properly distribute Bears in the world
    public enum SpeciesBear implements IStringSerializable {

        BLACK		(ModEntity.BLACK_BEAR, EntityBlackBear.getRarity(), Biome.Category.FOREST, Biome.Category.TAIGA),
        BLIND		(ModEntity.BLIND_BEAR, EntityBlindBear.getRarity()),
        BROWN		(ModEntity.BROWN_BEAR, EntityBrownBear.getRarity(), Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS),
        CAVE		(ModEntity.CAVE_BEAR, EntityCaveBear.getRarity(), Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS),
        PANDA		(ModEntity.PANDA_BEAR, EntityGiantPanda.getRarity(), Biome.Category.JUNGLE),
        POLAR		(ModEntity.POLAR_BEAR, EntityPolarBear.getRarity(), Biome.Category.ICY),
        SPECTACLED	(ModEntity.SPECTACLED_BEAR, EntitySpectacledBear.getRarity(), Biome.Category.EXTREME_HILLS),
        SUN 		(ModEntity.SUN_BEAR, EntitySunBear.getRarity(), Biome.Category.JUNGLE);
        public EntityType<? extends AbstractBear> type;
        public int rarity;
        public Biome.Category[] spawnBiomes;

        SpeciesBear(EntityType<? extends AbstractBear> type, int rolls, Biome.Category... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getString() {
            return "why would you do this?";
        }

        public static EntityType<? extends AbstractBear> getSpeciesByBiome(IWorld world, BlockPos pos) {
            Optional<RegistryKey<Biome>> optional = world.func_242406_i(pos);
            if (Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) || Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
                return ModEntity.POLAR_BEAR;
            }
            if (Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE)) || Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE_HILLS))) {
                return ModEntity.PANDA_BEAR;
            }
            Biome biome = world.getBiome(pos);
            List<AbstractBear.SpeciesBear> types = new ArrayList<>();
            for (AbstractBear.SpeciesBear type : values()) {
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