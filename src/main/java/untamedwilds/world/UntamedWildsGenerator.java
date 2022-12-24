package untamedwilds.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.world.gen.feature.*;

import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class UntamedWildsGenerator {

    public static final DeferredRegister<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, UntamedWilds.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, UntamedWilds.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, UntamedWilds.MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATION = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, UntamedWilds.MOD_ID);
    public static final Map<String, Float> biodiversity_levels = new java.util.HashMap<>();

    private static final RegistryObject<Feature<CountConfiguration>> SEA_ANEMONE = regFeature("sea_anemone", () -> new FeatureSeaAnemone(CountConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> REEDS = regFeature("reeds", () -> new FeatureReedClusters(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> ALGAE = regFeature("algae", () -> new FeatureUnderwaterAlgae(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> VEGETATION = regFeature("vegetation", () -> new FeatureVegetation(ProbabilityFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> FLOATING_VEGETATION = regFeature("floating_vegetation", () -> new FeatureFloatingPlants(NoneFeatureConfiguration.CODEC));

    // TODO: Unused because can't attach decorators to vanilla features. If I ever implement trees, this will go there
    public static final RegistryObject<TreeDecoratorType<?>> TREE_ORCHID = TREE_DECORATION.register("orchid", () -> new TreeDecoratorType<>(TreeDecorator.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> UNDERGROUND = regFeature("underground", () -> new FeatureUndergroundFaunaLarge(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<ConfiguredFeature<?,?>> UNDERGROUND_CONFIGURED = CONFIGURED_FEATURES.register("underground", () -> new ConfiguredFeature<>(UNDERGROUND.get(), FeatureConfiguration.NONE));
    private static final RegistryObject<PlacedFeature> UNDERGROUND_PLACED = PLACED_FEATURES.register("underground", () -> new PlacedFeature(UNDERGROUND_CONFIGURED.getHolder().get(), FeatureUndergroundFaunaLarge.placed()));

    private static final RegistryObject<Feature<NoneFeatureConfiguration>> APEX = regFeature("apex_predator", () -> new FeatureApexPredators(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> HERBIVORES = regFeature("herbivores", () -> new FeatureHerbivores(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> CRITTERS = regFeature("critter", () -> new FeatureCritters(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> SESSILE = regFeature("sessile", () -> new FeatureOceanSessileSpawns(NoneFeatureConfiguration.CODEC));

    private static final RegistryObject<Feature<NoneFeatureConfiguration>> OCEAN = regFeature("ocean_rare", () -> new FeatureOceanSwimming(NoneFeatureConfiguration.CODEC));
    private static final RegistryObject<Feature<NoneFeatureConfiguration>> DENSE_WATER = regFeature("dense_water", () -> new FeatureDenseWater(NoneFeatureConfiguration.CODEC));

    private static final RegistryObject<Feature<NoneFeatureConfiguration>> CRITTER_BURROW = regFeature("burrow", () -> new FeatureCritterBurrow(NoneFeatureConfiguration.CODEC));

    private static <B extends Feature<?>> RegistryObject<B> regFeature(String name, Supplier<? extends B> supplier) {
        return UntamedWildsGenerator.FEATURES.register(name, supplier);
    }

    @SubscribeEvent
    public static void onBiomesLoad(BiomeLoadingEvent event) {
        // Thanks Mojang, very cool 😎
        // event.getSpawns().withSpawner()
        //Features.JUNGLE_TREE.getConfig().decorators.add(new TreeOrchidDecorator());

        if (event.getCategory() == Biome.BiomeCategory.OCEAN) {
            registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(SESSILE.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqSessile.get());
            registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(OCEAN.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqOcean.get());

            if (!event.getName().toString().equals("minecraft:frozen_ocean") && !event.getName().toString().equals("minecraft:deep_frozen_ocean")) {
                if (ConfigFeatureControl.addAnemones.get()) {
                    registerFeatureWithFreq(event, GenerationStep.Decoration.VEGETAL_DECORATION, new ConfiguredFeature<>(SEA_ANEMONE.get(), new CountConfiguration(4)), 6);
                }
            }
        }

        if ((event.getCategory() == Biome.BiomeCategory.JUNGLE))
            registerFeatureWithFreq(event, GenerationStep.Decoration.VEGETAL_DECORATION, new ConfiguredFeature<>(FLOATING_VEGETATION.get(), FeatureConfiguration.NONE), 1, ConfigFeatureControl.addAlgae.get());
        if ((event.getCategory() == Biome.BiomeCategory.RIVER || event.getCategory() == Biome.BiomeCategory.JUNGLE || event.getCategory() == Biome.BiomeCategory.SWAMP) && !ConfigFeatureControl.reedBlacklist.get().contains(event.getName().toString()))
            registerFeatureWithFreq(event, GenerationStep.Decoration.VEGETAL_DECORATION, new ConfiguredFeature<>(REEDS.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqReeds.get(), ConfigFeatureControl.addReeds.get());
        if (!ConfigFeatureControl.algaeBlacklist.get().contains(event.getName().toString()))
            registerFeatureWithFreq(event, GenerationStep.Decoration.VEGETAL_DECORATION, new ConfiguredFeature<>(ALGAE.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqAlgae.get(), ConfigFeatureControl.addAlgae.get());
        if (!ConfigFeatureControl.floraBlacklist.get().contains(event.getName().toString()))
            registerFeatureWithFreq(event, GenerationStep.Decoration.VEGETAL_DECORATION, new ConfiguredFeature<>(VEGETATION.get(), new ProbabilityFeatureConfiguration(4)), ConfigFeatureControl.freqFlora.get(), ConfigFeatureControl.addFlora.get());

        registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(DENSE_WATER.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqWater.get());
        registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(ConfigFeatureControl.addBurrows.get() ? CRITTER_BURROW.get() : CRITTERS.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqCritter.get());
        registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(APEX.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqApex.get());
        registerFeatureWithFreq(event, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new ConfiguredFeature<>(HERBIVORES.get(), FeatureConfiguration.NONE), ConfigFeatureControl.freqHerbivores.get());

        // TODO: Restore intended Underground fauna generation
        // TODO: Currently only applies to "Underground" biomes, extend to any biome?
        if ((event.getCategory() == Biome.BiomeCategory.UNDERGROUND) && ConfigFeatureControl.probUnderground.get() != 0 && /*!FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_UNDERGROUND).isEmpty() &&*/ ConfigMobControl.masterSpawner.get()) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, UNDERGROUND_PLACED.getHolder().get());
        }
    }

    private static void registerFeatureWithFreq(BiomeLoadingEvent event, GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature, int freq) {
        registerFeatureWithFreq(event, decoration, feature, freq, ConfigMobControl.masterSpawner.get());
    }

    private static void registerFeatureWithFreq(BiomeLoadingEvent event, GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature, int freq, boolean enable) {
        if (freq > 0 && enable) {
            //Holder.direct(new PlacedFeature(Holder.hackyErase(p_206507_), List.of(p_206508_)))
            //Holder<PlacedFeature> feature = new Holder<PlacedFeature>();
            registerFeature(event, decoration, PlacementUtils.inlinePlaced(Holder.direct(feature), RarityFilter.onAverageOnceEvery(freq)), feature.feature().getRegistryName());
            //registerFeature(event, decoration, PlacementUtils.register(feature.feature().getRegistryName().getPath(), Holder.direct(feature), RarityFilter.onAverageOnceEvery(freq)), feature.feature().getRegistryName());
        }
    }

    private static void registerFeature(BiomeLoadingEvent event, GenerationStep.Decoration decoration, Holder<PlacedFeature> feature, ResourceLocation name) {
        if (UntamedWilds.DEBUG)
            UntamedWilds.LOGGER.info("Adding feature " + name + " to biome " + event.getName());
        event.getGeneration().addFeature(decoration, feature);
    }

    public static void readBioDiversityLevels() {
        /*try (InputStream inputstream = Language.class.getResourceAsStream("/data/untamedwilds/tags/biodiversity_levels.json")) {
            JsonObject jsonobject = new Gson().fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonObject.class);

            for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                biodiversity_levels.put(entry.getKey(), entry.getValue().getAsFloat());
            }
        } catch (JsonParseException | IOException ioexception) {
            UntamedWilds.LOGGER.error("Couldn't read data from /data/untamedwilds/tags/biodiversity_levels.json", ioexception);
        }*/
    }

    // Returns the biodiversity level of a biome. Values are data-driven, defaulting to 0.6 if no key is found.
    public static float getBioDiversityLevel(ResourceLocation biome) {
        String key = biome.toString();
        if (biodiversity_levels.containsKey(key)) {
            return biodiversity_levels.get(key);
        }
        return 0.6F;
    }
}