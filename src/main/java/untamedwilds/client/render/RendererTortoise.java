package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelTortoise;
import untamedwilds.entity.reptile.EntityTortoise;

import javax.annotation.Nonnull;

public class RendererTortoise extends MobRenderer<EntityTortoise, EntityModel<EntityTortoise>> {

    private static final ModelTortoise SOFTSHELL_TURTLE_MODEL = new ModelTortoise();

    public RendererTortoise(EntityRendererManager rendererManager) {
        super(rendererManager, SOFTSHELL_TURTLE_MODEL, 0.4F);
    }

    @Override
    protected void preRenderCallback(EntityTortoise entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntityTortoise.SpeciesTortoise.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTortoise entity) {
        return entity.getTexture();
    }

}
