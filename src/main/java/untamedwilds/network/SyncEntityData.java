package untamedwilds.network;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityDataHolder;

import java.util.function.Supplier;

public class SyncEntityData {

    private final ResourceLocation id;
    private final CompoundNBT data;

    public SyncEntityData(PacketBuffer buf) {
        id = buf.readResourceLocation();
        data = buf.readCompoundTag();
    }

    public SyncEntityData(ResourceLocation id, CompoundNBT data) {
        this.id = id;
        this.data = data;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(id);
        buf.writeCompoundTag(data);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UntamedWilds.LOGGER.info("Handling packet with " + data.toString());
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(id);
            if (entityType == null) {
                throw new IllegalStateException("This cannot happen! Unknown id '" + id.toString() + "'!");
            }
            if (!ComplexMob.ENTITY_DATA_HASH.containsKey(entityType)) {
                ComplexMob.ENTITY_DATA_HASH.put(entityType, new EntityDataHolder(data)); // TODO: Unnecessary
                ComplexMob.processData(new EntityDataHolder(data), entityType);
            }
        });
        return true;
    }

}