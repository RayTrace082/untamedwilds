package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.layer.LayerFootballFishLure;
import untamedwilds.client.model.ModelFootballFish;
import untamedwilds.entity.fish.EntityFootballFish;

import javax.annotation.Nonnull;

public class RendererFootballFish extends MobRenderer<EntityFootballFish, EntityModel<EntityFootballFish>> {

    private static final ModelFootballFish FOOTBALL_FISH_MODEL = new ModelFootballFish();

    public RendererFootballFish(EntityRendererProvider.Context rendermanager) {
        super(rendermanager, FOOTBALL_FISH_MODEL, 0.6F);
        this.addLayer(new LayerFootballFishLure<>(this));
    }

    protected void scale(EntityFootballFish entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize() * 0.8F;
        f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f * 0.6F;
    }

    public @NotNull ResourceLocation getTextureLocation(EntityFootballFish entity) {
        return entity.getTexture();
    }
}
