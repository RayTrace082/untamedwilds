package untamedwilds.world.gen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.common.Tags;
import untamedwilds.block.IPostGenUpdate;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.ModBlockTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeatureVegetation extends Feature<ProbabilityFeatureConfiguration> {

    public FeatureVegetation(Codec<ProbabilityFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext context) {
        RandomSource rand = context.level().getRandom();
        WorldGenLevel world = context.level();
        BlockPos genPos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, context.origin());
        if (ConfigFeatureControl.dimensionFeatureBlacklist.get().contains(world.getLevel().dimension().location().toString()))
            return false;

        boolean flag = false;
        int x = rand.nextInt(16) - 8;
        int z = rand.nextInt(16) - 8;
        BlockPos pos = genPos.offset(x, 0, z);
        Pair<Block, Integer> flora = FloraTypes.getFloraForPos(world, genPos);
        if (flora != null) {
            Block block = flora.getFirst();
            int size = flora.getSecond();
            for (int i = 0; i < size; ++i) {
                BlockPos blockpos = pos.offset(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
                if (world.getBlockState(blockpos.below()).is(ModBlockTags.ALOE_PLANTABLE_ON)) {
                    if (!world.getBlockState(blockpos).isFaceSturdy(world, blockpos, Direction.UP) && (world.getFluidState(blockpos).isEmpty())) {
                        if (block != null) {
                            if (world.getBlockState(blockpos).getBlock() == Blocks.SNOW && world.getBlockState(blockpos.below()).getBlock() == Blocks.GRASS_BLOCK)
                                world.setBlock(blockpos.below(), Blocks.GRASS_BLOCK.defaultBlockState().setValue(GrassBlock.SNOWY, false), 2); // TODO: Shitty hack to fix grey grass in snowy biomes

                            world.setBlock(blockpos, block.defaultBlockState(), 2);
                            if (block instanceof IPostGenUpdate) {
                                ((IPostGenUpdate) block).updatePostGen(world, blockpos);
                            }
                            flag = true;
                        }
                    }
                }
            }
        }

        return flag;
    }

    // Plants available, referenced to properly distribute them in the world if their conditions are filled
    public enum FloraTypes {

        TEMPERATE_BUSH(ModBlock.BUSH_TEMPERATE.get(), 6, ConfigFeatureControl.addFlora.get(), false, 24,BiomeTags.IS_FOREST, Tags.Biomes.IS_SWAMP, BiomeTags.IS_MOUNTAIN,BiomeTags.IS_TAIGA, Tags.Biomes.IS_PLAINS),
        CREOSOTE_BUSH(ModBlock.BUSH_CREOSOTE.get(), 2, ConfigFeatureControl.addFlora.get(), false, 4, BiomeTags.IS_BADLANDS, Tags.Biomes.IS_DESERT),
        ELEPHANT_EAR(ModBlock.ELEPHANT_EAR.get(), 6, ConfigFeatureControl.addFlora.get(), false, 24, BiomeTags.IS_JUNGLE),
        HEMLOCK(ModBlock.HEMLOCK.get(), 1, ConfigFeatureControl.addFlora.get(), false, 12, BiomeTags.IS_FOREST),
        TITAN_ARUM(ModBlock.TITAN_ARUM.get(), 6, ConfigFeatureControl.addFlora.get(), false, 1, BiomeTags.IS_JUNGLE),
        ZIMBABWE_ALOE(ModBlock.ZIMBABWE_ALOE.get(), 4, ConfigFeatureControl.addFlora.get(), false, 1, BiomeTags.IS_BADLANDS),
        FLOWER_YARROW(ModBlock.YARROW.get(), 6, ConfigFeatureControl.addFlora.get(), false, 18, BiomeTags.IS_FOREST, Tags.Biomes.IS_PLAINS,BiomeTags.IS_MOUNTAIN),
        GRASS_JUNEGRASS(ModBlock.JUNEGRASS.get(), 8, ConfigFeatureControl.addFlora.get(), false, 18,Tags.Biomes.IS_PLAINS),
        CANOLA(ModBlock.CANOLA.get(), 6, ConfigFeatureControl.addFlora.get(), false, 12, Tags.Biomes.IS_PLAINS);

        public Block type;
        public int rarity;
        public boolean enabled;
        public boolean spawnsInWater;
        public int size;
        public TagKey<Biome>[] spawnBiomes;

        FloraTypes(Block type, int rolls, boolean add, boolean spawnsInWater, int size, TagKey<Biome>... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.enabled = add;
            this.spawnsInWater = spawnsInWater;
            this.spawnBiomes = biomes;
            this.size = size;
        }

        public static Pair<Block, Integer> getFloraForPos(WorldGenLevel world, BlockPos pos) {
            Holder<Biome> biome = world.getBiome(pos);
            List<FeatureVegetation.FloraTypes> types = new ArrayList<>();
            for (FeatureVegetation.FloraTypes type : values()) {
                if (type.enabled && !(!type.spawnsInWater && world.getBlockState(pos).getBlock() == Blocks.WATER)) {
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