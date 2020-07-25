package untamedwilds.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.placement.IPlacementConfig;

public class WorldGenEntityConfig implements IPlacementConfig {
    public final double probability;

    public WorldGenEntityConfig(double prob) {
        this.probability = prob;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> p_214634_1_) {
        return new Dynamic(p_214634_1_, p_214634_1_.createMap(ImmutableMap.of(p_214634_1_.createString("probability"), p_214634_1_.createDouble(this.probability))));
    }

    public static <T> WorldGenEntityConfig deserialize(Dynamic<T> p_214642_0_) {
        float prob = p_214642_0_.get("probability").asFloat(0.0F);
        return new WorldGenEntityConfig(prob);
    }
}
