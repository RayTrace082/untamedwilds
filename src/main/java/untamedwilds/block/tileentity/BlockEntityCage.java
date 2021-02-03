package untamedwilds.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockEntityCage extends TileEntity {

    private CompoundNBT spawndata;
    private boolean caged;

    public BlockEntityCage() {
        super(ModBlock.BLOCKENTITY_CAGE.get());
    }

    public static boolean isBlacklisted(Entity entity) {
        return EntityTypeTags.getCollection().get(ModTags.EntityTags.CAGE_BLACKLIST).contains(entity.getType());
    }

    public boolean cageEntity(Entity entity) {
        if (!this.hasCagedEntity()) {
            if (!isBlacklisted(entity)) {
                String entityID = EntityType.getKey(entity.getType()).toString();
                CompoundNBT nbttagcompound = new CompoundNBT();
                nbttagcompound.putString("id", entityID);
                CompoundNBT nbttagcompound2 = new CompoundNBT();
                entity.writeUnlessRemoved(nbttagcompound2);
                nbttagcompound.put("EntityTag", nbttagcompound2);
                this.setTagCompound(nbttagcompound);
                this.setCagedEntity(true);
                entity.remove();
                markDirty();
                return true;
            }
        }
        return false;
    }

    public void spawnCagedCreature(ServerWorld worldIn, BlockPos pos, boolean offsetHitbox) {
        if (!worldIn.isRemote && this.hasCagedEntity()) {

            EntityType<?> entity = this.getType(this.spawndata);
            if (entity != null) {
                Entity caged_entity = entity.create(worldIn, this.spawndata, null, null, pos, SpawnReason.BUCKET, true, !Objects.equals(pos, this.getPos()));
                caged_entity.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? caged_entity.getHeight() : 1), pos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);

                if (!worldIn.addEntityIfNotDuplicate(caged_entity)) {
                    caged_entity.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                    worldIn.addEntity(caged_entity);
                }
                this.setTagCompound(null);
            }
        }
    }

    @Nullable
    private EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT entityNBT = nbt.getCompound("EntityTag");
            if (entityNBT.contains("id", 8)) {
                return EntityType.byKey(entityNBT.getString("id")).orElse(null);
            }
        }
        return null;
    }

    @Nullable
    public CompoundNBT getTagCompound() { return this.spawndata; }

    private void setTagCompound(@Nullable CompoundNBT nbt) { this.spawndata = nbt; }

    public boolean hasTagCompound() { return this.spawndata != null; }

    public boolean hasCagedEntity() { return this.caged; }

    private void setCagedEntity(boolean trap) { this.caged = trap; }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.setTagCompound(compound.getCompound("EntityTag"));
        this.setCagedEntity(compound.getBoolean("closed"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("closed", hasCagedEntity());
        if (this.getTagCompound() != null) {
            compound.put("EntityTag", this.getTagCompound());
        }
        return compound;
    }
}