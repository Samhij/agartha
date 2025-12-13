package net.lonk.agartha.item.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhiteMonsterItem extends HoneyBottleItem {
    public WhiteMonsterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (pEntityLiving instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, pStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!pLevel.isClientSide()) {
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 3));
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 100));
        }
        return pStack;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}
