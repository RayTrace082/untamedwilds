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

    private final ResourceLocation BLACK         = new ResourceLocation("untamedwilds:textures/entity/arowana/black.png");
    private final ResourceLocation DRAGON    = new ResourceLocation("untamedwilds:textures/entity/arowana/dragon.png");
    private final ResourceLocation GOLDEN         = new ResourceLocation("untamedwilds:textures/entity/arowana/golden.png");
    private final ResourceLocation GREEN = new ResourceLocation("untamedwilds:textures/entity/arowana/green.png");
    private final ResourceLocation JARDINI        = new ResourceLocation("untamedwilds:textures/entity/arowana/jardini.png");
    private final ResourceLocation SILVER        = new ResourceLocation("untamedwilds:textures/entity/arowana/silver.png");

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
        switch (entity.getVariant()) {
            default:
            case 0: return BLACK;
            case 1: return DRAGON;
            case 2: return GOLDEN;
            case 3: return GREEN;
            case 4: return JARDINI;
            case 5: return SILVER;
        }
    }
}
