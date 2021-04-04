package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelShark;
import untamedwilds.entity.fish.EntityShark;

import javax.annotation.Nonnull;

public class RendererShark extends MobRenderer<EntityShark, EntityModel<EntityShark>> {

    private static final ModelShark SHARK_MODEL = new ModelShark();

    private final ResourceLocation BIGEYE         = new ResourceLocation("untamedwilds:textures/entity/shark/bigeye_sand_tiger.png");
    private final ResourceLocation BLUNTNOSE         = new ResourceLocation("untamedwilds:textures/entity/shark/bluntnose.png");
    private final ResourceLocation BULL         = new ResourceLocation("untamedwilds:textures/entity/shark/bull.png");
    private final ResourceLocation GOBLIN         = new ResourceLocation("untamedwilds:textures/entity/shark/goblin.png");
    private final ResourceLocation GREAT_WHITE         = new ResourceLocation("untamedwilds:textures/entity/shark/great_white.png");
    private final ResourceLocation GREENLAND         = new ResourceLocation("untamedwilds:textures/entity/shark/greenland.png");
    private final ResourceLocation HAMMERHEAD         = new ResourceLocation("untamedwilds:textures/entity/shark/hammerhead.png");
    private final ResourceLocation LEMON         = new ResourceLocation("untamedwilds:textures/entity/shark/lemon.png");
    private final ResourceLocation MAKO         = new ResourceLocation("untamedwilds:textures/entity/shark/mako.png");
    private final ResourceLocation TIGER         = new ResourceLocation("untamedwilds:textures/entity/shark/tiger.png");

    public RendererShark(EntityRendererManager rendermanager) {
        super(rendermanager, SHARK_MODEL, 0.2F);
    }
    @Override
    protected void preRenderCallback(EntityShark entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.8F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= (EntityShark.SpeciesShark.values()[entity.getVariant()].scale);
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityShark entity) {
        switch (entity.getVariant()) {
            default:
            case 0: return BIGEYE;
            case 1: return BLUNTNOSE;
            case 2: return BULL;
            case 3: return GOBLIN;
            case 4: return GREAT_WHITE;
            case 5: return GREENLAND;
            case 6: return HAMMERHEAD;
            case 7: return LEMON;
            case 8: return MAKO;
            case 9: return TIGER;
        }
    }
}
