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
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.amphibian.EntityNewt;
import untamedwilds.entity.fish.EntityArowana;
import untamedwilds.entity.fish.EntityFootballFish;
import untamedwilds.entity.fish.EntitySunfish;
import untamedwilds.entity.fish.EntityTrevally;
import untamedwilds.entity.mollusk.EntityGiantClam;
import untamedwilds.entity.reptile.EntityAnaconda;
import untamedwilds.entity.reptile.EntitySnake;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;
import untamedwilds.entity.reptile.EntityTortoise;
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
            ModItems.ITEMS.register("egg_snake", () -> new MobEggItem(ModEntity.SNAKE, EntitySnake.SpeciesSnake.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntitySnake.SpeciesSnake.values().length; i++) {
                int snake = i;
                ModItems.ITEMS.register("snake_" + EntitySnake.SpeciesSnake.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.SNAKE, snake, EntitySnake.SpeciesSnake.values()[snake].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Softshell Turtle Items
        if (ConfigMobControl.addSoftshellTurtle.get()) {
            ModItems.ITEMS.register("egg_softshell_turtle", () -> new MobEggItem(ModEntity.SOFTSHELL_TURTLE, EntitySoftshellTurtle.SpeciesSoftshellTurtle.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntitySoftshellTurtle.SpeciesSoftshellTurtle.values().length; i++) {
                int softshell_turtleSpecies = i;
                ModItems.ITEMS.register("softshell_turtle_" + EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.SOFTSHELL_TURTLE, softshell_turtleSpecies, EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[softshell_turtleSpecies].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Giant Clam Items
        if (ConfigMobControl.addGiantClam.get()) {
            ModItems.ITEMS.register("egg_giant_clam", () -> new MobEggItem(ModEntity.GIANT_CLAM, EntityGiantClam.SpeciesGiantClam.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityGiantClam.SpeciesGiantClam.values().length; i++) {
                int giant_clamSpecies = i;
                // TODO: Keep in mind the achievement when streamlining mob spawn items
                ModItems.ITEMS.register("giant_clam_" + EntityGiantClam.SpeciesGiantClam.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.GIANT_CLAM, giant_clamSpecies, EntityGiantClam.SpeciesGiantClam.values()[giant_clamSpecies].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }

        // Sunfish Items
        if (ConfigMobControl.addSunfish.get()) {
            ModItems.ITEMS.register("egg_sunfish", () -> new MobEggItem(ModEntity.SUNFISH, EntitySunfish.SpeciesSunfish.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
        // Trevally Items
        if (ConfigMobControl.addTrevally.get()) {
            ModItems.ITEMS.register("egg_trevally", () -> new MobEggItem(ModEntity.TREVALLY, EntityTrevally.SpeciesTrevally.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityTrevally.SpeciesTrevally.values().length; i++) {
                int trevallySpecies = i;
                ModItems.ITEMS.register("bucket_trevally_" + EntityTrevally.SpeciesTrevally.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.TREVALLY, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), trevallySpecies, EntityTrevally.SpeciesTrevally.values()[trevallySpecies].name().toLowerCase()));
            }
        }
        // Arowana Items
        if (ConfigMobControl.addArowana.get()) {
            ModItems.ITEMS.register("egg_arowana", () -> new MobEggItem(ModEntity.AROWANA, EntityArowana.SpeciesArowana.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityArowana.SpeciesArowana.values().length; i++) {
                int arowanaSpecies = i;
                ModItems.ITEMS.register("bucket_arowana_" + EntityArowana.SpeciesArowana.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.AROWANA, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), arowanaSpecies, EntityArowana.SpeciesArowana.values()[arowanaSpecies].name().toLowerCase()));
            }
        }
        // Football Fish Items
        if (ConfigMobControl.addFootballFish.get()) {
            ModItems.ITEMS.register("egg_football_fish", () -> new MobEggItem(ModEntity.FOOTBALL_FISH, EntityFootballFish.SpeciesFootballFish.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityFootballFish.SpeciesFootballFish.values().length; i++) {
                int footballFishSpecies = i;
                ModItems.ITEMS.register("bucket_football_fish_" + EntityFootballFish.SpeciesFootballFish.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.FOOTBALL_FISH, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), footballFishSpecies, EntityFootballFish.SpeciesFootballFish.values()[footballFishSpecies].name().toLowerCase()));
            }
        }
        // Giant Salamander Items
        if (ConfigMobControl.addGiantSalamander.get()) {
            ModItems.ITEMS.register("egg_giant_salamander", () -> new MobEggItem(ModEntity.GIANT_SALAMANDER, EntityGiantSalamander.SpeciesGiantSalamander.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityGiantSalamander.SpeciesGiantSalamander.values().length; i++) {
                int giant_salamander = i;
                ModItems.ITEMS.register("bucket_giant_salamander_" + EntityGiantSalamander.SpeciesGiantSalamander.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.GIANT_SALAMANDER, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), giant_salamander, EntityGiantSalamander.SpeciesGiantSalamander.values()[giant_salamander].name().toLowerCase()));
            }
        }
        // Newt Items
        if (ConfigMobControl.addNewt.get()) {
            ModItems.ITEMS.register("egg_newt", () -> new MobEggItem(ModEntity.NEWT, EntityNewt.SpeciesNewt.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityNewt.SpeciesNewt.values().length; i++) {
                int newt = i;
                ModItems.ITEMS.register("bucket_newt_" + EntityNewt.SpeciesNewt.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.NEWT, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), newt, EntityNewt.SpeciesNewt.values()[newt].name().toLowerCase()));
            }
        }
        // Tortoise Items
        if (ConfigMobControl.addTortoise.get()) {
            ModItems.ITEMS.register("egg_tortoise", () -> new MobEggItem(ModEntity.TORTOISE, EntityTortoise.SpeciesTortoise.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            for (int i = 0; i < EntityTortoise.SpeciesTortoise.values().length; i++) {
                int tortoise = i;
                ModItems.ITEMS.register("tortoise_" + EntityTortoise.SpeciesTortoise.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.TORTOISE, tortoise, EntityTortoise.SpeciesTortoise.values()[tortoise].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Large Snake Items
        if (ConfigMobControl.addLargeSnake.get()) {
            ModItems.ITEMS.register("egg_large_snake", () -> new MobEggItem(ModEntity.ANACONDA, EntityAnaconda.SpeciesAnaconda.values().length, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
        }
    }
}
