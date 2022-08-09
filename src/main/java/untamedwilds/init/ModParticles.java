package untamedwilds.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import untamedwilds.UntamedWilds;
import untamedwilds.client.particle.ChumParticle;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {

    public static final SimpleParticleType CHUM_DISPERSE = new SimpleParticleType(true);

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        manager.register(CHUM_DISPERSE, ChumParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> evt) {
        CHUM_DISPERSE.setRegistryName(UntamedWilds.MOD_ID, "chum");
        evt.getRegistry().registerAll(CHUM_DISPERSE);
    }
}