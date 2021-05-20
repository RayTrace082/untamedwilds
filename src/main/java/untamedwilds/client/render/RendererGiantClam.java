package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelGiantClam;
import untamedwilds.entity.mollusk.EntityGiantClam;

import javax.annotation.Nonnull;

public class RendererGiantClam extends MobRenderer<EntityGiantClam, EntityModel<EntityGiantClam>> {

    private static final ModelGiantClam GIANT_CLAM_MODEL = new ModelGiantClam();

    public RendererGiantClam(EntityRendererManager renderManager) {
        super(renderManager, GIANT_CLAM_MODEL, 1F);
    }

    @Override
    protected void preRenderCallback(EntityGiantClam entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = EntityGiantClam.SpeciesGiantClam.values()[entity.getVariant()].scale;
        f += (entity.getMobSize() * 0.25f);
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = EntityGiantClam.SpeciesGiantClam.values()[entity.getVariant()].scale * 0.8f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityGiantClam entity) {
        return entity.getTexture();
    }
}
