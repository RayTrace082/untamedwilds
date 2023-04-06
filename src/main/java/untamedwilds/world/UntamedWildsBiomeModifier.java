package untamedwilds.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;

import java.util.List;

public record UntamedWildsBiomeModifier(TagKey<Biome> dimension, List<HolderSet<Biome>> biomes,
                                        List<HolderSet<Biome>> blacklist, GenerationStep.Decoration decoration,
                                        Holder<PlacedFeature> feature, String configOption) implements BiomeModifier {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, UntamedWilds.MOD_ID);

    public static final RegistryObject<Codec<UntamedWildsBiomeModifier>> BIOME_MODIFIER_SERIALIZER = BIOME_MODIFIER_SERIALIZERS.register("biome_modifier_serializer",
        () -> RecordCodecBuilder.create(builder -> builder.group(
            TagKey.codec(Registry.BIOME_REGISTRY).fieldOf("dimension").forGetter(UntamedWildsBiomeModifier::dimension),
            Biome.LIST_CODEC.listOf().fieldOf("biomes").forGetter(UntamedWildsBiomeModifier::biomes),
            Biome.LIST_CODEC.listOf().fieldOf("blacklist").forGetter(UntamedWildsBiomeModifier::blacklist),
            GenerationStep.Decoration.CODEC.fieldOf("decoration").forGetter(UntamedWildsBiomeModifier::decoration),
            PlacedFeature.CODEC.fieldOf("feature").forGetter(UntamedWildsBiomeModifier::feature),
            PrimitiveCodec.STRING.fieldOf("configOption").forGetter(UntamedWildsBiomeModifier::configOption)
        ).apply(builder, UntamedWildsBiomeModifier::new)));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ForgeConfigSpec.BooleanValue option = ConfigFeatureControl.options.get(configOption);
        if (configOption.isEmpty() || (option != null && option.get() || option == null &&
            // TODO just a quick and dirty check for underground features
            ConfigFeatureControl.probUnderground.get() != 0 && ConfigMobControl.masterSpawner.get())) {
            if (phase != Phase.ADD)
                return;
            if (!biome.is(dimension))
                return;
            if (blacklist.stream().anyMatch(set -> set.contains(biome)))
                return;
            if (biomes.isEmpty() || biomes.stream().anyMatch(set -> set.contains(biome)))
                builder.getGenerationSettings().addFeature(decoration, feature);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return BIOME_MODIFIER_SERIALIZER.get();
    }
}