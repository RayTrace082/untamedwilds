package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelShark;
import untamedwilds.entity.fish.EntityShark;

import javax.annotation.Nonnull;

public class RendererShark extends MobRenderer<EntityShark, EntityModel<EntityShark>> {

    private static final ModelShark SHARK_MODEL = new ModelShark();

    public RendererShark(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, SHARK_MODEL, 0.2F);
    }
    @Override
    protected void scale(EntityShark entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityShark entity) {
        return entity.getTexture();
    }
}
