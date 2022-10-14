package untamedwilds.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.item.*;
import untamedwilds.item.debug.*;
import untamedwilds.util.ModCreativeModeTab;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UntamedWilds.MOD_ID);
    // Wild Level Item instances

    // Debug Tools
    public static RegistryObject<Item> LOGO = createItem("logo", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> OWNERSHIP_DEED = createItem("ownership_deed", () -> new OwnershipDeedItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> DEBUG_ERASER = createItem("debug_eraser", () -> new EraserItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_ANALYZER = createItem("debug_analyzer", () -> new AnalyzerItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_IPECAC = createItem("debug_ipecac", () -> new IpecacItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_LOVE_POTION = createItem("debug_love_potion", () -> new LovePotionItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_GROWTH_TONIC = createItem("debug_growth_tonic", () -> new GrowthTonicItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_HIGHLIGHTER = createItem("debug_highlighter", () -> new HighlighterItem(new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.untamedwilds_items).rarity(Rarity.EPIC)));

    // Materials
    public static RegistryObject<Item> MATERIAL_FAT = createItem("material_fat", () -> new LardItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(1).saturationMod(1F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MATERIAL_BLUBBER = createItem("material_blubber", () -> new LardItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(1).saturationMod(1F).meat().build()).tab(CreativeModeTab.TAB_FOOD)){
        public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
            return 1200;
        }
    });
    public static RegistryObject<Item> MATERIAL_PEARL = createItem("material_pearl", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> RARE_GIANT_PEARL = createItem("material_giant_pearl", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> MATERIAL_SNAKE_SKIN = createItem("material_snake_skin", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> CHUM = createItem("chum", () -> new ChumItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1200, 0), 1F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));

    // Food
    public static RegistryObject<Item> MEAT_BEAR_RAW = createItem("food_bear_raw", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MEAT_BEAR_COOKED = createItem("food_bear_cooked", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(7).saturationMod(1F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_RAW = createItem("food_turtle_raw", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_COOKED = createItem("food_turtle_cooked", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.6F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_RAW = createItem("food_pachyderm_raw", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.7F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_COOKED = createItem("food_pachyderm_cooked", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(7).saturationMod(1.1F).meat().build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> FOOD_TURTLE_SOUP = createItem("food_turtle_soup", () -> new BowlFoodItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.6F).build()).tab(CreativeModeTab.TAB_FOOD).stacksTo(1)));
    public static RegistryObject<Item> FOOD_PEMMICAN = createItem("food_pemmican", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(1.0F).build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> VEGETABLE_AARDVARK_CUCUMBER = createItem("food_aardvark_cucumber", () -> new Item(new Item.Properties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build()).tab(CreativeModeTab.TAB_FOOD)));
    public static RegistryObject<Item> FOOD_HEMLOCK_STEW = createItem("food_hemlock_stew", () -> new BowlFoodItem(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.POISON, 1200, 3), 1.0F).alwaysEat().build()).tab(CreativeModeTab.TAB_FOOD).stacksTo(1)));

    // Hides
    public static RegistryObject<Item> HIDE_BEAR_ASHEN = createItem("hide_bear_ashen", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BLACK = createItem("hide_bear_black", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BROWN = createItem("hide_bear_brown", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_WHITE = createItem("hide_bear_white", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_JAGUAR = createItem("hide_bigcat_jaguar", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LEOPARD = createItem("hide_bigcat_leopard", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LION = createItem("hide_bigcat_lion", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PANTHER = createItem("hide_bigcat_panther", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PUMA = createItem("hide_bigcat_puma", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_SNOW_LEOPARD = createItem("hide_bigcat_snow_leopard", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_TIGER = createItem("hide_bigcat_tiger", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

    // Item Blocks
    public static RegistryObject<Item> SEED_TITAN_ARUM = createItem("flora_titan_arum_corm", () -> new ItemNameBlockItem(ModBlock.TITAN_ARUM.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static RegistryObject<Item> SEED_ZIMBABWE_ALOE = createItem("flora_zimbabwe_aloe_sapling", () -> new ItemNameBlockItem(ModBlock.ZIMBABWE_ALOE.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static RegistryObject<Item> WATER_HYACINTH_BLOCK = createItem("flora_water_hyacinth_item", () -> new WaterLilyBlockItem(ModBlock.WATER_HYACINTH.get(), new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

    public static <I extends Item> RegistryObject<I> createItem(String name, Supplier<? extends I> supplier) {
        return ModItems.ITEMS.register(name, supplier);
    }

    public static void registerSpawnItems() {
        // These items have no associated objects, as they are not supposed to be accessed, and I do not want to register each variant
        // Tarantula Items
        ModItems.ITEMS.register("egg_tarantula", () -> new MobEggItem(ModEntity.TARANTULA, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bottle_tarantula", () -> new MobBottledItem(ModEntity.TARANTULA, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Small Snake Items
        ModItems.ITEMS.register("egg_snake", () -> new MobEggItem(ModEntity.SNAKE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("spawn_snake", () -> new MobSpawnItem(ModEntity.SNAKE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Softshell Turtle Items
        ModItems.ITEMS.register("egg_softshell_turtle", () -> new MobEggItem(ModEntity.SOFTSHELL_TURTLE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("spawn_softshell_turtle", () -> new MobSpawnItem(ModEntity.SOFTSHELL_TURTLE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Giant Clam Items
        ModItems.ITEMS.register("egg_giant_clam", () -> new MobEggItem(ModEntity.GIANT_CLAM, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("spawn_giant_clam", () -> new MobSpawnItem(ModEntity.GIANT_CLAM, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Sunfish Items
        ModItems.ITEMS.register("egg_sunfish", () -> new MobEggItem(ModEntity.SUNFISH, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Trevally Items
        ModItems.ITEMS.register("egg_trevally", () -> new MobEggItem(ModEntity.TREVALLY, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_trevally", () -> new MobBucketedItem(ModEntity.TREVALLY, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Arowana Items
        ModItems.ITEMS.register("egg_arowana", () -> new MobEggItem(ModEntity.AROWANA, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_arowana", () -> new MobBucketedItem(ModEntity.AROWANA, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Football Fish Items
        ModItems.ITEMS.register("egg_football_fish", () -> new MobEggItem(ModEntity.FOOTBALL_FISH, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_football_fish", () -> new MobBucketedItem(ModEntity.FOOTBALL_FISH, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Giant Salamander Items
        ModItems.ITEMS.register("egg_giant_salamander", () -> new MobEggItem(ModEntity.GIANT_SALAMANDER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_giant_salamander", () -> new MobBucketedItem(ModEntity.GIANT_SALAMANDER, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Newt Items
        ModItems.ITEMS.register("egg_newt", () -> new MobEggItem(ModEntity.NEWT, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_newt", () -> new MobBucketedItem(ModEntity.NEWT, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Tortoise Items
        ModItems.ITEMS.register("egg_tortoise", () -> new MobEggItem(ModEntity.TORTOISE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("spawn_tortoise", () -> new MobSpawnItem(ModEntity.TORTOISE, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Large Snake Items
        ModItems.ITEMS.register("egg_large_snake", () -> new MobEggItem(ModEntity.ANACONDA, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Triggerfish Items
        ModItems.ITEMS.register("egg_triggerfish", () -> new MobEggItem(ModEntity.TRIGGERFISH, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_triggerfish", () -> new MobBucketedItem(ModEntity.TRIGGERFISH, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Catfish Items
        ModItems.ITEMS.register("egg_catfish", () -> new MobEggItem(ModEntity.CATFISH, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_catfish", () -> new MobBucketedItem(ModEntity.CATFISH, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // King Crab Items
        ModItems.ITEMS.register("egg_king_crab", () -> new MobEggItem(ModEntity.KING_CRAB, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
        ModItems.ITEMS.register("bucket_king_crab", () -> new MobBucketedItem(ModEntity.KING_CRAB, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));

        // Monitor Items
        ModItems.ITEMS.register("egg_monitor", () -> new MobEggItem(ModEntity.MONITOR, new Item.Properties().tab(ModCreativeModeTab.untamedwilds_items)));
    }
}
