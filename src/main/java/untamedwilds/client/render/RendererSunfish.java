package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelSunfish;
import untamedwilds.entity.fish.EntitySunfish;

import javax.annotation.Nonnull;

public class RendererSunfish extends MobRenderer<EntitySunfish, EntityModel<EntitySunfish>> {

    private static final ModelSunfish SUNFISH_MODEL = new ModelSunfish();

    public RendererSunfish(EntityRendererManager rendermanager) {
        super(rendermanager, SUNFISH_MODEL, 1F);
    }

    protected void preRenderCallback(EntitySunfish entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntitySunfish entity) {
        return entity.getTexture();
    }
}
