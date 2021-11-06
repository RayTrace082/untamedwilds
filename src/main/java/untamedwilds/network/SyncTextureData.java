package untamedwilds.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityUtils;

import java.util.function.Supplier;

public class SyncTextureData {

    private final String entityName;
    private final String speciesName;
    private final Integer id;

    public SyncTextureData(PacketBuffer buf) {
        entityName = buf.readString();
        speciesName = buf.readString();
        id = buf.readInt();
    }

    public SyncTextureData(String str, String species_name, Integer id) {
        this.entityName = str;
        this.speciesName = species_name;
        this.id = id;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(entityName);
        buf.writeString(speciesName);
        buf.writeInt(id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            UntamedWilds.LOGGER.info("Handling texture data for entity: " + entityName + " with species: " + speciesName);
            EntityUtils.buildSkinArrays(entityName, speciesName, id, ComplexMob.TEXTURES_COMMON, ComplexMob.TEXTURES_RARE);
            // TODO: Store entityName and speciesName into a CLIENT-accessible array
        });
        return true;
    }

}