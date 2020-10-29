package untamedwilds.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CaveEdgeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.world.gen.feature.*;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class UntamedWildsGenerator {

    private static final Feature<FeatureSpreadConfig> SEA_ANEMONE = Registry.register(Registry.FEATURE, "untamedwilds:sea_anemone", new FeatureSeaAnemone(FeatureSpreadConfig.field_242797_a));
    private static final Feature<NoFeatureConfig> REEDS = Registry.register(Registry.FEATURE, "untamedwilds:reeds", new FeatureReedClusters(NoFeatureConfig.field_236558_a_));

    private static final Feature<NoFeatureConfig> UNDERGROUND = Registry.register(Registry.FEATURE, "untamedwilds:underground", new FeatureUndergroundFaunaLarge(NoFeatureConfig.field_236558_a_));
    private static final Feature<NoFeatureConfig> BEAR = Registry.register(Registry.FEATURE, "untamedwilds:bear", new FeatureBears(NoFeatureConfig.field_236558_a_));
    //private static final Feature<NoFeatureConfig> BIG_CATS = new FeatureBigCats(NoFeatureConfig.field_236558_a_);
    private static final Feature<NoFeatureConfig> APEX = Registry.register(Registry.FEATURE, "untamedwilds:apex_predator", new FeatureApexPredators(NoFeatureConfig.field_236558_a_));
    private static final Feature<NoFeatureConfig> CRITTERS = Registry.register(Registry.FEATURE, "untamedwilds:critter", new FeatureCritters(NoFeatureConfig.field_236558_a_));
    private static final Feature<NoFeatureConfig> SESSILE = Registry.register(Registry.FEATURE, "untamedwilds:sessile", new FeatureOceanSessileSpawns(NoFeatureConfig.field_236558_a_));

    private static final Feature<NoFeatureConfig> OCEAN_RARE = Registry.register(Registry.FEATURE, "untamedwilds:ocean_rare", new FeatureOceanSwimming(NoFeatureConfig.field_236558_a_));

    @SubscribeEvent
    public static void onBiomesLoad(BiomeLoadingEvent event) {
        // Thanks Mojang, very cool 😎
        if (event.getCategory() == Biome.Category.OCEAN) {
            registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, SESSILE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), SESSILE.getRegistryName());
            registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, OCEAN_RARE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), OCEAN_RARE.getRegistryName());
            if (event.getName().toString().equals("minecraft:frozen_ocean") || event.getName().toString().equals("minecraft:deep_frozen_ocean")) {
                // TODO: Hardcoded Polar Bears in Frozen Ocean, because fuck this shit. Replace the feature with something less stupid
                registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, BEAR.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), BEAR.getRegistryName());
            }
            else {
                registerFeature(event, GenerationStage.Decoration.VEGETAL_DECORATION, SEA_ANEMONE.withConfiguration(new FeatureSpreadConfig(6)).withPlacement(Features.Placements.field_244003_n).func_242729_a(4), SEA_ANEMONE.getRegistryName());
            }
        }
        if (event.getCategory() == Biome.Category.RIVER || event.getCategory() == Biome.Category.JUNGLE || event.getCategory() == Biome.Category.SWAMP) {
            registerFeature(event, GenerationStage.Decoration.VEGETAL_DECORATION, REEDS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.field_244003_n).func_242729_a(2), REEDS.getRegistryName());
        }
        event.getGeneration().func_242513_a(GenerationStage.Decoration.UNDERGROUND_DECORATION, UNDERGROUND.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.AIR, 0.01F))));
        event.getGeneration().func_242513_a(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, APEX.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        event.getGeneration().func_242513_a(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, CRITTERS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    private static void registerFeature(BiomeLoadingEvent event, GenerationStage.Decoration decoration, ConfiguredFeature<?, ?> feature, ResourceLocation name) {
        if (UntamedWilds.DEBUG) {
            UntamedWilds.LOGGER.info("Adding feature " + name + " to biome " + event.getName());
        }
        event.getGeneration().func_242513_a(decoration, feature);
    }
}