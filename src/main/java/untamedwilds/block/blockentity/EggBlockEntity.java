package untamedwilds.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.INeedsPostUpdate;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModEntity;
import untamedwilds.util.EntityUtils;

import java.util.Random;

public class EggBlockEntity extends BlockEntity {

    private EntityType<?> entityType = ModEntity.SPITTER.get();
    private int variant = 0;
    private boolean canSpawn = true;

    public EggBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TILE_ENTITY_EGG.get(), pos, state);
    }

    public void releaseOrCreateMob(ServerLevel worldIn) {
        if (this.getCanSpawn() && this.getEntityType() != null && this.getVariant() >= 0 && worldIn.getRandom().nextFloat() < 0.1D) {
            BlockPos blockpos = this.getBlockPos();
            if (worldIn.hasNearbyAlivePlayer((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, ConfigMobControl.critterSpawnRange.get())) {
                if (this.getEntityType() != null) {
                    // Turns out that calling EntityType.create(...) will fucking crash the game if it pulls an invalid variant
                    //Entity spawn = this.getEntityType().create(worldIn, null, null, null, blockpos, MobSpawnType.CHUNK_GENERATION, true, false);
                    Entity spawn = this.getEntityType().create(worldIn);
                    if (spawn != null) {
                        spawn.moveTo(blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                        if (spawn instanceof Mob mobSpawn) {
                            mobSpawn.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockpos), MobSpawnType.BREEDING, null, null);
                        }
                        if (spawn instanceof ComplexMob entitySpawn) {
                            entitySpawn.setVariant(EntityUtils.getClampedNumberOfSpecies(this.variant, this.entityType));
                            entitySpawn.setAge(ComplexMob.getEntityData(this.entityType).getGrowingTime(this.variant) * ConfigGamerules.cycleLength.get() * -2);
                            entitySpawn.setBaby(true);
                            entitySpawn.setGender(worldIn.getRandom().nextInt(2));
                            entitySpawn.setRandomMobSize();
                            entitySpawn.chooseSkinForSpecies(entitySpawn, true);
                            if (entitySpawn instanceof INeedsPostUpdate) {
                                ((INeedsPostUpdate) entitySpawn).updateAttributes();
                            }
                        }
                        spawnParticles(worldIn, spawn.getX(), spawn.getY(), spawn.getZ(), new BlockParticleOption(ParticleTypes.BLOCK, Blocks.TURTLE_EGG.defaultBlockState()));
                        worldIn.destroyBlock(this.getBlockPos(), false);
                        worldIn.addFreshEntityWithPassengers(spawn);
                        this.setCanSpawn(false);
                        setChanged();
                    }
                }
            }
        }
    }

    public void setCanSpawn(boolean canSpawn) {
        this.canSpawn = canSpawn;
    }
    public boolean getCanSpawn() { return this.canSpawn; }

    public void setVariant(int variant) {
        this.variant = variant;
    }
    public int getVariant() { return this.variant; }

    public void setEntityType(EntityType<?> type) {
        this.entityType = type;
    }
    public EntityType<?> getEntityType() { return this.entityType; }

    private <T extends ParticleOptions> void spawnParticles(Level worldIn, double x, double y, double z, T particle) {
        Random random = worldIn.getRandom();
        float d3 = random.nextFloat() * 0.02F;
        float d1 = random.nextFloat() * 0.02F;
        float d2 = random.nextFloat() * 0.02F;
        ((ServerLevel)worldIn).sendParticles(particle, x, y, z, 15, d3, d1, d2, 0.12F);
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setCanSpawn(compound.getBoolean("CanSpawn"));
        if (compound.contains("entityType")) {
            this.setEntityType(EntityType.byString(compound.getString("entityType")).orElse(null));
        }
    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("CanSpawn", this.getCanSpawn());
        compound.putInt("Variant", this.getVariant());
        if (this.getEntityType() != null) {
            compound.putString("entityType", this.getEntityType().getRegistryName().toString());
        }
    }
}