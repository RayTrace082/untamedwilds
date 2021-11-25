package untamedwilds.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import untamedwilds.client.model.ModelWhaleShark;
import untamedwilds.entity.fish.EntityWhaleShark;

import javax.annotation.Nonnull;

public class RendererWhaleShark extends MobRenderer<EntityWhaleShark, EntityModel<EntityWhaleShark>> {

    private static final ModelWhaleShark SHARK_MODEL = new ModelWhaleShark();

    public RendererWhaleShark(EntityRendererManager rendermanager) {
        super(rendermanager, SHARK_MODEL, 0.2F);
    }
    @Override
    protected void preRenderCallback(EntityWhaleShark entity, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getRenderScale();
        matrixStackIn.scale(f, f, f);
        this.shadowSize = f;
    }

    public ResourceLocation getEntityTexture(@Nonnull EntityWhaleShark entity) {
        return entity.getTexture();
    }
}
