package untamedwilds.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.client.render.*;
import untamedwilds.entity.ProjectileSpit;
import untamedwilds.entity.amphibian.EntityGiantSalamander;
import untamedwilds.entity.amphibian.EntityNewt;
import untamedwilds.entity.arthropod.EntityKingCrab;
import untamedwilds.entity.arthropod.EntityTarantula;
import untamedwilds.entity.fish.*;
import untamedwilds.entity.mammal.*;
import untamedwilds.entity.mollusk.EntityGiantClam;
import untamedwilds.entity.relict.EntitySpitter;
import untamedwilds.entity.reptile.*;
import untamedwilds.item.UntamedSpawnEggItem;
import untamedwilds.world.FaunaHandler;

import java.util.List;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntity {
    public final static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, UntamedWilds.MOD_ID);
    //public static Map<RegistryObject<EntityType<? extends Mob>>, EntityRendererProvider<?>> map = Collections.emptyMap(); 
    
    // Arthropods
    public static RegistryObject<EntityType<EntityTarantula>> TARANTULA = createEntity(EntityTarantula::new, "tarantula", 0.4f, 0.3f, 0xB5B095, 0x26292B);
    public static RegistryObject<EntityType<EntityKingCrab>> KING_CRAB = createEntity(EntityKingCrab::new, "king_crab", 0.6f, 0.5f, 0x715236, 0xAD9050);

    // Reptiles
    public static RegistryObject<EntityType<EntitySnake>> SNAKE = createEntity(EntitySnake::new, "snake", 0.95f, 0.3f, 0xD8A552, 0x5C3525);
    public static RegistryObject<EntityType<EntitySoftshellTurtle>> SOFTSHELL_TURTLE = createEntity(EntitySoftshellTurtle::new, "softshell_turtle", 0.6f, 0.3f, 0x828444, 0x26292B);
    public static RegistryObject<EntityType<EntityTortoise>> TORTOISE = createEntity(EntityTortoise::new, "tortoise", 0.6f, 0.6f, 0xAF9F74, 0x775232);
    public static RegistryObject<EntityType<EntityAnaconda>> ANACONDA = createEntity(EntityAnaconda::new, "large_snake", 1.5f, 0.6f, 0x65704C, 0x42291A);
    public static RegistryObject<EntityType<EntityMonitor>> MONITOR = createEntity(EntityMonitor::new, "monitor", 1.3f, 0.6f, 0x423C2C, 0x958C66);

    // Mollusks
    public static RegistryObject<EntityType<EntityGiantClam>> GIANT_CLAM = createEntity(EntityGiantClam::new, "giant_clam", 1.0F, 1.0F, 0x346B70, 0xAD713C);
    //public static RegistryObject<EntityType<EntityGiantClam>> GIANT_CLAM = createEntity(EntityGiantClam::new, MobCategory.WATER_CREATURE, "giant_clam", 32, 10, true, 1.0F, 1.0F, 0x346B70, 0xAD713C, animalType.SESSILE, 1);

    // Mammals
    public static RegistryObject<EntityType<EntityBear>> BEAR = createEntity(EntityBear::new, "bear", 1.3F, 1.3F, 0x20130B, 0x564C45);
    public static RegistryObject<EntityType<EntityBigCat>> BIG_CAT = createEntity(EntityBigCat::new, "big_cat", 1.2F, 1.0F, 0xC59F45,0x383121);
    public static RegistryObject<EntityType<EntityHippo>> HIPPO = createEntity(EntityHippo::new, "hippo", 1.8F, 1.8F, 0x463A31, 0x956761);
    public static RegistryObject<EntityType<EntityAardvark>> AARDVARK = createEntity(EntityAardvark::new, "aardvark", 0.9F, 0.9F, 0x463A31, 0x956761);
    public static RegistryObject<EntityType<EntityRhino>> RHINO = createEntity(EntityRhino::new, "rhino", 2.0F, 1.8F, 0x787676, 0x665956);
    public static RegistryObject<EntityType<EntityHyena>> HYENA = createEntity(EntityHyena::new, "hyena", 0.9F, 1.1F, 0x6C6857, 0x978966);
    public static RegistryObject<EntityType<EntityBoar>> BOAR = createEntity(EntityBoar::new, "boar", 1.2F, 1.2F, 0x503C2A, 0x605449);
    public static RegistryObject<EntityType<EntityBison>> BISON = createEntity(EntityBison::new, "bison", 1.7F, 1.6F, 0x845B2B, 0x49342A);
    public static RegistryObject<EntityType<EntityCamel>> CAMEL = createEntity(EntityCamel::new, "camel", 1.8F, 2F, 0xE0B989, 0x976B3D);
    public static RegistryObject<EntityType<EntityManatee>> MANATEE = createEntity(EntityManatee::new, "manatee", 1.8F, 2F, 0x4A4040, 0x787676);
    public static RegistryObject<EntityType<EntityBaleenWhale>> BALEEN_WHALE = createEntity(EntityBaleenWhale::new, "baleen_whale", 2.6F, 1.6F, 0x12141E, 0x5B6168);

    // Fish
    public static RegistryObject<EntityType<EntitySunfish>> SUNFISH = createEntity(EntitySunfish::new, "sunfish", 1.6F, 1.6F, 0x2C545B, 0xB6D0D3);
    public static RegistryObject<EntityType<EntityTrevally>> TREVALLY = createEntity(EntityTrevally::new, "trevally", 0.8F, 0.8F, 0xA5B4AF, 0xC89D17);
    public static RegistryObject<EntityType<EntityArowana>> AROWANA = createEntity(EntityArowana::new, "arowana", 0.6F, 0.6F, 0x645C45, 0xB29F52);
    public static RegistryObject<EntityType<EntityShark>> SHARK = createEntity(EntityShark::new, "shark", 1.8F, 1.3F, 0x6B5142, 0xB0B0A3);
    public static RegistryObject<EntityType<EntityFootballFish>> FOOTBALL_FISH = createEntity(EntityFootballFish::new, "football_fish", 0.8F, 0.8F, 0x53556C, 0x2F3037);
    public static RegistryObject<EntityType<EntityWhaleShark>> WHALE_SHARK = createEntity(EntityWhaleShark::new, "whale_shark", 2.6F, 1.6F, 0x222426, 0x7E7D84);
    public static RegistryObject<EntityType<EntityTriggerfish>> TRIGGERFISH = createEntity(EntityTriggerfish::new, "triggerfish", 0.8F, 0.8F, 0x1F0A19, 0xFCBD00);
    public static RegistryObject<EntityType<EntityCatfish>> CATFISH = createEntity(EntityCatfish::new, "catfish", 0.8F, 0.8F, 0x545963, 0x3A2C23);
    public static RegistryObject<EntityType<EntitySpadefish>> SPADEFISH = createEntity(EntitySpadefish::new, "spadefish", 0.8F, 0.8F, 0x545963, 0x3A2C23);

    // Amphibians
    public static RegistryObject<EntityType<EntityGiantSalamander>> GIANT_SALAMANDER = createEntity(EntityGiantSalamander::new, "giant_salamander", 1F, 0.6f, 0x3A2C23, 0x6B5142);
    public static RegistryObject<EntityType<EntityNewt>> NEWT = createEntity(EntityNewt::new, "newt", 0.6F, 0.3f, 0x232323, 0xFF8D00);

    // Relicts
    public static RegistryObject<EntityType<EntitySpitter>> SPITTER = createEntity(EntitySpitter::new, "spitter", 1.3F, 1.3f, 0x3A345E, 0xB364E0);

    // Projectiles
    public static RegistryObject<EntityType<ProjectileSpit>> SPIT = createProjectile(ProjectileSpit::new, "spit", 64, 1, true,0.6F, 0.3f);

    private static <T extends Projectile> RegistryObject<EntityType<T>> createProjectile(EntityType.EntityFactory<T> factory, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY) {
        RegistryObject<EntityType<T>> type = ENTITIES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC)
                .sized(sizeX, sizeY)
                .clientTrackingRange(trackingRange)
                .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                .build(name));

        return type;
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> createEntity(EntityType.EntityFactory<T> factory, String name, float sizeX, float sizeY, int baseColor, int overlayColor) {
        return createEntity(factory, MobCategory.CREATURE, name, 64, 1, true, sizeX, sizeY, baseColor, overlayColor);
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> createEntity(EntityType.EntityFactory<T> factory, MobCategory classification, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, float sizeX, float sizeY, int maincolor, int backcolor) {
        RegistryObject<EntityType<T>> type = ENTITIES.register(name, () -> EntityType.Builder.of(factory, classification)
                .sized(sizeX, sizeY)
                .clientTrackingRange(trackingRange)
                .setShouldReceiveVelocityUpdates(sendsVelocityUpdates)
                .build(name));

        ModItems.ITEMS.register(name + "_spawn_egg", () -> new UntamedSpawnEggItem(type, maincolor, backcolor, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        return type;
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent event) {
        event.put(TARANTULA.get(), EntityTarantula.registerAttributes().build());
        event.put(KING_CRAB.get(), EntityKingCrab.registerAttributes().build());

        event.put(SNAKE.get(), EntitySnake.registerAttributes().build());
        event.put(SOFTSHELL_TURTLE.get(), EntitySoftshellTurtle.registerAttributes().build());
        event.put(TORTOISE.get(), EntityTortoise.registerAttributes().build());
        event.put(ANACONDA.get(), EntityAnaconda.registerAttributes().build());
        event.put(MONITOR.get(), EntityMonitor.registerAttributes().build());

        event.put(GIANT_CLAM.get(), EntityGiantClam.registerAttributes().build());

        event.put(BEAR.get(), EntityBear.registerAttributes().build());
        event.put(BIG_CAT.get(), EntityBigCat.registerAttributes().build());
        event.put(HIPPO.get(), EntityHippo.registerAttributes().build());
        event.put(AARDVARK.get(), EntityAardvark.registerAttributes().build());
        event.put(RHINO.get(), EntityRhino.registerAttributes().build());
        event.put(HYENA.get(), EntityHyena.registerAttributes().build());
        event.put(BOAR.get(), EntityBoar.registerAttributes().build());
        event.put(BISON.get(), EntityBison.registerAttributes().build());
        event.put(CAMEL.get(), EntityCamel.registerAttributes().build());
        event.put(MANATEE.get(), EntityManatee.registerAttributes().build());
        event.put(BALEEN_WHALE.get(), EntityBaleenWhale.registerAttributes().build());

        event.put(SUNFISH.get(), EntitySunfish.registerAttributes().build());
        event.put(TREVALLY.get(), EntityTrevally.registerAttributes().build());
        event.put(AROWANA.get(), EntityArowana.registerAttributes().build());
        event.put(SHARK.get(), EntityShark.registerAttributes().build());
        event.put(FOOTBALL_FISH.get(), EntityFootballFish.registerAttributes().build());
        event.put(WHALE_SHARK.get(), EntityWhaleShark.registerAttributes().build());
        event.put(TRIGGERFISH.get(), EntityTriggerfish.registerAttributes().build());
        event.put(CATFISH.get(), EntityCatfish.registerAttributes().build());
        event.put(SPADEFISH.get(), EntitySpadefish.registerAttributes().build());

        event.put(GIANT_SALAMANDER.get(), EntityGiantSalamander.registerAttributes().build());
        event.put(NEWT.get(), EntityNewt.registerAttributes().build());

        event.put(SPITTER.get(), EntitySpitter.registerAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntity.TARANTULA.get(), RendererTarantula::new);
        event.registerEntityRenderer(ModEntity.KING_CRAB.get(), RendererKingCrab::new);

        event.registerEntityRenderer(ModEntity.SOFTSHELL_TURTLE.get(), RendererSoftshellTurtle::new);
        event.registerEntityRenderer(ModEntity.SNAKE.get(), RendererSnake::new);
        event.registerEntityRenderer(ModEntity.TORTOISE.get(), RendererTortoise::new);
        event.registerEntityRenderer(ModEntity.ANACONDA.get(), RendererAnaconda::new);
        event.registerEntityRenderer(ModEntity.MONITOR.get(), RendererMonitor::new);

        event.registerEntityRenderer(ModEntity.GIANT_CLAM.get(), RendererGiantClam::new);

        event.registerEntityRenderer(ModEntity.BEAR.get(), RendererBear::new);
        event.registerEntityRenderer(ModEntity.BIG_CAT.get(), RendererBigCat::new);
        event.registerEntityRenderer(ModEntity.HIPPO.get(), RendererHippo::new);
        event.registerEntityRenderer(ModEntity.AARDVARK.get(), RendererAardvark::new);
        event.registerEntityRenderer(ModEntity.RHINO.get(), RendererRhino::new);
        event.registerEntityRenderer(ModEntity.HYENA.get(), RendererHyena::new);
        event.registerEntityRenderer(ModEntity.BOAR.get(), RendererBoar::new);
        event.registerEntityRenderer(ModEntity.BISON.get(), RendererBison::new);
        event.registerEntityRenderer(ModEntity.CAMEL.get(), RendererCamel::new);
        event.registerEntityRenderer(ModEntity.MANATEE.get(), RendererManatee::new);
        event.registerEntityRenderer(ModEntity.BALEEN_WHALE.get(), RendererBaleenWhale::new);

        event.registerEntityRenderer(ModEntity.SUNFISH.get(), RendererSunfish::new);
        event.registerEntityRenderer(ModEntity.TREVALLY.get(), RendererTrevally::new);
        event.registerEntityRenderer(ModEntity.AROWANA.get(), RendererArowana::new);
        event.registerEntityRenderer(ModEntity.SHARK.get(), RendererShark::new);
        event.registerEntityRenderer(ModEntity.FOOTBALL_FISH.get(), RendererFootballFish::new);
        event.registerEntityRenderer(ModEntity.WHALE_SHARK.get(), RendererWhaleShark::new);
        event.registerEntityRenderer(ModEntity.TRIGGERFISH.get(), RendererTriggerfish::new);
        event.registerEntityRenderer(ModEntity.CATFISH.get(), RendererCatfish::new);
        event.registerEntityRenderer(ModEntity.SPADEFISH.get(), RendererSpadefish::new);

        event.registerEntityRenderer(ModEntity.GIANT_SALAMANDER.get(), RendererGiantSalamander::new);
        event.registerEntityRenderer(ModEntity.NEWT.get(), RendererNewt::new);

        event.registerEntityRenderer(ModEntity.SPITTER.get(), RendererSpitter::new);

        event.registerEntityRenderer(ModEntity.SPIT.get(), RendererProjectileSpit::new);
    }

    public static void addWorldSpawn(EntityType<?> entityClass, int weightedProb, FaunaHandler.animalType type, int groupCount) {
        List<FaunaHandler.SpawnListEntry> spawns = FaunaHandler.getSpawnableList(type);
        boolean found = false;
        for (FaunaHandler.SpawnListEntry entry : spawns) {
            // Adjusting an existing spawn entry
            if (entry.entityType == entityClass) {
                entry.itemWeight = weightedProb;
                //entry.groupCount = groupCount;
                found = true;
                break;
            }
        }

        //if (!found)
            //spawns.add(new FaunaHandler.SpawnListEntry(entityClass, weightedProb, groupCount));
    }
}