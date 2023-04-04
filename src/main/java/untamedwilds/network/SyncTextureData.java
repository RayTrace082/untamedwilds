package untamedwilds.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.util.EntityDataHolderClient;
import untamedwilds.util.EntityUtils;

import java.util.HashMap;
import java.util.function.Supplier;

public class SyncTextureData {

    private final ResourceLocation entityName;
    private final String speciesName;
    private final Integer skinsData;
    private final Integer id;

    public SyncTextureData(FriendlyByteBuf buf) {
        entityName = buf.readResourceLocation();
        speciesName = buf.readUtf();
        skinsData = buf.readInt();
        id = buf.readInt();
    }

    public SyncTextureData(ResourceLocation str, String species_name, Integer skins, Integer id) {
        this.entityName = str;
        this.speciesName = species_name;
        this.skinsData = skins;
        this.id = id;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(entityName);
        buf.writeUtf(speciesName);
        buf.writeInt(skinsData);
        buf.writeInt(id);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (UntamedWilds.DEBUG) {
                UntamedWilds.LOGGER.info("Handling texture data for entity: " + entityName + " with species: " + speciesName);
            }
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(entityName);
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(type)) {
                ComplexMob.CLIENT_DATA_HASH.put(type, new EntityDataHolderClient(new HashMap<>(), new HashMap<>()));
            }
            EntityUtils.buildSkinArrays(entityName.getPath(), speciesName, skinsData, id, ComplexMob.TEXTURES_COMMON, ComplexMob.TEXTURES_RARE);
            ComplexMob.CLIENT_DATA_HASH.get(type).addSpeciesName(id, speciesName);
        });
        return true;
    }
}