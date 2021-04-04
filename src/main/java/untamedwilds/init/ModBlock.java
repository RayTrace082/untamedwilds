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
import untamedwilds.block.blockentity.CageBlockEntity;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.item.FuelBlockItem;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UntamedWilds.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, UntamedWilds.MOD_ID);

    public static RegistryObject<Block> CARPET_STRAW  = createBlock("carpet_straw", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS);
    public static RegistryObject<Block> CARPET_BEAR_ASHEN  = createBlock("carpet_bear_ashen", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GRAY).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBear.get());
    public static RegistryObject<Block> CARPET_BEAR_BLACK  = createBlock("carpet_bear_black", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BLACK).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBear.get());
    public static RegistryObject<Block> CARPET_BEAR_BROWN  = createBlock("carpet_bear_brown", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBear.get());
    public static RegistryObject<Block> CARPET_BEAR_WHITE  = createBlock("carpet_bear_white", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.SNOW).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBear.get());

    public static RegistryObject<Block> CARPET_BIGCAT_JAGUAR  = createBlock("carpet_bigcat_jaguar", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GOLD).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_LEOPARD  = createBlock("carpet_bigcat_leopard", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GOLD).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_LION  = createBlock("carpet_bigcat_lion", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_PANTHER  = createBlock("carpet_bigcat_panther", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.SNOW).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_PUMA  = createBlock("carpet_bigcat_puma", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.GRAY).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_SNOW  = createBlock("carpet_bigcat_snow_leopard", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BLACK).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());
    public static RegistryObject<Block> CARPET_BIGCAT_TIGER  = createBlock("carpet_bigcat_tiger", () -> new CarpetBlock(Block.Properties.create(Material.CARPET, MaterialColor.BROWN).hardnessAndResistance(0.1F).sound(SoundType.CLOTH)), ItemGroup.DECORATIONS, ConfigMobControl.addBigCat.get());

    public static RegistryObject<Block> TRAP_CAGE  = createBlock("trap_cage", () -> new CageBlock(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD)), ItemGroup.TRANSPORTATION);
    public static RegistryObject<Block> LARD_BLOCK  = createBlock("block_lard", () -> new LardBlock(Block.Properties.create(Material.CLAY, MaterialColor.YELLOW).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.BUILDING_BLOCKS);
    public static RegistryObject<Block> PEARL_BLOCK  = createBlock("block_pearl", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE)), ItemGroup.BUILDING_BLOCKS, ConfigMobControl.addGiantClam.get());
    
    public static RegistryObject<Block> ANEMONE_ROSE_BULB  = createBlock("anemone_rose_bulb", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.RED).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS, ConfigFeatureControl.addAnemones.get());
    public static RegistryObject<Block> ANEMONE_SAND  = createBlock("anemone_sand", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.PINK).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS, ConfigFeatureControl.addAnemones.get());
    public static RegistryObject<Block> ANEMONE_SEBAE  = createBlock("anemone_sebae", () -> new AnemoneBlock(Block.Properties.create(Material.OCEAN_PLANT, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(0.1F).sound(SoundType.SLIME)), ItemGroup.DECORATIONS, ConfigFeatureControl.addAnemones.get());
    public static RegistryObject<Block> ORCHID_MAGENTA = createBlock("flora_orchid_magenta", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.MAGENTA).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS, ConfigFeatureControl.addTreeOrchids.get());
    public static RegistryObject<Block> ORCHID_PURPLE = createBlock("flora_orchid_purple", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS, ConfigFeatureControl.addTreeOrchids.get());
    public static RegistryObject<Block> ORCHID_PINK = createBlock("flora_orchid_pink", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.PINK).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS, ConfigFeatureControl.addTreeOrchids.get());
    public static RegistryObject<Block> ORCHID_RED = createBlock("flora_orchid_red", () -> new EpyphitePlantBlock(Block.Properties.create(Material.PLANTS, MaterialColor.RED).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), ItemGroup.DECORATIONS, ConfigFeatureControl.addTreeOrchids.get());
    public static RegistryObject<Block> COMMON_REED = createBlock("flora_common_reed", () -> new ReedBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0.1F).sound(SoundType.VINE).doesNotBlockMovement()), ItemGroup.DECORATIONS, ConfigFeatureControl.addReeds.get(), 100);
    public static RegistryObject<Block> AMAZON_SWORD = createBlock("flora_amazon_sword", () -> new AlgaeBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(0).sound(SoundType.WET_GRASS).doesNotBlockMovement()), ItemGroup.DECORATIONS, ConfigFeatureControl.addAlgae.get());
    public static RegistryObject<Block> BUSH_TEMPERATE = createBlock("flora_bush_temperate", () -> new UndergrowthBlock(Block.Properties.create(Material.PLANTS, MaterialColor.GREEN).hardnessAndResistance(1.0F).sound(SoundType.WET_GRASS).doesNotBlockMovement()), ItemGroup.DECORATIONS, ConfigFeatureControl.addBushes.get());

    public static RegistryObject<TileEntityType<CageBlockEntity>> BLOCKENTITY_CAGE = TILE_ENTITY_TYPES.register("trap_cage", () -> new TileEntityType<>(CageBlockEntity::new, Sets.newHashSet(ModBlock.TRAP_CAGE.get()), null));

    public static <B extends Block> RegistryObject<B> createBlock(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group) {
        return createBlock(name, supplier, group, true);
    }

    public static <B extends Block> RegistryObject<B> createBlock(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group, boolean add) {
        return createBlock(name, supplier, group, add, 0);
    }

    public static <B extends Block> RegistryObject<B> createBlock(String name, Supplier<? extends B> supplier, @Nullable ItemGroup group, boolean add, int burnTime) {
        if (add) {
            RegistryObject<B> block = ModBlock.BLOCKS.register(name, supplier);
            if (burnTime != 0) {
                ModItems.ITEMS.register(name, () -> new FuelBlockItem(block.get(), burnTime, new Item.Properties().group(group)));
            }
            else {
                ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(group)));
            }
            return block;
        }
        return null;
    }

    public static <B extends Block> RegistryObject<B> createItemlessBlock(String name, Supplier<? extends B> supplier) {
        return ModBlock.BLOCKS.register(name, supplier);
    }

    public static void registerRendering() {
        if (ConfigFeatureControl.addReeds.get()) {
            RenderTypeLookup.setRenderLayer(ModBlock.COMMON_REED.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.AMAZON_SWORD.get(), RenderType.getCutout());
        }
        if (ConfigFeatureControl.addAnemones.get()) {
            RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_SEBAE.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_SAND.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.ANEMONE_ROSE_BULB.get(), RenderType.getCutout());
        }
        if (ConfigFeatureControl.addTreeOrchids.get()) {
            RenderTypeLookup.setRenderLayer(ModBlock.ORCHID_MAGENTA.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.ORCHID_PINK.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.ORCHID_PURPLE.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlock.ORCHID_RED.get(), RenderType.getCutout());
        }
        if (ConfigFeatureControl.addBushes.get()) {
            RenderTypeLookup.setRenderLayer(ModBlock.BUSH_TEMPERATE.get(), RenderType.getCutout());
        }
    }
}
