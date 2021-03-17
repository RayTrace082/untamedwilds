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
import net.minecraft.item.SpawnEggItem;
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
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.EntityArowana;
import untamedwilds.entity.fish.EntitySunfish;
import untamedwilds.entity.fish.EntityTrevally;
import untamedwilds.entity.mammal.EntityAardvark;
import untamedwilds.entity.mammal.EntityHippo;
import untamedwilds.entity.mammal.bear.*;
import untamedwilds.entity.mammal.bigcat.*;
import untamedwilds.entity.mollusk.EntityGiantClam;
import untamedwilds.entity.reptile.EntitySnake;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;
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
    public static EntityType<EntityTarantula> TARANTULA = createEntity(ConfigMobControl.addTarantula.get(), EntityTarantula::new,  "tarantula",  0.4f, 0.5f, 0xB5B095, 0x26292B, animalType.CRITTER, 4);

    // Reptiles
    public static EntityType<EntitySnake> SNAKE = createEntity(ConfigMobControl.addSnake.get(), EntitySnake::new,  "snake",  0.5f, 0.5f, 0xD8A552, 0x5C3525, animalType.CRITTER, 4, 2);
    public static EntityType<EntitySoftshellTurtle> SOFTSHELL_TURTLE = createEntity(ConfigMobControl.addSoftshellTurtle.get(), EntitySoftshellTurtle::new,  "softshell_turtle",  0.6f, 0.6f, 0x828444, 0x26292B, animalType.CRITTER, 3, 3);

    // Mollusks
    public static EntityType<EntityGiantClam> GIANT_CLAM = createEntity(ConfigMobControl.addGiantClam.get(), EntityGiantClam::new, EntityClassification.WATER_CREATURE, "giant_clam", 32, 10, true, 1.0F, 1.0F, 0x346B70, 0xAD713C, animalType.SESSILE, 1);

    // Mammals
    public static EntityType<EntityHippo> HIPPO = createEntity(ConfigMobControl.addHippo.get(), EntityHippo::new,  "hippo",  1.8F, 1.8F, 0x463A31, 0x956761, animalType.APEX_PRED, 6);
    public static EntityType<EntityAardvark> AARDVARK = createEntity(ConfigMobControl.addAardvark.get(), EntityAardvark::new,  "aardvark",  0.9F, 0.9F, 0x463A31, 0x956761, animalType.CRITTER, 2);
    // Bears
    public static EntityType<EntityBlackBear> BLACK_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityBlackBear::new,  "bear_black",  1.3F, 1.3F, 0x0B0A08, 0x3D3226, animalType.APEX_PRED, 1);
    public static EntityType<EntityBlindBear> BLIND_BEAR = createEntity(ConfigMobControl.addBear.get() && ConfigGamerules.fantasyMobs.get(), EntityBlindBear::new,  "bear_blind",  1.6F, 1.6F, 0x241D1B, 0x4B3B35, animalType.LARGE_UNDERGROUND, 1);
    public static EntityType<EntityBrownBear> BROWN_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityBrownBear::new,  "bear_brown",  1.5F, 1.5F, 0x624125, 0x20130B, animalType.APEX_PRED, 1);
    public static EntityType<EntityCaveBear> CAVE_BEAR = createEntity(ConfigMobControl.addBear.get() && ConfigGamerules.extinctMobs.get(), EntityCaveBear::new,  "bear_cave",  1.6F, 1.6F, 0x564C45, 0x27190F, animalType.APEX_PRED, 1);
    public static EntityType<EntityGiantPanda> PANDA_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityGiantPanda::new, "bear_panda",  1.2F, 1.2F, 15198183, 1776418, animalType.APEX_PRED, 1);
    public static EntityType<EntityPolarBear> POLAR_BEAR = createEntity(ConfigMobControl.addBear.get(), EntityPolarBear::new,  "bear_polar",  1.6F, 1.6F, 15921906, 9803152, animalType.APEX_PRED, 1);
    public static EntityType<EntitySpectacledBear> SPECTACLED_BEAR = createEntity(ConfigMobControl.addBear.get(), EntitySpectacledBear::new,  "bear_spectacled",  1.2F, 1.2F, 0x624125, 0x8E6E51, animalType.APEX_PRED, 1);
    public static EntityType<EntitySunBear> SUN_BEAR = createEntity(ConfigMobControl.addBear.get(), EntitySunBear::new,  "bear_sun",  0.9F, 0.9F, 0x0B0A08, 0xA27345, animalType.APEX_PRED, 1);
    // Big Cats
    public static EntityType<EntityJaguar> JAGUAR = createEntity(ConfigMobControl.addBigCat.get(), EntityJaguar::new,  "bigcat_jaguar",  1.2F, 1.0F, 0xC59F45,0x383121, animalType.APEX_PRED, 1);
    public static EntityType<EntityLeopard> LEOPARD = createEntity(ConfigMobControl.addBigCat.get(), EntityLeopard::new,  "bigcat_leopard",  1.2F, 1.0F, 0xC59F45, 0x383121, animalType.APEX_PRED, 1);
    public static EntityType<EntityLion> LION = createEntity(ConfigMobControl.addBigCat.get(), EntityLion::new, "bigcat_lion", 1.2F, 1.2F, 0xDCBA84, 0x442917, animalType.APEX_PRED, 4);
    public static EntityType<EntityPuma> PUMA = createEntity(ConfigMobControl.addBigCat.get(), EntityPuma::new,  "bigcat_puma",  1.2F, 1.0F, 0x774C23, 0xECC38E, animalType.APEX_PRED, 1);
    public static EntityType<EntitySnowLeopard> SNOW_LEOPARD = createEntity(ConfigMobControl.addBigCat.get(), EntitySnowLeopard::new,  "bigcat_snow_leopard",  1.2F, 1.0F, 0xD3C38D, 0x46361C, animalType.APEX_PRED, 1);
    public static EntityType<EntityTiger> TIGER = createEntity(ConfigMobControl.addBigCat.get(), EntityTiger::new,  "bigcat_tiger",  1.2F, 1.0F, 0xD1741D, 0x1A0400, animalType.APEX_PRED, 1);
    public static EntityType<EntityCaveLion> CAVE_LION = createEntity(UntamedWilds.DEBUG, EntityCaveLion::new,  "bigcat_cave_lion",  1.2F, 1.0F, 0x5B4924, 0xCCBC8F, animalType.APEX_PRED, 1);
    public static EntityType<EntityMarsupialLion> MARSUPIAL_LION = createEntity(UntamedWilds.DEBUG, EntityMarsupialLion::new,  "bigcat_marsupial_lion",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);
    public static EntityType<EntitySabertooth> SABERTOOTH = createEntity(UntamedWilds.DEBUG, EntitySabertooth::new,  "bigcat_sabertooth",  1.2F, 1.0F, 0x97845A, 0x3A3026, animalType.APEX_PRED, 1);
    public static EntityType<EntityDireLion> DIRE_LION = createEntity(UntamedWilds.DEBUG, EntityDireLion::new,  "bigcat_dire_lion",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);

    // Fish
    public static EntityType<EntitySunfish> SUNFISH = createEntity(ConfigMobControl.addSunfish.get(), EntitySunfish::new,  "sunfish",  1.6F, 1.6F, 0x2C545B, 0xB6D0D3, animalType.LARGE_OCEAN, 1);
    public static EntityType<EntityTrevally> TREVALLY = createEntity(ConfigMobControl.addTrevally.get(), EntityTrevally::new,  "trevally",  1.0F, 1.0F, 0xA5B4AF, 0xC89D17, animalType.LARGE_OCEAN, 2, 8);
    public static EntityType<EntityArowana> AROWANA = createEntity(ConfigMobControl.addArowana.get(), EntityArowana::new,  "arowana",  1.0F, 0.6F, 0x645C45, 0xB29F52, animalType.CRITTER, 1);

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
        if (enable) {
            ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, name);
            EntityType<T> type = EntityType.Builder.create(factory, classification)
                    .size(sizeX, sizeY)
                    .setTrackingRange(trackingRange)
                    .setUpdateInterval(updateFrequency)
                    .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                    .build(location.toString());
            spawnEggs.add(registerEntitySpawnEgg(type, name, maincolor, backcolor));
            type.setRegistryName(name);
            entities.add(type);
            if (ConfigMobControl.masterSpawner.get()) {
                addWorldSpawn(type, weight, spawnType, groupCount);
            }
            return type;
        }
        return null;
    }

    private static Item registerEntitySpawnEgg(EntityType<?> type, String name, int maincolor, int backcolor) {
        return new SpawnEggItem(type, maincolor, backcolor, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(name + "_spawn_egg");
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent event) {
        // TODO: I am 95% sure that with some fuckery this can be abstracted with a for-loop through `entities`
        event.put(TARANTULA, EntityTarantula.registerAttributes().create());

        event.put(SNAKE, EntitySnake.registerAttributes().create());
        event.put(SOFTSHELL_TURTLE, EntitySoftshellTurtle.registerAttributes().create());

        event.put(GIANT_CLAM, EntityGiantClam.registerAttributes().create());

        event.put(HIPPO, EntityHippo.registerAttributes().create());
        event.put(AARDVARK, EntityAardvark.registerAttributes().create());
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
        //event.put(CAVE_LION, CaveLionBigCat.registerAttributes().create());
        //event.put(DIRE_LION, DireLionBigCat.registerAttributes().create());
        //event.put(MARSUPIAL_LION, MarsupialLionBigCat.registerAttributes().create());
        //event.put(SABERTOOTH, SabertoothBigCat.registerAttributes().create());

        event.put(SUNFISH, EntitySunfish.registerAttributes().create());
        event.put(TREVALLY, EntityTrevally.registerAttributes().create());
        event.put(AROWANA, EntityArowana.registerAttributes().create());
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        for (Item spawnEgg : spawnEggs) {
            event.getRegistry().register(spawnEgg);
        }
    }

    public static void registerRendering() {
        if (ConfigMobControl.addTarantula.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.TARANTULA, RendererTarantula::new);
        }
        if (ConfigMobControl.addSoftshellTurtle.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.SOFTSHELL_TURTLE, RendererSoftshellTurtle::new);
        }
        if (ConfigMobControl.addSnake.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNAKE, RendererSnake::new);
        }
        if (ConfigMobControl.addGiantClam.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_CLAM, RendererGiantClam::new);
        }
        if (ConfigMobControl.addHippo.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.HIPPO, RendererHippo::new);
        }
        if (ConfigMobControl.addAardvark.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.AARDVARK, RendererAardvark::new);
        }
        if (ConfigMobControl.addBear.get()) {
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
        }
        if (ConfigMobControl.addBigCat.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.JAGUAR, RendererBigCat::new);
            EntityJaguar.registerTextures(EntityJaguar.SKIN_NUMBER);
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.PUMA, RendererBigCat::new);
            //RenderingRegistry.registerEntityRenderingHandler(ModEntity.CAVE_LION, RendererBigCat::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.TIGER, RendererBigCat::new);
            //RenderingRegistry.registerEntityRenderingHandler(ModEntity.SABERTOOTH, RendererBigCat::new);
            //RenderingRegistry.registerEntityRenderingHandler(ModEntity.MARSUPIAL_LION, RendererBigCat::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.LION, RendererBigCat::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.LEOPARD, RendererBigCat::new);
            EntityLeopard.registerTextures(EntityLeopard.SKIN_NUMBER);
            //RenderingRegistry.registerEntityRenderingHandler(ModEntity.DIRE_LION, RendererBigCat::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNOW_LEOPARD, RendererBigCat::new);
        }
        if (ConfigMobControl.addSunfish.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUNFISH, RendererSunfish::new);
        }
        if (ConfigMobControl.addTrevally.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.TREVALLY, RendererTrevally::new);
        }
        if (ConfigMobControl.addArowana.get()) {
            RenderingRegistry.registerEntityRenderingHandler(ModEntity.AROWANA, RendererArowana::new);
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