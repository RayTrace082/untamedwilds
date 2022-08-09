package untamedwilds.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import untamedwilds.client.model.ModelBigCat;
import untamedwilds.client.model.ModelBigCatCub;
import untamedwilds.entity.mammal.EntityBigCat;

public class RendererBigCat extends MobRenderer<EntityBigCat, EntityModel<EntityBigCat>> {

    private static final ModelBigCat BIG_CAT_MODEL = new ModelBigCat();
    private static final ModelBigCatCub BIG_CAT_MODEL_CUB = new ModelBigCatCub();

    public RendererBigCat(EntityRendererProvider.Context renderManager) {
        super(renderManager, BIG_CAT_MODEL, 1F);
    }

    @Override
    public void render(EntityBigCat entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.isBaby()) {
            model = BIG_CAT_MODEL_CUB;
        } else {
            model = BIG_CAT_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityBigCat entity, PoseStack matrixStackIn, float partialTickTime) {
        float f = entity.getMobSize();
        //f *= entity.getScale();
        matrixStackIn.scale(f, f, f);
        this.shadowRadius = f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityBigCat entity) {
        return entity.getTexture();
    }
}
