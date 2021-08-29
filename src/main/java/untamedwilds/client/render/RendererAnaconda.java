package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelAnaconda;
import untamedwilds.entity.reptile.EntityAnaconda;

import javax.annotation.Nonnull;

public class RendererAnaconda extends MobRenderer<EntityAnaconda, EntityModel<EntityAnaconda>> {

    private static final ModelAnaconda SNAKE_MODEL = new ModelAnaconda();

    public RendererAnaconda(EntityRendererManager renderManager) {
        super(renderManager, SNAKE_MODEL, 0F);
    }

    protected void preRenderCallback(EntityAnaconda entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.3f);
        f *= entity.getRenderScale();
        f *= (EntityAnaconda.SpeciesAnaconda.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityAnaconda entity) {
        return entity.getTexture();
    }
}
