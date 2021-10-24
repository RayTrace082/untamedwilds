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
import untamedwilds.config.ConfigGamerules;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.ComplexMob;
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.amphibian.EntityNewt;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.*;
import untamedwilds.entity.mammal.*;
import untamedwilds.entity.mammal.bear.*;
import untamedwilds.entity.mammal.bigcat.*;
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
    public static EntityType<EntityHippo> HIPPO = createEntity(ConfigMobControl.addHippo.get(), EntityHippo::new,  "hippo",  1.8F, 1.8F, 0x463A31, 0x956761, animalType.APEX_PRED, 10, 5);
    public static EntityType<EntityAardvark> AARDVARK = createEntity(ConfigMobControl.addAardvark.get(), EntityAardvark::new,  "aardvark",  0.9F, 0.9F, 0x463A31, 0x956761, animalType.CRITTER, 2);
    public static EntityType<EntityRhino> RHINO = createEntity(ConfigMobControl.addRhino.get(), EntityRhino::new,  "rhino",  2.0F, 1.8F, 0x787676, 0x665956, animalType.APEX_PRED, 6);
    public static EntityType<EntityHyena> HYENA = createEntity(ConfigMobControl.addHyena.get(), EntityHyena::new,  "hyena",  0.9F, 1.1F, 0x6C6857, 0x978966, animalType.APEX_PRED, 10, 6);
    public static EntityType<EntityBoar> BOAR = createEntity(ConfigMobControl.addBoar.get(), EntityBoar::new,  "boar",  1.2F, 1.2F, 0x503C2A, 0x605449, animalType.APEX_PRED, 6, 3);
    // Bears
    public static EntityType<EntityBlackBear> BLACK_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityBlackBear::new,  "bear_black",  1.3F, 1.3F, 0x0B0A08, 0x3D3226, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityBlindBear> BLIND_BEAR = createEntity(ConfigMobControl.addBear.get() && ConfigGamerules.fantasyMobs.get(), EntityBlindBear::new,  "bear_blind",  1.6F, 1.6F, 0x241D1B, 0x4B3B35, animalType.LARGE_UNDERGROUND, 1, 0);
    public static EntityType<EntityBrownBear> BROWN_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityBrownBear::new,  "bear_brown",  1.5F, 1.5F, 0x624125, 0x20130B, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityCaveBear> CAVE_BEAR = createEntity(ConfigMobControl.addBear.get() && ConfigGamerules.extinctMobs.get(), EntityCaveBear::new,  "bear_cave",  1.6F, 1.6F, 0x564C45, 0x27190F, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityGiantPanda> PANDA_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityGiantPanda::new, "bear_panda",  1.2F, 1.2F, 15198183, 1776418, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityPolarBear> POLAR_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityPolarBear::new,  "bear_polar",  1.6F, 1.6F, 15921906, 9803152, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntitySpectacledBear> SPECTACLED_BEAR = createEntity(ConfigMobControl.addBear.get(), EntitySpectacledBear::new,  "bear_spectacled",  1.2F, 1.2F, 0x624125, 0x8E6E51, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntitySunBear> SUN_BEAR = createEntity(ConfigMobControl.addBear.get(), EntitySunBear::new,  "bear_sun",  0.9F, 0.9F, 0x0B0A08, 0xA27345, animalType.APEX_PRED, 1, 0);
    // Big Cats
    public static EntityType<EntityJaguar> JAGUAR = createEntity(ConfigMobControl.addBigCat.get(), EntityJaguar::new,  "bigcat_jaguar",  1.2F, 1.0F, 0xC59F45,0x383121, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityLeopard> LEOPARD = createEntity(ConfigMobControl.addBigCat.get(), EntityLeopard::new,  "bigcat_leopard",  1.2F, 1.0F, 0xC59F45, 0x383121, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityLion> LION = createEntity(ConfigMobControl.addBigCat.get(), EntityLion::new, "bigcat_lion", 1.3F, 1.2F, 0xDCBA84, 0x442917, animalType.APEX_PRED, 4, 0); // TODO: Have lions/cave lions/Sun Bear spawn in groups (currently hardcoded because it could be applied to other Big Cats)
    public static EntityType<EntityPuma> PUMA = createEntity(ConfigMobControl.addBigCat.get(), EntityPuma::new,  "bigcat_puma",  1.2F, 1.0F, 0x774C23, 0xECC38E, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntitySnowLeopard> SNOW_LEOPARD = createEntity(ConfigMobControl.addBigCat.get(), EntitySnowLeopard::new,  "bigcat_snow_leopard",  1.2F, 1.0F, 0xD3C38D, 0x46361C, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityTiger> TIGER = createEntity(ConfigMobControl.addBigCat.get(), EntityTiger::new,  "bigcat_tiger",  1.3F, 1.0F, 0xD1741D, 0x1A0400, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityCaveLion> CAVE_LION = createEntity(ConfigMobControl.addBigCat.get() && ConfigGamerules.extinctMobs.get(), EntityCaveLion::new,  "bigcat_cave_lion",  1.3F, 1.0F, 0x5B4924, 0xCCBC8F, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntityMarsupialLion> MARSUPIAL_LION = createEntity(ConfigMobControl.addBigCat.get() && ConfigGamerules.extinctMobs.get(), EntityMarsupialLion::new,  "bigcat_marsupial_lion",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1, 0);
    public static EntityType<EntitySabertooth> SABERTOOTH = createEntity(ConfigMobControl.addBigCat.get() && ConfigGamerules.extinctMobs.get(), EntitySabertooth::new,  "bigcat_sabertooth",  1.2F, 1.0F, 0x97845A, 0x3A3026, animalType.APEX_PRED, 1, 0);

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

        event.put(HIPPO, EntityHippo.registerAttributes().create());
        event.put(AARDVARK, EntityAardvark.registerAttributes().create());
        event.put(RHINO, EntityRhino.registerAttributes().create());
        event.put(HYENA, EntityHyena.registerAttributes().create());
        event.put(BOAR, EntityBoar.registerAttributes().create());
        event.put(BLACK_BEAR, EntityBlackBear.registerAttributes().create());
        event.put(BROWN_BEAR, EntityBrownBear.registerAttributes().create());
        event.put(CAVE_BEAR, EntityCaveBear.registerAttributes().create());
        event.put(BLIND_BEAR, EntityBlindBear.registerAttributes().create());
        event.put(PANDA_BEAR, EntityGiantPanda.registerAttributes().create());
        event.put(POLAR_BEAR, EntityPolarBear.registerAttributes().create());
        event.put(SPECTACLED_BEAR, EntitySpectacledBear.registerAttributes().create());
        event.put(SUN_BEAR, EntitySunBear.registerAttributes().create());

        event.put(JAGUAR, EntityJaguar.registerAttributes().create());
        event.put(LEOPARD, EntityLeopard.registerAttributes().create());
        event.put(LION, EntityLion.registerAttributes().create());
        event.put(PUMA, EntityPuma.registerAttributes().create());
        event.put(SNOW_LEOPARD, EntitySnowLeopard.registerAttributes().create());
        event.put(TIGER, EntityTiger.registerAttributes().create());
        event.put(CAVE_LION, EntityCaveLion.registerAttributes().create());
        event.put(MARSUPIAL_LION, EntityMarsupialLion.registerAttributes().create());
        event.put(SABERTOOTH, EntitySabertooth.registerAttributes().create());

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
        EntitySoftshellTurtle.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNAKE, RendererSnake::new);
        EntitySnake.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TORTOISE, RendererTortoise::new);
        EntityTortoise.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.ANACONDA, RendererAnaconda::new);
        EntityAnaconda.processSkins();

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_CLAM, RendererGiantClam::new);
        //EntityGiantClam.processSkins();

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.HIPPO, RendererHippo::new);
        EntityHippo.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.AARDVARK, RendererAardvark::new);
        EntityAardvark.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.RHINO, RendererRhino::new);
        EntityRhino.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.HYENA, RendererHyena::new);
        EntityHyena.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BOAR, RendererBoar::new);
        EntityBoar.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BLACK_BEAR, RendererBear::new);
        EntityBlackBear.registerTextures(EntityBlackBear.SKIN_NUMBER);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BLIND_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BROWN_BEAR, RendererBear::new);
        EntityBrownBear.registerTextures(EntityBrownBear.SKIN_NUMBER);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.CAVE_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PANDA_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.POLAR_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SPECTACLED_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUN_BEAR, RendererBear::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.JAGUAR, RendererBigCat::new);
        EntityJaguar.registerTextures(EntityJaguar.SKIN_NUMBER);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PUMA, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.CAVE_LION, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TIGER, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SABERTOOTH, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.MARSUPIAL_LION, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.LION, RendererBigCat::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.LEOPARD, RendererBigCat::new);
        EntityLeopard.registerTextures(EntityLeopard.SKIN_NUMBER);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNOW_LEOPARD, RendererBigCat::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUNFISH, RendererSunfish::new);
        EntitySunfish.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TREVALLY, RendererTrevally::new);
        EntityTrevally.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.AROWANA, RendererArowana::new);
        EntityArowana.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SHARK, RendererShark::new);
        EntityShark.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.FOOTBALL_FISH, RendererFootballFish::new);
        EntityFootballFish.processSkins();

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_SALAMANDER, RendererGiantSalamander::new);
        //EntityGiantSalamander.processSkins();
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.NEWT, RendererNewt::new);
        //EntityNewt.processSkins();

        if (UntamedWilds.DEBUG) {
            UntamedWilds.LOGGER.info("---Dump of Common and Rare Texture arrays---");
            UntamedWilds.LOGGER.info(ComplexMob.TEXTURES_COMMON);
            UntamedWilds.LOGGER.info(ComplexMob.TEXTURES_RARE);
        }
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