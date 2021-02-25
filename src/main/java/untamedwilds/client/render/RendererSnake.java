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

    private static final ModelSnake SNAKE_MODEL = new ModelSnake();

    private final ResourceLocation ADDER         = new ResourceLocation("untamedwilds:textures/entity/snake/adder.png");
    private final ResourceLocation BALL_PYTHON = new ResourceLocation("untamedwilds:textures/entity/snake/ball_python.png");
    private final ResourceLocation BLACK_MAMBA        = new ResourceLocation("untamedwilds:textures/entity/snake/black_mamba.png");
    private final ResourceLocation CARPET_PYTHON        = new ResourceLocation("untamedwilds:textures/entity/snake/carpet_python.png");
    private final ResourceLocation CAVE_RACER        = new ResourceLocation("untamedwilds:textures/entity/snake/cave_racer.png");
    private final ResourceLocation CORAL        = new ResourceLocation("untamedwilds:textures/entity/snake/coral.png");
    private final ResourceLocation CORAL_BLUE        = new ResourceLocation("untamedwilds:textures/entity/snake/coral_blue.png");
    private final ResourceLocation CORN        = new ResourceLocation("untamedwilds:textures/entity/snake/corn.png");
    private final ResourceLocation EMERALD        = new ResourceLocation("untamedwilds:textures/entity/snake/emerald.png");
    private final ResourceLocation GRASS        = new ResourceLocation("untamedwilds:textures/entity/snake/grass_snake.png");
    private final ResourceLocation GRAY_KINGSNAKE        = new ResourceLocation("untamedwilds:textures/entity/snake/gray_kingsnake.png");
    private final ResourceLocation GREEN_MAMBA        = new ResourceLocation("untamedwilds:textures/entity/snake/green_mamba.png");
    private final ResourceLocation RATTLESNAKE        = new ResourceLocation("untamedwilds:textures/entity/snake/rattlesnake.png");
    private final ResourceLocation RICE_PADDY        = new ResourceLocation("untamedwilds:textures/entity/snake/rice_paddy.png");
    private final ResourceLocation SWAMP_MOCCASIN        = new ResourceLocation("untamedwilds:textures/entity/snake/swamp_moccasin.png");
    private final ResourceLocation TAIPAN        = new ResourceLocation("untamedwilds:textures/entity/snake/taipan.png");
    private final ResourceLocation WESTERN_RATTLESNAKE        = new ResourceLocation("untamedwilds:textures/entity/snake/western_rattlesnake.png");

    public RendererSnake(EntityRendererManager renderManager) {
        super(renderManager, SNAKE_MODEL, 0F);
    }

    protected void preRenderCallback(EntitySnake entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.3f);
        f *= entity.getRenderScale();
        f *= (EntitySnake.SpeciesSnake.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getEntityTexture(@Nonnull EntitySnake entity) {

        switch (entity.getVariant()) {
            default:
            case 0: return ADDER;
            case 1: return BALL_PYTHON;
            case 2: return BLACK_MAMBA;
            case 3: return CARPET_PYTHON;
            case 4: return CAVE_RACER;
            case 5: return CORAL;
            case 6: return CORAL_BLUE;
            case 7: return CORN;
            case 8: return EMERALD;
            case 9: return GRASS;
            case 10: return GRAY_KINGSNAKE;
            case 11: return GREEN_MAMBA;
            case 12: return RATTLESNAKE;
            case 13: return RICE_PADDY;
            case 14: return SWAMP_MOCCASIN;
            case 15: return TAIPAN;
            case 16: return WESTERN_RATTLESNAKE;
        }
    }
}
