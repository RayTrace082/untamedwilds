package untamedwilds.block.blockentity;

import com.google.common.collect.Lists;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModEntity;

import javax.annotation.Nullable;
import java.util.List;

public class CritterBurrowBlockEntity extends TileEntity implements ITickableTileEntity {

    private final List<Inhabitants> inhabitants = Lists.newArrayList();
    private EntityType<?> mobToSpawn = ModEntity.AARDVARK;
    private int variant;
    private int count = 4;

    public CritterBurrowBlockEntity() {
        super(ModBlock.BLOCKENTITY_BURROW.get());
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty() {
        if (this.isNearFire()) {
            this.angerBees((PlayerEntity)null, this.world.getBlockState(this.getPos()), BeehiveTileEntity.State.EMERGENCY);
        }

        super.markDirty();
    }

    public boolean isNearFire() {
        if (this.world == null) {
            return false;
        } else {
            for(BlockPos blockpos : BlockPos.getAllInBoxMutable(this.pos.add(-1, -1, -1), this.pos.add(1, 1, 1))) {
                if (this.world.getBlockState(blockpos).getBlock() instanceof FireBlock) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean hasNoMobs() {
        return this.inhabitants.size() == 0;
    }

    public void angerBees(@Nullable PlayerEntity p_226963_1_, BlockState p_226963_2_, BeehiveTileEntity.State p_226963_3_) {
        List<Entity> list = this.trySpawnMobs(p_226963_2_, p_226963_3_);
        if (p_226963_1_ != null) {
            for(Entity entity : list) {
                if (entity instanceof BeeEntity) {
                    BeeEntity beeentity = (BeeEntity)entity;
                    if (p_226963_1_.getPositionVec().squareDistanceTo(entity.getPositionVec()) <= 16.0D) {
                        if (!this.isSmoked()) {
                            beeentity.setAttackTarget(p_226963_1_);
                        } else {
                            beeentity.setStayOutOfHiveCountdown(400);
                        }
                    }
                }
            }
        }

    }

    private List<Entity> trySpawnMobs(BlockState p_226965_1_, BeehiveTileEntity.State p_226965_2_) {
        List<Entity> list = Lists.newArrayList();
        this.inhabitants.removeIf((p_226966_4_) -> {
            return this.func_235651_a_(p_226965_1_, p_226966_4_, list, p_226965_2_);
        });
        return list;
    }

    public EntityType<?> getMobToSpawn() {
        return this.mobToSpawn;
    }

    public boolean isSmoked() {
        return CampfireBlock.isSmokingBlockAt(this.world, this.getPos());
    }

    public void tryEnterBurrow(Entity entityIn) {
        entityIn.stopRiding();
        entityIn.removePassengers();
        CompoundNBT compoundnbt = new CompoundNBT();
        entityIn.writeUnlessPassenger(compoundnbt);
        this.inhabitants.add(new Inhabitants(compoundnbt));
        if (this.world != null) {
            BlockPos blockpos = this.getPos();
            this.world.playSound((PlayerEntity)null, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        entityIn.remove();
    }

    private boolean func_235651_a_(BlockState p_235651_1_, Inhabitants p_235651_2_, @Nullable List<Entity> p_235651_3_, BeehiveTileEntity.State p_235651_4_) {
        if ((this.world.isNightTime() || this.world.isRaining()) && p_235651_4_ != BeehiveTileEntity.State.EMERGENCY) {
            return false;
        } else {
            BlockPos blockpos = this.getPos();
            CompoundNBT compoundnbt = p_235651_2_.entityData;
            compoundnbt.remove("Passengers");
            compoundnbt.remove("Leash");
            compoundnbt.remove("UUID");
            Direction direction = p_235651_1_.get(BeehiveBlock.FACING);
            BlockPos blockpos1 = blockpos.offset(direction);
            boolean flag = !this.world.getBlockState(blockpos1).getCollisionShape(this.world, blockpos1).isEmpty();
            if (flag && p_235651_4_ != BeehiveTileEntity.State.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityAndExecute(compoundnbt, this.world, (p_226960_0_) -> {
                    return p_226960_0_;
                });
                if (entity != null) {
                    if (!entity.getType().isContained(EntityTypeTags.BEEHIVE_INHABITORS)) {
                        return false;
                    } else {
                        if (entity instanceof BeeEntity) {
                            BeeEntity beeentity = (BeeEntity)entity;

                            if (p_235651_3_ != null) {
                                p_235651_3_.add(beeentity);
                            }

                            float f = entity.getWidth();
                            double d3 = flag ? 0.0D : 0.55D + (double)(f / 2.0F);
                            double d0 = (double)blockpos.getX() + 0.5D + d3 * (double)direction.getXOffset();
                            double d1 = (double)blockpos.getY() + 0.5D - (double)(entity.getHeight() / 2.0F);
                            double d2 = (double)blockpos.getZ() + 0.5D + d3 * (double)direction.getZOffset();
                            entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
                        }

                        this.world.playSound((PlayerEntity)null, blockpos, SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return this.world.addEntity(entity);
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public void setCount(int newCount) {
        this.count = newCount;
    }
    public int getCount() { return this.count; }

    public void setVariant(int variant) {
        this.variant = variant;
    }
    public int getVariant() { return this.variant; }

    public void setEntityType(EntityType<?> type) {
        this.mobToSpawn = type;
    }
    public EntityType<?> getEntityType() { return this.mobToSpawn; }

    public void tick() {
        if (!this.world.isRemote) {
            ServerWorld worldIn = (ServerWorld)this.world;
            BlockPos blockpos = this.getPos();
            if (this.world.getRandom().nextDouble() < 0.002D) {
                if (!this.getInhabitants().isEmpty()) {
                    int i = this.world.rand.nextInt(this.inhabitants.size());
                    Entity spawn = getMobToSpawn().create(worldIn, this.inhabitants.get(i).entityData, null, null, blockpos.offset(Direction.UP), SpawnReason.DISPENSER, true, false);
                    if (spawn != null) {
                        UntamedWilds.LOGGER.info("Spawning mob stored into burrow");
                        worldIn.addEntity(spawn);
                        this.inhabitants.remove(i);
                    }
                }
                else if (this.getCount() > 0) {
                    Entity spawn = getMobToSpawn().create(worldIn, null, null, null, blockpos.offset(Direction.UP), SpawnReason.CHUNK_GENERATION, true, false);
                    if (spawn != null) {
                        if (spawn instanceof MobEntity) {
                            ((MobEntity)spawn).onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(blockpos), SpawnReason.CHUNK_GENERATION, null, null);
                        }
                        if (spawn instanceof ComplexMob) {
                            ComplexMob entitySpawn = (ComplexMob) spawn;
                            entitySpawn.setVariant(this.variant);
                            entitySpawn.setHome(this.getPos());
                        }
                        UntamedWilds.LOGGER.info("Spawning new random mob from burrow");
                        worldIn.addEntity(spawn);
                        this.setCount(this.getCount() - 1);
                    }
                }
            }
        }
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.inhabitants.clear();
        ListNBT listnbt = nbt.getList("Bees", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            Inhabitants beehivetileentity$bee = new Inhabitants(compoundnbt.getCompound("EntityData"));
            this.inhabitants.add(beehivetileentity$bee);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("Bees", this.getInhabitants());

        return compound;
    }

    public ListNBT getInhabitants() {
        ListNBT listnbt = new ListNBT();

        for(Inhabitants beehivetileentity$bee : this.inhabitants) {
            beehivetileentity$bee.entityData.remove("UUID");
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.put("EntityData", beehivetileentity$bee.entityData);
            listnbt.add(compoundnbt);
        }

        return listnbt;
    }

    static class Inhabitants {
        private final CompoundNBT entityData;

        private Inhabitants(CompoundNBT nbt) {
            nbt.remove("UUID");
            this.entityData = nbt;
        }
    }
}