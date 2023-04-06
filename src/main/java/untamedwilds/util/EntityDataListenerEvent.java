package untamedwilds.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMob;
import untamedwilds.init.ModEntity;
import untamedwilds.network.SyncTextureData;
import untamedwilds.network.UntamedInstance;

import java.util.HashMap;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class EntityDataListenerEvent {

    public static final JSONLoader<EntityDataHolder> ENTITY_DATA_HOLDERS = new JSONLoader<>("entities", EntityDataHolder.CODEC);
    public static EntityDataHolder TARANTULA;
    public static EntityDataHolder KING_CRAB;

    public static EntityDataHolder GIANT_CLAM;
    public static EntityDataHolder GIANT_SALAMANDER;
    public static EntityDataHolder NEWT;
    public static EntityDataHolder AROWANA;
    public static EntityDataHolder FOOTBALL_FISH;
    public static EntityDataHolder SHARK;
    public static EntityDataHolder SUNFISH;
    public static EntityDataHolder WHALE_SHARK;
    public static EntityDataHolder TREVALLY;
    public static EntityDataHolder TRIGGERFISH;
    public static EntityDataHolder CATFISH;
    public static EntityDataHolder SPADEFISH;

    public static EntityDataHolder SNAKE;
    public static EntityDataHolder ANACONDA;
    public static EntityDataHolder SOFTSHELL_TURTLE;
    public static EntityDataHolder TORTOISE;
    public static EntityDataHolder MONITOR;

    public static EntityDataHolder AARDVARK;
    public static EntityDataHolder HIPPO;
    public static EntityDataHolder RHINO;
    public static EntityDataHolder HYENA;
    public static EntityDataHolder BOAR;
    public static EntityDataHolder BEAR;
    public static EntityDataHolder BIG_CAT;
    public static EntityDataHolder BISON;
    public static EntityDataHolder CAMEL;
    public static EntityDataHolder MANATEE;
    public static EntityDataHolder BALEEN_WHALE;
    public static EntityDataHolder OPOSSUM;

    public static EntityDataHolder SPITTER;

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ENTITY_DATA_HOLDERS);
        registerData();
    }

    private static void registerData() {
        TARANTULA = registerEntityData(ModEntity.TARANTULA.get());
        KING_CRAB = registerEntityData(ModEntity.KING_CRAB.get());

        GIANT_CLAM = registerEntityData(ModEntity.GIANT_CLAM.get());

        GIANT_SALAMANDER = registerEntityData(ModEntity.GIANT_SALAMANDER.get());
        NEWT = registerEntityData(ModEntity.NEWT.get());

        AROWANA = registerEntityData(ModEntity.AROWANA.get());
        FOOTBALL_FISH = registerEntityData(ModEntity.FOOTBALL_FISH.get());
        SHARK = registerEntityData(ModEntity.SHARK.get());
        SUNFISH = registerEntityData(ModEntity.SUNFISH.get());
        TREVALLY = registerEntityData(ModEntity.TREVALLY.get());
        WHALE_SHARK = registerEntityData(ModEntity.WHALE_SHARK.get());
        TRIGGERFISH = registerEntityData(ModEntity.TRIGGERFISH.get());
        CATFISH = registerEntityData(ModEntity.CATFISH.get());
        SPADEFISH = registerEntityData(ModEntity.SPADEFISH.get());

        SNAKE = registerEntityData(ModEntity.SNAKE.get());
        ANACONDA = registerEntityData(ModEntity.ANACONDA.get());
        SOFTSHELL_TURTLE = registerEntityData(ModEntity.SOFTSHELL_TURTLE.get());
        TORTOISE = registerEntityData(ModEntity.TORTOISE.get());
        MONITOR = registerEntityData(ModEntity.MONITOR.get());

        BEAR = registerEntityData(ModEntity.BEAR.get());
        BIG_CAT = registerEntityData(ModEntity.BIG_CAT.get());
        AARDVARK = registerEntityData(ModEntity.AARDVARK.get());
        BOAR = registerEntityData(ModEntity.BOAR.get());
        RHINO = registerEntityData(ModEntity.RHINO.get());
        HYENA = registerEntityData(ModEntity.HYENA.get());
        HIPPO = registerEntityData(ModEntity.HIPPO.get());
        BISON = registerEntityData(ModEntity.BISON.get());
        CAMEL = registerEntityData(ModEntity.CAMEL.get());
        MANATEE = registerEntityData(ModEntity.MANATEE.get());
        BALEEN_WHALE = registerEntityData(ModEntity.BALEEN_WHALE.get());
        OPOSSUM = registerEntityData(ModEntity.OPOSSUM.get());

        SPITTER = registerEntityData(ModEntity.SPITTER.get());
    }

    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        UntamedWilds.LOGGER.info("Firing player login event");
        registerData();

        for (EntityType<?> types : ComplexMob.ENTITY_DATA_HASH.keySet()) {
            ResourceLocation entityName = types.builtInRegistryHolder().key().location();
            int size = 0;

            for (SpeciesDataHolder speciesData : ComplexMob.ENTITY_DATA_HASH.get(types).getSpeciesData()) {
                UntamedInstance.sendToClient(new SyncTextureData(entityName, speciesData.getName(), speciesData.getSkins(), size++), (ServerPlayer) event.getEntity());
            }
        }
    }

    public static EntityDataHolder registerEntityData(EntityType<?> typeIn) {
        String nameIn = Objects.requireNonNull(typeIn.builtInRegistryHolder().key().location()).getPath();
        if (ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn)) != null) {
            EntityDataHolder data = ENTITY_DATA_HOLDERS.getData(new ResourceLocation(UntamedWilds.MOD_ID, nameIn));
            processData(data, typeIn);
            return data;
        }
        return null;
    }

    /**
     * Method that links an EntityType with an EntityDataHolder object, and uses the EntityDataHolder to build a
     * hash with only Variant data, to be synced and accessed by the client
     * @param dataIn The EntityDataHolder to introduce in ENTITY_DATA_HASH
     * @param typeIn The EntityType to be associated with the dataIn object
     */
    private static void processData(EntityDataHolder dataIn, EntityType<?> typeIn) {
        ComplexMob.ENTITY_DATA_HASH.put(typeIn, dataIn);
        processSkins(dataIn, typeIn.getRegistryName().getPath());
        for (SpeciesDataHolder speciesData : ComplexMob.ENTITY_DATA_HASH.get(typeIn).getSpeciesData()) {
            if (!ComplexMob.CLIENT_DATA_HASH.containsKey(typeIn)) {
                ComplexMob.CLIENT_DATA_HASH.put(typeIn, new EntityDataHolderClient(new HashMap<>(), new HashMap<>()));
            }
            ComplexMob.CLIENT_DATA_HASH.get(typeIn).species_data.put(speciesData.getVariant(), speciesData.getName());
        }
    }

    private static void processSkins(EntityDataHolder dataIn, String nameIn) {
        for (SpeciesDataHolder speciesDatum : dataIn.getSpeciesData()) {
            EntityUtils.buildSkinArrays(nameIn, speciesDatum.getName().toLowerCase(), dataIn, speciesDatum.getVariant(), ComplexMob.TEXTURES_COMMON, ComplexMob.TEXTURES_RARE);
        }
    }
}