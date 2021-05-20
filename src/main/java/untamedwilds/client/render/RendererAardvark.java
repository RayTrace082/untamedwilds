package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelAardvark;
import untamedwilds.entity.mammal.EntityAardvark;

import javax.annotation.Nonnull;

public class RendererAardvark extends MobRenderer<EntityAardvark, EntityModel<EntityAardvark>> {

    private static final ModelAardvark AARDVARK_MODEL = new ModelAardvark();

    public RendererAardvark(EntityRendererManager renderManager) {
        super(renderManager, AARDVARK_MODEL, 0.4F);
    }

    @Override
    public void render(EntityAardvark entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        entityModel = AARDVARK_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(EntityAardvark entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.25f);
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.6f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityAardvark entity) {
        return entity.getTexture();
    }
}
