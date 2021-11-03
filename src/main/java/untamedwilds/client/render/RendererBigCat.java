package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelBigCat;
import untamedwilds.client.model.ModelBigCatCub;
import untamedwilds.entity.mammal.EntityBigCat;

public class RendererBigCat extends MobRenderer<EntityBigCat, EntityModel<EntityBigCat>> {

    private static final ModelBigCat BIG_CAT_MODEL = new ModelBigCat();
    private static final ModelBigCatCub BIG_CAT_MODEL_CUB = new ModelBigCatCub();

    public RendererBigCat(EntityRendererManager renderManager) {
        super(renderManager, BIG_CAT_MODEL, 1F);
    }

    @Override
    public void render(EntityBigCat entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = BIG_CAT_MODEL_CUB;
        } else {
            entityModel = BIG_CAT_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(EntityBigCat entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBigCat entity) {
        return entity.getTexture();
    }
}
