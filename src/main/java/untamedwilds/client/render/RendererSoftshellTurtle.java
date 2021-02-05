package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import untamedwilds.client.model.ModelTurtleSoftshell;
import untamedwilds.entity.reptile.EntitySoftshellTurtle;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class RendererSoftshellTurtle extends MobRenderer<EntitySoftshellTurtle, EntityModel<EntitySoftshellTurtle>> {

    private static final ModelTurtleSoftshell SOFTSHELL_TURTLE_MODEL = new ModelTurtleSoftshell();

    private final ResourceLocation BLACK         = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/black.png");
    private final ResourceLocation CHINESE         = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/chinese.png");
    private final ResourceLocation FLAPSHELL         = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/flapshell.png");
    private final ResourceLocation NILE = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/nile.png");
    private final ResourceLocation PEACOCK        = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/peacock.png");
    private final ResourceLocation PIG_NOSE         = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/pig_nose.png");
    private final ResourceLocation SPINY       = new ResourceLocation("untamedwilds:textures/entity/softshell_turtle/spiny.png");

    public RendererSoftshellTurtle(EntityRendererManager rendererManager) {
        super(rendererManager, SOFTSHELL_TURTLE_MODEL, 0.4F);
    }

    @Override
    protected void preRenderCallback(EntitySoftshellTurtle entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 1F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntitySoftshellTurtle.SpeciesSoftshellTurtle.values()[entity.getSpecies()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntitySoftshellTurtle entity) {

        switch (entity.getSpecies()) {
            default:
            case 0: return BLACK;
            case 1: return CHINESE;
            case 2: return FLAPSHELL;
            case 3: return NILE;
            case 4: return PEACOCK;
            case 5: return PIG_NOSE;
            case 6: return SPINY;
        }
    }
}
