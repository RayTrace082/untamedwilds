package untamedwilds.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.world.FaunaHandler;

import java.util.List;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class SpawnDataListenerEvent {

    public static final JSONLoader<SpawnDataHolder> SPAWN_DATA_HOLDER = new JSONLoader<>("spawn_tables", SpawnDataHolder.CODEC);
    public static SpawnDataHolder CRITTER;

    public static SpawnDataHolder HERBIVORES;
    public static SpawnDataHolder PREDATORS;

    public static SpawnDataHolder BENTHOS;
    public static SpawnDataHolder WATER_OCEAN;
    public static SpawnDataHolder WATER_RIVER;

    public static SpawnDataHolder UNDERGROUND;

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SPAWN_DATA_HOLDER);
        registerData();
    }

    private static void registerData() {
        CRITTER = registerSpawnData("critter");

        HERBIVORES = registerSpawnData("herbivores");
        PREDATORS = registerSpawnData("predators");

        BENTHOS = registerSpawnData("benthos");
        WATER_OCEAN = registerSpawnData("water_ocean");
        WATER_RIVER = registerSpawnData("water_river");

        UNDERGROUND = registerSpawnData("underground");
    }

    public static SpawnDataHolder registerSpawnData(String nameIn) {
        if (SPAWN_DATA_HOLDER.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)) != null) {
            SpawnDataHolder data = SPAWN_DATA_HOLDER.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn));

            if (FaunaHandler.getSpawnableList(nameIn) != null) {
                List<FaunaHandler.SpawnListEntry> spawn_list = FaunaHandler.getSpawnableList(nameIn);
                if (!spawn_list.isEmpty())
                    spawn_list.addAll(data.getEntries());
                return data;
            }
        }
        return null;
    }
}