package net.lonk.agartha.event;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.lonk.agartha.sound.custom.YakubAmbientSound;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = AgarthaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEvents {
    private static final Map<YakubEntity, YakubAmbientSound> ambientSounds = new WeakHashMap<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                mc.level.getEntitiesOfClass(YakubEntity.class, mc.player.getBoundingBox().inflate(50)).forEach(yakub -> {
                    if (!ambientSounds.containsKey(yakub) && yakub.isAlive()) {
                        YakubAmbientSound sound = new YakubAmbientSound(yakub);
                        mc.getSoundManager().play(sound);
                        ambientSounds.put(yakub, sound);
                    }
                });
                ambientSounds.entrySet().removeIf(entry -> !entry.getKey().isAlive() || entry.getKey().isRemoved());
            }
        }
    }
}