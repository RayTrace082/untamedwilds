package untamedwilds.network;

import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityDataHolderClient;

import java.util.HashMap;
import java.util.function.Supplier;

@Deprecated
public class SyncSoundData {

    private final ResourceLocation entityName;
    private final Integer id;
    private final String soundType;
    private final ResourceLocation soundResource;

    public SyncSoundData(PacketBuffer buf) {
        entityName = buf.readResourceLocation();
        id = buf.readInt();
        soundType = buf.readString();
        soundResource = buf.readResourceLocation();
    }

    public SyncSoundData(ResourceLocation str, Integer id, String soundType, ResourceLocation soundResource) {
        this.entityName = str;
        this.id = id;
        this.soundType = soundType;
        this.soundResource = soundResource;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(entityName);
        buf.writeInt(id);
        buf.writeString(soundType);
        buf.writeResourceLocation(soundResource);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Handling sound data for entity: " + entityName + " with species: " + id);
            }
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(entityName);
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(type)) {
                ComplexMob.CLIENT_DATA_HASH.put(type, new EntityDataHolderClient(new HashMap<>(), new HashMap<>()));
            }
            ComplexMob.CLIENT_DATA_HASH.get(type).addSoundData(id, soundType, soundResource);
            //ComplexMob.CLIENT_DATA_HASH.get(type).put(id, speciesName);
        });
        return true;
    }
}