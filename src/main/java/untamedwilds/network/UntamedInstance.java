package untamedwilds.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import untamedwilds.UntamedWilds;

public class UntamedInstance {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(UntamedWilds.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int ID = 0;

    public static void registerMessages() {
        UntamedWilds.LOGGER.info("Registering Packets!");
        INSTANCE.messageBuilder(SyncTextureData.class, ID++)
                .encoder(SyncTextureData::toBytes)
                .decoder(SyncTextureData::new)
                .consumer(SyncTextureData::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
