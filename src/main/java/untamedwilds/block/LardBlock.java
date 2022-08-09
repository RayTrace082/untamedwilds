package untamedwilds.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LardBlock extends Block {

    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public LardBlock(Block.Properties properties) {
        super(properties);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        if (!worldIn.isClientSide) {
            worldIn.broadcastEntityEvent(entityIn, (byte)54);
        }

        if (entityIn.causeFallDamage(fallDistance, 0.2F, DamageSource.FALL)) {
            entityIn.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    public void updateEntityAfterFallOn(BlockGetter getter, Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(getter, entityIn);
        } else {
            //showJumpParticles(entityIn);
            this.bounceUp(entityIn);
        }

    }

    private void bounceUp(Entity p_56404_) {
        Vec3 vec3 = p_56404_.getDeltaMovement();
        if (vec3.y < 0.0D) {
            double d0 = p_56404_ instanceof LivingEntity ? 1.0D : 0.8D;
            p_56404_.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (this.isSlidingDown(pos, entityIn)) {
            this.doSlideMovement(entityIn);
            showSlideParticles(entityIn, worldIn, pos);
        }

        super.entityInside(state, worldIn, pos, entityIn);
    }

    private void doSlideMovement(Entity entityIn) {
        Vec3 vec3 = entityIn.getDeltaMovement();
        if (vec3.y < -0.13D) {
            double d0 = -0.05D / vec3.y;
            entityIn.setDeltaMovement(new Vec3(vec3.x * d0, -0.05D, vec3.z * d0));
        } else {
            entityIn.setDeltaMovement(new Vec3(vec3.x, -0.05D, vec3.z));
        }

        entityIn.resetFallDistance();
    }

    private boolean isSlidingDown(BlockPos p_54008_, Entity p_54009_) {
        if (p_54009_.isOnGround()) {
            return false;
        } else if (p_54009_.getY() > (double)p_54008_.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (p_54009_.getDeltaMovement().y >= -0.08D) {
            return false;
        } else {
            double d0 = Math.abs((double)p_54008_.getX() + 0.5D - p_54009_.getX());
            double d1 = Math.abs((double)p_54008_.getZ() + 0.5D - p_54009_.getZ());
            double d2 = 0.4375D + (double)(p_54009_.getBbWidth() / 2.0F);
            return d0 + 1.0E-7D > d2 || d1 + 1.0E-7D > d2;
        }
    }

    public static void showSlideParticles(Entity entity, Level world, BlockPos pos) {
        showParticles(entity, world, pos, 5);
    }

    public static void showJumpParticles(Entity entity, Level world, BlockPos pos) {
        showParticles(entity, world, pos, 10);
    }

    private static void showParticles(Entity entity, Level world, BlockPos pos, int p_53990_) {
        if (entity.level.isClientSide) {
            BlockState blockstate = world.getBlockState(pos);

            for(int i = 0; i < p_53990_; ++i) {
                entity.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
