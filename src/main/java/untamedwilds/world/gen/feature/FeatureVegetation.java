package untamedwilds.world.gen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import untamedwilds.block.IPostGenUpdate;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModTags.BlockTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeatureVegetation extends Feature<FeatureSpreadConfig> {

    public FeatureVegetation(Codec<FeatureSpreadConfig> codec) {
        super(codec);
    }

    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos genPos, FeatureSpreadConfig config) {
        boolean flag = false;
        int x = rand.nextInt(16) - 8;
        int z = rand.nextInt(16) - 8;
        BlockPos pos = genPos.add(x, 0, z);
        Pair<Block, Integer> flora = FloraTypes.getFloraForPos(world, genPos);
        if (flora != null) {
            Block block = flora.getFirst();
            int size = flora.getSecond();
            for(int i = 0; i < size; ++i) {
                BlockPos blockpos = pos.add(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
                if(world.getBlockState(blockpos.down()).getBlock().isIn(BlockTags.REEDS_PLANTABLE_ON)) {
                    if (world.getBlockState(blockpos).canBeReplacedByLeaves(world, blockpos) && (world.getFluidState(blockpos).isEmpty())) {
                        if (block != null) {
                            world.setBlockState(blockpos, block.getDefaultState(), 2);
                            if (block.getBlock() instanceof IPostGenUpdate) {
                                ((IPostGenUpdate)block.getBlock()).updatePostGen(world, blockpos);
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
    public enum FloraTypes implements IStringSerializable {

        TEMPERATE_BUSH	(ModBlock.BUSH_TEMPERATE.get(), 8, ConfigFeatureControl.addBushes.get(), false, 32, Biome.Category.FOREST, Biome.Category.SWAMP, Biome.Category.EXTREME_HILLS, Biome.Category.TAIGA, Biome.Category.PLAINS),
        ELEPHANT_EAR	(ModBlock.ELEPHANT_EAR.get(), 4, ConfigFeatureControl.addBushes.get(), false, 32, Biome.Category.JUNGLE),
        HEMLOCK     	(ModBlock.HEMLOCK.get(), 1, ConfigFeatureControl.addBushes.get(), false, 16, Biome.Category.FOREST, Biome.Category.PLAINS),
        TITAN_ARUM     	(ModBlock.TITAN_ARUM.get(), 12, ConfigFeatureControl.addBushes.get(), false, 1, Biome.Category.JUNGLE);


        public Block type;
        public int rarity;
        public boolean enabled;
        public boolean spawnsInWater;
        public int size;
        public Biome.Category[] spawnBiomes;

        FloraTypes(Block type, int rolls, boolean add, boolean spawnsInWater, int size, Biome.Category... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.enabled = add;
            this.spawnsInWater = spawnsInWater;
            this.spawnBiomes = biomes;
            this.size = size;
        }

        @Override
        public String getString() {
            return "why would you do this?";
        }

        public static Pair<Block, Integer> getFloraForPos(IWorld world, BlockPos pos) {
            Biome biome = world.getBiome(pos);
            List<FeatureVegetation.FloraTypes> types = new ArrayList<>();
            for (FeatureVegetation.FloraTypes type : values()) {
                if (type.enabled && !(!type.spawnsInWater && world.getBlockState(pos).getBlock() == Blocks.WATER)) {
                    for(Biome.Category biomeTypes : type.spawnBiomes) {
                        if(biome.getCategory() == biomeTypes){
                            for (int i=0; i < type.rarity; i++) {
                                types.add(type);
                            }
                        }
                    }
                }
            }
            if (!types.isEmpty()) {
                int i = new Random().nextInt(types.size());
                return new Pair(types.get(i).type, types.get(i).size);
            }
            return null;
        }
    }
}
