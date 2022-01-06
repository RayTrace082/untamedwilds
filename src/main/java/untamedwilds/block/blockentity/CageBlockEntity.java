package untamedwilds.block.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class CageBlockEntity extends TileEntity {

    private CompoundNBT spawndata;
    private boolean caged;

    public CageBlockEntity() {
        super(ModBlock.TILE_ENTITY_CAGE.get());
    }

    public static boolean isBlacklisted(Entity entity) {
        return EntityTypeTags.getCollection().get(ModTags.EntityTags.CAGE_BLACKLIST).contains(entity.getType());
    }

    public boolean cageEntity(MobEntity entity) {
        if (!this.hasCagedEntity()) {
            if (!isBlacklisted(entity) && (ConfigGamerules.easyMobCapturing.get() || entity.getAttackTarget() == null)) {
                ((ComplexMob)entity).setVariant(99);
                this.setTagCompound(EntityUtils.writeEntityToNBT( entity));
                this.setCagedEntity(true);
                entity.remove();
                markDirty();
                return true;
            }
        }
        return false;
    }

    public boolean spawnCagedCreature(ServerWorld worldIn, BlockPos pos, boolean offsetHitbox) {
        if (!worldIn.isRemote && this.hasCagedEntity()) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(this.getTagCompound(), null);
            if (entity != null) {
                if (worldIn.hasNoCollisions(entity.getBoundingBoxWithSizeApplied(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? entity.getHeight() + 0.4F : 0), pos.getZ() + 0.5F))) {
                    if (worldIn.getEntityByUuid(this.spawndata.getCompound("EntityTag").getUniqueId("UUID")) != null) {
                        UntamedWilds.LOGGER.info("Randomizing UUID for mob");
                        this.spawndata.getCompound("EntityTag").putUniqueId("UUID", MathHelper.getRandomUUID(worldIn.rand));
                    }
                    Entity caged_entity = entity.create(worldIn, this.spawndata, null, null, pos, SpawnReason.DISPENSER, true, !Objects.equals(pos, this.getPos()));
                    caged_entity.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? caged_entity.getHeight() + 0.4 : 0.8), pos.getZ() + 0.5F, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    if (!worldIn.addEntityIfNotDuplicate(caged_entity)) {
                        caged_entity.setUniqueId(MathHelper.getRandomUUID(worldIn.rand));
                        worldIn.addEntity(caged_entity);
                    }
                    this.setTagCompound(null);
                    this.caged = true;
                    return true;
                }
            }
        }
        return false;
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
        this.setTagCompound(compound.copy());
        this.setCagedEntity(compound.getBoolean("closed"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("closed", this.hasCagedEntity());
        if (this.getTagCompound() != null) {
            compound.put("EntityTag", this.getTagCompound().getCompound("EntityTag"));
        }
        return compound;
    }
}