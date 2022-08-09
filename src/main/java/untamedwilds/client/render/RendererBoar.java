package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBoar;
import untamedwilds.entity.mammal.EntityBoar;

import javax.annotation.Nonnull;

public class RendererBoar extends MobRenderer<EntityBoar, EntityModel<EntityBoar>> {

    private static final ModelBoar BOAR_MODEL = new ModelBoar();

    public RendererBoar(EntityRendererProvider.Context renderManager) {
        super(renderManager, BOAR_MODEL, 0.4F);
    }

    @Override
    public void render(EntityBoar entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        model = BOAR_MODEL;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityBoar entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.6F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityBoar entity) {
        return entity.getTexture();
    }
}
