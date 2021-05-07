package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelGiantSalamander;
import untamedwilds.entity.amphibian.EntityGiantSalamander;

import javax.annotation.Nonnull;

public class RendererGiantSalamander extends MobRenderer<EntityGiantSalamander, EntityModel<EntityGiantSalamander>> {

    private static final ModelGiantSalamander GIANT_SALAMANDER_MODEL = new ModelGiantSalamander();

    private static final ResourceLocation CHINESE         = new ResourceLocation("untamedwilds:textures/entity/giant_salamander/chinese.png");
    private static final ResourceLocation HELLBENDER = new ResourceLocation("untamedwilds:textures/entity/giant_salamander/hellbender.png");
    private static final ResourceLocation JAPANESE         = new ResourceLocation("untamedwilds:textures/entity/giant_salamander/japanese.png");

    public RendererGiantSalamander(EntityRendererManager renderManager) {
        super(renderManager, GIANT_SALAMANDER_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityGiantSalamander entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getModelScale();
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntityGiantSalamander.SpeciesGiantSalamander.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
        this.shadowSize = entity.getModelScale() * 0.4f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityGiantSalamander entity) {
        if (entity.getGrowingAge() < 0) {
            return CHINESE;
        }
        switch (entity.getVariant()) {
            default:
            case 0: return CHINESE;
            case 1: return HELLBENDER;
            case 2: return JAPANESE;
        }
    }
}
