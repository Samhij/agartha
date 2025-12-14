package net.lonk.agartha.event;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.ModEntities;
import net.lonk.agartha.entity.client.YakubRenderer;
import net.lonk.agartha.entity.client.YakubModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AgarthaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.YAKUB.get(), YakubRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(YakubModel.LAYER_LOCATION, YakubModel::createBodyLayer);
    }
}
