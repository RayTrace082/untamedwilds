package untamedwilds.entity.mammal.bigcat;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.lang3.tuple.Pair;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;
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
        IDLE_TALK = Animation.create(20);
        IDLE_STRETCH = Animation.create(110);
        this.stepHeight = 1;
        this.experienceValue = 10;
        this.dexterity = 0.1F;
    }

    public boolean isActive() {
        if (this.forceSleep < 0) {
            return false;
        }
        float f = this.world.getCelestialAngle(0F);
        return f < 0.21F || f > 0.78;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public void livingTick() {
        if (!this.world.isRemote) {

            if (this.world.getGameTime() % 1000 == 0) {
                this.addHunger(-3);
                if (!this.isStarving()) {
                    this.heal(2.0F);
                }
            }
            if (this.isSleeping() && this.forceSleep == 0) {
                List<PlayerEntity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                if (!list.isEmpty()) {
                    PlayerEntity player = list.get(0);
                    if (!player.isShiftKeyDown() && !player.isCreative()) {
                        this.setSleeping(false);
                        this.setAttackTarget(player);
                        this.forceSleep = -300;
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
            this.setAngry(this.getAttackTarget() != null);
        }
        if (this.getAnimation() == ANIMATION_EAT && (this.getAnimationTick() == 10 || this.getAnimationTick() == 20 || this.getAnimationTick() == 30)) {
            this.playSound(SoundEvents.ENTITY_HORSE_EAT,1.5F, 0.8F);
        }
        if (this.getAnimation() == IDLE_TALK && this.getAnimationTick() == 1 && this.getAmbientSound() != null) {
            this.playSound(this.getAmbientSound(), 1.5F, 1);
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

    protected SoundEvent getAmbientSound() {
        return this.isChild() ? null : ModSounds.ENTITY_BIG_CAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return !this.isChild() ? ModSounds.ENTITY_BIG_CAT_HURT : SoundEvents.ENTITY_OCELOT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return !this.isChild() ? ModSounds.ENTITY_BIG_CAT_DEATH : SoundEvents.ENTITY_OCELOT_DEATH;
    }

    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(Hand.MAIN_HAND);
        if (hand == Hand.MAIN_HAND && !this.world.isRemote()) { // Prevents all code from running twice
            if (!this.world.isRemote()) {
                if (player.isCreative() && itemstack.isEmpty()) {
                    this.setAnimation(IDLE_STRETCH);
                    //this.getJumpController().setJumping();
                    this.getNavigator().clearPath();
                    UntamedWilds.LOGGER.info(this.getDistanceSq(this.getHomeAsVec()) + " | " + this.getPosition() + " | " + this.getHome());
                }

                if (this.isTamed() && this.getOwner() == player) {
                    if (itemstack.isEmpty()) {
                        this.setCommandInt(this.getCommandInt() + 1);
                        player.sendMessage(new TranslationTextComponent("entity.untamedwilds.command." + this.getCommandInt()));
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
                            if (pair.getLeft() != null && this.world.rand.nextFloat() < pair.getRight()) {
                                this.addPotionEffect(new EffectInstance(pair.getLeft()));
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
            }
            if (!this.isTamed() && this.isChild() && this.getHealth() == this.getMaxHealth() && this.isFavouriteFood(itemstack)) {
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, 1.5F, 0.8F);
                if (this.getRNG().nextInt(3) == 0) {
                    this.setTamedBy(player);
                    //this.registerGoals(); // AI Reset Hook
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.HEART, 3, 6);
                } else {
                    EntityUtils.spawnParticlesOnEntity(this.world, this, ParticleTypes.SMOKE, 3, 3);
                }
            }
        }

        return super.processInteract(player, hand);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getAnimation() == NO_ANIMATION && !this.isChild()) {
            Animation anim = chooseAttackAnimation(this);
            this.setAnimation(anim);
            this.setAnimationTick(0);
        }
        return flag;
    }

    private Animation chooseAttackAnimation(Entity entityIn) {
        return ATTACK_POUNCE;
    }

    //public Animation getAnimationEat() { return ANIMATION_EAT; }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ATTACK_POUNCE, IDLE_TALK, IDLE_STRETCH};
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.9F;
    }

    public enum SpeciesBigCat implements IStringSerializable {

        JAGUAR		(ModEntity.JAGUAR, JaguarBigCat.getRarity(), BiomeDictionary.Type.JUNGLE),
        LEOPARD		(ModEntity.LEOPARD, LeopardBigCat.getRarity(), BiomeDictionary.Type.SAVANNA, BiomeDictionary.Type.CONIFEROUS),
        LION		(ModEntity.LION, LionBigCat.getRarity(), BiomeDictionary.Type.SAVANNA),
        PANTHER		(ModEntity.PANTHER, PantherBigCat.getRarity(), BiomeDictionary.Type.JUNGLE),
        PUMA		(ModEntity.PUMA, PumaBigCat.getRarity(), BiomeDictionary.Type.MESA, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.CONIFEROUS),
        SNOW_LEOPARD(ModEntity.SNOW_LEOPARD, SnowLeopardBigCat.getRarity(), BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.CONIFEROUS),
        TIGER		(ModEntity.TIGER, TigerBigCat.getRarity(), BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.CONIFEROUS);

        public EntityType<? extends AbstractBigCat> type;
        public int rarity;
        public BiomeDictionary.Type[] spawnBiomes;

        SpeciesBigCat(EntityType<? extends AbstractBigCat> type, int rolls, BiomeDictionary.Type... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getName() {
            return "why would you do this?";
        }

        public static EntityType<? extends AbstractBigCat> getSpeciesByBiome(Biome biome) {
            List<AbstractBigCat.SpeciesBigCat> types = new ArrayList<>();
            for (AbstractBigCat.SpeciesBigCat type : values()) {
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