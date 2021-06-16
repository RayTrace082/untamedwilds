package untamedwilds.init;

import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.config.ConfigFeatureControl;
import untamedwilds.config.ConfigMobControl;
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.EntityArowana;
import untamedwilds.entity.fish.EntityFootballFish;
import untamedwilds.entity.fish.EntitySunfish;
import untamedwilds.entity.fish.EntityTrevally;
import untamedwilds.entity.mollusk.EntityGiantClam;
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
    public static RegistryObject<Item> OWNERSHIP_DEED = createItem("ownership_deed", () -> new OwnershipDeedItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> DEBUG_ERASER = createItem("debug_eraser", () -> new EraserItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_ANALYZER = createItem("debug_analyzer", () -> new AnalyzerItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_IPECAC = createItem("debug_ipecac", () -> new IpecacItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_LOVE_POTION = createItem("debug_love_potion", () -> new LovePotionItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_GROWTH_TONIC = createItem("debug_growth_tonic", () -> new GrowthTonicItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));
    public static RegistryObject<Item> DEBUG_HIGHLIGHTER = createItem("debug_highlighter", () -> new HighlighterItem(new Item.Properties().maxStackSize(1).group(ItemGroupUT.untamedwilds_items).rarity(Rarity.EPIC)));

    // Materials
    public static RegistryObject<Item> MATERIAL_FAT = createItem("material_fat", () -> new LardItem(new Item.Properties().food((new Food.Builder()).hunger(1).saturation(1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MATERIAL_PEARL = createItem(ConfigMobControl.addGiantClam.get(),"material_pearl", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> RARE_GIANT_PEARL = createItem(ConfigMobControl.addGiantClam.get(), "material_giant_pearl", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> MATERIAL_SNAKE_SKIN = createItem(ConfigMobControl.addSnake.get(),"material_snake_skin", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));

    // Food
    public static RegistryObject<Item> MEAT_BEAR_RAW = createItem(ConfigMobControl.addBear.get(),"food_bear_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.6F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_BEAR_COOKED = createItem(ConfigMobControl.addBear.get(),"food_bear_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(7).saturation(1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_RAW = createItem(ConfigMobControl.addSoftshellTurtle.get(),"food_turtle_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(2).saturation(0.3F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_TURTLE_COOKED = createItem(ConfigMobControl.addSoftshellTurtle.get(),"food_turtle_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(0.6F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_RAW = createItem(ConfigMobControl.addHippo.get(),"food_pachyderm_raw", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.7F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> MEAT_HIPPO_COOKED = createItem(ConfigMobControl.addHippo.get(),"food_pachyderm_cooked", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(7).saturation(1.1F).meat().build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> FOOD_TURTLE_SOUP = createItem(ConfigMobControl.addSoftshellTurtle.get(),"food_turtle_soup", () -> new SoupItem(new Item.Properties().food((new Food.Builder()).hunger(8).saturation(0.6F).build()).group(ItemGroup.FOOD).maxStackSize(1)));
    public static RegistryObject<Item> FOOD_PEMMICAN = createItem("food_pemmican", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(1.0F).build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> VEGETABLE_AARDVARK_CUCUMBER = createItem(ConfigMobControl.addAardvark.get(),"food_aardvark_cucumber", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(3).saturation(0.2F).build()).group(ItemGroup.FOOD)));
    public static RegistryObject<Item> FOOD_HEMLOCK_STEW = createItem("food_hemlock_stew", () -> new Item(new Item.Properties().food((new Food.Builder()).hunger(6).saturation(0.1F).effect(new EffectInstance(Effects.POISON, 1200, 3), 1.0F).setAlwaysEdible().build()).group(ItemGroup.FOOD).maxStackSize(1))); // Advancement Trigger: "Asebeia"
    public static RegistryObject<Item> SEED_TITAN_ARUM = createItem(ConfigFeatureControl.addFlora.get(), "flora_titan_arum_corm", () -> new BlockNamedItem(ModBlock.TITAN_ARUM.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));

    // Hides
    public static RegistryObject<Item> HIDE_BEAR_ASHEN = createItem(ConfigMobControl.addBear.get(),"hide_bear_ashen", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BLACK = createItem(ConfigMobControl.addBear.get(),"hide_bear_black", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_BROWN = createItem(ConfigMobControl.addBear.get(),"hide_bear_brown", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BEAR_WHITE = createItem(ConfigMobControl.addBear.get(),"hide_bear_white", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_JAGUAR = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_jaguar", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LEOPARD = createItem(ConfigMobControl.addBigCat.get(), "hide_bigcat_leopard", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_LION = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_lion", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PANTHER = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_panther", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_PUMA = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_puma", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_SNOW_LEOPARD = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_snow_leopard", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
    public static RegistryObject<Item> HIDE_BIGCAT_TIGER = createItem(ConfigMobControl.addBigCat.get(),"hide_bigcat_tiger", () -> new Item(new Item.Properties().group(ItemGroupUT.untamedwilds_items)));

    public static <I extends Item> RegistryObject<I> createItem(String name, Supplier<? extends I> supplier) {
        return createItem(true, name, supplier);
    }

    public static <I extends Item> RegistryObject<I> createItem(boolean enable, String name, Supplier<? extends I> supplier) {
        if (enable) {
            return ModItems.ITEMS.register(name, supplier);
        }
        return null;
    }

    public static void registerSpawnItems() {
        // These items have no associated objects, as they are not supposed to be accessed, and I do not want to register each variant
        // Tarantula Items
        if (ConfigMobControl.addTarantula.get()) {
            for (int i = 0; i < EntityTarantula.SpeciesTarantula.values().length; i++) {
                int tarantulaSpecies = i;
                ModItems.ITEMS.register("egg_tarantula_" + EntityTarantula.SpeciesTarantula.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.TARANTULA, tarantulaSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("bottle_tarantula_" + EntityTarantula.SpeciesTarantula.values()[i].name().toLowerCase(), () -> new MobBottledItem(ModEntity.TARANTULA, tarantulaSpecies, EntityTarantula.SpeciesTarantula.values()[tarantulaSpecies].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Small Snake Items
        if (ConfigMobControl.addSnake.get()) {
            for (int i = 0; i < EntitySnake.SpeciesSnake.values().length; i++) {
                int snake = i;
                ModItems.ITEMS.register("snake_" + EntitySnake.SpeciesSnake.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.SNAKE, snake, EntitySnake.SpeciesSnake.values()[snake].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("egg_snake_" + EntitySnake.SpeciesSnake.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.SNAKE, snake, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Softshell Turtle Items
        if (ConfigMobControl.addSoftshellTurtle.get()) {
            for (int i = 0; i < EntitySoftshellTurtle.SpeciesSoftshellTurtle.values().length; i++) {
                int softshell_turtleSpecies = i;
                ModItems.ITEMS.register("softshell_turtle_" + EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.SOFTSHELL_TURTLE, softshell_turtleSpecies, EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[softshell_turtleSpecies].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("egg_softshell_turtle_" + EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.SOFTSHELL_TURTLE, softshell_turtleSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Giant Clam Items
        if (ConfigMobControl.addGiantClam.get()) {
            for (int i = 0; i < EntityGiantClam.SpeciesGiantClam.values().length; i++) {
                int giant_clamSpecies = i;
                ModItems.ITEMS.register("giant_clam_" + EntityGiantClam.SpeciesGiantClam.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.GIANT_CLAM, giant_clamSpecies, EntityGiantClam.SpeciesGiantClam.values()[giant_clamSpecies].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("egg_giant_clam_" + EntityGiantClam.SpeciesGiantClam.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.GIANT_CLAM, giant_clamSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }

        // Sunfish Items
        if (ConfigMobControl.addSunfish.get()) {
            for (int i = 0; i < EntitySunfish.SpeciesSunfish.values().length; i++) {
                int sunfishSpecies = i;
                ModItems.ITEMS.register("egg_sunfish_" + EntitySunfish.SpeciesSunfish.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.SUNFISH, sunfishSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
        // Trevally Items
        if (ConfigMobControl.addTrevally.get()) {
            for (int i = 0; i < EntityTrevally.SpeciesTrevally.values().length; i++) {
                int trevallySpecies = i;
                ModItems.ITEMS.register("egg_trevally_" + EntityTrevally.SpeciesTrevally.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.TREVALLY, trevallySpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("bucket_trevally_" + EntityTrevally.SpeciesTrevally.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.TREVALLY, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), trevallySpecies, EntityTrevally.SpeciesTrevally.values()[trevallySpecies].name().toLowerCase()));
            }
        }
        // Arowana Items
        if (ConfigMobControl.addArowana.get()) {
            for (int i = 0; i < EntityArowana.SpeciesArowana.values().length; i++) {
                int arowanaSpecies = i;
                ModItems.ITEMS.register("egg_arowana_" + EntityArowana.SpeciesArowana.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.AROWANA, arowanaSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("bucket_arowana_" + EntityArowana.SpeciesArowana.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.AROWANA, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), arowanaSpecies, EntityArowana.SpeciesArowana.values()[arowanaSpecies].name().toLowerCase()));
            }
        }
        // Football Fish Items
        if (ConfigMobControl.addFootballFish.get()) {
            for (int i = 0; i < EntityFootballFish.SpeciesFootballFish.values().length; i++) {
                int footballFishSpecies = i;
                ModItems.ITEMS.register("egg_football_fish_" + EntityFootballFish.SpeciesFootballFish.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.FOOTBALL_FISH, footballFishSpecies, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("bucket_football_fish_" + EntityFootballFish.SpeciesFootballFish.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.FOOTBALL_FISH, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), footballFishSpecies, EntityFootballFish.SpeciesFootballFish.values()[footballFishSpecies].name().toLowerCase()));
            }
        }
        // Giant Salamander Items
        if (ConfigMobControl.addGiantSalamander.get()) {
            for (int i = 0; i < EntityGiantSalamander.SpeciesGiantSalamander.values().length; i++) {
                int giant_salamander = i;
                ModItems.ITEMS.register("egg_giant_salamander_" + EntityGiantSalamander.SpeciesGiantSalamander.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.GIANT_SALAMANDER, giant_salamander, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("bucket_giant_salamander_" + EntityGiantSalamander.SpeciesGiantSalamander.values()[i].name().toLowerCase(), () -> new MobBucketedItem(ModEntity.GIANT_SALAMANDER, Fluids.WATER, new Item.Properties().group(ItemGroupUT.untamedwilds_items), giant_salamander, EntityGiantSalamander.SpeciesGiantSalamander.values()[giant_salamander].name().toLowerCase()));
            }
        }
        // Tortoise Items
        if (ConfigMobControl.addGiantSalamander.get()) {
            for (int i = 0; i < EntityTortoise.SpeciesTortoise.values().length; i++) {
                int tortoise = i;
                ModItems.ITEMS.register("egg_tortoise_" + EntityTortoise.SpeciesTortoise.values()[i].name().toLowerCase(), () -> new MobEggItem(ModEntity.TORTOISE, tortoise, new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
                ModItems.ITEMS.register("tortoise_" + EntityTortoise.SpeciesTortoise.values()[i].name().toLowerCase(), () -> new MobSpawnItem(ModEntity.TORTOISE, tortoise, EntityTortoise.SpeciesTortoise.values()[tortoise].name().toLowerCase(), new Item.Properties().group(ItemGroupUT.untamedwilds_items)));
            }
        }
    }

    public static void addSpawnEggWithData(int speciesNumber, String typeName) {
        NonNullList<ItemStack> items = NonNullList.create();

        ItemGroupUT.untamedwilds_items.fill(items);
    }
}
