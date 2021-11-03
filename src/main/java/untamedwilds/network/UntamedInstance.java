package untamedwilds.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
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
        INSTANCE.messageBuilder(SpawnPacket.class, ID++)
                .encoder(SpawnPacket::toBytes)
                .decoder(SpawnPacket::new)
                .consumer(SpawnPacket::handle)
                .add();
        INSTANCE.messageBuilder(SyncEntityData.class, ID++)
                .encoder(SyncEntityData::toBytes)
                .decoder(SyncEntityData::new)
                .consumer(SyncEntityData::handle)
                .add();
        INSTANCE.messageBuilder(SyncTextureData.class, ID++)
                .encoder(SyncTextureData::toBytes)
                .decoder(SyncTextureData::new)
                .consumer(SyncTextureData::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
