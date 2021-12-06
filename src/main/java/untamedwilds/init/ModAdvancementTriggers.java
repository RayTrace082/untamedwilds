package untamedwilds.init;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import untamedwilds.UntamedWilds;

public class ModAdvancementTriggers {
    public static UntamedTriggers<?> BAIT_BASIC = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "used_bait"));
    public static UntamedTriggers<?> MASTER_BAIT = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "master_bait"));

    public static void register() {
        CriteriaTriggers.register(BAIT_BASIC);
        CriteriaTriggers.register(MASTER_BAIT);
    }

    public static class UntamedTriggers<T extends CriterionInstance> extends AbstractCriterionTrigger<UntamedTriggers.Instance> {
        private final ResourceLocation id;

        public UntamedTriggers(ResourceLocation resourceLocation) {
            this.id = resourceLocation;
        }

        public Instance deserializeTrigger(JsonObject objectIn, EntityPredicate.AndPredicate predicateIn, ConditionArrayParser p_230241_3_) {
            return new UntamedTriggers.Instance(predicateIn, id);
        }

        public void trigger(ServerPlayerEntity entityIn) {
            this.triggerListeners(entityIn, (input) -> true);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        public static class Instance extends CriterionInstance {

            public Instance(EntityPredicate.AndPredicate p_i231507_1_, ResourceLocation res) {
                super(res, p_i231507_1_);
            }

            public JsonObject serialize(ConditionArraySerializer p_230240_1_) {
                return super.serialize(p_230240_1_);
            }
        }
    }
}