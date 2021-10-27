package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelBear;
import untamedwilds.client.model.ModelBearCub;
import untamedwilds.entity.mammal.EntityBear;

public class RendererBear extends MobRenderer<EntityBear, EntityModel<EntityBear>> {

    private static final ModelBear BEAR_MODEL = new ModelBear();
    private static final ModelBearCub BEAR_MODEL_CUB = new ModelBearCub();

    public RendererBear(EntityRendererManager renderManager) {
        super(renderManager, BEAR_MODEL, 1F);
    }

    @Override
    public void render(EntityBear entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = BEAR_MODEL_CUB;
        } else {
            entityModel = BEAR_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(EntityBear entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= entity.getModelScale(entity.getVariant());
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(EntityBear entity) {
        return entity.getTexture();
    }
}
