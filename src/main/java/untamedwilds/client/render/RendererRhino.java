package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelRhino;
import untamedwilds.client.model.ModelRhinoCalf;
import untamedwilds.entity.mammal.EntityRhino;

import javax.annotation.Nonnull;

public class RendererRhino extends MobRenderer<EntityRhino, EntityModel<EntityRhino>> {

    private static final ModelRhino RHINO_MODEL = new ModelRhino();
    private static final ModelRhinoCalf RHINO_MODEL_CALF = new ModelRhinoCalf();

    public RendererRhino(EntityRendererManager renderManager) {
        super(renderManager, RHINO_MODEL, 1F);
    }

    @Override
    public void render(EntityRhino entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isChild()) {
            entityModel = RHINO_MODEL_CALF;
        } else {
            entityModel = RHINO_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(EntityRhino entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityRhino entity) {
        return entity.getTexture();
    }
}
