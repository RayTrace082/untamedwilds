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

    public RendererTarantula(EntityRendererManager renderManager) {
        super(renderManager, TARANTULA_MODEL, 0.2F);
    }

    @Override
    protected void preRenderCallback(EntityTarantula entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.7F;
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f * 0.6F;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityTarantula entity) {
        return entity.getTexture();
    }
}
