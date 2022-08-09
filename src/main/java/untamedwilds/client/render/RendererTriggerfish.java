package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelTriggerfish;
import untamedwilds.entity.fish.EntityTriggerfish;

import javax.annotation.Nonnull;

public class RendererTriggerfish extends MobRenderer<EntityTriggerfish, EntityModel<EntityTriggerfish>> {

    private static final ModelTriggerfish TRIGGERFISH_MODEL = new ModelTriggerfish();

    public RendererTriggerfish(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, TRIGGERFISH_MODEL, 0.2F);
    }

    @Override
    protected void scale(EntityTriggerfish entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f * 1.1F);
        this.shadowRadius = f * 0.5F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityTriggerfish entity) {
        return entity.getTexture();
    }
}
