package untamedwilds.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigGamerules;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags;
import untamedwilds.util.EntityUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class CageBlockEntity extends BlockEntity {

    private CompoundTag data;
    private boolean locked;

    public CageBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TILE_ENTITY_CAGE.get(), pos, state);
    }

    public static boolean isBlacklisted(Entity entity) {
        return entity.getType().is(ModTags.EntityTags.CAGE_BLACKLIST);
    }

    public boolean cageEntity(Mob entity) {
        if (!this.isLocked()) {
            if (!isBlacklisted(entity) && (ConfigGamerules.easyMobCapturing.get() || entity.getTarget() == null)) {
                this.setTagCompound(EntityUtils.writeEntityToNBT(entity));
                this.setLocked(true);
                entity.discard();
                setChanged();
                return true;
            }
        }
        return false;
    }

    public boolean spawnCagedCreature(ServerLevel worldIn, BlockPos pos, boolean offsetHitbox) {
        if (!worldIn.isClientSide && this.isLocked()) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(this.getTagCompound(), null);
            if (entity != null) {
                if (worldIn.noCollision(entity.getAABB(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? entity.getHeight() + 1.2F : 0), pos.getZ() + 0.5F))) {
                    if (worldIn.getEntity(this.data.getCompound("EntityTag").getUUID("UUID")) != null) {
                        UntamedWilds.LOGGER.info("UUID is already present in the Level; Randomizing UUID for the new mob");
                        this.data.getCompound("EntityTag").putUUID("UUID", Mth.createInsecureUUID(worldIn.random));
                    }
                    Entity caged_entity = entity.create(worldIn, this.data, null, null, pos, MobSpawnType.DISPENSER, true, !Objects.equals(pos, this.getBlockPos()));
                    if (caged_entity != null) {
                        caged_entity.moveTo(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? caged_entity.getBbHeight() + 1.2 : 0.8), pos.getZ() + 0.5F, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                        if (!worldIn.tryAddFreshEntityWithPassengers(caged_entity)) {
                            caged_entity.setUUID(Mth.createInsecureUUID(worldIn.random));
                            worldIn.addFreshEntityWithPassengers(caged_entity);
                        }
                        this.setTagCompound(null);
                        this.setLocked(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    public CompoundTag getTagCompound() { return this.data; }

    private void setTagCompound(@Nullable CompoundTag nbt) { this.data = nbt; }

    public boolean hasTagCompound() { return this.data != null; }

    public boolean isLocked() { return this.locked; }

    private void setLocked(boolean locked) { this.locked = locked; }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.setTagCompound(compound.copy());
        this.setLocked(compound.getBoolean("closed"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("closed", this.isLocked());
        if (this.getTagCompound() != null) {
            compound.put("EntityTag", this.getTagCompound().getCompound("EntityTag"));
        }
    }
}