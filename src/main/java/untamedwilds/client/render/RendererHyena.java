package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelHyena;
import untamedwilds.client.model.ModelRhinoCalf;
import untamedwilds.entity.mammal.EntityHyena;

import javax.annotation.Nonnull;

public class RendererHyena extends MobRenderer<EntityHyena, EntityModel<EntityHyena>> {

    private static final ModelHyena HYENA_MODEL = new ModelHyena();
    private static final ModelRhinoCalf RHINO_MODEL_CALF = new ModelRhinoCalf();

    public RendererHyena(EntityRendererManager renderManager) {
        super(renderManager, HYENA_MODEL, 1F);
    }

    /*@Override
    public void render(EntityHyena entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = HYENA_MODEL;
        } else {
            entityModel = HYENA_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }*/

    protected void preRenderCallback(EntityHyena entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.25f);
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.6f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityHyena entity) {
        return entity.getTexture();
    }
}
