package untamedwilds.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.block.*;
import untamedwilds.block.blockentity.CageBlockEntity;
import untamedwilds.block.blockentity.CritterBurrowBlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UntamedWilds.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, UntamedWilds.MOD_ID);

    private final static List<Pair<RegistryObject<Block>, String>> RENDER_TYPE_DATA = Lists.newArrayList();

    // Carpets
    public static RegistryObject<Block> CARPET_STRAW  = createBlock("carpet_straw", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_ASHEN  = createBlock("carpet_bear_ashen", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GRAY).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_BLACK  = createBlock("carpet_bear_black", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BLACK).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_BROWN  = createBlock("carpet_bear_brown", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_WHITE  = createBlock("carpet_bear_white", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.SNOW).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_JAGUAR  = createBlock("carpet_bigcat_jaguar", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GOLD).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_LEOPARD  = createBlock("carpet_bigcat_leopard", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GOLD).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_LION  = createBlock("carpet_bigcat_lion", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_PANTHER  = createBlock("carpet_bigcat_panther", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.SNOW).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_PUMA  = createBlock("carpet_bigcat_puma", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GRAY).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_SNOW  = createBlock("carpet_bigcat_snow_leopard", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BLACK).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BIGCAT_TIGER  = createBlock("carpet_bigcat_tiger", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);

    // Storage
    public static RegistryObject<Block> LARD_BLOCK  = createBlock("block_lard", () -> new LardBlock(Block.Properties.create(Material.CLAY, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.BUILDING_BLOCKS);
    public static RegistryObject<Block> PEARL_BLOCK  = createBlock("block_pearl", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE)), ItemGroup.BUILDING_BLOCKS);

    // Machines
    public static RegistryObject<Block> TRAP_CAGE  = createBlock("trap_cage", () -> new CageBlock(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD)), ItemGroup.TRANSPORTATION);

    // Fauna
    public static RegistryObject<Block> ANEMONE_ROSE_BULB  = createBlock("anemone_rose_bulb", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.RED).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ANEMONE_SAND  = createBlock("anemone_sand", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.PINK).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ANEMONE_SEBAE  = createBlock("anemone_sebae", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), "cutout", ItemGroup.DECORATIONS);

    // Flora - Reeds
    public static RegistryObject<Block> COMMON_REED = createBlock("flora_common_reed", () -> new ReedBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0.1F).sound(SoundType.VINE).doesNotBlockMovement()), "cutout", ItemGroup.DECORATIONS, 100);
    // Flora - Bushes
    public static RegistryObject<Block> BUSH_TEMPERATE = createBlock("flora_bush_temperate", () -> new UndergrowthBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(1.0F).sound(SoundType.WET_GRASS).doesNotBlockMovement()), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ELEPHANT_EAR = createBlock("flora_elephant_ear", () -> new UndergrowthBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(1.0F).sound(SoundType.WET_GRASS).doesNotBlockMovement(), AbstractBlock.OffsetType.XYZ), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> HEMLOCK = createBlock("flora_hemlock", () -> new UndergrowthPoisonousBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(1.0F).sound(SoundType.WET_GRASS).doesNotBlockMovement(), AbstractBlock.OffsetType.XYZ), "cutout", ItemGroup.DECORATIONS);
    // Flora - Multistage
    public static RegistryObject<Block> ZIMBABWE_ALOE = createItemlessBlock("flora_zimbabwe_aloe", () -> new TallPlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(1.0F).sound(SoundType.WOOD)), "cutout");
    // Flora - Floating
    public static RegistryObject<Block> WATER_HYACINTH = createItemlessBlock("flora_water_hyacinth", () -> new FloatingPlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0.0F).sound(SoundType.PLANT).doesNotBlockMovement()), "cutout");
    // Flora - Algae
    public static RegistryObject<Block> AMAZON_SWORD = createBlock("flora_amazon_sword", () -> new AlgaeBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0).sound(SoundType.WET_GRASS).doesNotBlockMovement()), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> EELGRASS = createBlock("flora_eelgrass", () -> new AlgaeBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0).sound(SoundType.WET_GRASS).doesNotBlockMovement()), "cutout", ItemGroup.DECORATIONS);
    // Flora - Flowers
    public static RegistryObject<Block> ORCHID_MAGENTA = createBlock("flora_orchid_magenta", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.MAGENTA).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ORCHID_PURPLE = createBlock("flora_orchid_purple", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ORCHID_PINK = createBlock("flora_orchid_pink", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.PINK).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ORCHID_RED = createBlock("flora_orchid_red", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.RED).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), "cutout", ItemGroup.DECORATIONS);
    public static RegistryObject<Block> TITAN_ARUM = createItemlessBlock("flora_titan_arum", () -> new TitanArumBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(2.0F).sound(SoundType.WET_GRASS).doesNotBlockMovement()), "cutout");

    // Technical Blocks
    public static RegistryObject<Block> BURROW = createBlock("block_burrow", () -> new CritterBurrowBlock(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(1.0F).sound(SoundType.GROUND).doesNotBlockMovement()),  "translucent", ItemGroup.DECORATIONS);

    // Block Entities
    public static RegistryObject<TileEntityType<CageBlockEntity>> TILE_ENTITY_CAGE = TILE_ENTITIES.register("trap_cage", () -> new TileEntityType<>(CageBlockEntity::new, Sets.newHashSet(ModBlock.TRAP_CAGE.get()), null));
    public static RegistryObject<TileEntityType<CritterBurrowBlockEntity>> TILE_ENTITY_BURROW = TILE_ENTITIES.register("critter_burrow", () -> new TileEntityType<>(CritterBurrowBlockEntity::new, Sets.newHashSet(ModBlock.BURROW.get()), null));

    public static <B extends Block> RegistryObject<Block> createBlock(String name, Supplier<? extends B> supplier, ItemGroup group) {
        return createBlock(name, supplier, null, group, 0);
    }

    public static <B extends Block> RegistryObject<Block> createBlock(String name, Supplier<? extends B> supplier, @Nullable String renderType, ItemGroup group) {
        return createBlock(name, supplier, renderType, group, 0);
    }

    public static <B extends Block> RegistryObject<Block> createBlock(String name, Supplier<? extends B> supplier, @Nullable String renderType, ItemGroup group, int burnTime) {
        RegistryObject<Block> block = ModBlock.BLOCKS.register(name, supplier);
        if (renderType != null) {
            RENDER_TYPE_DATA.add(new Pair<>(block, renderType));
        }
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group)) {
            public int getBurnTime(ItemStack itemStack) {
                return burnTime;
            }
        });
        return block;
    }

    public static <B extends Block> RegistryObject<Block> createItemlessBlock(String name, Supplier<? extends B> supplier) {
        return createItemlessBlock(name, supplier, null);
    }

    public static <B extends Block> RegistryObject<Block> createItemlessBlock(String name, Supplier<? extends B> supplier, @Nullable String renderType) {
        RegistryObject<Block> block = ModBlock.BLOCKS.register(name, supplier);
        if (renderType != null) {
            RENDER_TYPE_DATA.add(new Pair<>(block, renderType));
        }
        return block;
    }

    public static void registerRendering() {
        for (Pair<RegistryObject<Block>, String> i : RENDER_TYPE_DATA) {
            RenderType j = RenderType.getCutout();
            if (Objects.equals(i.getSecond(), "cutout"))
                j = RenderType.getCutout();
            else if (Objects.equals(i.getSecond(), "translucent"))
                j = RenderType.getTranslucent();
            RenderTypeLookup.setRenderLayer(i.getFirst().get(), j);
        }
    }
}
