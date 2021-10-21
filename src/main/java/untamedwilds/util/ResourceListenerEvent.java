package untamedwilds.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.init.ModEntity;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ResourceListenerEvent {

    public static final JSONLoader<EntityDataHolder> ENTITY_DATA_HOLDERS = new JSONLoader<>("entities", EntityDataHolder.CODEC);
    public static EntityDataHolder TARANTULA;
    public static EntityDataHolder GIANT_CLAM;

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ENTITY_DATA_HOLDERS);
        if (ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, "tarantula")) != null) {
            TARANTULA = ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, "tarantula"));
            EntityTarantula.processData(TARANTULA, ModEntity.TARANTULA);
            UntamedWilds.LOGGER.info(TARANTULA.getString());
            TARANTULA.printSpeciesData();
        }
        if (ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, "giant_clam")) != null) {
            GIANT_CLAM = ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, "giant_clam"));
            EntityTarantula.processData(GIANT_CLAM, ModEntity.GIANT_CLAM);
            UntamedWilds.LOGGER.info(GIANT_CLAM.getString());
            GIANT_CLAM.printSpeciesData();
        }
    }
}