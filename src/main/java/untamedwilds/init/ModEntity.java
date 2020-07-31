package untamedwilds.init;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.client.model.*;
import untamedwilds.client.render.*;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.arthropod.Tarantula;
import untamedwilds.entity.fish.Sunfish;
import untamedwilds.entity.mammal.bear.*;
import untamedwilds.entity.mammal.bigcat.*;
import untamedwilds.entity.mollusk.GiantClam;
import untamedwilds.entity.reptile.EntitySnake;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;
import untamedwilds.world.FaunaHandler;
import untamedwilds.world.FaunaHandler.animalType;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntity {
    private final static List<EntityType<?>> entities = Lists.newArrayList();
    private final static List<Item> spawnEggs = Lists.newArrayList();

    // Arthropods
    public static EntityType<Tarantula> TARANTULA = createEntity(ConfigMobControl.addTarantula.get(), Tarantula::new,  "tarantula",  0.4f, 0.5f, 0xB5B095, 0x26292B, animalType.CRITTER, 4);

    // Reptiles
    public static EntityType<EntitySnake> SNAKE = createEntity(ConfigMobControl.addSnake.get(), EntitySnake::new,  "snake",  0.5f, 0.5f, 0xD8A552, 0x5C3525, animalType.CRITTER, 4);
    public static EntityType<EntitySoftshellTurtle> SOFTSHELL_TURTLE = createEntity(ConfigMobControl.addSoftshellTurtle.get(), EntitySoftshellTurtle::new,  "softshell_turtle",  0.6f, 0.6f, 0x828444, 0x26292B, animalType.CRITTER, 3);

    // Mollusks
    public static EntityType<GiantClam> GIANT_CLAM = createEntity(ConfigMobControl.addGiantClam.get(), GiantClam::new, EntityClassification.WATER_CREATURE, "giant_clam", 32, 10, true, 1.0F, 1.0F, 0x346B70, 0xAD713C, animalType.SESSILE, 1);

    // Mammals
    // Bears
    public static EntityType<BlackBear> BLACK_BEAR = createEntity(ConfigMobControl.addBear.get(), BlackBear::new,  "bear_black",  1.3F, 1.3F, 0x0B0A08, 0x3D3226, animalType.APEX_PRED, 1);
    public static EntityType<BlindBear> BLIND_BEAR = createEntity(ConfigMobControl.addBear.get(), BlindBear::new,  "bear_blind",  1.6F, 1.6F, 0x241D1B, 0x4B3B35, animalType.LARGE_UNDERGROUND, 1);
    public static EntityType<BrownBear> BROWN_BEAR = createEntity(ConfigMobControl.addBear.get(), BrownBear::new,  "bear_brown",  1.5F, 1.5F, 0x624125, 0x20130B, animalType.APEX_PRED, 1);
    public static EntityType<CaveBear> CAVE_BEAR = createEntity(ConfigMobControl.addBear.get(), CaveBear::new,  "bear_cave",  1.6F, 1.6F, 0x564C45, 0x27190F, animalType.APEX_PRED, 1);
    public static EntityType<PandaBear> PANDA_BEAR = createEntity(ConfigMobControl.addBear.get(), PandaBear::new,  "bear_panda",  1.2F, 1.2F, 15198183, 1776418, animalType.APEX_PRED, 1);
    public static EntityType<PolarBear> POLAR_BEAR = createEntity(ConfigMobControl.addBear.get(), PolarBear::new,  "bear_polar",  1.6F, 1.6F, 15921906, 9803152, animalType.APEX_PRED, 1);
    public static EntityType<SpectacledBear> SPECTACLED_BEAR = createEntity(ConfigMobControl.addBear.get(), SpectacledBear::new,  "bear_spectacled",  1.2F, 1.2F, 0x624125, 0x8E6E51, animalType.APEX_PRED, 1);
    public static EntityType<SunBear> SUN_BEAR = createEntity(ConfigMobControl.addBear.get(), SunBear::new,  "bear_sun",  0.9F, 0.9F, 0x0B0A08, 0xA27345, animalType.APEX_PRED, 1);
    // Big Cats
    public static EntityType<JaguarBigCat> JAGUAR = createEntity(UntamedWilds.DEBUG, JaguarBigCat::new,  "bigcat_jaguar",  1.2F, 1.0F, 0xC59F45,0x383121, animalType.APEX_PRED, 1);
    public static EntityType<LeopardBigCat> LEOPARD = createEntity(UntamedWilds.DEBUG, LeopardBigCat::new,  "bigcat_leopard",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);
    public static EntityType<LionBigCat> LION = createEntity(UntamedWilds.DEBUG, LionBigCat::new, "bigcat_lion", 1.2F, 1.2F, 0x346B70, 0xAD713C, animalType.APEX_PRED, 1);
    public static EntityType<PantherBigCat> PANTHER = createEntity(UntamedWilds.DEBUG, PantherBigCat::new,  "bigcat_panther",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);
    public static EntityType<PumaBigCat> PUMA = createEntity(UntamedWilds.DEBUG, PumaBigCat::new,  "bigcat_puma",  1.2F, 1.0F, 0x9F6936, 0xECC38E, animalType.APEX_PRED, 1);
    public static EntityType<SnowLeopardBigCat> SNOW_LEOPARD = createEntity(UntamedWilds.DEBUG, SnowLeopardBigCat::new,  "bigcat_snow_leopard",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);
    public static EntityType<TigerBigCat> TIGER = createEntity(UntamedWilds.DEBUG, TigerBigCat::new,  "bigcat_tiger",  1.2F, 1.0F, 0xD1741D, 0x1A0400, animalType.APEX_PRED, 1);
    public static EntityType<CaveLionBigCat> CAVE_LION = createEntity(UntamedWilds.DEBUG, CaveLionBigCat::new,  "bigcat_cave_lion",  1.2F, 1.0F, 0x5B4924, 0xCCBC8F, animalType.APEX_PRED, 1);
    public static EntityType<MarsupialLionBigCat> MARSUPIAL_LION = createEntity(UntamedWilds.DEBUG, MarsupialLionBigCat::new,  "bigcat_marsupial_lion",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);
    public static EntityType<SabertoothBigCat> SABERTOOTH = createEntity(UntamedWilds.DEBUG, SabertoothBigCat::new,  "bigcat_sabertooth",  1.2F, 1.0F, 0x97845A, 0x3A3026, animalType.APEX_PRED, 1);
    public static EntityType<DireLionBigCat> DIRE_LION = createEntity(UntamedWilds.DEBUG, DireLionBigCat::new,  "bigcat_dire_lion",  1.2F, 1.0F, 0xA37341, 0xE2CBA4, animalType.APEX_PRED, 1);

    // Fish
    public static EntityType<Sunfish> SUNFISH = createEntity(ConfigMobControl.addSunfish.get(), Sunfish::new,  "sunfish",  1.6F, 1.6F, 0x8CA1A4, 0x2C545B, animalType.LARGE_OCEAN, 1);

    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
        for (EntityType<?> entity : entities) {
            event.getRegistry().register(entity);
        }
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, EntityClassification classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int baseColor, int overlayColor) {
        return createEntity(enable, factory, classification, name, trackingRange, updateFrequency, sendsVelocityUpdates, sizeX, sizeY, baseColor, overlayColor, FaunaHandler.animalType.CRITTER, 1);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, String name, float sizeX, float sizeY, int baseColor, int overlayColor, FaunaHandler.animalType spawnType, int weight) {
        return createEntity(enable, factory, EntityClassification.CREATURE, name, 64, 1, true, sizeX, sizeY, baseColor, overlayColor, spawnType, weight);
    }

    private static <T extends Entity> EntityType<T> createEntity(boolean enable, EntityType.IFactory<T> factory, EntityClassification classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int maincolor, int backcolor, FaunaHandler.animalType spawnType, int weight) {
        if (enable) {
            ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, name);
            EntityType<T> type = EntityType.Builder.create(factory, classification)
                    .size(sizeX, sizeY)
                    .setTrackingRange(trackingRange)
                    .setUpdateInterval(updateFrequency)
                    .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                    .build(location.toString());

            spawnEggs.add(registerEntitySpawnEgg(type, name, maincolor, backcolor, ItemGroup.MISC));
            type.setRegistryName(name);
            entities.add(type);
            if (ConfigMobControl.masterSpawner.get()) {
                addWorldSpawn(type, weight, spawnType, 1);
            }
            return type;
        }
        return null;
    }

    private static Item registerEntitySpawnEgg(EntityType<?> type, String name, int maincolor, int backcolor, ItemGroup group) {
        return new SpawnEggItem(type, maincolor, backcolor, new Item.Properties().group(group)).setRegistryName(name + "_spawn_egg");
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        for (Item spawnEgg : spawnEggs) {
            event.getRegistry().register(spawnEgg);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRendering() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TARANTULA, manager -> new RendererTarantula());

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNAKE, manager -> new RendererSnake(manager, new ModelSnake(), 0.0f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SOFTSHELL_TURTLE, manager -> new RendererSoftshellTurtle());

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.GIANT_CLAM, manager -> new RendererGiantClam(manager, new ModelGiantClam(), 1f));

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BLACK_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BLIND_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.BROWN_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.CAVE_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PANDA_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.POLAR_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SPECTACLED_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUN_BEAR, manager -> new RendererBear(manager, new ModelBear(), 1f));

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SUNFISH, manager -> new RendererSunfish(manager, new ModelSunfish(), 1f));

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.JAGUAR, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PUMA, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.CAVE_LION, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.TIGER, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SABERTOOTH, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.MARSUPIAL_LION, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.LION, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PANTHER, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.LEOPARD, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.DIRE_LION, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.SNOW_LEOPARD, manager -> new RendererBigCat(manager, new ModelBigCat(), 1f));

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
            spawns.add(new FaunaHandler.SpawnListEntry(entityClass, weightedProb, 1));
    }

    public static void addVanillaSpawn(Class <? extends LivingEntity> entityClass, int weightedProb, int min, int max, BiomeDictionary.Type... biomes) {
        Set<Biome> spawnBiomes = new ObjectArraySet<>();
        for (Biome b : ForgeRegistries.BIOMES) {
            Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(b);
            for(BiomeDictionary.Type biomeTypes : biomes) {
                if (types.contains(biomeTypes))
                    spawnBiomes.add(b);
            }
        }
    }
}