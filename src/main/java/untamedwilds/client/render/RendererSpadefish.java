package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelSpadefish;
import untamedwilds.entity.fish.EntitySpadefish;

public class RendererSpadefish extends MobRenderer<EntitySpadefish, EntityModel<EntitySpadefish>> {

    private static final ModelSpadefish SPADEFISH_MODEL = new ModelSpadefish();

    public RendererSpadefish(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, SPADEFISH_MODEL, 0.2F);
    }
    @Override
    protected void scale(EntitySpadefish entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.5F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntitySpadefish entity) {
        return entity.getTexture();
    }
}
