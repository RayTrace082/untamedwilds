package untamedwilds.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = UntamedWilds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UntamedWilds.MOD_ID);

    public static final SoundEvent ENTITY_ATTACK_BITE = registerSound("entity.generic.bite");

    public static final SoundEvent ENTITY_HIPPO_AMBIENT = registerSound("entity.hippo.ambient");

    public static final SoundEvent ENTITY_BISON_AMBIENT = registerSound("entity.bison.ambient");

    public static final SoundEvent ENTITY_BEAR_AMBIENT = registerSound("entity.bear.ambient");
    public static final SoundEvent ENTITY_BEAR_WARNING = registerSound("entity.bear.warning");
    public static final SoundEvent ENTITY_BEAR_HURT = registerSound("entity.bear.hurt");
    public static final SoundEvent ENTITY_BEAR_DEATH = registerSound("entity.bear.death");
    public static final SoundEvent ENTITY_BEAR_BABY_AMBIENT = registerSound("entity.bear.baby.ambient");
    public static final SoundEvent ENTITY_BEAR_BABY_CRY = registerSound("entity.bear.baby.cry");

    public static final SoundEvent ENTITY_BIG_CAT_AMBIENT = registerSound("entity.big_cat.ambient");
    public static final SoundEvent ENTITY_BIG_CAT_HURT = registerSound("entity.big_cat.hurt");
    public static final SoundEvent ENTITY_BIG_CAT_DEATH = registerSound("entity.big_cat.death");

    public static final SoundEvent ENTITY_AARDVARK_AMBIENT = registerSound("entity.aardvark.ambient");
    public static final SoundEvent ENTITY_AARDVARK_HURT = registerSound("entity.aardvark.hurt");
    public static final SoundEvent ENTITY_AARDVARK_DEATH = registerSound("entity.aardvark.death");

    public static final SoundEvent ENTITY_HYENA_LAUGHING = registerSound("entity.hyena.laugh");
    public static final SoundEvent ENTITY_HYENA_GROWL = registerSound("entity.hyena.growl");
    public static final SoundEvent ENTITY_HYENA_AMBIENT = registerSound("entity.hyena.ambient");
    public static final SoundEvent ENTITY_HYENA_HURT = registerSound("entity.hyena.hurt");
    public static final SoundEvent ENTITY_HYENA_DEATH = registerSound("entity.hyena.death");

    public static final SoundEvent ENTITY_BOAR_SQUEAL = registerSound("entity.boar.squeal");
    public static final SoundEvent ENTITY_BOAR_AMBIENT = registerSound("entity.boar.ambient");
    public static final SoundEvent ENTITY_BOAR_HURT = registerSound("entity.boar.hurt");
    public static final SoundEvent ENTITY_BOAR_DEATH = registerSound("entity.boar.death");

    public static final SoundEvent ENTITY_CAMEL_SPIT = registerSound("entity.camel.spit");
    public static final SoundEvent ENTITY_CAMEL_AMBIENT = registerSound("entity.camel.ambient");
    public static final SoundEvent ENTITY_CAMEL_HURT = registerSound("entity.camel.hurt");
    public static final SoundEvent ENTITY_CAMEL_DEATH = registerSound("entity.camel.death");

    public static final SoundEvent ENTITY_SPITTER_SPIT = registerSound("entity.spitter.spit");
    public static final SoundEvent ENTITY_SPITTER_AMBIENT = registerSound("entity.spitter.ambient");
    public static final SoundEvent ENTITY_SPITTER_HURT = registerSound("entity.spitter.hurt");
    public static final SoundEvent ENTITY_SPITTER_DEATH = registerSound("entity.spitter.death");

    public static final SoundEvent ENTITY_OPOSSUM_HISS = registerSound("entity.opossum.hiss");
    public static final SoundEvent ENTITY_OPOSSUM_AMBIENT = registerSound("entity.opossum.ambient");
    public static final SoundEvent ENTITY_OPOSSUM_HURT = registerSound("entity.opossum.hurt");
    public static final SoundEvent ENTITY_OPOSSUM_DEATH = registerSound("entity.opossum.death");

    public static final SoundEvent ENTITY_TARANTULA_AMBIENT = registerSound("entity.tarantula.ambient");

    public static final SoundEvent ENTITY_SNAKE_HISS = registerSound("entity.snake.warning");
    public static final SoundEvent ENTITY_SNAKE_RATTLE = registerSound("entity.snake.rattle");

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(UntamedWilds.MOD_ID, soundName);
        SoundEvent event = new SoundEvent(location);
        ModSounds.SOUNDS.register(soundName, () -> event);
        return event;
    }
}
