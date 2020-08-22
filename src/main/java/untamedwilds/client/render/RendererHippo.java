package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelHippo;
import untamedwilds.client.model.ModelHippoCalf;
import untamedwilds.entity.mammal.EntityHippo;

public class RendererHippo extends MobRenderer<EntityHippo, EntityModel<EntityHippo>> {

    private static final ModelHippo HIPPO_MODEL = new ModelHippo();
    private static final ModelHippoCalf HIPPO_MODEL_CALF = new ModelHippoCalf();

    public RendererHippo(EntityRendererManager renderManager, ModelHippo model, float shadowSize) {
        super(renderManager, model, shadowSize);
    }

    @Override
    public void render(EntityHippo entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = HIPPO_MODEL_CALF;
        } else {
            entityModel = HIPPO_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(EntityHippo entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.25f);
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.8f;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityHippo entity) {
        return new ResourceLocation("untamedwilds:textures/entity/hippo/common.png");
    }
}
