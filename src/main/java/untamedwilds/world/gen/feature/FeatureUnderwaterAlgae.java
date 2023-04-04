package untamedwilds.world.gen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.init.ModBlock;

import java.util.*;

public class FeatureUnderwaterAlgae extends Feature<NoneFeatureConfiguration> {
    public FeatureUnderwaterAlgae(Codec<NoneFeatureConfiguration> p_i231988_1_) {
        super(p_i231988_1_);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        BlockPos pos = context.origin();
        WorldGenLevel world = context.level();
        if (ConfigFeatureControl.dimensionFeatureBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        boolean flag = false;
        int i = rand.nextInt(8) - rand.nextInt(8);
        int j = rand.nextInt(8) - rand.nextInt(8);
        int k = world.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX() + i, pos.getZ() + j);
        BlockPos blockpos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
        Pair<Block, Integer> algae = AlgaeTypes.getAlgaeForPos(world, blockpos);
        if (world.getBlockState(blockpos).is(Blocks.WATER) && algae != null) {
            BlockState blockstate = algae.getFirst().defaultBlockState();
            if (blockstate.canSurvive(world, blockpos)) {
                world.setBlock(blockpos, blockstate, 2);
                flag = true;
            }
        }

        return flag;
    }

    // Algae available, referenced to properly distribute them in the world if their conditions are filled
    public enum AlgaeTypes {

        AMAZON_SWORD(ModBlock.AMAZON_SWORD.get(), 4, ConfigFeatureControl.addAlgae.get(), 6, Tags.Biomes.IS_SWAMP, BiomeTags.IS_JUNGLE),
        EELGRASS(ModBlock.EELGRASS.get(), 4, ConfigFeatureControl.addAlgae.get(), 6, BiomeTags.IS_OCEAN);

        public Block type;
        public int rarity;
        public boolean enabled;
        public int size;
        public TagKey<Biome>[] spawnBiomes;

        AlgaeTypes(Block type, int rolls, boolean add, int size, TagKey<Biome>... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.enabled = add;
            this.spawnBiomes = biomes;
            this.size = size;
        }

        public static Pair<Block, Integer> getAlgaeForPos(WorldGenLevel world, BlockPos pos) {
            Optional<ResourceKey<Biome>> optional = world.getBiome(pos).unwrapKey();
            if (Objects.equals(optional, Biomes.FROZEN_OCEAN) || Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
                return null;
            }

            Holder<Biome> biome = world.getBiome(pos);
            List<AlgaeTypes> types = new ArrayList<>();
            for (AlgaeTypes type : values()) {
                if (type.enabled) {
                    for (TagKey<Biome> biomeTypes : type.spawnBiomes) {
                        if (biome.is(biomeTypes)) {
                            for (int i = 0; i < type.rarity; i++) {
                                types.add(type);
                            }
                        }
                    }
                }
            }
            if (!types.isEmpty()) {
                int i = new Random().nextInt(types.size());
                return new Pair<>(types.get(i).type, types.get(i).size);
            }
            return null;
        }
    }
}