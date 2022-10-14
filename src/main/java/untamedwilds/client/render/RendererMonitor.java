package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelMonitor;
import untamedwilds.entity.reptile.EntityMonitor;

public class RendererMonitor extends MobRenderer<EntityMonitor, EntityModel<EntityMonitor>> {

    private static final ModelMonitor MODEL_MONITOR = new ModelMonitor();

    public RendererMonitor(EntityRendererProvider.Context renderManager) {
        super(renderManager, MODEL_MONITOR, 0F);
    }

    protected void scale(EntityMonitor entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
    }

    public @NotNull ResourceLocation getTextureLocation(EntityMonitor entity) {
        return entity.getTexture();
    }
}
