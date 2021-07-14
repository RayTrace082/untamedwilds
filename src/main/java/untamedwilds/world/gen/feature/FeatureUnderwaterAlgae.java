package untamedwilds.world.gen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.init.ModBlock;

import java.util.*;

public class FeatureUnderwaterAlgae extends Feature<NoFeatureConfig> {
   public FeatureUnderwaterAlgae(Codec<NoFeatureConfig> p_i231988_1_) {
      super(p_i231988_1_);
   }

   public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
      boolean flag = false;
      int i = rand.nextInt(8) - rand.nextInt(8);
      int j = rand.nextInt(8) - rand.nextInt(8);
      int k = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX() + i, pos.getZ() + j);
      BlockPos blockpos = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
      Pair<Block, Integer> algae = AlgaeTypes.getAlgaeForPos(reader, blockpos);
      if (reader.getBlockState(blockpos).isIn(Blocks.WATER) && algae != null) {
         BlockState blockstate = algae.getFirst().getDefaultState();
         if (blockstate.isValidPosition(reader, blockpos)) {
            reader.setBlockState(blockpos, blockstate, 2);
            flag = true;
         }
      }

      return flag;
   }

   // Algae available, referenced to properly distribute them in the world if their conditions are filled
   public enum AlgaeTypes implements IStringSerializable {

      AMAZON_SWORD	(ModBlock.AMAZON_SWORD.get(), 4, ConfigFeatureControl.addAlgae.get(), 6, Biome.Category.SWAMP, Biome.Category.JUNGLE),
      EELGRASS	(ModBlock.EELGRASS.get(), 4, ConfigFeatureControl.addAlgae.get(), 6, Biome.Category.OCEAN);

      public Block type;
      public int rarity;
      public boolean enabled;
      public int size;
      public Biome.Category[] spawnBiomes;

      AlgaeTypes(Block type, int rolls, boolean add, int size, Biome.Category... biomes) {
         this.type = type;
         this.rarity = rolls;
         this.enabled = add;
         this.spawnBiomes = biomes;
         this.size = size;
      }

      @Override
      public String getString() {
         return "why would you do this?";
      }

      public static Pair<Block, Integer> getAlgaeForPos(IWorld world, BlockPos pos) {
         // TODO: Add this information to the Blacklist
         Optional<RegistryKey<Biome>> optional = world.func_242406_i(pos);
         if (Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) || Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
            return null;
         }

         Biome biome = world.getBiome(pos);
         List<AlgaeTypes> types = new ArrayList<>();
         for (AlgaeTypes type : values()) {
            if (type.enabled) {
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
            return new Pair<>(types.get(i).type, types.get(i).size);
         }
         return null;
      }
   }
}
