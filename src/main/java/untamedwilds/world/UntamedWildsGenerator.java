package untamedwilds.world;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
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
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.world.gen.feature.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UntamedWildsGenerator {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, UntamedWilds.MOD_ID);
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
    private static final RegistryObject<ConfiguredFeature<?, ?>> UNDERGROUND_CONFIGURED = CONFIGURED_FEATURES.register("underground", () -> new ConfiguredFeature<>(UNDERGROUND.get(), FeatureConfiguration.NONE));

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

    public static Map<ResourceLocation, BiomeModifier> generateModifierByLocation(RegistryOps<JsonElement> registryOps) {
        Map<ResourceLocation, BiomeModifier> map = new HashMap<>();
        addFeature(map, "sessile", new Builder(registryOps, new ConfiguredFeature<>(SESSILE.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqSessile.get()))
            .tag(Arrays.asList(BiomeTags.IS_OCEAN)).decoration(TOP_LAYER_MODIFICATION));
        addFeature(map, "ocean_rare", new Builder(registryOps, new ConfiguredFeature<>(OCEAN.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqOcean.get()))
            .tag(Arrays.asList(BiomeTags.IS_OCEAN)).decoration(TOP_LAYER_MODIFICATION));
        addFeature(map, "sea_anemone", new Builder(registryOps, new ConfiguredFeature<>(SEA_ANEMONE.get(), new CountConfiguration(4)), "gencontrol.anemone")
            .placementModifier(RarityFilter.onAverageOnceEvery(6))
            .extraBiomes(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN).decoration(GenerationStep.Decoration.VEGETAL_DECORATION));

        addFeature(map, "floating_vegetation", new Builder(registryOps, new ConfiguredFeature<>(FLOATING_VEGETATION.get(), FeatureConfiguration.NONE), "gencontrol.algae")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqAlgae.get()))
            .tag(Arrays.asList(BiomeTags.IS_JUNGLE)).decoration(GenerationStep.Decoration.VEGETAL_DECORATION));
        addFeature(map, "reeds", new Builder(registryOps, new ConfiguredFeature<>(REEDS.get(), FeatureConfiguration.NONE), "gencontrol.reeds")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqReeds.get()))
            .blacklist(ConfigFeatureControl.reedBlacklist.get().stream().map(s -> ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(s))).collect(Collectors.toList()))
            .decoration(GenerationStep.Decoration.VEGETAL_DECORATION));

        addFeature(map, "algae", new Builder(registryOps, new ConfiguredFeature<>(ALGAE.get(), FeatureConfiguration.NONE), "gencontrol.algae")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqAlgae.get()))
            .blacklist(ConfigFeatureControl.algaeBlacklist.get().stream().map(s -> ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(s))).collect(Collectors.toList()))
            .decoration(GenerationStep.Decoration.VEGETAL_DECORATION));
        addFeature(map, "vegetation", new Builder(registryOps, new ConfiguredFeature<>(VEGETATION.get(), new ProbabilityFeatureConfiguration(1f / 4)), "gencontrol.bush")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqFlora.get()))
            .blacklist(ConfigFeatureControl.floraBlacklist.get().stream().map(s -> ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(s))).collect(Collectors.toList()))
            .decoration(GenerationStep.Decoration.VEGETAL_DECORATION));

        addFeature(map, "dense_water", new Builder(registryOps, new ConfiguredFeature<>(DENSE_WATER.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqWater.get()))
            .decoration(TOP_LAYER_MODIFICATION));
        addFeature(map, "burrow", new Builder(registryOps, new ConfiguredFeature<>(ConfigFeatureControl.addBurrows.get() ? CRITTER_BURROW.get() : CRITTERS.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqCritter.get()))
            .decoration(TOP_LAYER_MODIFICATION));
        addFeature(map, "apex_predator", new Builder(registryOps, new ConfiguredFeature<>(APEX.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqApex.get()))
            .decoration(TOP_LAYER_MODIFICATION));
        addFeature(map, "herbivores", new Builder(registryOps, new ConfiguredFeature<>(HERBIVORES.get(), FeatureConfiguration.NONE), "")
            .placementModifier(RarityFilter.onAverageOnceEvery(ConfigFeatureControl.freqHerbivores.get()))
            .decoration(TOP_LAYER_MODIFICATION));

        // TODO: Restore intended Underground fauna generation
        // TODO: Currently only applies to "Underground" biomes, extend to any biome?
        /*!FaunaHandler.getSpawnableList(FaunaHandler.animalType.LARGE_UNDERGROUND).isEmpty() &&*/
        addFeature(map, "underground", new Builder(registryOps, UNDERGROUND_CONFIGURED.get(), "mobcontrol.masterspawner")
            .tag(Arrays.asList(Tags.Biomes.IS_UNDERGROUND)).decoration(GenerationStep.Decoration.UNDERGROUND_DECORATION)
            .placementModifiers(FeatureUndergroundFaunaLarge.placed()));
        return map;
    }

    private static class Builder {

        private final ConfiguredFeature<?, ?> placedFeatureName;
        private TagKey<Biome> dimension = BiomeTags.IS_OVERWORLD;
        private final List<PlacementModifier> placementModifiers = new ArrayList<>();
        private final List<TagKey<Biome>> biomeTags = new ArrayList<>();
        private final List<Holder<Biome>> blacklist = new ArrayList<>();
        private final List<Holder<Biome>> extraBiomes = new ArrayList<>();
        private final RegistryOps<JsonElement> registryOps;
        private GenerationStep.Decoration decoration = GenerationStep.Decoration.VEGETAL_DECORATION;
        private final String configOption;

        public Builder(RegistryOps<JsonElement> registryOps, ConfiguredFeature<?, ?> placedFeature, String configOption) {
            this.registryOps = registryOps;
            this.placedFeatureName = placedFeature;
            this.configOption = configOption;
        }

        public Builder dimension(TagKey<Biome> tag) {
            this.dimension = tag;
            return this;
        }

        public Builder placementModifier(PlacementModifier placementModifier) {
            this.placementModifiers.add(placementModifier);
            return this;
        }

        public Builder placementModifiers(List<PlacementModifier> placementModifiers) {
            this.placementModifiers.addAll(placementModifiers);
            return this;
        }

        public Builder tag(List<TagKey<Biome>> tags) {
            this.biomeTags.addAll(tags);
            return this;
        }

        public Builder blacklist(List<ResourceKey<Biome>> biomes) {
            for (ResourceKey<Biome> biome : biomes) {
                this.blacklist.add(registryOps.registry(Registry.BIOME_REGISTRY).get().getHolder(biome).get());
            }
            return this;
        }

        public Builder extraBiomes(ResourceKey<Biome>... biomes) {
            for (ResourceKey<Biome> biome : biomes) {
                this.extraBiomes.add(registryOps.registry(Registry.BIOME_REGISTRY).get().getHolder(biome).get());
            }
            return this;
        }

        public Builder decoration(GenerationStep.Decoration decoration) {
            this.decoration = decoration;
            return this;
        }

        private static HolderSet<Biome> getBiomesByTag(RegistryOps<JsonElement> registryOps, TagKey<Biome> tag) {
            return new HolderSet.Named<>(registryOps.registry(Registry.BIOME_REGISTRY).get(), tag);
        }

        public UntamedWildsBiomeModifier build() {
            List<HolderSet<Biome>> biomesSet = new ArrayList<>(this.biomeTags.stream().map(tag -> getBiomesByTag(registryOps, tag)).collect(Collectors.toList()));
            List<HolderSet<Biome>> blacklistSet = new ArrayList<>();
            blacklistSet.add(HolderSet.direct(this.blacklist));
            Holder<PlacedFeature> placedFeature = Holder.direct(new PlacedFeature(Holder.direct(placedFeatureName), placementModifiers));
            return new UntamedWildsBiomeModifier(dimension, biomesSet, blacklistSet, decoration, placedFeature, configOption);
        }
    }

    private static void addFeature(Map<ResourceLocation, BiomeModifier> map, String placedFeatureName, Builder builder) {
        BiomeModifier modifier = builder.build();
        ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, placedFeatureName);
        map.put(location, modifier);
    }

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        Map<ResourceLocation, BiomeModifier> featureGenMap = generateModifierByLocation(registryOps);
        JsonCodecProvider<BiomeModifier> jsonCodecProvider = JsonCodecProvider.forDatapackRegistry(gen, helper, UntamedWilds.MOD_ID, registryOps, ForgeRegistries.Keys.BIOME_MODIFIERS, featureGenMap);
        gen.addProvider(event.includeServer(), jsonCodecProvider);
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