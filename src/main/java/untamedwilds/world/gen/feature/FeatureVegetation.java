package untamedwilds.world.gen.feature;

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
        for(int i = 0; i < 32; ++i) {
            BlockPos blockpos = pos.add(rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6));
            if(world.getBlockState(blockpos.down()).getBlock().isIn(BlockTags.REEDS_PLANTABLE_ON)) {
                if(world.isAirBlock(blockpos) || (world.getBlockState(blockpos).getBlock() == Blocks.WATER && world.isAirBlock(blockpos.up()))) {
                    Block block = FloraTypes.getFloraForPos(world, blockpos);
                    if (block != null) {
                        world.setBlockState(blockpos, block.getDefaultState(), 2);
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }

    // Plants available, referenced to properly distribute them in the world if their conditions are filled
    public enum FloraTypes implements IStringSerializable {

        TEMPERATE_BUSH	(ModBlock.BUSH_TEMPERATE.get(), 2, ConfigFeatureControl.addBushes.get(), false, Biome.Category.FOREST, Biome.Category.SWAMP, Biome.Category.EXTREME_HILLS, Biome.Category.TAIGA, Biome.Category.PLAINS),
        ELEPHANT_EAR	(ModBlock.ELEPHANT_EAR.get(), 2, ConfigFeatureControl.addBushes.get(), false, Biome.Category.JUNGLE);

        public Block type;
        public int rarity;
        public boolean enabled;
        public boolean spawnsInWater;
        public Biome.Category[] spawnBiomes;

        FloraTypes(Block type, int rolls, boolean add, boolean spawnsInWater, Biome.Category... biomes) {
            this.type = type;
            this.rarity = rolls;
            this.enabled = add;
            this.spawnsInWater = spawnsInWater;
            this.spawnBiomes = biomes;
        }

        @Override
        public String getString() {
            return "why would you do this?";
        }

        public static Block getFloraForPos(IWorld world, BlockPos pos) {
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
            return !types.isEmpty() ? types.get(new Random().nextInt(types.size())).type : null;
        }
    }
}
