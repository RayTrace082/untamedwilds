package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelNewt;
import untamedwilds.entity.amphibian.EntityNewt;

import javax.annotation.Nonnull;

public class RendererNewt extends MobRenderer<EntityNewt, EntityModel<EntityNewt>> {

    private static final ModelNewt NEWT_MODEL = new ModelNewt();

    public RendererNewt(EntityRendererProvider.Context renderManager) {
        super(renderManager, NEWT_MODEL, 0.1F);
    }

    @Override
    protected void scale(EntityNewt entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.2F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityNewt entity) {
        return entity.getTexture();
    }
}
