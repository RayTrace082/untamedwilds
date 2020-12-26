package untamedwilds.world;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CaveEdgeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModItems;
import untamedwilds.world.gen.feature.*;

import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID)
public class UntamedWildsGenerator {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, UntamedWilds.MOD_ID);

    private static final RegistryObject<Feature<FeatureSpreadConfig>> SEA_ANEMONE = regFeature("sea_anemone", () -> new FeatureSeaAnemone(FeatureSpreadConfig.CODEC));
    private static final RegistryObject<Feature<NoFeatureConfig>> REEDS = regFeature("reeds", () -> new FeatureReedClusters(NoFeatureConfig.field_236558_a_));

    private static final RegistryObject<Feature<NoFeatureConfig>> UNDERGROUND = regFeature("underground", () -> new FeatureUndergroundFaunaLarge(NoFeatureConfig.field_236558_a_));
    private static final RegistryObject<Feature<NoFeatureConfig>> BEAR = regFeature("bear", () -> new FeatureBears(NoFeatureConfig.field_236558_a_));
    //private static final Feature<NoFeatureConfig> BIG_CATS = new FeatureBigCats(NoFeatureConfig.field_236558_a_);
    private static final RegistryObject<Feature<NoFeatureConfig>> APEX = regFeature("apex_predator", () -> new FeatureApexPredators(NoFeatureConfig.field_236558_a_));
    private static final RegistryObject<Feature<NoFeatureConfig>> CRITTERS = regFeature("critter", () -> new FeatureCritters(NoFeatureConfig.field_236558_a_));
    private static final RegistryObject<Feature<NoFeatureConfig>> SESSILE = regFeature("sessile", () -> new FeatureOceanSessileSpawns(NoFeatureConfig.field_236558_a_));

    private static final RegistryObject<Feature<NoFeatureConfig>> OCEAN_RARE = regFeature("ocean_rare", () -> new FeatureOceanSwimming(NoFeatureConfig.field_236558_a_));

    private static <B extends Feature<?>> RegistryObject<B> regFeature(String name, Supplier<? extends B> supplier) {
        return FEATURES.register(name, supplier);
    }


    @SubscribeEvent
    public static void onBiomesLoad(BiomeLoadingEvent event) {
        // Thanks Mojang, very cool 😎
        if (event.getCategory() == Biome.Category.OCEAN) {
            registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, SESSILE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), SESSILE.get().getRegistryName());
            registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, OCEAN_RARE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), OCEAN_RARE.get().getRegistryName());
            if (event.getName().toString().equals("minecraft:frozen_ocean") || event.getName().toString().equals("minecraft:deep_frozen_ocean")) {
                // TODO: Hardcoded Polar Bears in Frozen Ocean, because fuck this shit. Replace the feature with something less stupid
                registerFeature(event, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, BEAR.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), BEAR.get().getRegistryName());
            }
            else {
                registerFeature(event, GenerationStage.Decoration.VEGETAL_DECORATION, SEA_ANEMONE.get().withConfiguration(new FeatureSpreadConfig(4)).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(3), SEA_ANEMONE.get().getRegistryName());
            }
        }
        if (event.getCategory() == Biome.Category.RIVER || event.getCategory() == Biome.Category.JUNGLE || event.getCategory() == Biome.Category.SWAMP) {
            registerFeature(event, GenerationStage.Decoration.VEGETAL_DECORATION, REEDS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(1), REEDS.get().getRegistryName());
        }
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, UNDERGROUND.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.AIR, 0.01F))));
        event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, APEX.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, CRITTERS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }

    private static void registerFeature(BiomeLoadingEvent event, GenerationStage.Decoration decoration, ConfiguredFeature<?, ?> feature, ResourceLocation name) {
        if (UntamedWilds.DEBUG) {
            UntamedWilds.LOGGER.info("Adding feature " + name + " to biome " + event.getName());
        }
        event.getGeneration().withFeature(decoration, feature);
    }
}