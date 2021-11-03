package untamedwilds;

import net.minecraft.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import untamedwilds.block.CageBlock;
import untamedwilds.compat.CompatBridge;
import untamedwilds.config.ConfigBase;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModVillagerTrades;
import untamedwilds.network.UntamedInstance;
import untamedwilds.util.ModEntityRightClickEvent;
import untamedwilds.world.UntamedWildsGenerator;

@Mod(value = UntamedWilds.MOD_ID)
public class UntamedWilds {

    // TODO: Abstract Herd logic to be functional with any LivingEntity (instead of being limited to IPackEntity ComplexMob)
    // TODO: Store the children's UUID in their mother's NBT, to allow checking for Children without constant AABB checking
    // TODO: Have carnivorous mobs gain hunger when attacking
    // TODO: Make "ProtectChildren" only apply to mobs of similar ecoLevel, make HurtByTargetGoal call in adult mobs in Range if the target is children
    // TODO: Rework ecoLevels to be dynamically calculated based on HP and damage sqrt(HP * Attack) / 2.5

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "untamedwilds";
    public static final boolean DEBUG = false;

    public UntamedWilds() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigBase.common_config);
        ConfigBase.loadConfig(ConfigBase.common_config, FMLPaths.CONFIGDIR.get().resolve("untamedwilds-common.toml").toString());
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);
        ModBlock.BLOCKS.register(eventBus);
        ModBlock.TILE_ENTITIES.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModItems.registerSpawnItems();
        UntamedWildsGenerator.FEATURES.register(eventBus);
        CompatBridge.RegisterCompat();
        MinecraftForge.EVENT_BUS.register(ModVillagerTrades.class); // Custom Villager Trades
        MinecraftForge.EVENT_BUS.register(UntamedWildsGenerator.class); // Custom Biome Features
        MinecraftForge.EVENT_BUS.register(ModEntityRightClickEvent.class); // TODO: WIP solution because Wolves are really stupid, and there seems to be no way to 'this.' an entity through mixin
        UntamedWildsGenerator.readBioDiversityLevels();
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
        UntamedInstance.registerMessages();
        DispenserBlock.registerDispenseBehavior(ModBlock.TRAP_CAGE.get().asItem(), new CageBlock.DispenserBehaviorTrapCage());
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ModEntity.registerRendering();
        ModBlock.registerRendering();
        //ModParticles.registerParticles(); Handled through events
    }
}
