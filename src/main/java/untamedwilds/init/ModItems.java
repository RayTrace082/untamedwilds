package untamedwilds.init;

import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.item.*;
import untamedwilds.item.debug.*;
import untamedwilds.util.ItemGroupUT;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UntamedWilds.MOD_ID);
    public static final List<RegistryObject<Item>> SPAWN_EGGS = Lists.newArrayList();

    // Wild World Item instances

    // Debug Tools
    public static RegistryObject<Item> LOGO = createItem("logo", () -> new Item(new Item.Properties().maxStackSize(1).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> OWNERSHIP_DEED = createItem("ownership_deed", () -> new OwnershipDeedItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> DEBUG_ERASER = createItem("debug_eraser", () -> new EraserItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_ANALYZER = createItem("debug_analyzer", () -> new AnalyzerItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_IPECAC = createItem("debug_ipecac", () -> new IpecacItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_LOVE_POTION = createItem("debug_love_potion", () -> new LovePotionItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_GROWTH_TONIC = createItem("debug_growth_tonic", () -> new GrowthTonicItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_HIGHLIGHTER = createItem("debug_highlighter", () -> new HighlighterItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));

    // Materials
    public static RegistryObject<Item> MATERIAL_FAT = createItem("material_fat", () -> new LardItem(new Item.Properties().food((new Food.Builder()).hunger(1).saturation(1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MATERIAL_PEARL = createItem("material_pearl", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> RARE_GIANT_PEARL = createItem("material_giant_pearl", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> MATERIAL_SNAKE_SKIN = createItem("material_snake_skin", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> CHUM = createItem("chum", () -> new ChumItem(new Item.Properties().food((new Food.Builder()).hunger(1).saturation(0.1F).effect(() -> new EffectInstance(Effects.NAUSEA, 1200, 0), 1F).meat().build()).group(ItemGroup.FOOD)));

    // Food
    public static RegistryObject<Item> MEAT_BEAR_RAW = createItem("food_bear_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.6F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_BEAR_COOKED = createItem("food_bear_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(7).saturation(1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_RAW = createItem("food_turtle_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(2).saturation(0.3F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_COOKED = createItem("food_turtle_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(0.6F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_RAW = createItem("food_pachyderm_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.7F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_COOKED = createItem("food_pachyderm_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(7).saturation(1.1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> FOOD_TURTLE_SOUP = createItem("food_turtle_soup", () -> new SoupItem(new Item.Properties().food((new Food.Builder()).hunger(8).saturation(0.6F).build()).group(ItemGroup.FOOD).maxStackSize(1)));
    public static RegistryObject<Item> FOOD_PEMMICAN = createItem("food_pemmican", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(1.0F).build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> VEGETABLE_AARDVARK_CUCUMBER = createItem("food_aardvark_cucumber", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.2F).build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> FOOD_HEMLOCK_STEW = createItem("food_hemlock_stew", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(0.1F).effect(() -> new EffectInstance(Effects.POISON, 1200, 3), 1.0F).setAlwaysEdible().build()).group(ItemGroup.FOOD).maxStackSize(1)));

    // Hides
    public static RegistryObject<Item> HIDE_BEAR_ASHEN = createItem("hide_bear_ashen", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BLACK = createItem("hide_bear_black", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BROWN = createItem("hide_bear_brown", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_WHITE = createItem("hide_bear_white", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_JAGUAR = createItem("hide_bigcat_jaguar", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LEOPARD = createItem("hide_bigcat_leopard", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LION = createItem("hide_bigcat_lion", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PANTHER = createItem("hide_bigcat_panther", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PUMA = createItem("hide_bigcat_puma", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_SNOW_LEOPARD = createItem("hide_bigcat_snow_leopard", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_TIGER = createItem("hide_bigcat_tiger", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));

    // Item Blocks
    public static RegistryObject<Item> SEED_TITAN_ARUM = createItem("flora_titan_arum_corm", () -> new BlockNamedItem(ModBlock.TITAN_ARUM.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static RegistryObject<Item> WATER_HYACINTH_BLOCK = createItem("flora_water_hyacinth_item", () -> new LilyPadItem(ModBlock.WATER_HYACINTH.get(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));

    public static <I extends Item> RegistryObject<I> createItem(String name, Supplier<? extends I> supplier) {
        return ModItems.ITEMS.register(name, supplier);
    }

    public static void registerSpawnItems() {
        // These items have no associated objects, as they are not supposed to be accessed, and I do not want to register each variant
        // Tarantula Items
        if (ConfigMobControl.addTarantula.get()) {
            ModItems.ITEMS.register("egg_tarantula", () -> new MobEggItem(ModEntity.TARANTULA, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bottle_tarantula", () -> new MobBottledItem(ModEntity.TARANTULA, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Small Snake Items
        if (ConfigMobControl.addSnake.get()) {
            ModItems.ITEMS.register("egg_snake", () -> new MobEggItem(ModEntity.SNAKE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("spawn_snake", () -> new MobSpawnItem(ModEntity.SNAKE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Softshell Turtle Items
        if (ConfigMobControl.addSoftshellTurtle.get()) {
            ModItems.ITEMS.register("egg_softshell_turtle", () -> new MobEggItem(ModEntity.SOFTSHELL_TURTLE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("spawn_softshell_turtle", () -> new MobSpawnItem(ModEntity.SOFTSHELL_TURTLE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Giant Clam Items
        if (ConfigMobControl.addGiantClam.get()) {
            ModItems.ITEMS.register("egg_giant_clam", () -> new MobEggItem(ModEntity.GIANT_CLAM, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("spawn_giant_clam", () -> new MobSpawnItem(ModEntity.GIANT_CLAM, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Sunfish Items
        if (ConfigMobControl.addSunfish.get()) {
            ModItems.ITEMS.register("egg_sunfish", () -> new MobEggItem(ModEntity.SUNFISH, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Trevally Items
        if (ConfigMobControl.addTrevally.get()) {
            ModItems.ITEMS.register("egg_trevally", () -> new MobEggItem(ModEntity.TREVALLY, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_trevally", () -> new MobBucketedItem(ModEntity.TREVALLY, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Arowana Items
        if (ConfigMobControl.addArowana.get()) {
            ModItems.ITEMS.register("egg_arowana", () -> new MobEggItem(ModEntity.AROWANA, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_arowana", () -> new MobBucketedItem(ModEntity.AROWANA, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Football Fish Items
        if (ConfigMobControl.addFootballFish.get()) {
            ModItems.ITEMS.register("egg_football_fish", () -> new MobEggItem(ModEntity.FOOTBALL_FISH, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_football_fish", () -> new MobBucketedItem(ModEntity.FOOTBALL_FISH, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Giant Salamander Items
        if (ConfigMobControl.addGiantSalamander.get()) {
            ModItems.ITEMS.register("egg_giant_salamander", () -> new MobEggItem(ModEntity.GIANT_SALAMANDER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_giant_salamander", () -> new MobBucketedItem(ModEntity.GIANT_SALAMANDER, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Newt Items
        if (ConfigMobControl.addNewt.get()) {
            ModItems.ITEMS.register("egg_newt", () -> new MobEggItem(ModEntity.NEWT, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_newt", () -> new MobBucketedItem(ModEntity.NEWT, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Tortoise Items
        if (ConfigMobControl.addTortoise.get()) {
            ModItems.ITEMS.register("egg_tortoise", () -> new MobEggItem(ModEntity.TORTOISE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("spawn_tortoise", () -> new MobSpawnItem(ModEntity.TORTOISE, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Large Snake Items
        if (ConfigMobControl.addLargeSnake.get()) {
            ModItems.ITEMS.register("egg_large_snake", () -> new MobEggItem(ModEntity.ANACONDA, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Triggerfish Items
        if (ConfigMobControl.addTriggerfish.get()) {
            ModItems.ITEMS.register("egg_triggerfish", () -> new MobEggItem(ModEntity.TRIGGERFISH, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            ModItems.ITEMS.register("bucket_triggerfish", () -> new MobBucketedItem(ModEntity.TRIGGERFISH, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
    }
}
