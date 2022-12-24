package untamedwilds.init;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import untamedwilds.UntamedWilds;

public class ModAdvancementTriggers {
    public static UntamedTriggers<?> NO_PATCHOULI_LOADED = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "guidebook_alt"));
    public static UntamedTriggers<?> BAIT_BASIC = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "used_bait"));
    public static UntamedTriggers<?> MASTER_BAIT = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "master_bait"));

    // TODO: Abstract the "Discovered" Trigger to activate for any mob
    public static UntamedTriggers<?> DISCOVERED_SPITTER = new UntamedTriggers<>(new ResourceLocation(UntamedWilds.MOD_ID, "discovered_spitter"));

    public static void register() {
        CriteriaTriggers.register(NO_PATCHOULI_LOADED);
        CriteriaTriggers.register(BAIT_BASIC);
        CriteriaTriggers.register(MASTER_BAIT);

        CriteriaTriggers.register(DISCOVERED_SPITTER);
    }

    public static class UntamedTriggers<T extends CriterionTriggerInstance> extends SimpleCriterionTrigger<UntamedTriggers.Instance> {
        private final ResourceLocation id;

        public UntamedTriggers(ResourceLocation resourceLocation) {
            this.id = resourceLocation;
        }

        public Instance createInstance(JsonObject objectIn, EntityPredicate.Composite predicateIn, DeserializationContext p_230241_3_) {
            return new UntamedTriggers.Instance(predicateIn, id);
        }

        public void trigger(ServerPlayer entityIn) {
            this.trigger(entityIn, (input) -> true);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        public static class Instance extends AbstractCriterionTriggerInstance  {

            public Instance(EntityPredicate.Composite p_i231507_1_, ResourceLocation res) {
                super(res, p_i231507_1_);
            }

            public JsonObject serializeToJson(SerializationContext p_230240_1_) {
                return super.serializeToJson(p_230240_1_);
            }
        }
    }
}