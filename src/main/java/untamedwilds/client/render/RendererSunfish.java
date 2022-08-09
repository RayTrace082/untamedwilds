package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelSunfish;
import untamedwilds.entity.fish.EntitySunfish;

import javax.annotation.Nonnull;

public class RendererSunfish extends MobRenderer<EntitySunfish, EntityModel<EntitySunfish>> {

    private static final ModelSunfish SUNFISH_MODEL = new ModelSunfish();

    public RendererSunfish(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, SUNFISH_MODEL, 1F);
    }

    protected void scale(EntitySunfish entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    public @NotNull ResourceLocation getTextureLocation(EntitySunfish entity) {
        return entity.getTexture();
    }
}
