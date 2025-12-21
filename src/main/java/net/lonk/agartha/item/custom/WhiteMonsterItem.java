package net.lonk.agartha.item.custom;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collections;

public class WhiteMonsterItem extends HoneyBottleItem {
    public WhiteMonsterItem(Properties pProperties) {
        super(pProperties.durability(5));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 5;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (pEntityLiving instanceof Player player && player.canChangeDimensions()) {
            int chance = pLevel.getGameRules().getInt(AgarthaMod.WHITE_MONSTER_TELEPORT_CHANCE);
            if (pLevel.random.nextInt(chance) == 0) {
                handleAgarthaPortal(player);
            }
        }

        if (!pLevel.isClientSide()) {
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 2));
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 10));
            pStack.hurtAndBreak(1, pEntityLiving, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            return pStack;
        } else {
            return super.finishUsingItem(pStack, pLevel, pEntityLiving);
        }
    }

    private void handleAgarthaPortal(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        ResourceKey<Level> destKey = (player.level().dimension() == ModDimensions.AGARTHA_LEVEL_KEY)
                ? Level.OVERWORLD : ModDimensions.AGARTHA_LEVEL_KEY;

        ServerLevel destLevel = server.getLevel(destKey);

        if (destLevel != null && !player.isPassenger()) {
            BlockPos spawnPos = destLevel.getSharedSpawnPos();

            // Ensure the destination chunks are loaded
            destLevel.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, new ChunkPos(spawnPos), 1, player.getId());

            // Use an empty set so the player is moved to the EXACT coordinates provided
            player.teleportTo(
                    destLevel,
                    spawnPos.getX() + 0.5,
                    spawnPos.getY(),
                    spawnPos.getZ() + 0.5,
                    Collections.emptySet(), // No relative movement
                    player.getYRot(),
                    player.getXRot()
            );
        }
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}
