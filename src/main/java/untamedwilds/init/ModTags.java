package untamedwilds.init;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import untamedwilds.UntamedWilds;

public class ModTags {
    public static class EntityTags {
        // The CAGE_BLACKLIST is provided for mod packs and/or data pack makers, nothing is there by default (players are hard-blacklisted since they will crash the game)
        public static final Tag<EntityType<?>> CAGE_BLACKLIST = createTag("cage_trap_blacklist");

        public static Tag<EntityType<?>> createTag(String name) {
            return new EntityTypeTags.Wrapper(new ResourceLocation(UntamedWilds.MOD_ID, name));
        }
    }
}
