package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.client.model.ModelBear;
import untamedwilds.client.model.ModelBearCub;
import untamedwilds.entity.mammal.bear.AbstractBear;

@OnlyIn(Dist.CLIENT)
public class RendererBear extends MobRenderer<AbstractBear, EntityModel<AbstractBear>> {

    private static final ModelBear BEAR_MODEL = new ModelBear();
    private static final ModelBearCub BEAR_MODEL_CUB = new ModelBearCub();

    public RendererBear(EntityRendererManager renderManager) {
        super(renderManager, BEAR_MODEL, 1F);
    }

    @Override
    public void render(AbstractBear entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = BEAR_MODEL_CUB;
        } else {
            entityModel = BEAR_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(AbstractBear entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.25f);
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.8f;
    }

    public ResourceLocation getEntityTexture(AbstractBear entity) {
        return entity.getTexture();
    }
}
