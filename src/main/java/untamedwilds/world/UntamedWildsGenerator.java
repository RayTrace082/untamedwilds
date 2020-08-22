package untamedwilds.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.world.gen.feature.*;

public class UntamedWildsGenerator {

    private static final FeatureSeaAnemone SEA_ANEMONE = new FeatureSeaAnemone(CountConfig::deserialize);
    private static final Feature<NoFeatureConfig> UNDERGROUND = new FeatureUndergroundFaunaLarge(NoFeatureConfig::deserialize);
    private static final Feature<NoFeatureConfig> BEAR = new FeatureBears(NoFeatureConfig::deserialize);
    //private static final Feature<NoFeatureConfig> BIG_CATS = new FeatureBigCats(NoFeatureConfig::deserialize);
    private static final Feature<NoFeatureConfig> APEX = new FeatureApexPredators(NoFeatureConfig::deserialize);
    private static final Feature<NoFeatureConfig> CRITTERS = new FeatureCritters(NoFeatureConfig::deserialize);
    private static final Feature<NoFeatureConfig> SESSILE = new FeatureOceanSessileSpawns(NoFeatureConfig::deserialize);

    private static final Feature<NoFeatureConfig> OCEAN_RARE = new FeatureOceanSwimming(NoFeatureConfig::deserialize);

    public static void setUp() {
        UntamedWilds.LOGGER.debug(Biome.BIOMES);

        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) ) {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN)) {
                    biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, SESSILE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(1))));
                    biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, OCEAN_RARE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(1))));
                    if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                        // TODO: Yep, hardcoded Polar Bears, because fuck this shit. Replace the feature with "SpawnUniqueMob" to not have to use a list
                        biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, BEAR.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
                    }
                    else {
                        biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, SEA_ANEMONE.withConfiguration(new CountConfig(8)).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(16))));
                    }
                } else {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, UNDERGROUND.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.AIR, 0.01F))));
                    biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, APEX.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
                    //biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, BEAR.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
                    //biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, BIG_CATS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
                    biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, CRITTERS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(3))));
                }
            }
        }
    }
}