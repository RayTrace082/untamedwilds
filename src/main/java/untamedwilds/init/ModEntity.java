package untamedwilds.init;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.client.render.*;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.amphibian.EntityNewt;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.*;
import untamedwilds.entity.mammal.*;
import untamedwilds.entity.mollusk.EntityGiantClam;
import untamedwilds.entity.reptile.EntityAnaconda;
import untamedwilds.entity.reptile.EntitySnake;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;
import untamedwilds.entity.reptile.EntityTortoise;
import untamedwilds.item.UntamedSpawnEggItem;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaHandler.animalType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntity {
    private final static List<EntityType<? extends Entity>> entities = Lists.newArrayList();
    private final static List<Item> spawnEggs = Lists.newArrayList();
    public static final Map<String, Integer> eco_levels = new java.util.HashMap<>();

    // Arthropods
    public static EntityType<EntityTarantula> TARANTULA = createEntity(ConfigMobControl.addTarantula.get(), EntityTarantula::new,  "tarantula",  0.4f, 0.3f, 0xB5B095, 0x26292B, animalType.CRITTER, 4);

    // Reptiles
    public static EntityType<EntitySnake> SNAKE = createEntity(ConfigMobControl.addSnake.get(), EntitySnake::new,  "snake",  0.6f, 0.3f, 0xD8A552, 0x5C3525, animalType.CRITTER, 4, 1);
    public static EntityType<EntitySoftshellTurtle> SOFTSHELL_TURTLE = createEntity(ConfigMobControl.addSoftshellTurtle.get(), EntitySoftshellTurtle::new,  "softshell_turtle",  0.6f, 0.3f, 0x828444, 0x26292B, animalType.CRITTER, 3, 2);
    public static EntityType<EntityTortoise> TORTOISE = createEntity(ConfigMobControl.addTortoise.get(), EntityTortoise::new,  "tortoise",  0.6f, 0.6f, 0xAF9F74, 0x775232, animalType.CRITTER, 3, 2);
    public static EntityType<EntityAnaconda> ANACONDA = createEntity(ConfigMobControl.addLargeSnake.get(), EntityAnaconda::new,  "large_snake",  1.5f, 0.6f, 0x65704C, 0x42291A, animalType.APEX_PRED, 4, 1);

    // Mollusks
    public static EntityType<EntityGiantClam> GIANT_CLAM = createEntity(ConfigMobControl.addGiantClam.get(), EntityGiantClam::new, EntityClassification.WATER_CREATURE, "giant_clam", 32, 10, true, 1.0F, 1.0F, 0x346B70, 0xAD713C, animalType.SESSILE, 1);

    // Mammals
    public static EntityType<EntityBear> BEAR = createEntity(ConfigMobControl.addBear.get(), EntityBear::new,  "bear",  1.3F, 1.3F, 0x20130B, 0x564C45, animalType.APEX_PRED, 10, 1);
    public static EntityType<EntityBigCat> BIG_CAT = createEntity(ConfigMobControl.addBigCat.get(), EntityBigCat::new,  "big_cat",  1.2F, 1.0F, 0xC59F45,0x383121, animalType.APEX_PRED, 10, 1);
    public static EntityType<EntityHippo> HIPPO = createEntity(ConfigMobControl.addHippo.get(), EntityHippo::new,  "hippo",  1.8F, 1.8F, 0x463A31, 0x956761, animalType.APEX_PRED, 10, 5);
    public static EntityType<EntityAardvark> AARDVARK = createEntity(ConfigMobControl.addAardvark.get(), EntityAardvark::new,  "aardvark",  0.9F, 0.9F, 0x463A31, 0x956761, animalType.CRITTER, 2);
    public static EntityType<EntityRhino> RHINO = createEntity(ConfigMobControl.addRhino.get(), EntityRhino::new,  "rhino",  2.0F, 1.8F, 0x787676, 0x665956, animalType.APEX_PRED, 6);
    public static EntityType<EntityHyena> HYENA = createEntity(ConfigMobControl.addHyena.get(), EntityHyena::new,  "hyena",  0.9F, 1.1F, 0x6C6857, 0x978966, animalType.APEX_PRED, 10, 6);
    public static EntityType<EntityBoar> BOAR = createEntity(ConfigMobControl.addBoar.get(), EntityBoar::new,  "boar",  1.2F, 1.2F, 0x503C2A, 0x605449, animalType.APEX_PRED, 6, 3);
    public static EntityType<EntityBison> BISON = createEntity(ConfigMobControl.addBison.get(), EntityBison::new,  "bison",  1.7F, 1.6F, 0x845B2B, 0x49342A, animalType.APEX_PRED, 6, 6);

    // Fish
    public static EntityType<EntitySunfish> SUNFISH = createEntity(ConfigMobControl.addSunfish.get(), EntitySunfish::new,  "sunfish",  1.6F, 1.6F, 0x2C545B, 0xB6D0D3, animalType.LARGE_OCEAN, 3);
    public static EntityType<EntityTrevally> TREVALLY = createEntity(ConfigMobControl.addTrevally.get(), EntityTrevally::new,  "trevally",  0.8F, 0.8F, 0xA5B4AF, 0xC89D17, animalType.LARGE_OCEAN, 6, 8);
    public static EntityType<EntityArowana> AROWANA = createEntity(ConfigMobControl.addArowana.get(), EntityArowana::new,  "arowana",  0.6F, 0.6F, 0x645C45, 0xB29F52, animalType.DENSE_WATER, 1);
    public static EntityType<EntityShark> SHARK = createEntity(ConfigMobControl.addShark.get(), EntityShark::new,  "shark",  1.8F, 1.3F, 0x6B5142, 0xB0B0A3, animalType.LARGE_OCEAN, 2);
    public static EntityType<EntityFootballFish> FOOTBALL_FISH = createEntity(ConfigMobControl.addFootballFish.get(), EntityFootballFish::new,  "football_fish",  0.8F, 0.8F, 0x53556C, 0x2F3037, animalType.LARGE_OCEAN, 1);

    // Amphibians
    public static EntityType<EntityGiantSalamander> GIANT_SALAMANDER = createEntity(ConfigMobControl.addGiantSalamander.get(), EntityGiantSalamander::new,  "giant_salamander",  1F, 0.6f, 0x3A2C23, 0x6B5142, animalType.DENSE_WATER, 1);
    public static EntityType<EntityNewt> NEWT = createEntity(ConfigMobControl.addNewt.get(), EntityNewt::new,  "newt",  0.6F, 0.3f, 0x232323, 0xFF8D00, animalType.CRITTER, 2);

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        for (EntityType<?> entity : entities) {
            event.getRegistry().register(entity);
        }
        readEcoLevels();
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, EntityClassification classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int baseColor, int overlayColor) {
        return createEntity(enable, factory, classification, name, trackingRange, updateFrequency, sendsVelocityUpdates, sizeX, sizeY, baseColor, overlayColor, FaunaHandler.animalType.CRITTER, 1);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, String name, float sizeX, float sizeY, int baseColor, int overlayColor, FaunaHandler.animalType spawnType, int weight) {
        return createEntity(enable, factory, EntityClassification.CREATURE, name, 64, 1, true, sizeX, sizeY, baseColor, overlayColor, spawnType, weight);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, String name, float sizeX, float sizeY, int baseColor, int overlayColor, FaunaHandler.animalType spawnType, int weight, int groupCount) {
        return createEntity(enable, factory, EntityClassification.CREATURE, name, 64, 1, true, sizeX, sizeY, baseColor, overlayColor, spawnType, weight, groupCount);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, EntityClassification classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int maincolor, int backcolor, FaunaHandler.animalType spawnType, int weight) {
        return createEntity(enable, factory, EntityClassification.CREATURE, name, 64, 1, true, sizeX, sizeY, maincolor, backcolor, spawnType, weight, 1);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, EntityClassification classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int maincolor, int backcolor, FaunaHandler.animalType spawnType, int weight, int groupCount) {
        ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, name);
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .size(sizeX, sizeY)
                .setTrackingRange(trackingRange)
                .setUpdateInterval(updateFrequency)
                .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                .build(location.toString());
        type.setRegistryName(name);
        entities.add(type);
        if (enable) {
            spawnEggs.add(new UntamedSpawnEggItem(type, maincolor, backcolor, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(name + "_spawn_egg"));
            if (ConfigMobControl.masterSpawner.get()) {
                addWorldSpawn(type, weight, spawnType, groupCount);
            }
        }
        return type;
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent event) {
        // TODO: I am 95% sure that with some fuckery this can be abstracted with a for-loop through `entities`
        event.put(TARANTULA, EntityTarantula.registerAttributes().create());

        event.put(SNAKE, EntitySnake.registerAttributes().create());
        event.put(SOFTSHELL_TURTLE, EntitySoftshellTurtle.registerAttributes().create());
        event.put(TORTOISE, EntityTortoise.registerAttributes().create());
        event.put(ANACONDA, EntityAnaconda.registerAttributes().create());

        event.put(GIANT_CLAM, EntityGiantClam.registerAttributes().create());

        event.put(BEAR, EntityBear.registerAttributes().create());
        event.put(BIG_CAT, EntityBigCat.registerAttributes().create());
        event.put(HIPPO, EntityHippo.registerAttributes().create());
        event.put(AARDVARK, EntityAardvark.registerAttributes().create());
        event.put(RHINO, EntityRhino.registerAttributes().create());
        event.put(HYENA, EntityHyena.registerAttributes().create());
        event.put(BOAR, EntityBoar.registerAttributes().create());
        event.put(BISON, EntityBison.registerAttributes().create());

        event.put(SUNFISH, EntitySunfish.registerAttributes().create());
        event.put(TREVALLY, EntityTrevally.registerAttributes().create());
        event.put(AROWANA, EntityArowana.registerAttributes().create());
        event.put(SHARK, EntityShark.registerAttributes().create());
        event.put(FOOTBALL_FISH, EntityFootballFish.registerAttributes().create());

        event.put(GIANT_SALAMANDER, EntityGiantSalamander.registerAttributes().create());
        event.put(NEWT, EntityNewt.registerAttributes().create());

    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        for (Item spawnEgg : spawnEggs) {
            event.getRegistry().register(spawnEgg);
        }
    }

    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TARANTULA, RendererTarantula::new);
        //EntityTarantula.processSkins();

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SOFTSHELL_TURTLE, RendererSoftshellTurtle::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNAKE, RendererSnake::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TORTOISE, RendererTortoise::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.ANACONDA, RendererAnaconda::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_CLAM, RendererGiantClam::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BIG_CAT, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.HIPPO, RendererHippo::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.AARDVARK, RendererAardvark::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.RHINO, RendererRhino::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.HYENA, RendererHyena::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BOAR, RendererBoar::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BISON, RendererBison::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUNFISH, RendererSunfish::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TREVALLY, RendererTrevally::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.AROWANA, RendererArowana::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SHARK, RendererShark::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.FOOTBALL_FISH, RendererFootballFish::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_SALAMANDER, RendererGiantSalamander::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.NEWT, RendererNewt::new);
    }

    public static void addWorldSpawn(EntityType<?> entityClass, int weightedProb, FaunaHandler.animalType type, int groupCount) {
        List<FaunaHandler.SpawnListEntry> spawns = FaunaHandler.getSpawnableList(type);
        boolean found = false;
        for (FaunaHandler.SpawnListEntry entry : spawns) {
            // Adjusting an existing spawn entry
            if (entry.entityType == entityClass) {
                entry.itemWeight = weightedProb;
                entry.groupCount = groupCount;
                found = true;
                break;
            }
        }

        if (!found)
            spawns.add(new FaunaHandler.SpawnListEntry(entityClass, weightedProb, groupCount));
    }

    // TODO: Reminder to rework ecoLevels
    @Deprecated
    private static void readEcoLevels() {
        try (InputStream inputstream = LanguageMap.class.getResourceAsStream("/data/untamedwilds/eco_levels.json")) {
            JsonObject jsonobject = new Gson().fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonObject.class);

            for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                eco_levels.put(entry.getKey(), entry.getValue().getAsInt());
            }
        } catch (JsonParseException | IOException ioexception) {
            UntamedWilds.LOGGER.error("Couldn't read data from /data/untamedwilds/eco_levels.json", ioexception);
        }

    }

    /*public static void addVanillaSpawn(Class <? extends LivingEntity> entityClass, int weightedProb, int min, int max, BiomeDictionary.Type... biomes) {
        Set<Biome> spawnBiomes = new ObjectArraySet<>();
        for (Biome b : ForgeRegistries.BIOMES) {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            for(BiomeDictionary.Type biomeTypes : biomes) {
                if (types.contains(biomeTypes))
                    spawnBiomes.add(b);
            }
        }
    }*/
}