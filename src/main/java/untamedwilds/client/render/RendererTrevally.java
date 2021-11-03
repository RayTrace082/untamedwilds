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

    public RendererTrevally(EntityRendererManager rendermanager) {
        super(rendermanager, TREVALLY_MODEL, 0.2F);
    }
    @Override
    protected void preRenderCallback(EntityTrevally entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTrevally entity) {
        return entity.getTexture();
    }
}
