package untamedwilds.entity.fish;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.entity.PartEntity;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ComplexMobAquatic;
import untamedwilds.entity.INewSkins;
import untamedwilds.entity.ISpecies;
import untamedwilds.entity.ai.SmartMateGoal;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;
import untamedwilds.util.SpeciesDataHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityWhaleShark extends ComplexMobAquatic implements ISpecies, INewSkins {

    public final int length;
    public final EntityWhaleSharkPart[] whale_shark_parts;
    public int ringBufferIndex = -1;
    public final double[][] ringBuffer = new double[64][3];

    public EntityWhaleShark(EntityType<? extends ComplexMob> type, World worldIn) {
        super(type, worldIn);
        this.length = getMultiparts();
        this.whale_shark_parts = new EntityWhaleSharkPart[this.length];
        for (int i = 0; i < this.length; i++) {
            this.whale_shark_parts[i] = new EntityWhaleSharkPart(this, this.getWidth(), this.getHeight());
        }
        this.turn_speed = 0.1F;
    }

    private void setPartPosition(EntityWhaleSharkPart part, double offsetX, double offsetY, double offsetZ) {
        part.setPosition(this.getPosX() + offsetX * part.scale, this.getPosY() + offsetY * part.scale, this.getPosZ() + offsetZ * part.scale);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public PartEntity<?>[] getParts() {
        return this.whale_shark_parts;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.8D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1D)
                .createMutableAttribute(Attributes.ARMOR, 6D);
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(3, new SmartMateGoal(this, 1D));
        this.goalSelector.addGoal(4, new SwimGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (this.world.getGameTime() % 4000 == 0) {
                this.heal(1.0F);
            }
        }

        if (!this.isAIDisabled() && !this.isChild()) {
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
            this.ringBuffer[this.ringBufferIndex][0] = MathHelper.interpolateAngle(0.5F, this.prevRotationYaw, this.rotationYaw);
            this.ringBuffer[ringBufferIndex][1] = this.getPosY();
            Vector3d[] avector3d = new Vector3d[this.whale_shark_parts.length];

            for (int j = 0; j < this.whale_shark_parts.length; ++j) {
                this.whale_shark_parts[j].collideWithNearbyEntities();
                avector3d[j] = new Vector3d(this.whale_shark_parts[j].getPosX(), this.whale_shark_parts[j].getPosY(), this.whale_shark_parts[j].getPosZ());
            }
            float f15 = (float) (this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F * ((float) Math.PI / 180F);
            float f16 = MathHelper.cos(f15);
            float yaw = this.rotationYaw * ((float) Math.PI / 180F);
            float pitch = this.rotationPitch * ((float) Math.PI / 180F);
            float f3 = MathHelper.sin(yaw) * (1 - Math.abs(this.rotationPitch / 90F));
            float f18 = MathHelper.cos(yaw) * (1 - Math.abs(this.rotationPitch / 90F));

            double[] adouble = this.getMovementOffsets(5, 1.0F);

            for (int k = 0; k < getMultiparts(); ++k) {
                EntityWhaleSharkPart whale_shark_part = this.whale_shark_parts[k];

                double[] adouble1 = this.getMovementOffsets(5 + k * 2, 1.0F);
                float f7 = yaw + (float) MathHelper.wrapDegrees(adouble1[0] - adouble[0]) * ((float) Math.PI / 180F);
                float f20 = MathHelper.sin(f7) * (1 - Math.abs(this.rotationPitch / 90F));
                float f21 = MathHelper.cos(f7) * (1 - Math.abs(this.rotationPitch / 90F));
                float f23 = k == 0 ? (float) (k + 3) : (k + 3) * -1;
                float value = MathHelper.clamp(pitch * k, -20, 20);
                this.setPartPosition(whale_shark_part, -(f3 * 0.5F + f20 * f23) * f16, value, (f18 * 0.5F + f21 * f23) * f16);

                this.whale_shark_parts[k].prevPosX = avector3d[k].x;
                this.whale_shark_parts[k].prevPosY = avector3d[k].y;
                this.whale_shark_parts[k].prevPosZ = avector3d[k].z;
                this.whale_shark_parts[k].lastTickPosX = avector3d[k].x;
                this.whale_shark_parts[k].lastTickPosY = avector3d[k].y;
                this.whale_shark_parts[k].lastTickPosZ = avector3d[k].z;
            }
        }
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public double[] getMovementOffsets(int offset, float partialTicks) {
        if (this.getShouldBeDead()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - offset & 63;
        int j = this.ringBufferIndex - offset - 1 & 63;
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
                List<EntityWhaleShark> list = this.world.getEntitiesWithinAABB(EntityWhaleShark.class, this.getBoundingBox().grow(6.0D, 4.0D, 6.0D));
                list.removeIf(input -> EntityUtils.isInvalidPartner(this, input, false));
                if (list.size() >= 1) {
                    this.setGrowingAge(this.getPregnancyTime());
                    list.get(0).setGrowingAge(this.getPregnancyTime());
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return create_offspring(new EntityWhaleShark(ModEntity.WHALE_SHARK, this.world));
    }

    public boolean attackEntityPartFrom(EntityWhaleSharkPart whale_shark_part, DamageSource source, float amount) {
        return this.attackEntityFrom(source, amount);
    }

    // Flags Parameters
    public int getMultiparts() {
        return 2;
    }

    @Override
    public int setSpeciesByBiome(RegistryKey<Biome> biomekey, Biome biome, SpawnReason reason) {
        if (biomekey.equals(Biomes.WARM_OCEAN) || biomekey.equals(Biomes.LUKEWARM_OCEAN) || biomekey.equals(Biomes.DEEP_WARM_OCEAN) || biomekey.equals(Biomes.DEEP_LUKEWARM_OCEAN)) {
            if (ConfigGamerules.randomSpecies.get() || isArtificialSpawnReason(reason)) {
                return this.getRNG().nextInt(getEntityData(this.getType()).getSpeciesData().size());
            }
            List<Integer> validTypes = new ArrayList<>();
            if (ComplexMob.ENTITY_DATA_HASH.containsKey(this.getType())) {
                for (SpeciesDataHolder speciesDatum : getEntityData(this.getType()).getSpeciesData()) {
                    for(Biome.Category biomeTypes : speciesDatum.getBiomeCategories()) {
                        if(biome.getCategory() == biomeTypes){
                            for (int i=0; i < speciesDatum.getRarity(); i++) {
                                validTypes.add(speciesDatum.getVariant());
                            }
                        }
                    }
                }
                if (validTypes.isEmpty()) {
                    return 99;
                } else {
                    return validTypes.get(new Random().nextInt(validTypes.size()));
                }
            }
        }
        if (isArtificialSpawnReason(reason)) {
            return this.rand.nextInt(getEntityData(this.getType()).getSpeciesData().size());
        }
        return 99;
    }

    public boolean canBeTargeted() { return false; }

    public static class EntityWhaleSharkPart extends PartEntity<EntityWhaleShark> {

        private final EntitySize size;
        public float scale = 1;

        public EntityWhaleSharkPart(EntityWhaleShark parent, float sizeX, float sizeY) {
            super(parent);
            this.size = EntitySize.flexible(sizeX, sizeY);
            this.recalculateSize();
        }

        protected void collideWithNearbyEntities() {
            List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            Entity parent = this.getParent();
            if (parent != null) {
                entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityWhaleSharkPart && ((EntityWhaleSharkPart) entity).getParent() == parent) && entity.canBePushed()).forEach(entity -> entity.applyEntityCollision(parent));
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