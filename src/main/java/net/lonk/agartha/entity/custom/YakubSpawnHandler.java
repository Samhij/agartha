package net.lonk.agartha.entity.custom;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AgarthaMod.MODID)
public class YakubSpawnHandler {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide() || event.phase != TickEvent.Phase.END) return;
        ServerLevel serverLevel = (ServerLevel) event.level;

        // Only spawn in overworld, at night, and if canSpawn is true
        if (serverLevel.dimension() != ServerLevel.OVERWORLD || !serverLevel.isNight() || !YakubEntity.canSpawn) return;

        // Check gamerule for spawn chance
        int chance = serverLevel.getGameRules().getInt(AgarthaMod.YAKUB_SPAWN_CHANCE);
        if (serverLevel.random.nextInt(chance) != 0) return;

        // Find a random player and attempt to spawn near them
        serverLevel.players().forEach(player -> {
            if (serverLevel.random.nextFloat() < 0.1f) { // Additional rarity per player
                BlockPos spawnPos = findValidSpawnPos(serverLevel, player.blockPosition());
                if (spawnPos != null) {
                    YakubEntity yakub = new YakubEntity(ModEntities.YAKUB.get(), serverLevel);
                    yakub.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, serverLevel.random.nextFloat() * 360, 0);
                    if (serverLevel.addFreshEntity(yakub)) {
                        YakubEntity.canSpawn = false; // Prevent multiple spawns
                    }
                }
            }
        });
    }

    private static BlockPos findValidSpawnPos(ServerLevel level, BlockPos center) {
        for (int attempts = 0; attempts < 10; attempts++) {
            BlockPos pos = center.offset(level.random.nextInt(50) - 25, level.random.nextInt(10) - 5, level.random.nextInt(50) - 25);
            if (level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        return null;
    }
}
