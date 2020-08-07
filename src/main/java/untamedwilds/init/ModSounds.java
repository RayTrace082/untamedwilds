package untamedwilds.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;

@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static final SoundEvent ENTITY_ATTACK_BITE = registerSound("entity.generic.bite");

    public static final SoundEvent ENTITY_BEAR_AMBIENT = registerSound("entity.bear.ambient");
    public static final SoundEvent ENTITY_BEAR_WARNING = registerSound("entity.bear.warning");
    public static final SoundEvent ENTITY_BEAR_HURT = registerSound("entity.bear.hurt");
    public static final SoundEvent ENTITY_BEAR_DEATH = registerSound("entity.bear.death");
    public static final SoundEvent ENTITY_BEAR_BABY_AMBIENT = registerSound("entity.bear.baby.ambient");
    public static final SoundEvent ENTITY_BEAR_BABY_CRY = registerSound("entity.bear.baby.cry");

    public static final SoundEvent ENTITY_BIG_CAT_AMBIENT = registerSound("entity.big_cat.ambient");
    public static final SoundEvent ENTITY_BIG_CAT_HURT = registerSound("entity.big_cat.hurt");
    public static final SoundEvent ENTITY_BIG_CAT_DEATH = registerSound("entity.big_cat.death");

    public static final SoundEvent ENTITY_TARANTULA_AMBIENT = registerSound("entity.tarantula.ambient");

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, soundName);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
