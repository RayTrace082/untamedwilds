package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelGiantSalamander;
import untamedwilds.entity.amphibian.EntityGiantSalamander;

import javax.annotation.Nonnull;

public class RendererGiantSalamander extends MobRenderer<EntityGiantSalamander, EntityModel<EntityGiantSalamander>> {

    private static final ModelGiantSalamander GIANT_SALAMANDER_MODEL = new ModelGiantSalamander();

    public RendererGiantSalamander(EntityRendererManager renderManager) {
        super(renderManager, GIANT_SALAMANDER_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityGiantSalamander entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f * 0.6F;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityGiantSalamander entity) {
        return entity.getTexture();
    }
}
