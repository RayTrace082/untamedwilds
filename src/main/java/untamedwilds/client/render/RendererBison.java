package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelBison;
import untamedwilds.client.model.ModelRhinoCalf;
import untamedwilds.entity.mammal.EntityBison;

import javax.annotation.Nonnull;

public class RendererBison extends MobRenderer<EntityBison, EntityModel<EntityBison>> {

    private static final ModelBison BISON_MODEL = new ModelBison();
    private static final ModelRhinoCalf BISON_CALF_MODEL = new ModelRhinoCalf();

    public RendererBison(EntityRendererManager renderManager) {
        super(renderManager, BISON_MODEL, 1F);
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

    protected void preRenderCallback(EntityBison entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f * 0.6F;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityBison entity) {
        return entity.getTexture();
    }
}
