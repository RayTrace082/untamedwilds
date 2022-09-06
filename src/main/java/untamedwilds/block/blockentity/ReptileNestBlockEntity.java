package untamedwilds.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

public class ReptileNestBlockEntity extends BlockEntity {

    private EntityType<?> entityType = ModEntity.SOFTSHELL_TURTLE.get();
    private int variant = 1;
    private int eggCount = 4;

    public ReptileNestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TILE_ENTITY_NEST_REPTILE.get(), pos, state);
    }

    public int getSumMobs() { return this.eggCount; }
    public boolean hasNoMobs() {
        return this.getSumMobs() == 0;
    }

    public void createMobs(ServerLevel worldIn) {
        if (!this.hasNoMobs() && this.getEntityType() != null && worldIn.getRandom().nextFloat() < 0.05D) {
            BlockPos blockpos = this.getBlockPos();
            if (worldIn.hasNearbyAlivePlayer((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, ConfigMobControl.critterSpawnRange.get())) {
                int spawnCount = worldIn.getRandom().nextInt(3) + 1;
                for(int i = 0; i < spawnCount; ++i) {
                    Random rand = worldIn.getRandom();
                    float offsetX = rand.nextFloat();
                    float offsetZ = rand.nextFloat();
                    if (this.getEggCount() > 0 && this.getEntityType() != null && worldIn.noCollision(this.getEntityType().getAABB(blockpos.getX() + offsetX, blockpos.getY(), blockpos.getZ() + offsetZ).deflate(this.getEntityType().getWidth() / 4))) {
                        // Turns out that calling EntityType.create(...) will fucking crash the game if it pulls an invalid variant
                        //Entity spawn = this.getEntityType().create(worldIn, null, null, null, blockpos, MobSpawnType.CHUNK_GENERATION, true, false);
                        Entity spawn = this.getEntityType().create(worldIn);
                        if (spawn != null) {
                            spawn.moveTo(blockpos.getX() + offsetX, blockpos.getY(), blockpos.getZ() + offsetZ, Mth.wrapDegrees(rand.nextFloat() * 360.0F), 0.0F);
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
                            worldIn.addFreshEntityWithPassengers(spawn);
                            worldIn.playSound(null, this.getBlockPos(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + worldIn.getRandom().nextFloat() * 0.2F);
                            spawnParticles(worldIn, spawn.getX(), spawn.getY(), spawn.getZ(), new BlockParticleOption(ParticleTypes.BLOCK, Blocks.TURTLE_EGG.defaultBlockState()));
                            removeEggs(worldIn, 1);
                            setChanged();
                        }
                    }
                }
            }
        }
    }

    public void destroyEgg(Level p_57792_, BlockPos p_57793_, BlockState p_57794_) {
        p_57792_.playSound(null, p_57793_, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + p_57792_.random.nextFloat() * 0.2F);
        if (this.eggCount <= 1) {
            p_57792_.destroyBlock(p_57793_, false);
        } else {
            removeEggs(p_57792_, 1);
            p_57792_.levelEvent(2001, p_57793_, Block.getId(p_57794_));
        }
    }

    public void removeEggs(Level worldIn, int count) {
        this.setEggCount(this.eggCount - count);
        if (this.eggCount <= 0) {
            worldIn.destroyBlock(this.getBlockPos(), false);
        }
    }
    public void setEggCount(int newCount) {
        this.eggCount = newCount;
    }
    public int getEggCount() { return this.eggCount; }

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
        this.setEggCount(compound.getInt("Count"));
        if (compound.contains("entityType")) {
            this.setEntityType(EntityType.byString(compound.getString("entityType")).orElse(null));
        }
    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Count", this.getEggCount());
        compound.putInt("Variant", this.getVariant());
        if (this.getEntityType() != null) {
            compound.putString("entityType", this.getEntityType().getRegistryName().toString());
        }
    }
}