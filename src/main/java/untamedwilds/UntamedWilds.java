package untamedwilds;

import net.minecraft.block.DispenserBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import untamedwilds.block.BlockCage;
import untamedwilds.compat.CompatBridge;
import untamedwilds.config.ConfigBase;
import untamedwilds.init.ModBlock;
import untamedwilds.init.ModEntity;
import untamedwilds.init.ModItems;
import untamedwilds.init.ModVillagerTrades;
import untamedwilds.world.UntamedWildsGenerator;

@Mod(value = UntamedWilds.MOD_ID)
public class UntamedWilds {

    // TODO: Migrate entities to DeferredRegistries if Spawn Eggs ever get fixed
    // TODO: Consider Brains as a replacement for Goals when necessary (Bears may benefit from removing half their Goals when adult/tamed)
    // TODO: Skins! Reuse the Species system in complex mobs to introduce random skins
    // TODO: Migration AI, rare events when a hungry mob will choose a direction and keep moving there

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "untamedwilds";
    public static final boolean DEBUG = true;

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public UntamedWilds() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigBase.server_config);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigBase.client_config);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        ConfigBase.loadConfig(ConfigBase.server_config, FMLPaths.CONFIGDIR.get().resolve("untamedwilds-server.toml").toString());
        ConfigBase.loadConfig(ConfigBase.client_config, FMLPaths.CONFIGDIR.get().resolve("untamedwilds-client.toml").toString());
        ModBlock.BLOCKS.register(eventBus);
        ModBlock.TILE_ENTITY_TYPES.register(eventBus);
        ModItems.registerSpawnItems(ModItems.ITEMS);
        ModItems.ITEMS.register(eventBus);
        CompatBridge.RegisterCompat();
        MinecraftForge.EVENT_BUS.register(ModVillagerTrades.class); // Custom Villager Trades
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
        DispenserBlock.registerDispenseBehavior(ModBlock.TRAP_CAGE.get().asItem(), new BlockCage.DispenserBehaviorTrapCage());
        UntamedWildsGenerator.setUp();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ModEntity.registerRendering();
        ModBlock.registerRendering();
        LOGGER.info("Client registry method registered.");
    }
}
