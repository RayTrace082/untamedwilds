package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelSnake;
import untamedwilds.entity.reptile.EntitySnake;

import javax.annotation.Nonnull;

public class RendererSnake extends MobRenderer<EntitySnake, EntityModel<EntitySnake>> {

    private static final ModelSnake SNAKE_MODEL = new ModelSnake();

    public RendererSnake(EntityRendererProvider.Context renderManager) {
        super(renderManager, SNAKE_MODEL, 0F);
    }

    protected void scale(EntitySnake entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
    }

    public @NotNull ResourceLocation getTextureLocation(EntitySnake entity) {
        return entity.getTexture();
    }
}
