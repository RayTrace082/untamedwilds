package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelAnaconda;
import untamedwilds.entity.reptile.EntityAnaconda;

public class RendererAnaconda extends MobRenderer<EntityAnaconda, EntityModel<EntityAnaconda>> {

    private static final ModelAnaconda SNAKE_MODEL = new ModelAnaconda();

    public RendererAnaconda(EntityRendererProvider.Context renderManager) {
        super(renderManager, SNAKE_MODEL, 0F);
    }

    protected void scale(EntityAnaconda entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
    }

    public @NotNull ResourceLocation getTextureLocation(EntityAnaconda entity) {
        return entity.getTexture();
    }
}
