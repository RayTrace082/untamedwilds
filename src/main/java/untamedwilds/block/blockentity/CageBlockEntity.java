package untamedwilds.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
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
    private boolean caged;

    public CageBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TILE_ENTITY_CAGE.get(), pos, state);
    }

    public static boolean isBlacklisted(Entity entity) {
        return entity.getType().is(ModTags.EntityTags.CAGE_BLACKLIST);
    }

    public boolean cageEntity(Mob entity) {
        if (!this.hasCagedEntity()) {
            if (!isBlacklisted(entity) && (ConfigGamerules.easyMobCapturing.get() || entity.getTarget() == null)) {
                this.setTagCompound(EntityUtils.writeEntityToNBT(entity));
                this.setCagedEntity(true);
                entity.discard();
                setChanged();
                return true;
            }
        }
        return false;
    }

    public boolean spawnCagedCreature(ServerLevel worldIn, BlockPos pos, boolean offsetHitbox) {
        if (!worldIn.isClientSide && this.hasCagedEntity()) {
            EntityType<?> entity = EntityUtils.getEntityTypeFromTag(this.getTagCompound(), null);
            if (entity != null) {
                if (worldIn.noCollision(entity.getAABB(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? entity.getHeight() + 0.4F : 0), pos.getZ() + 0.5F))) {
                    if (worldIn.getEntity(this.data.getCompound("EntityTag").getUUID("UUID")) != null) {
                        UntamedWilds.LOGGER.info("Randomizing UUID for mob");
                        this.data.getCompound("EntityTag").putUUID("UUID", Mth.createInsecureUUID(worldIn.random));
                    }
                    Entity caged_entity = entity.create(worldIn, this.data, null, null, pos, MobSpawnType.DISPENSER, true, !Objects.equals(pos, this.getBlockPos()));
                    caged_entity.moveTo(pos.getX() + 0.5F, pos.getY() - (offsetHitbox ? caged_entity.getBbHeight() + 0.4 : 0.8), pos.getZ() + 0.5F, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                    if (!worldIn.tryAddFreshEntityWithPassengers(caged_entity)) {
                        caged_entity.setUUID(Mth.createInsecureUUID(worldIn.random));
                        worldIn.addFreshEntityWithPassengers(caged_entity);
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
    public CompoundTag getTagCompound() { return this.data; }

    private void setTagCompound(@Nullable CompoundTag nbt) { this.data = nbt; }

    public boolean hasTagCompound() { return this.data != null; }

    public boolean hasCagedEntity() { return this.caged; }

    private void setCagedEntity(boolean trap) { this.caged = trap; }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.setTagCompound(compound.copy());
        this.setCagedEntity(compound.getBoolean("closed"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("closed", this.hasCagedEntity());
        if (this.getTagCompound() != null) {
            compound.put("EntityTag", this.getTagCompound().getCompound("EntityTag"));
        }
    }
}