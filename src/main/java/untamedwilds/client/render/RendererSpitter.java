package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.MonsterSpitter;
import untamedwilds.client.model.MonsterSpitterLarva;
import untamedwilds.entity.relict.EntitySpitter;

public class RendererSpitter extends MobRenderer<EntitySpitter, EntityModel<EntitySpitter>> {

    private static final MonsterSpitter SPITTER_MODEL = new MonsterSpitter();
    private static final MonsterSpitterLarva SPITTER_MODEL_LARVA = new MonsterSpitterLarva();

    public RendererSpitter(EntityRendererProvider.Context renderManager) {
        super(renderManager, SPITTER_MODEL, 1F);
    }

    @Override
    public void render(EntitySpitter entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.isBaby()) {
            model = SPITTER_MODEL_LARVA;
        } else {
            model = SPITTER_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntitySpitter entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntitySpitter entity) {
        return entity.getTexture();
    }
}
