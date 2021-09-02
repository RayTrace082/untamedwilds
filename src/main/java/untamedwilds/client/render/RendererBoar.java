package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelBoar;
import untamedwilds.entity.mammal.EntityBoar;

import javax.annotation.Nonnull;

public class RendererBoar extends MobRenderer<EntityBoar, EntityModel<EntityBoar>> {

    private static final ModelBoar BOAR_MODEL = new ModelBoar();

    public RendererBoar(EntityRendererManager renderManager) {
        super(renderManager, BOAR_MODEL, 0.4F);
    }

    @Override
    public void render(EntityBoar entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = BOAR_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(EntityBoar entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.25f);
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.6f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityBoar entity) {
        return entity.getTexture();
    }
}
