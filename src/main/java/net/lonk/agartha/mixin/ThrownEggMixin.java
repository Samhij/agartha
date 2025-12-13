package net.lonk.agartha.mixin;

import net.lonk.agartha.entity.ModEntities;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggMixin {
    @Inject(
            method = "onHit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void agartha_spawnYakubOnHit(HitResult pHitResult, CallbackInfo ci) {
        ThrownEgg self = (ThrownEgg) (Object) this;
        Level level = self.level();
        RandomSource random = self.level().random;

        // Check if on server side
        assert level != null;
        if (!level.isClientSide()) {
            // Reimplement the chance logic (1 in 8 chance to spawn a Yakub)
            if (random.nextInt(8) == 0) {
                YakubEntity yakub = ModEntities.YAKUB.get().create(level);
                if (yakub != null) {
                    // Set position to the egg's position
                    yakub.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0f);
                    // Add the entity to the world
                    level.addFreshEntity(yakub);
                }
            }

            // Broadcast the entity event (ID 3 for particle effect)
            level.broadcastEntityEvent(self, (byte) 3);
            // Discard the projectile
            self.discard();
        }
        // Cancel the original method execution to prevent vanilla behavior
        ci.cancel();
    }
}
