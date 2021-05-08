package untamedwilds.block.blockentity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModBlock;
import untamedwilds.util.EntityUtils;

import java.util.List;

public class CritterBurrowBlockEntity extends TileEntity implements ITickableTileEntity {

    private final List<Inhabitants> inhabitants = Lists.newArrayList();
    private EntityType<?> entityType;
    private int variant;
    private int count;

    public CritterBurrowBlockEntity() {
        super(ModBlock.BLOCKENTITY_BURROW.get());
    }

    public int getSumMobs() { return this.inhabitants.size() + this.count; }
    public boolean hasNoMobs() {
        return this.getSumMobs() == 0;
    }

    public void tryEnterBurrow(LivingEntity entityIn) {
        entityIn.stopRiding();
        entityIn.removePassengers();
        CompoundNBT compoundnbt = EntityUtils.writeEntityToNBT(entityIn);
        this.inhabitants.add(new Inhabitants(compoundnbt));
        if (this.world != null) {
            BlockPos blockpos = this.getPos();
            this.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        entityIn.remove();
        markDirty();
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
        this.entityType = type;
    }
    public EntityType<?> getEntityType() { return this.entityType; }

    public void tick() {
        if (this.world != null && !this.world.isRemote && !this.hasNoMobs() && this.getEntityType() != null && this.world.getRandom().nextDouble() < (Math.min(0.001, 0.0001D * (this.getSumMobs() * this.getSumMobs())))) {
            BlockPos blockpos = this.getPos();
            if (this.world.getRandom().nextInt(10) == 0) {
                this.setCount(this.getCount() + 1);
            }
            else if (this.getWorld().isPlayerWithin((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, ConfigMobControl.critterSpawnRange.get())) {
                ServerWorld worldIn = (ServerWorld)this.world;
                if (!this.getInhabitants().isEmpty()) {
                    int i = worldIn.rand.nextInt(this.inhabitants.size());
                    Entity spawn = this.getEntityType().create(worldIn, this.inhabitants.get(i).entityData, null, null, blockpos, SpawnReason.DISPENSER, true, false);
                    if (spawn != null) {
                        UntamedWilds.LOGGER.info("Spawning mob stored into burrow");
                        worldIn.addEntity(spawn);
                        this.inhabitants.remove(i);
                        markDirty();
                    }
                }
                else if (this.getCount() > 0 && this.getEntityType() != null) {
                    Entity spawn = this.getEntityType().create(worldIn, null, null, null, blockpos, SpawnReason.CHUNK_GENERATION, true, false);
                    if (spawn != null) {
                        if (spawn instanceof MobEntity) {
                            ((MobEntity)spawn).onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(blockpos), SpawnReason.CHUNK_GENERATION, null, null);
                        }
                        if (spawn instanceof ComplexMob) {
                            ComplexMob entitySpawn = (ComplexMob) spawn;
                            entitySpawn.setVariant(this.variant);
                            entitySpawn.setHome(this.getPos());
                        }
                        worldIn.addEntity(spawn);
                        this.setCount(this.getCount() - 1);
                        markDirty();
                    }
                }
            }
        }
    }

    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.inhabitants.clear();
        ListNBT listnbt = nbt.getList("Inhabitants", 10);
        this.setVariant(nbt.getInt("Variant"));
        this.setCount(nbt.getInt("Count"));
        if (nbt.contains("entityType")) {
            this.setEntityType(EntityType.byKey(nbt.getString("entityType")).orElse(null));
        }

        for(int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            Inhabitants beehivetileentity$bee = new Inhabitants(compoundnbt.getCompound("EntityData"));
            this.inhabitants.add(beehivetileentity$bee);
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("Inhabitants", this.getInhabitants());
        compound.putInt("Count", this.getCount());
        compound.putInt("Variant", this.getVariant());
        if (this.getEntityType() != null) {
            compound.putString("entityType", this.getEntityType().getRegistryName().toString());
        }
        return compound;
    }

    public ListNBT getInhabitants() {
        ListNBT listnbt = new ListNBT();

        for(Inhabitants inhabitant : this.inhabitants) {
            //beehivetileentity$bee.entityData.remove("UUID");
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.put("EntityData", inhabitant.entityData);
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