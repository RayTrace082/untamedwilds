package untamedwilds.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModEntity;
import untamedwilds.network.SyncTextureData;
import untamedwilds.network.UntamedInstance;

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
    public static EntityDataHolder SNAKE;
    public static EntityDataHolder ANACONDA;
    public static EntityDataHolder SOFTSHELL_TURTLE;
    public static EntityDataHolder TORTOISE;

    public static EntityDataHolder AARDVARK;
    public static EntityDataHolder HIPPO;
    public static EntityDataHolder RHINO;
    public static EntityDataHolder HYENA;
    public static EntityDataHolder BOAR;
    public static EntityDataHolder BEAR;
    public static EntityDataHolder BIG_CAT;

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ENTITY_DATA_HOLDERS);
        registerData();
    }

    private static void registerData() {
        TARANTULA = registerEntityData(ModEntity.TARANTULA);

        GIANT_CLAM = registerEntityData(ModEntity.GIANT_CLAM);

        GIANT_SALAMANDER = registerEntityData(ModEntity.GIANT_SALAMANDER);
        NEWT = registerEntityData(ModEntity.NEWT);

        AROWANA = registerEntityData(ModEntity.AROWANA);
        FOOTBALL_FISH = registerEntityData(ModEntity.FOOTBALL_FISH);
        SHARK = registerEntityData(ModEntity.SHARK);
        SUNFISH = registerEntityData(ModEntity.SUNFISH);
        TREVALLY = registerEntityData(ModEntity.TREVALLY);

        SNAKE = registerEntityData(ModEntity.SNAKE);
        ANACONDA = registerEntityData(ModEntity.ANACONDA);
        SOFTSHELL_TURTLE = registerEntityData(ModEntity.SOFTSHELL_TURTLE);
        TORTOISE = registerEntityData(ModEntity.TORTOISE);

        BEAR = registerEntityData(ModEntity.BEAR);
        BIG_CAT = registerEntityData(ModEntity.BIG_CAT);
        AARDVARK = registerEntityData(ModEntity.AARDVARK);
        BOAR = registerEntityData(ModEntity.BOAR);
        RHINO = registerEntityData(ModEntity.RHINO);
        HYENA = registerEntityData(ModEntity.HYENA);
        HIPPO = registerEntityData(ModEntity.HIPPO);
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        UntamedWilds.LOGGER.info("Firing player login event");
        registerData();
        for (EntityType<?> types : ComplexMob.ENTITY_DATA_HASH.keySet()) {
            String entityName = types.getRegistryName().getPath();
            int size = 0;
            UntamedWilds.LOGGER.info("Sending entity data for " + entityName);
            //UntamedInstance.sendToClient(new SyncEntityData(types.getRegistryName(), ComplexMob.ENTITY_DATA_HASH.get(types).writeEntityDataToNBT()), (ServerPlayerEntity) event.getPlayer());
            for (SpeciesDataHolder speciesData : ComplexMob.ENTITY_DATA_HASH.get(types).getSpeciesData()) {
                UntamedInstance.sendToClient(new SyncTextureData(entityName, speciesData.getName(), size++), (ServerPlayerEntity) event.getPlayer());
            }
            // TODO: Send a single array with the species data (species name + number of species) to do all calcs
        }
    }

    public static EntityDataHolder registerEntityData(EntityType<?> typeIn) {
        UntamedWilds.LOGGER.info(typeIn.getRegistryName().getPath());
        String nameIn = Objects.requireNonNull(typeIn.getRegistryName()).getPath();
        if (ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)) != null) {
            //ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)).printSpeciesData();
            EntityDataHolder data = ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn));
            ComplexMob.processData(data, typeIn);
            return data;
        }
        return null;
    }
}