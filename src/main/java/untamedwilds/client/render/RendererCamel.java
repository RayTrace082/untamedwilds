package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelCamel;
import untamedwilds.client.model.ModelCamelCalf;
import untamedwilds.entity.mammal.EntityCamel;

public class RendererCamel extends MobRenderer<EntityCamel, EntityModel<EntityCamel>> {

    private static final ModelCamel CAMEL_MODEL = new ModelCamel();
    private static final ModelCamelCalf CAMEL_CALF_MODEL = new ModelCamelCalf();

    public RendererCamel(EntityRendererProvider.Context renderManager) {
        super(renderManager, CAMEL_MODEL, 1F);
    }

    @Override
    public void render(EntityCamel entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        model = !entityIn.isBaby() ? CAMEL_MODEL : CAMEL_CALF_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityCamel entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * (entity.isBaby() ? 0.7F : 1F);
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.9F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityCamel entity) {
        return entity.getTexture();
    }
}
