package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelGiantClam;
import untamedwilds.entity.mollusk.EntityGiantClam;

import javax.annotation.Nonnull;

public class RendererGiantClam extends MobRenderer<EntityGiantClam, EntityModel<EntityGiantClam>> {

    private static final ModelGiantClam GIANT_CLAM_MODEL = new ModelGiantClam();

    public RendererGiantClam(EntityRendererProvider.Context renderManager) {
        super(renderManager, GIANT_CLAM_MODEL, 1F);
    }

    @Override
    protected void scale(EntityGiantClam entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityGiantClam entity) {
        return entity.getTexture();
    }
}
