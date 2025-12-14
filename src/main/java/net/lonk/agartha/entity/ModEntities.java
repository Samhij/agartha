package net.lonk.agartha.entity;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.custom.YakubEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AgarthaMod.MODID);

    public static final RegistryObject<EntityType<YakubEntity>> YAKUB = ENTITY_TYPES.register("yakub",
            () -> EntityType.Builder.of(YakubEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build("yakub"));
}
