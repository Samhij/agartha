package net.lonk.agartha.entity.client;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class YakubRenderer extends MobRenderer<YakubEntity, YakubModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "textures/entity/yakub_texture.png");

    public YakubRenderer(EntityRendererProvider.Context context) {
        super(context, new YakubModel(context.bakeLayer(YakubModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(YakubEntity yakubEntity) {
        return TEXTURE;
    }
}
