package net.lonk.agartha.sound.custom;

import net.lonk.agartha.entity.custom.YakubEntity;
import net.lonk.agartha.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class YakubAmbientSound extends SimpleSoundInstance implements TickableSoundInstance {
    private final YakubEntity yakub;

    public YakubAmbientSound(YakubEntity yakub) {
        super(ModSounds.YAKUB_AMBIENT_SOUND.get().getLocation(),
                SoundSource.NEUTRAL,
                1f,
                1f,
                RandomSource.create(),
                true,
                0,
                Attenuation.NONE,
                yakub.getX(), yakub.getY(), yakub.getZ(),
                false
        );
        this.yakub = yakub;
    }

    @Override
    public void tick() {
        if (this.yakub.isAlive() && this.yakub.level() == Minecraft.getInstance().level) {
            this.x = (float) this.yakub.getX();
            this.y = (float) this.yakub.getY();
            this.z = (float) this.yakub.getZ();
        }
    }

    @Override
    public boolean isStopped() {
        return !this.yakub.isAlive() || this.yakub.isRemoved();
    }

    @Override
    public boolean canPlaySound() {
        return this.yakub.isAlive() && Minecraft.getInstance().level == this.yakub.level();
    }
}
