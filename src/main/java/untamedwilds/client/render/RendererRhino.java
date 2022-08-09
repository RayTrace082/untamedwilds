package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelRhino;
import untamedwilds.client.model.ModelRhinoCalf;
import untamedwilds.entity.mammal.EntityRhino;

import javax.annotation.Nonnull;

public class RendererRhino extends MobRenderer<EntityRhino, EntityModel<EntityRhino>> {

    private static final ModelRhino RHINO_MODEL = new ModelRhino();
    private static final ModelRhinoCalf RHINO_MODEL_CALF = new ModelRhinoCalf();

    public RendererRhino(EntityRendererProvider.Context renderManager) {
        super(renderManager, RHINO_MODEL, 1F);
    }

    @Override
    public void render(EntityRhino entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.isBaby()) {
            model = RHINO_MODEL_CALF;
        } else {
            model = RHINO_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityRhino entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityRhino entity) {
        return entity.getTexture();
    }
}
