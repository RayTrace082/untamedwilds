package untamedwilds.util;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModEntity;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class ResourceListenerEvent {

    public static final JSONLoader<EntityDataHolder> ENTITY_DATA_HOLDERS = new JSONLoader<>("entities", EntityDataHolder.CODEC);
    public static EntityDataHolder TARANTULA;
    public static EntityDataHolder GIANT_CLAM;
    public static EntityDataHolder GIANT_SALAMANDER;
    public static EntityDataHolder NEWT;
    public static EntityDataHolder AROWANA;
    public static EntityDataHolder FOOTBALL_FISH;
    public static EntityDataHolder SHARK;
    public static EntityDataHolder SUNFISH;
    public static EntityDataHolder TREVALLY;

    public static EntityDataHolder BEAR;
    public static EntityDataHolder BIG_CAT;

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ENTITY_DATA_HOLDERS);
        TARANTULA = registerEntityData(ModEntity.TARANTULA);

        GIANT_CLAM = registerEntityData(ModEntity.GIANT_CLAM);

        GIANT_SALAMANDER = registerEntityData(ModEntity.GIANT_SALAMANDER);
        NEWT = registerEntityData(ModEntity.NEWT);

        AROWANA = registerEntityData(ModEntity.AROWANA);
        FOOTBALL_FISH = registerEntityData(ModEntity.FOOTBALL_FISH);
        SHARK = registerEntityData(ModEntity.SHARK);
        SUNFISH = registerEntityData(ModEntity.SUNFISH);
        TREVALLY = registerEntityData(ModEntity.TREVALLY);

        BEAR = registerEntityData(ModEntity.BEAR);
        BIG_CAT = registerEntityData(ModEntity.BIG_CAT);
    }

    public static void secondReloadListener(final AddReloadListenerEvent event) {
        TARANTULA = registerEntityData(ModEntity.TARANTULA);

        GIANT_CLAM = registerEntityData(ModEntity.GIANT_CLAM);

        GIANT_SALAMANDER = registerEntityData(ModEntity.GIANT_SALAMANDER);
        NEWT = registerEntityData(ModEntity.NEWT);

        AROWANA = registerEntityData(ModEntity.AROWANA);
        FOOTBALL_FISH = registerEntityData(ModEntity.FOOTBALL_FISH);
        SHARK = registerEntityData(ModEntity.SHARK);
        SUNFISH = registerEntityData(ModEntity.SUNFISH);
        TREVALLY = registerEntityData(ModEntity.TREVALLY);

        BEAR = registerEntityData(ModEntity.BEAR);
        BIG_CAT = registerEntityData(ModEntity.BIG_CAT);
    }

    // TODO: this should for the 2nd time before entities are loaded
    public static EntityDataHolder registerEntityData(EntityType<?> typeIn) {
        UntamedWilds.LOGGER.info(typeIn.getRegistryName().getPath());
        String nameIn = Objects.requireNonNull(typeIn.getRegistryName()).getPath();
        if (ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)) != null) {
            ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)).printSpeciesData();
            EntityDataHolder data = ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn));
            ComplexMob.processData(data, typeIn);
            return data;
        }
        return null;
    }
}