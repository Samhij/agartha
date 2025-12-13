package net.lonk.agartha.entity;

import net.lonk.agartha.AgarthaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModDamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, AgarthaMod.MODID);

    public static final RegistryObject<DamageType> AGARTHAN_DAMAGE = DAMAGE_TYPES.register("agarthan_damage",
            () -> new DamageType("agarthan_damage", DamageScaling.NEVER, 0.1f));
}
