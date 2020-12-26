package untamedwilds.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockLard extends BreakableBlock {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlockLard(Properties properties) {
        super(properties);
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.playSound(SoundEvents.BLOCK_HONEY_BLOCK_FALL, 1.0F, 1.0F);
        if (!worldIn.isRemote) {
            worldIn.setEntityState(entityIn, (byte)54);
        }

        if (entityIn.onLivingFall(fallDistance, 0.2F)) {
            entityIn.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (this.method_23356(pos, entity)) {
            Vector3d vec3d_1 = entity.getMotion();
            if (vec3d_1.y < -0.05D) {
                entity.setMotion(new Vector3d(vec3d_1.x, -0.05D, vec3d_1.z));
            }

            entity.fallDistance = 0.0F;
            this.method_23358(world, pos, entity);
            if (world.getGameTime() % 10L == 0L) {
                entity.playSound(SoundEvents.BLOCK_SLIME_BLOCK_STEP, 1.0F, 1.0F);
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    private boolean method_23356(BlockPos blockPos_1, Entity entity_1) {
        if (!entity_1.isAirBorne) {
            return false;
        } else if (entity_1.getPosY() > (double)blockPos_1.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (entity_1.getMotion().y >= 0.0D) {
            return false;
        }
        return true;
        /*else {
            double double_1 = Math.abs((double)blockPos_1.getX() + 0.5D - entity_1.getPosition().getX());
            double double_2 = Math.abs((double)blockPos_1.getZ() + 0.5D - entity_1.getPosition().getZ());
            double double_3 = 0.4375D + (double)(entity_1.getWidth() / 2.0F);
            return double_1 + 1.0E-7D > double_3 || double_2 + 1.0E-7D > double_3;
        }*/
    }

    /*private void method_23357(World world_1, BlockPos blockPos_1, Entity entity_1) {
        float float_1 = entity_1.getDimensions(EntityPose.STANDING).width;
        this.method_23355(entity_1, world_1, blockPos_1, 1, ((double)world_1.random.nextFloat() - 0.5D) * (double)float_1, (double)(world_1.random.nextFloat() / 2.0F), ((double)world_1.random.nextFloat() - 0.5D) * (double)float_1, (double)world_1.random.nextFloat() - 0.5D, (double)(world_1.random.nextFloat() - 1.0F), (double)world_1.random.nextFloat() - 0.5D);
    }*/

    private void method_23358(World world_1, BlockPos blockPos_1, Entity entity_1) {
        float float_1 = entity_1.getWidth();
        this.spawnParticleEffects(entity_1, world_1, blockPos_1, 1, ((double)world_1.rand.nextFloat() - 0.5D) * (double)float_1, 0.0D, ((double)world_1.rand.nextFloat() - 0.5D) * (double)float_1, (double)world_1.rand.nextFloat() - 0.5D, 0.5D, (double)world_1.rand.nextFloat() - 0.5D);
    }

    private void spawnParticleEffects(Entity entity, World world, BlockPos blockPos_1, int int_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
        BlockState blockState_1 = world.getBlockState(new BlockPos(blockPos_1));

        for(int int_2 = 0; int_2 < int_1; ++int_2) {
            entity.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockState_1), entity.getPosX() + double_1, entity.getPosY() + double_2, entity.getPosZ() + double_3, double_4, double_5, double_6);
        }
    }
}
