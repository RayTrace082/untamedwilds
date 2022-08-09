package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelGiantSalamander;
import untamedwilds.entity.amphibian.EntityGiantSalamander;

import javax.annotation.Nonnull;

public class RendererGiantSalamander extends MobRenderer<EntityGiantSalamander, EntityModel<EntityGiantSalamander>> {

    private static final ModelGiantSalamander GIANT_SALAMANDER_MODEL = new ModelGiantSalamander();

    public RendererGiantSalamander(EntityRendererProvider.Context renderManager) {
        super(renderManager, GIANT_SALAMANDER_MODEL, 0.2F);
    }

    @Override
    protected void scale(EntityGiantSalamander entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.4F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityGiantSalamander entity) {
        return entity.getTexture();
    }
}
