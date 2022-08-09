package untamedwilds.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import untamedwilds.UntamedWilds;

public class ModTags {
    public static class EntityTags {
        // The CAGE_BLACKLIST is provided for mod packs and/or data pack makers, nothing is there by default (players are hard-blacklisted since they will crash the game)
        public static final TagKey<EntityType<?>> CAGE_BLACKLIST = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(UntamedWilds.MOD_ID, "cage_trap_blacklist"));
    }

    public static class ModBlockTags {
        public static final TagKey<Block> REEDS_PLANTABLE_ON = BlockTags.create(new ResourceLocation(UntamedWilds.MOD_ID, "reeds_plantable_on"));
        public static final TagKey<Block> ALOE_PLANTABLE_ON = BlockTags.create(new ResourceLocation(UntamedWilds.MOD_ID, "aloe_plantable_on"));
        public static final TagKey<Block> GRAZEABLE_BLOCKS = BlockTags.create(new ResourceLocation(UntamedWilds.MOD_ID, "grazeable_blocks"));
        public static final TagKey<Block> GRAZEABLE_ALGAE = BlockTags.create(new ResourceLocation(UntamedWilds.MOD_ID, "grazeable_algae"));
    }
}
