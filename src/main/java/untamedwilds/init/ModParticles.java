package untamedwilds.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import untamedwilds.UntamedWilds;
import untamedwilds.client.particle.ChumParticle;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, value = Dist.CLIENT)
public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, UntamedWilds.MOD_ID);

    public static final RegistryObject<SimpleParticleType> CHUM_DISPERSE = PARTICLES.register("chum", () -> new SimpleParticleType(false));

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegisterParticleFactories {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
            ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
            particleManager.register(CHUM_DISPERSE.get(), ChumParticle.Provider::new);
        }
    }
}