package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelSnake;
import untamedwilds.entity.reptile.EntitySnake;

import javax.annotation.Nonnull;

public class RendererSnake extends MobRenderer<EntitySnake, EntityModel<EntitySnake>> {

    private static final ModelSnake SNAKE_MODEL = new ModelSnake();

    public RendererSnake(EntityRendererManager renderManager) {
        super(renderManager, SNAKE_MODEL, 0F);
    }

    protected void preRenderCallback(EntitySnake entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.3f);
        f *= entity.getRenderScale();
        f *= (EntitySnake.SpeciesSnake.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntitySnake entity) {
        return entity.getTexture();
    }
}
