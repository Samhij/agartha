package net.lonk.agartha.entity.custom;

import net.lonk.agartha.entity.ModEntities;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class YakubSpawner {
    private static final Map<UUID, Integer> playerTimers = new HashMap<>();
    private static final int TIMER_DURATION = 2400; // 2 minutes = 2 * 60 * 20 = 2400 ticks
    private static final double SPAWN_RADIUS = 50.0D;

    private static boolean hasSentWarning = false;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.player;
        UUID playerId = player.getUUID();

        // Initialize or decrement timer
        playerTimers.putIfAbsent(playerId, TIMER_DURATION);
        int timer = playerTimers.get(playerId) - 1;
        playerTimers.put(playerId, timer);

        if (timer <= TIMER_DURATION / 2 && !hasSentWarning && YakubEntity.canSpawn(player.level())) {
            // Notify player when half the time has passed
            player.displayClientMessage(Component.literal("§4Do you come from a land down under?§r"), true);
            hasSentWarning = true;
        }

        if (timer <= 0) {
            // Reset timer
            playerTimers.put(playerId, TIMER_DURATION);
            hasSentWarning = false;

            Level level = player.level();

            if (YakubEntity.canSpawn(level)) {
                // Spawn at random location near player
                double offsetX = (level.random.nextDouble() - 0.5) * SPAWN_RADIUS * 2;
                double offsetZ = (level.random.nextDouble() - 0.5) * SPAWN_RADIUS * 2;
                YakubEntity newYakub = new YakubEntity(ModEntities.YAKUB.get(), level);
                newYakub.setPos(player.getX() + offsetX, player.getY(), player.getZ() + offsetZ);
                level.addFreshEntity(newYakub);
            }
        }
    }
}
