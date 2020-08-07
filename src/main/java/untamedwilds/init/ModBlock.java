package untamedwilds.init;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.block.*;
import untamedwilds.block.tileentity.BlockEntityCage;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, UntamedWilds.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, UntamedWilds.MOD_ID);

    public static RegistryObject<Block> CARPET_STRAW  = createBlock("carpet_straw", () -> new BlockCarpet(Block.Properties.create(Material.CARPET, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_ASHEN  = createBlock("carpet_bear_ashen", () -> new BlockCarpet(Block.Properties.create(Material.CARPET, MaterialColor.GRAY).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_BLACK  = createBlock("carpet_bear_black", () -> new BlockCarpet(Block.Properties.create(Material.CARPET, MaterialColor.BLACK).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_BROWN  = createBlock("carpet_bear_brown", () -> new BlockCarpet(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_WHITE  = createBlock("carpet_bear_white", () -> new BlockCarpet(Block.Properties.create(Material.CARPET, MaterialColor.SNOW).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS);

    public static RegistryObject<Block> TRAP_CAGE  = createBlock("trap_cage", () -> new BlockCage(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD)), ItemGroup.TRANSPORTATION);
    public static RegistryObject<Block> LARD_BLOCK  = createBlock("block_lard", () -> new BlockLard(Block.Properties.create(Material.CLAY, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.BUILDING_BLOCKS);
    public static RegistryObject<Block> PEARL_BLOCK  = createBlock("block_pearl", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE)), ItemGroup.BUILDING_BLOCKS);


    public static RegistryObject<Block> ANEMONE_ROSE_BULB  = createBlock("anemone_rose_bulb", () -> new BlockFaunaAnemone(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.RED).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ANEMONE_SAND  = createBlock("anemone_sand", () -> new BlockFaunaAnemone(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.RED).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ANEMONE_SEBAE  = createBlock("anemone_sebae", () -> new BlockFaunaAnemone(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.RED).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> ORCHID_RED = createBlock("flora_orchid_red", () -> new BlockPlantEpyphite(Block.Properties.create(Material.PLANTS, MaterialColor.RED).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS);

    public static RegistryObject<TileEntityType<BlockEntityCage>> BLOCKENTITY_CAGE = TILE_ENTITY_TYPES.register("trap_cage", () -> new TileEntityType<>(BlockEntityCage::new, Sets.newHashSet(ModBlock.TRAP_CAGE.get()), null));

    public static <B extends Block> RegistryObject<B> createBlock(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group) {
        RegistryObject<B> block = ModBlock.BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group)));
        return block;
    }

    public static <B extends Block> RegistryObject<B> createItemlessBlock(String name, Supplier<? extends B> supplier) {
        RegistryObject<B> block = ModBlock.BLOCKS.register(name, supplier);
        return block;
    }

    public static void registerRendering() {
        RenderTypeLookup.setRenderLayer(ModBlock.ORCHID_RED.get(), RenderType.getCutout());

        RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_SEBAE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_SAND.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_ROSE_BULB.get(), RenderType.getCutout());
    }
}
