package untamedwilds.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import untamedwilds.UntamedWilds;
import untamedwilds.entity.ComplexMobTerrestrial;
import untamedwilds.world.FaunaHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SpawnDataHolder {

    // Negative numbers are used to signify when to use the EntityDataHolder value instead
    public static final Codec<SpawnDataHolder> CODEC = RecordCodecBuilder.create((p_237051_0_) -> p_237051_0_.group(
            Codec.STRING.fieldOf("name").orElse("").forGetter((p_237056_0_) -> p_237056_0_.name),
            FaunaHandler.SpawnListEntry.CODEC.listOf().fieldOf("entries").orElse(Collections.emptyList()).forGetter((p_237054_0_) -> p_237054_0_.entries))
            .apply(p_237051_0_, SpawnDataHolder::new));

    private final String name;
    private final List<FaunaHandler.SpawnListEntry> entries;

    public SpawnDataHolder(String p_i232114_1_, List<FaunaHandler.SpawnListEntry> entriesIn) {
        this.name = p_i232114_1_;
        this.entries = entriesIn;
        if (FaunaHandler.getSpawnableList(p_i232114_1_) != null) {
            FaunaHandler.getSpawnableList(p_i232114_1_).addAll(entriesIn);
        }
    }

    public List<FaunaHandler.SpawnListEntry> getEntries () {
        return this.entries;
    }
}