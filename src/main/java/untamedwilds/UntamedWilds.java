package untamedwilds;

import net.minecraft.world.level.block.DispenserBlock;
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
import untamedwilds.init.*;
import untamedwilds.network.UntamedInstance;
import untamedwilds.world.UntamedWildsGenerator;

@Mod(value = UntamedWilds.MOD_ID)
public class UntamedWilds {

    // TODO: Abstract Herd logic to be functional with any LivingEntity (instead of being limited to IPackEntity ComplexMob)
    // TODO: Store the children's UUID in their mother's NBT, to allow checking for Children without constant AABB checking
    // TODO: Have carnivorous mobs gain hunger when attacking

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
        ModEntity.ENTITIES.register(eventBus);
        ModItems.registerSpawnItems();
        ModSounds.SOUNDS.register(eventBus);
        ModAdvancementTriggers.register();
        UntamedWildsGenerator.FEATURES.register(eventBus);
        CompatBridge.RegisterCompat();
        UntamedWildsGenerator.readBioDiversityLevels();
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
        UntamedInstance.registerMessages();
        DispenserBlock.registerBehavior(ModBlock.TRAP_CAGE.get().asItem(), new CageBlock.DispenserBehaviorTrapCage());
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ModBlock.registerRendering();
        ModBlock.registerBlockColors();

        //ModBlock.registerBlockColors();
        //ModParticles.registerParticles(); Handled through events
    }
}
