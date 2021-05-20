package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelTurtleSoftshell;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;

import javax.annotation.Nonnull;

public class RendererSoftshellTurtle extends MobRenderer<EntitySoftshellTurtle, EntityModel<EntitySoftshellTurtle>> {

    private static final ModelTurtleSoftshell SOFTSHELL_TURTLE_MODEL = new ModelTurtleSoftshell();

    public RendererSoftshellTurtle(EntityRendererManager rendererManager) {
        super(rendererManager, SOFTSHELL_TURTLE_MODEL, 0.4F);
    }

    @Override
    protected void preRenderCallback(EntitySoftshellTurtle entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 1F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntitySoftshellTurtle entity) {
        return entity.getTexture();
    }

}
