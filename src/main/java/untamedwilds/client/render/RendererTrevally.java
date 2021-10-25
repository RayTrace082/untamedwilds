package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelTrevally;
import untamedwilds.entity.fish.EntityTrevally;

import javax.annotation.Nonnull;

public class RendererTrevally extends MobRenderer<EntityTrevally, EntityModel<EntityTrevally>> {

    private static final ModelTrevally TREVALLY_MODEL = new ModelTrevally();

    private final ResourceLocation BIGEYE         = new ResourceLocation("untamedwilds:textures/entity/trevally/bigeye.png");
    private final ResourceLocation BLUESPOTTED    = new ResourceLocation("untamedwilds:textures/entity/trevally/bluespotted.png");
    private final ResourceLocation GIANT         = new ResourceLocation("untamedwilds:textures/entity/trevally/giant.png");
    private final ResourceLocation GOLDEN = new ResourceLocation("untamedwilds:textures/entity/trevally/golden.png");
    private final ResourceLocation JACK        = new ResourceLocation("untamedwilds:textures/entity/trevally/jack.png");

    public RendererTrevally(EntityRendererManager rendermanager) {
        super(rendermanager, TREVALLY_MODEL, 0.2F);
    }
    @Override
    protected void preRenderCallback(EntityTrevally entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 1F;
        f += (entity.getMobSize() * 0.2f);
        f *= entity.getRenderScale();
        f *= entity.getModelScale(entity.getVariant());
        matrixStackIn.scale(f, f, f);
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTrevally entity) {
        return entity.getTexture();
    }
}
