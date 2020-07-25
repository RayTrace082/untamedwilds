package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelSnake;
import untamedwilds.entity.reptile.EntitySnake;

import javax.annotation.Nonnull;

public class RendererSnake extends MobRenderer<EntitySnake, EntityModel<EntitySnake>> {

    private final ResourceLocation ADDER         = new ResourceLocation("untamedwilds:textures/entity/snake/adder.png");
    private final ResourceLocation BALL_PYTHON = new ResourceLocation("untamedwilds:textures/entity/snake/ball_python.png");
    private final ResourceLocation BLACK_MAMBA        = new ResourceLocation("untamedwilds:textures/entity/snake/black_mamba.png");
    private final ResourceLocation CARPET_PYTHON        = new ResourceLocation("untamedwilds:textures/entity/snake/carpet_python.png");
    private final ResourceLocation CORAL        = new ResourceLocation("untamedwilds:textures/entity/snake/coral.png");
    private final ResourceLocation CORAL_BLUE        = new ResourceLocation("untamedwilds:textures/entity/snake/coral_blue.png");
    private final ResourceLocation CORN        = new ResourceLocation("untamedwilds:textures/entity/snake/corn.png");

    public RendererSnake(EntityRendererManager rendermanager, ModelSnake model, float shadowsize) {
        super(rendermanager, model, shadowsize);
    }

    protected void preRenderCallback(EntitySnake entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.3f);
        f *= entity.getRenderScale();
        f *= (EntitySnake.SpeciesSnake.values()[entity.getSpecies()].sizeMult);
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getEntityTexture(@Nonnull EntitySnake entity) {

        switch (entity.getSpecies()) {
            default:
            case 0: return ADDER;
            case 1: return BALL_PYTHON;
            case 2: return BLACK_MAMBA;
            case 3: return CARPET_PYTHON;
            case 4: return CORAL;
            case 5: return CORAL_BLUE;
            case 6: return CORN;
        }
    }
}
