package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelTurtleSoftshell;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;

import javax.annotation.Nonnull;

public class RendererSoftshellTurtle extends MobRenderer<EntitySoftshellTurtle, EntityModel<EntitySoftshellTurtle>> {

    private static final ModelTurtleSoftshell SOFTSHELL_TURTLE_MODEL = new ModelTurtleSoftshell();

    public RendererSoftshellTurtle(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, SOFTSHELL_TURTLE_MODEL, 0.4F);
    }

    @Override
    protected void scale(EntitySoftshellTurtle entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.4F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntitySoftshellTurtle entity) {
        return entity.getTexture();
    }

}
