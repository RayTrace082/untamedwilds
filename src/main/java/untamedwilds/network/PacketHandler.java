package untamedwilds.network;

import net.minecraftforge.fml.network.PacketDistributor;
import untamedwilds.UntamedWilds;

public class PacketHandler {

    // Currently unused
    public static void register() {
        int id = 0;
        //UntamedWilds.INSTANCE.registerMessage(id++, PlayAnimation.class, PlayAnimation::encode, PlayAnimation::decode, PlayAnimation::handle);
    }

    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) {
        UntamedWilds.INSTANCE.send(target, message);
    }
}
