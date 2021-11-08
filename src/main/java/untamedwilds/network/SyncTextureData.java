package untamedwilds.network;

import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import java.util.HashMap;
import java.util.function.Supplier;

public class SyncTextureData {

    private final ResourceLocation entityName;
    private final String speciesName;
    private final Integer id;

    public SyncTextureData(PacketBuffer buf) {
        entityName = buf.readResourceLocation();
        speciesName = buf.readString();
        id = buf.readInt();
    }

    public SyncTextureData(ResourceLocation str, String species_name, Integer id) {
        this.entityName = str;
        this.speciesName = species_name;
        this.id = id;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(entityName);
        buf.writeString(speciesName);
        buf.writeInt(id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UntamedWilds.LOGGER.info("Handling texture data for entity: " + entityName + " with species: " + speciesName);
            EntityUtils.buildSkinArrays(entityName.getPath(), speciesName, id, ComplexMob.TEXTURES_COMMON, ComplexMob.TEXTURES_RARE);
            EntityType<?> type = ForgeRegistries.ENTITIES.getValue(entityName);
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(type)) {
                ComplexMob.CLIENT_DATA_HASH.put(type, new HashMap<>());
            }
            ComplexMob.CLIENT_DATA_HASH.get(type).put(id, speciesName);
            UntamedWilds.LOGGER.info(ComplexMob.CLIENT_DATA_HASH);
            // TODO: Store entityName and speciesName into a CLIENT-accessible array
        });
        return true;
    }

}