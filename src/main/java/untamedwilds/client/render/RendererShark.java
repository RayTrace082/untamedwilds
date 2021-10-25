package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelShark;
import untamedwilds.entity.fish.EntityShark;

import javax.annotation.Nonnull;

public class RendererShark extends MobRenderer<EntityShark, EntityModel<EntityShark>> {

    private static final ModelShark SHARK_MODEL = new ModelShark();

    public RendererShark(EntityRendererManager rendermanager) {
        super(rendermanager, SHARK_MODEL, 0.2F);
    }
    @Override
    protected void preRenderCallback(EntityShark entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= entity.getModelScale(entity.getVariant());
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityShark entity) {
        return entity.getTexture();
    }
}
