package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelCatfish;
import untamedwilds.entity.fish.EntityCatfish;

public class RendererCatfish extends MobRenderer<EntityCatfish, EntityModel<EntityCatfish>> {

    private static final ModelCatfish TREVALLY_MODEL = new ModelCatfish();

    public RendererCatfish(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, TREVALLY_MODEL, 0.2F);
    }

    @Override
    protected void scale(EntityCatfish entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.5F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityCatfish entity) {
        return entity.getTexture();
    }
}
