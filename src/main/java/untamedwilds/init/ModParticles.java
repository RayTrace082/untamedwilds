package untamedwilds.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import untamedwilds.UntamedWilds;
import untamedwilds.client.particle.ChumParticle;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {

    public static final SimpleParticleType CHUM_DISPERSE = new SimpleParticleType(true);

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        manager.register(CHUM_DISPERSE, ChumParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerParticleTypes(RegisterEvent evt) {
        evt.register(ForgeRegistries.Keys.PARTICLE_TYPES, (helper) -> {
            evt.getForgeRegistry().register(UntamedWilds.MOD_ID + ":chum", CHUM_DISPERSE);
        });
    }
}