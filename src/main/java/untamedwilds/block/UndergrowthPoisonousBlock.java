package untamedwilds.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class UndergrowthPoisonousBlock extends UndergrowthBlock implements IGrowable, net.minecraftforge.common.IForgeShearable {

    public UndergrowthPoisonousBlock(Properties properties) {
        super(properties);
        this.offset = OffsetType.NONE;
    }

    public UndergrowthPoisonousBlock(Properties properties, OffsetType type) {
        super(properties);
        this.offset = type;
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && !entityIn.isSneaking() && entityIn.getHeight() > 1) {
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 100, 1));
            entityIn.setMotionMultiplier(state, new Vector3d(0.95F, 1D, 0.95F));
            if (worldIn.getRandom().nextInt(20) == 0) {
                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_STEP, SoundCategory.AMBIENT, 1, 1, true);
            }
        }
    }

    public OffsetType getOffsetType() {
        return this.offset;
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
        BlockState blockstate = worldIn.getBlockState(pos);
        int i = pos.getY();
        if (i >= 1 && i + 1 < 256) { // TODO: Needs to be changed by 1.17
            for(int k = 0; k < 3; ++k) {
                BlockPos blockpos = pos.add(rand.nextInt(3) - 1, 1 - rand.nextInt(3), rand.nextInt(3) - 1);
                if (worldIn.getBlockState(blockpos).canBeReplacedByLeaves(worldIn, blockpos) && blockstate.isValidPosition(worldIn, blockpos)) {
                    worldIn.setBlockState(blockpos, blockstate, 2);
                }
            }
        }
    }

    //public OffsetType getOffsetType() {
    //    return OffsetType.XZ;
    //}
}

