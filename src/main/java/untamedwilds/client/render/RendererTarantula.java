package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelTarantula;
import untamedwilds.entity.arthropod.EntityTarantula;

import javax.annotation.Nonnull;

public class RendererTarantula extends MobRenderer<EntityTarantula, EntityModel<EntityTarantula>> {

    private static final ModelTarantula TARANTULA_MODEL = new ModelTarantula();

    private final ResourceLocation SLING         = new ResourceLocation("untamedwilds:textures/entity/tarantula/sling.png");
    private final ResourceLocation BLACK         = new ResourceLocation("untamedwilds:textures/entity/tarantula/black.png");
    private final ResourceLocation BLACK_AND_WHITE = new ResourceLocation("untamedwilds:textures/entity/tarantula/black_and_white.png");
    private final ResourceLocation COBALT        = new ResourceLocation("untamedwilds:textures/entity/tarantula/cobalt.png");
    private final ResourceLocation KING         = new ResourceLocation("untamedwilds:textures/entity/tarantula/king.png");
    private final ResourceLocation RED_KNEE       = new ResourceLocation("untamedwilds:textures/entity/tarantula/red_knee.png");
    private final ResourceLocation REGALIS       = new ResourceLocation("untamedwilds:textures/entity/tarantula/regalis.png");
    private final ResourceLocation ROSE       = new ResourceLocation("untamedwilds:textures/entity/tarantula/rose.png");
    private final ResourceLocation TIGER       = new ResourceLocation("untamedwilds:textures/entity/tarantula/tiger.png");

    public RendererTarantula(EntityRendererManager renderManager) {
        super(renderManager, TARANTULA_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityTarantula entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.5F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntityTarantula.SpeciesTarantula.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTarantula entity) {
        if (entity.getGrowingAge() < 0) {
            return SLING;
        }
        switch (entity.getVariant()) {
            default:
            case 0: return BLACK;
            case 1: return BLACK_AND_WHITE;
            case 2: return COBALT;
            case 3: return KING;
            case 4: return RED_KNEE;
            case 5: return REGALIS;
            case 6: return ROSE;
            case 7: return TIGER;
        }
    }
}
