package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelAardvark;
import untamedwilds.entity.mammal.EntityAardvark;

public class RendererAardvark extends MobRenderer<EntityAardvark, EntityModel<EntityAardvark>> {

    private static final ModelAardvark AARDVARK_MODEL = new ModelAardvark();

    public RendererAardvark(EntityRendererProvider.Context renderManager) {
        super(renderManager, AARDVARK_MODEL, 0.4F);
    }

    @Override
    public void render(EntityAardvark entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        model = AARDVARK_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityAardvark entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.6F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityAardvark entity) {
        return entity.getTexture();
    }
}
