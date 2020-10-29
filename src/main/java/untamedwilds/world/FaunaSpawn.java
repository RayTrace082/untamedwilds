package untamedwilds.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.fluid.IFluidState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public final class FaunaSpawn {

    private static boolean isSpawnableSpace(IBlockReader worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
        if (state.isCollisionShapeOpaque(worldIn, pos)) {
            return false;
        } else if (state.canProvidePower()) {
            return false;
        } else if (!fluidStateIn.isEmpty()) {
            return false;
        } else {
            return !state.isIn(BlockTags.RAILS);
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
        IFluidState ifluidstate = worldIn.getFluidState(pos);
        BlockPos blockpos = pos.up();
        BlockPos blockpos1 = pos.down();
        switch(placeType) {
            case IN_WATER:
                return ifluidstate.isTagged(FluidTags.WATER) && worldIn.getFluidState(blockpos1).isTagged(FluidTags.WATER) && !worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos);
            case ON_GROUND:
            default:
                BlockState blockstate1 = worldIn.getBlockState(blockpos1); // Hardcode exception for Ice blocks?
                if (!blockstate1.canCreatureSpawn(worldIn, blockpos1, placeType, entityTypeIn) || worldIn.getBlockState(blockpos1).getBlock() == Blocks.ICE) {
                    return false;
                } else {
                    return isSpawnableSpace(worldIn, pos, blockstate, ifluidstate) && isSpawnableSpace(worldIn, blockpos, worldIn.getBlockState(blockpos), worldIn.getFluidState(blockpos));
                }
        }
    }

    public static void performWorldGenSpawning(List<FaunaHandler.SpawnListEntry> list, EntitySpawnPlacementRegistry.PlacementType spawnType, IWorld worldIn, Biome biomeIn, BlockPos pos, Random rand) {
        if (list.size() != 0) {
            FaunaHandler.SpawnListEntry entry = WeightedRandom.getRandomItem(rand, list);
            performWorldGenSpawning(entry.entityType, spawnType, worldIn, biomeIn, pos, rand, 1);
        }
    }

    public static void performWorldGenSpawning(EntityType<?> entityType, EntitySpawnPlacementRegistry.PlacementType spawnType, IWorld worldIn, Biome biomeIn, BlockPos pos, Random rand, int groupSize) {
        if (entityType != null) {
            int i = pos.getX();
            int j = pos.getZ();

            while(rand.nextFloat() < biomeIn.getSpawningChance()) {
                int k = 1;
                if (groupSize != 1) {
                    k = 1 + rand.nextInt(groupSize);
                }

                for(int l1 = 0; l1 < k; ++l1) {
                    boolean flag = false;
                    int x = i;
                    int z = j;
                    int y = pos.getY();
                    if (l1 != 0) {
                        // Do not offset the first entity of the pack
                        x += rand.nextInt(8);
                        z += rand.nextInt(8);
                    }

                    for(int i2 = 0; !flag && i2 < 4; ++i2) { // 4 attempts at spawning are made for each mob
                        if (i2 != 0) {
                            if (i2 == 1) {
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

                            // Unmapped method to check if an entity's AABB box does not overlap with any blocks
                            if (!worldIn.func_226664_a_(entityType.func_220328_a(d0, pos.getY(), d1)) || !EntitySpawnPlacementRegistry.func_223515_a(entityType, worldIn, SpawnReason.CHUNK_GENERATION, new BlockPos(d0, pos.getY(), d1), worldIn.getRandom())) {
                                continue;
                            }

                            Entity entity;
                            try {
                                entity = entityType.create(worldIn.getWorld());
                            } catch (Exception exception) {
                                continue;
                            }

                            MobEntity entity2 = (MobEntity)entity;
                            assert entity != null;
                            entity.setLocationAndAngles(d0, y, d1, rand.nextFloat() * 360.0F, 0.0F);
                            if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(entity2, worldIn, d0, blockpos.getY(), d1, null, SpawnReason.NATURAL) == -1) continue;
                            if (entity2.canSpawn(worldIn, SpawnReason.CHUNK_GENERATION)) {

                                entity2.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.CHUNK_GENERATION, null, null);
                                if (entity2 instanceof ISpecies) {
                                    if (((ComplexMob)entity2).getSpecies() != 99) {
                                        worldIn.addEntity(entity2);
                                        break;
                                    }
                                }
                                else {
                                    worldIn.addEntity(entity2);
                                    break;
                                }
                                flag = true;
                            }
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