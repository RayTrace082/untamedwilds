package untamedwilds.init;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import untamedwilds.UntamedWilds;

public class ModTags {
    public static class EntityTags {
        // The CAGE_BLACKLIST is provided for mod packs and/or data pack makers, nothing is there by default (players are hard-blacklisted since they will crash the game)
        public static final ITag.INamedTag<EntityType<?>> CAGE_BLACKLIST = createTag("cage_trap_blacklist");

        private static ITag.INamedTag<EntityType<?>> createTag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation(UntamedWilds.MOD_ID + name));
        }
    }

    public static class UTBlockTags {
        public static final ITag.INamedTag<Block> REEDS_PLANTABLE_ON = BlockTags.makeWrapperTag(UntamedWilds.MOD_ID + ":reeds_plantable_on");
    }
}
