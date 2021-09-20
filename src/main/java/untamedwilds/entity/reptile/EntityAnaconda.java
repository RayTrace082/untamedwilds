package untamedwilds.entity.reptile;

import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.*;
import untamedwilds.entity.ai.AmphibiousRandomSwimGoal;
import untamedwilds.entity.ai.AmphibiousTransition;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.entity.ai.SmartWanderGoal;
import untamedwilds.entity.ai.target.HuntMobTarget;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModSounds;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityAnaconda extends ComplexMobAmphibious implements ISpecies, INeedsPostUpdate, INewSkins {

    private static final int GROWING = 7 * ConfigGamerules.cycleLength.get();
    private static final String BREEDING = "LATE_SUMMER";
    public final int length;

    public final EntityAnacondaPart[] anacondaParts;
    public final float[] buffer = new float[3];
    public int ringBufferIndex = -1;
    public final double[][] ringBuffer = new double[64][3];

    public EntityAnaconda(EntityType<? extends ComplexMobTerrestrial> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
        this.moveController = new ComplexMobAquatic.MoveHelperController(this, 0.8F);
        this.lookController = new DolphinLookController(this, 10);
        this.ticksToSit = 40;
        this.length = 5;
        this.anacondaParts = new EntityAnacondaPart[this.length];
        for (int i = 0; i < this.length; i++) {
            this.anacondaParts[i] = new EntityAnacondaPart(this, this.getWidth(), this.getHeight());
        }
    }

    private void setPartPosition(EntityAnacondaPart part, double offsetX, double offsetY, double offsetZ) {
        part.setPosition(this.getPosX() + offsetX * part.scale, this.getPosY() + offsetY * part.scale, this.getPosZ() + offsetZ * part.scale);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.anacondaParts;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.33D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .createMutableAttribute(Attributes.ARMOR, 4D);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(3, new AmphibiousTransition(this, 1D));
        this.goalSelector.addGoal(4, new AmphibiousRandomSwimGoal(this, 0.7, 400));
        this.goalSelector.addGoal(4, new SmartWanderGoal(this, 0.7, false) {
            public boolean shouldExecute() {
                if (this.creature.peacefulTicks != 0)
                    return false;
                return super.shouldExecute();
            }
        });
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new HuntMobTarget<>(this, LivingEntity.class, true, 30, false, input -> getEcoLevel(input) < 8));
    }

    public static void processSkins() {
        for (int i = 0; i < SpeciesAnaconda.values().length; i++) {
            EntityUtils.buildSkinArrays("large_snake", SpeciesAnaconda.values()[i].name().toLowerCase(), i, TEXTURES_COMMON, TEXTURES_RARE);
        }
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (this.ticksExisted % 1000 == 0) {
                if (this.rand.nextInt(40) == 0) {
                    this.entityDropItem(new ItemStack(ModItems.MATERIAL_SNAKE_SKIN.get()), 0.2F);
                }
            }
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
            if (this.getAnimation() == NO_ANIMATION && this.getAttackTarget() == null && !this.isSleeping()) {
                int i = this.rand.nextInt(3000);
                if (i <= 10 && !this.isInWater() && this.isNotMoving() && this.canMove()) {
                    this.getNavigator().clearPath();
                    this.setSitting(true);
                }
                if ((i == 11 || this.isInWater() || this.isActive()) && this.isSitting()) {
                    this.setSitting(false);
                }
            }
            this.setAngry(this.getAttackTarget() != null);
        }

        if (!this.isAIDisabled() && !this.isChild()) {
            if (this.canMove()) {
                if (this.ringBufferIndex < 0) {
                    for (int i = 0; i < this.ringBuffer.length; ++i) {
                        this.ringBuffer[i][0] = this.rotationYaw;
                        this.ringBuffer[i][1] = this.getPosY();
                    }
                }
                this.ringBufferIndex++;
                if (this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }
                this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
                this.ringBuffer[ringBufferIndex][1] = this.getPosY();
                Vector3d[] avector3d = new Vector3d[this.anacondaParts.length];

                for (int j = 0; j < this.anacondaParts.length; ++j) {
                    this.anacondaParts[j].collideWithNearbyEntities();
                    avector3d[j] = new Vector3d(this.anacondaParts[j].getPosX(), this.anacondaParts[j].getPosY(), this.anacondaParts[j].getPosZ());
                }
                float f15 = (float) (this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * ((float) Math.PI / 180F);
                float f16 = MathHelper.cos(f15);
                float yaw = this.rotationYaw * ((float) Math.PI / 180F);
                float pitch = this.rotationPitch * ((float) Math.PI / 180F);
                float f3 = MathHelper.sin(yaw) * (1 - Math.abs(this.rotationPitch / 90F));
                float f18 = MathHelper.cos(yaw) * (1 - Math.abs(this.rotationPitch / 90F));

                double[] adouble = this.getMovementOffsets(5, 1.0F);

                for (int k = 0; k < SpeciesAnaconda.values()[this.getVariant()].multiParts; ++k) {
                    EntityAnacondaPart anaconda_part = this.anacondaParts[k];

                    double[] adouble1 = this.getMovementOffsets(5 + k * 2, 1.0F);
                    float f7 = yaw + (float) MathHelper.wrapDegrees(adouble1[0] - adouble[0]) * ((float) Math.PI / 180F);
                    float f20 = MathHelper.sin(f7) * (1 - Math.abs(this.rotationPitch / 90F));
                    float f21 = MathHelper.cos(f7) * (1 - Math.abs(this.rotationPitch / 90F));
                    float f23 = k == 0 ? (float) (k + 1) : (k + 1) * -1;
                    float value = pitch * (k);
                    this.setPartPosition(anaconda_part, -(f3 * 0.5F + f20 * f23) * f16, value, (f18 * 0.5F + f21 * f23) * f16);

                    this.anacondaParts[k].prevPosX = avector3d[k].x;
                    this.anacondaParts[k].prevPosY = avector3d[k].y;
                    this.anacondaParts[k].prevPosZ = avector3d[k].z;
                    this.anacondaParts[k].lastTickPosX = avector3d[k].x;
                    this.anacondaParts[k].lastTickPosY = avector3d[k].y;
                    this.anacondaParts[k].lastTickPosZ = avector3d[k].z;
                }
            }
            else {
                for (int k = 0; k < SpeciesAnaconda.values()[this.getVariant()].multiParts; ++k) {
                    EntityAnacondaPart anaconda_part = this.anacondaParts[k];
                    this.setPartPosition(anaconda_part, 0, 0, 0);
                    this.anacondaParts[k].prevPosX = this.getPosX();
                    this.anacondaParts[k].prevPosY = this.getPosY();
                    this.anacondaParts[k].prevPosZ = this.getPosZ();
                    this.anacondaParts[k].lastTickPosX = this.getPosX();
                    this.anacondaParts[k].lastTickPosY = this.getPosY();
                    this.anacondaParts[k].lastTickPosZ = this.getPosZ();
                }
            }
        }
    }

    public double[] getMovementOffsets(int p_70974_1_, float partialTicks) {
        if (this.getShouldBeDead()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - p_70974_1_ & 63;
        int j = this.ringBufferIndex - p_70974_1_ - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = this.ringBuffer[j][0] - d0;
        adouble[0] = d0 + d1 * (double) partialTicks;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double) partialTicks;
        adouble[2] = MathHelper.lerp(partialTicks, this.ringBuffer[i][2], this.ringBuffer[j][2]);
        return adouble;
    }

    /* Breeding conditions for the Snake are:
     * A nearby Snake of the opposite gender and the same species */
    public boolean wantsToBreed() {
        if (super.wantsToBreed()) {
            if (!this.isSleeping() && this.getGrowingAge() == 0 && EntityUtils.hasFullHealth(this)) {
                List<EntityAnaconda> list = this.world.getEntitiesWithinAABB(EntityAnaconda.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
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

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return ModSounds.ENTITY_SNAKE_HISS;
        }
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.ENTITY_SNAKE_HISS;
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_SNAKE_HISS;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        if (this.isEggLayer()) {
            EntityUtils.dropEggs(this, "egg_large_snake_" + getRawSpeciesName(this.getVariant()).toLowerCase(), 4);
            return null;
        }
        return create_offspring(new EntityAnaconda(ModEntity.ANACONDA, this.world));
    }

    public String getBreedingSeason() {
        return BREEDING;
    }
    public int getAdulthoodTime() { return GROWING; }

    public boolean isBreedingItem(ItemStack stack) { return stack.getItem() == Items.RABBIT; }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomeKey, Biome biome, SpawnReason reason) {
        if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(EntityAnaconda.SpeciesAnaconda.values().length);
        }
        return EntityAnaconda.SpeciesAnaconda.getSpeciesByBiome(biome);
    }

    protected activityType getActivityType() {
        return activityType.CATHEMERAL;
    }
    public String getSpeciesName(int i) { return new TranslationTextComponent("entity.untamedwilds.large_snake_" + getRawSpeciesName(i)).getString(); }
    public String getRawSpeciesName(int i) { return SpeciesAnaconda.values()[i].name().toLowerCase(); }

    public float getModelScale() { return SpeciesAnaconda.values()[this.getVariant()].scale; }

    public boolean isEggLayer() { return SpeciesAnaconda.values()[this.getVariant()].isEggLayer; }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (this.peacefulTicks == 0 && entityIn instanceof LivingEntity && !(entityIn instanceof PlayerEntity) && entityIn.getWidth() * entityIn.getHeight() < 1.2F && (entityIn instanceof TameableEntity && !((TameableEntity) entityIn).isTamed())) {
                this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
                EntityUtils.spawnParticlesOnEntity(this.world, (LivingEntity)entityIn, ParticleTypes.POOF, 6, 2);
                this.setMotion(new Vector3d(entityIn.getPosX() - this.getPosX(), entityIn.getPosY() - this.getPosY(), entityIn.getPosZ() - this.getPosZ()).scale(0.15F));
                this.peacefulTicks = 144000; // Large Snakes will spend the next 6 days idling after eating prey
                entityIn.remove();
            }
            return true;
        }
        return flag;
    }

    public boolean attackEntityPartFrom(EntityAnacondaPart anaconda_part, DamageSource source, float amount) {
        return this.attackEntityFrom(source, amount);
    }

    @Override
    public void updateAttributes() {
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(SpeciesAnaconda.values()[this.getVariant()].attack);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(SpeciesAnaconda.values()[this.getVariant()].health);
        this.setHealth(this.getMaxHealth());

    }

    public enum SpeciesAnaconda implements IStringSerializable {

        ANACONDA			(0, 0.9F,	4F, 30F, false, 3, 0, 3, Biome.Category.JUNGLE, Biome.Category.SWAMP),
        RETICULATED_PYTHON	(1, 1.0F,	4F, 30F, true, 3, 0, 3, Biome.Category.JUNGLE, Biome.Category.SWAMP);
        //TITANOBOA			(2, 1.6F,	6F, 50F, false, 5, 0, 0, Biome.Category.JUNGLE, Biome.Category.SWAMP);

        public Float scale;
        public int species;
        public int rolls;
        public float attack;
        public float health;
        public boolean isEggLayer;
        public int multiParts;
        public int venomTier;
        public Biome.Category[] spawnBiomes;

        SpeciesAnaconda(int species, Float scale, float attack, float health, boolean isEggLayer, int multiParts, int venomTier, int rolls, Biome.Category... biomes) {
            this.species = species;
            this.scale = scale;
            this.rolls = rolls;
            this.attack = attack;
            this.health = health;
            this.isEggLayer = isEggLayer;
            this.multiParts = multiParts;
            this.venomTier = venomTier;
            this.spawnBiomes = biomes;
        }

        public int getSpecies() { return this.species; }

        public String getString() {
            return I18n.format("entity.large_snake." + this.name().toLowerCase());
        }

        public static int getSpeciesByBiome(Biome biome) {
            List<SpeciesAnaconda> types = new ArrayList<>();
            for (SpeciesAnaconda type : values()) {
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

        public int getVenomTier() {
            return venomTier;
        }
    }

    public static class EntityAnacondaPart extends PartEntity<EntityAnaconda> {

        private final EntitySize size;
        public float scale = 1;

        public EntityAnacondaPart(EntityAnaconda parent, float sizeX, float sizeY) {
            super(parent);
            this.size = EntitySize.flexible(sizeX, sizeY);
            this.recalculateSize();
        }

        protected void collideWithNearbyEntities() {
            List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            Entity parent = this.getParent();
            if (parent != null) {
                entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityAnacondaPart && ((EntityAnacondaPart) entity).getParent() == parent) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(parent));
            }
        }

        public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
            return this.getParent() == null ? ActionResultType.PASS : this.getParent().func_230254_b_(player, hand);
        }

        public void applyEntityCollision(Entity entityIn) {
            entityIn.applyEntityCollision(this);
        }

        public boolean canBeCollidedWith() {
            return true;
        }

        @Override
        protected void readAdditional(CompoundNBT compound) { }

        @Override
        protected void writeAdditional(CompoundNBT compound) { }

        @Override
        protected void registerData() { }

        public boolean attackEntityFrom(DamageSource source, float amount) {
            return !this.isInvulnerableTo(source) && this.getParent().attackEntityPartFrom(this, source, amount);
        }

        public boolean isEntityEqual(Entity entityIn) {
            return this == entityIn || this.getParent() == entityIn;
        }

        public IPacket<?> createSpawnPacket() {
            throw new UnsupportedOperationException();
        }

        public EntitySize getSize(Pose poseIn) {
            return this.size.scale(scale);
        }
    }
}