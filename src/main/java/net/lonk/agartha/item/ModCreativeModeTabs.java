package net.lonk.agartha.item;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AgarthaMod.MODID);

    public static final RegistryObject<CreativeModeTab> AGARTHA_TAB = CREATIVE_MODE_TABS.register("agartha_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.WHITE_MONSTER.get()))
                    .title(Component.translatable("creativetab.agartha_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.WHITE_MONSTER.get());
                        output.accept(ModItems.YAKUB_SPAWN_EGG.get());

                        output.accept(ModItems.AGARTHIUM.get());
                        output.accept(ModItems.AGARTHAN_RESIDUE.get());

                        output.accept(ModBlocks.AGARTHIUM_BLOCK.get());
                        output.accept(ModBlocks.AGARTHIUM_ORE.get());
                    }).build());
}
