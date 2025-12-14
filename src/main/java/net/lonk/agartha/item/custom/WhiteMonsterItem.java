package net.lonk.agartha.item.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhiteMonsterItem extends HoneyBottleItem {
    public WhiteMonsterItem(Properties pProperties) {
        super(pProperties.durability(64));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 64;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide()) {
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 2));
            pEntityLiving.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 10));
            pStack.hurtAndBreak(1, pEntityLiving, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            return pStack;
        } else {
            return super.finishUsingItem(pStack, pLevel, pEntityLiving);
        }
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }
}
