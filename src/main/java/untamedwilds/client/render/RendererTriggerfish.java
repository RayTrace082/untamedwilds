package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelTriggerfish;
import untamedwilds.entity.fish.EntityTriggerfish;

import javax.annotation.Nonnull;

public class RendererTriggerfish extends MobRenderer<EntityTriggerfish, EntityModel<EntityTriggerfish>> {

    private static final ModelTriggerfish TRIGGERFISH_MODEL = new ModelTriggerfish();

    public RendererTriggerfish(EntityRendererManager rendermanager) {
        super(rendermanager, TRIGGERFISH_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityTriggerfish entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f * 1.1F);
        this.shadowSize = f * 0.5F;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTriggerfish entity) {
        return entity.getTexture();
    }
}
