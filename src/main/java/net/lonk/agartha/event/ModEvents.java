package net.lonk.agartha.event;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.ModEntities;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AgarthaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.YAKUB.get(), YakubEntity.createAttributes().build());
    }
}
