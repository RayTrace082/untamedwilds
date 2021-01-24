package untamedwilds.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import untamedwilds.UntamedWilds;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public final class FaunaSpawn {

    private static boolean isSpawnableSpace(IBlockReader worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn, EntityType<?> entityTypeIn) {
        if (state.hasOpaqueCollisionShape(worldIn, pos)) {
            return false;
        } else if (state.canProvidePower()) {
            return false;
        } else if (!fluidStateIn.isEmpty()) {
            return false;
        } else if (state.isIn(BlockTags.PREVENT_MOB_SPAWNING_INSIDE)) {
            return false;
        } else {
            return !entityTypeIn.func_233597_a_(state);
        }
    }

    private static boolean canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.PlacementType placeType, IWorldReader worldIn, BlockPos pos, @Nullable EntityType<?> entityTypeIn) {
        if (placeType == EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS) {
            return true;
        } else if (entityTypeIn != null && worldIn.getWorldBorder().contains(pos)) {
            return canSpawnAtBody(placeType, worldIn, pos, entityTypeIn);
        }
        return false;
    }

    private static boolean canSpawnAtBody(EntitySpawnPlacementRegistry.PlacementType placeType, IWorldReader worldIn, BlockPos pos, @Nullable EntityType<?> entityTypeIn) {
        BlockState blockstate = worldIn.getBlockState(pos);
        FluidState ifluidstate = worldIn.getFluidState(pos);
        BlockPos blockpos = pos.up();
        BlockPos blockpos1 = pos.down();
        switch(placeType) {
            case IN_WATER:
                return ifluidstate.isTagged(FluidTags.WATER) && worldIn.getFluidState(blockpos1).isTagged(FluidTags.WATER) && !worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos);
            case IN_LAVA:
                return ifluidstate.isTagged(FluidTags.LAVA);
            case ON_GROUND:
            default:
                BlockState blockstate1 = worldIn.getBlockState(blockpos1);
                if (!blockstate1.canCreatureSpawn(worldIn, blockpos1, placeType, entityTypeIn)) {
                    return false;
                } else {
                    return isSpawnableSpace(worldIn, pos, blockstate, ifluidstate, entityTypeIn); /* && isSpawnableSpace(worldIn, blockpos, worldIn.getBlockState(blockpos), worldIn.getFluidState(blockpos)*/
                }
        }
    }

    public static void performWorldGenSpawning(List<FaunaHandler.SpawnListEntry> list, EntitySpawnPlacementRegistry.PlacementType spawnType, ISeedReader worldIn, BlockPos pos, Random rand) {
        if (list.size() != 0) {
            FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, list);
            performWorldGenSpawning(entry.entityType, spawnType, worldIn, pos, rand, entry.groupCount);
        }
    }

    public static void performWorldGenSpawning(EntityType<?> entityType, EntitySpawnPlacementRegistry.PlacementType spawnType, ISeedReader worldIn, BlockPos pos, Random rand, int groupSize) {
        //UntamedWilds.LOGGER.info(entityType);
        if (entityType != null) {
            int i = pos.getX() + rand.nextInt(16);
            int j = pos.getZ() + rand.nextInt(16);

            if (rand.nextFloat() < UntamedWildsGenerator.getBioDiversityLevel(worldIn.getBiome(pos).getRegistryName())) {
                int k = 1;
                if (groupSize != 1) {
                    k = 1 + rand.nextInt(groupSize);
                }
                for(int packSize = 0; packSize < k; ++packSize) {
                    boolean flag = false;
                    int x = i;
                    int z = j;
                    int y = pos.getY();
                    if (packSize != 0) {
                        // Do not offset the first entity of the pack
                        x += rand.nextInt(6);
                        z += rand.nextInt(6);
                    }

                    for(int attempt = 0; !flag && attempt < 4; ++attempt) { // 4 attempts at spawning are made for each mob
                        if (attempt != 0) {
                            if (attempt == 1) {
                                y += 1;
                            }
                            x += rand.nextInt(2);
                            z += rand.nextInt(2);
                        }
                        BlockPos blockpos = getTopSolidOrLiquidBlock(worldIn, entityType, x, z);
                        if (entityType.isSummonable() && canCreatureTypeSpawnAtLocation(spawnType, worldIn, blockpos, entityType)) {
                            float f = entityType.getWidth();
                            double d0 = MathHelper.clamp(x, (double)x + (double)f, (double)x + 16.0D - (double)f);
                            double d1 = MathHelper.clamp(z, (double)z + (double)f, (double)z + 16.0D - (double)f);
                            if (!worldIn.hasNoCollisions(entityType.getBoundingBoxWithSizeApplied(d0, pos.getY(), d1)) || !EntitySpawnPlacementRegistry.canSpawnEntity(entityType, worldIn, SpawnReason.CHUNK_GENERATION, new BlockPos(d0, pos.getY(), d1), worldIn.getRandom())) {
                                continue;
                            }

                            Entity entity;
                            try {
                                entity = entityType.create(worldIn.getWorld());
                            } catch (Exception exception) {
                                UntamedWilds.LOGGER.warn("Failed to create mob", exception);
                                continue;
                            }
                            assert entity != null;
                            entity.setLocationAndAngles(d0, y, d1, rand.nextFloat() * 360.0F, 0.0F);
                            flag = true;
                            MobEntity mobentity = (MobEntity)entity;
                            if (mobentity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(mobentity.getPosition()), SpawnReason.CHUNK_GENERATION, null, null) != null) {
                                worldIn.func_242417_l(mobentity);
                            }
                            net.minecraftforge.common.ForgeHooks.canEntitySpawn(mobentity, worldIn, d0, blockpos.getY(), d1, null, SpawnReason.CHUNK_GENERATION);
                        }
                    }
                }
            }
        }
    }

    private static BlockPos getTopSolidOrLiquidBlock(IWorldReader worldIn, @Nullable EntityType<?> entity, int posX, int posZ) {
        BlockPos blockpos = new BlockPos(posX, worldIn.getHeight(EntitySpawnPlacementRegistry.func_209342_b(entity), posX, posZ), posZ);
        BlockPos blockpos1 = blockpos.down();
        return worldIn.getBlockState(blockpos1).allowsMovement(worldIn, blockpos1, PathType.LAND) ? blockpos1 : blockpos;
    }
}