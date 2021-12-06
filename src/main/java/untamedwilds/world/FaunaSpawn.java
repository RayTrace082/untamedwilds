package untamedwilds.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.ISpecies;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class FaunaSpawn {

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
        //BlockPos blockpos = pos.up();
        BlockPos blockpos1 = pos.down();
        switch(placeType) {
            case IN_WATER:
                return ifluidstate.isTagged(FluidTags.WATER) /*&& worldIn.getFluidState(blockpos1).isTagged(FluidTags.WATER) && !worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos)*/;
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

    //public static boolean performWorldGenSpawning(EntityType<?> entityType, EntitySpawnPlacementRegistry.PlacementType spawnType, ISeedReader worldIn, BlockPos pos, Random rand, int groupSize) {
    //    return performWorldGenSpawning(entityType, spawnType, Heightmap.Type.WORLD_SURFACE, worldIn, pos, rand, groupSize);
    //}

    public static boolean performWorldGenSpawning(EntityType<?> entityType, EntitySpawnPlacementRegistry.PlacementType spawnType, @Nullable Heightmap.Type heightMap, ISeedReader worldIn, BlockPos pos, Random rand, int groupSize) {
        //UntamedWilds.LOGGER.info(entityType);
        if (entityType != null && !worldIn.isRemote()) {
            int i = pos.getX() + rand.nextInt(16);
            int j = pos.getZ() + rand.nextInt(16);

            if (heightMap != null) {
                pos.add(i, 0, j);
                pos = worldIn.getHeight(heightMap, pos);
                //worldIn.setBlockState(pos, Blocks.TORCH.getDefaultState(), 2);
            }

            if (rand.nextFloat() < UntamedWildsGenerator.getBioDiversityLevel(Objects.requireNonNull(worldIn.getBiome(pos).getRegistryName()))) {
                int k = 1;
                int species = -1;
                for(int packSize = 0; packSize < k; ++packSize) {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    if (packSize != 0) {
                        // Do not offset the first entity of the pack
                        x += rand.nextInt(6);
                        z += rand.nextInt(6);
                    }

                    for(int attempt = 0; attempt < 4; ++attempt) { // 4 attempts at spawning are made for each mob
                        if (attempt != 0) {
                            if (attempt == 1) {
                                y += 1;
                            }
                            x += rand.nextInt(2);
                            z += rand.nextInt(2);
                        }

                        BlockPos blockpos = new BlockPos(x, y, z);
                        //BlockPos blockpos = getTopSolidOrLiquidBlock(worldIn, entityType, x, z);

                        if (entityType.isSummonable() && canCreatureTypeSpawnAtLocation(spawnType, worldIn, blockpos, entityType)) {
                            float f = entityType.getWidth();
                            double d0 = MathHelper.clamp(x, (double)blockpos.getX() + (double)f, (double)blockpos.getX() + 16.0D - (double)f);
                            double d1 = MathHelper.clamp(z, (double)blockpos.getZ() + (double)f, (double)blockpos.getZ() + 16.0D - (double)f);
                            if (!worldIn.hasNoCollisions(entityType.getBoundingBoxWithSizeApplied(d0, y, d1)) || !EntitySpawnPlacementRegistry.canSpawnEntity(entityType, worldIn, SpawnReason.CHUNK_GENERATION, blockpos, worldIn.getRandom())) {
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
                            entity.setLocationAndAngles(blockpos.getX(), blockpos.getY(), blockpos.getZ(), rand.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof MobEntity) {
                                MobEntity mobentity = (MobEntity)entity;
                                if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(mobentity, worldIn, d0, blockpos.getY(), d1, null, SpawnReason.CHUNK_GENERATION) == -1) continue;
                                if (mobentity.canSpawn(worldIn, SpawnReason.CHUNK_GENERATION) && mobentity.isNotColliding(worldIn)) {
                                    mobentity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(mobentity.getPosition()), SpawnReason.CHUNK_GENERATION, null, null);
                                    if (mobentity instanceof ComplexMob) {
                                        if (mobentity instanceof ISpecies) {
                                            if (species == -1) {
                                                species = ((ComplexMob)mobentity).getVariant();
                                                if (species != 99)
                                                    k = EntityUtils.getPackSize(entityType, species);
                                            } else {
                                                ((ComplexMob)mobentity).setVariant(species);
                                            } // Wrong spawning messages are most likely due to their inclusion on onMobSpawning, not here
                                        }
                                        // TODO: Jesus christ, I hate the AddReloadListenerEvent event
                                        if (((ComplexMob)mobentity).getVariant() == 99 || !ComplexMob.ENTITY_DATA_HASH.containsKey(entityType)) {
                                            mobentity.remove();
                                            return false;
                                        }
                                    }
                                    if (UntamedWilds.DEBUG && mobentity instanceof ComplexMob) {
                                        if (mobentity instanceof ISpecies) {
                                            UntamedWilds.LOGGER.info("Spawned: " + ((ComplexMob)mobentity).getGenderString() + " " + ((ISpecies)mobentity).getSpeciesName());
                                        }
                                        else {
                                            UntamedWilds.LOGGER.info("Spawned: " + ((ComplexMob)mobentity).getGenderString() + " " + mobentity.getName().getString());
                                        }
                                    }

                                    worldIn.func_242417_l(mobentity);
                                    break;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    /*private static BlockPos getTopSolidOrLiquidBlock(IWorldReader worldIn, @Nullable EntityType<?> entity, int posX, int posZ) {
        BlockPos blockpos = new BlockPos(posX, worldIn.getHeight(EntitySpawnPlacementRegistry.func_209342_b(entity), posX, posZ), posZ);
        BlockPos blockpos1 = blockpos.down();
        return worldIn.getBlockState(blockpos1).allowsMovement(worldIn, blockpos1, PathType.WATER) ? blockpos1 : blockpos;
    }*/
}