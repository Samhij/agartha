package net.lonk.agartha.item;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.entity.ModEntities;
import net.lonk.agartha.item.custom.WhiteMonsterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AgarthaMod.MODID);

    public static final RegistryObject<Item> WHITE_MONSTER = ITEMS.register("white_monster",
            () -> new WhiteMonsterItem(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> YAKUB_SPAWN_EGG = ITEMS.register("yakub_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.YAKUB, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final RegistryObject<Item> AGARTHAN_RESIDUE = ITEMS.register("agarthan_residue",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AGARTHIUM = ITEMS.register("agarthium",
            () -> new Item(new Item.Properties()));
}
