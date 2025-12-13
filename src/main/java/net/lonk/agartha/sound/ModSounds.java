package net.lonk.agartha.sound;

import net.lonk.agartha.AgarthaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AgarthaMod.MODID);

    public static final RegistryObject<SoundEvent> YAKUB_AMBIENT_SOUND = SOUND_EVENTS.register("yakub_ambient",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "yakub_ambient")));
}
