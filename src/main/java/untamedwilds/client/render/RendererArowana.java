package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelArowana;
import untamedwilds.entity.fish.EntityArowana;

import javax.annotation.Nonnull;

public class RendererArowana extends MobRenderer<EntityArowana, EntityModel<EntityArowana>> {

    private static final ModelArowana AROWANA_MODEL = new ModelArowana();

    public RendererArowana(EntityRendererManager rendermanager) {
        super(rendermanager, AROWANA_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityArowana entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntityArowana.SpeciesArowana.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityArowana entity) {
        return entity.getTexture();
    }
}
