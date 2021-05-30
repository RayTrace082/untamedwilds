package untamedwilds.entity.mammal.bear;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.entity.ai.SmartFollowOwnerGoal;
import untamedwilds.entity.ai.target.SmartOwnerHurtTargetGoal;
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
                this.getMoveHelper().strafe(2F, 0);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.5F, 0.8F);
            }
            if (this.getAnimation() == ATTACK_MAUL) {
                if (this.getAnimationTick() == 10) {
                    this.playSound(ModSounds.ENTITY_BEAR_WARNING, 1.5F, 1);
                }
                if (this.getAnimationTick() == 20) {
                    this.playSound(SoundEvents.ENTITY_PLAYER_SMALL_FALL, 1.5F, 0.8F);
                    if (!this.world.isRemote()) {
                        BlockPos pos = this.getPosition().down();
                        BlockState state = this.world.getBlockState(pos);
                        if (!state.addLandingEffects((ServerWorld)this.world, pos, state, this, 40))
                            ((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), this.getPosX(), this.getPosY(), this.getPosZ(), 40, 0.0D, 0.0D, 0.0D, 99.3F);
                    }
                }
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
        switch (this.rand.nextInt(4)) {
            case 0: return ATTACK_SWIPE;
            case 1: return ATTACK_BITE;
            case 2: return ATTACK_MAUL;
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
    public boolean hasLongBody() { return false; }

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
            return !types.isEmpty() ? types.get(new Random().nextInt(types.size())).type : null;
        }
    }

    /*public enum SpeciesBear implements IStringSerializable {

        BLACK		(0, 0.8F, 5F, 30F, false, false, false, false, Items.SWEET_BERRIES, "EARLY_SUMMER", 8, 5, Biome.Category.FOREST, Biome.Category.TAIGA),
        BLIND		(1, 1.1F, 7F, 45F, false, true, false, false, Items.BEEF, "MID_SUMMER", 8, ConfigGamerules.fantasyMobs.get() ? 1 : 0),
        BROWN		(2, 1.1F, 7F, 40F, true, false, false, false, Items.SALMON, "EARLY_SUMMER", 8, 3, Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS),
        CAVE		(3, 1.3F, 9F, 50F, true, false, false, false, Items.POTATO, "MID_SUMMER", 10, ConfigGamerules.extinctMobs.get() ? 1 : 0, Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS),
        PANDA		(4, 0.7F, 4F, 25F,false, true, false, true, Items.BAMBOO, "MID_SPRING", 6, 0, Biome.Category.JUNGLE),
        POLAR		(5, 1.3F, 8F, 45F, false, false, true, false, ModItems.MATERIAL_FAT.get(), "LATE_SPRING", 10, 1, Biome.Category.ICY),
        SPECTACLED	(6, 0.8F, 5F, 30F, false, true, false, false, Items.APPLE, "LATE_WET", 7, 1, Biome.Category.EXTREME_HILLS),
        SUN 		(7, 0.6F, 4F, 15F, false, false, false, false, Items.HONEYCOMB, "MID_WET", 4, 4, Biome.Category.JUNGLE);

        public int species;
        public Float scale;
        public float attack;
        public float health;
        public boolean hasHump;
        public boolean hasShortSnout;
        public boolean hasLongBody;
        public boolean isPanda;
        public String breedingSeason;
        public int gestation;
        public Item favouriteFood;
        public int rarity;
        public Biome.Category[] spawnBiomes;

        SpeciesBear(int species, float scale, float attack, float health, boolean hasHump, boolean hasShortSnout, boolean hasLongBody, boolean isPanda, Item favouriteFood, String breeding, int growingAge, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.attack = attack;
            this.health = health;
            this.hasHump = hasHump;
            this.hasShortSnout = hasShortSnout;
            this.hasLongBody = hasLongBody;
            this.isPanda = isPanda;
            this.favouriteFood = favouriteFood;
            this.breedingSeason = breeding;
            this.gestation = growingAge;
            this.rarity = rolls;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getString() {
            return "why would you do this?";
        }

        public static int getSpeciesByBiome(Biome biome) {
            Optional<RegistryKey<Biome>> optional = biome.func_242406_i(pos);
            if (Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) || Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
                return ModEntity.POLAR_BEAR;
            }
            if (Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE)) || Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE_HILLS))) {
                return ModEntity.PANDA_BEAR;
            }
            List<SpeciesBear> types = new ArrayList<>();
            for (SpeciesBear type : values()) {
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

        public static EntityType<? extends AbstractBear> getSpeciesByBiome(IWorld world, BlockPos pos) {
            Optional<RegistryKey<Biome>> optional = world.func_242406_i(pos);
            if (Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) || Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
                return ModEntity.POLAR_BEAR;
            }
            if (Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE)) || Objects.equals(optional, Optional.of(Biomes.BAMBOO_JUNGLE_HILLS))) {
                return ModEntity.PANDA_BEAR;
            }
            List<SpeciesBear> types = new ArrayList<>();
            for (SpeciesBear type : values()) {
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
    }*/
}