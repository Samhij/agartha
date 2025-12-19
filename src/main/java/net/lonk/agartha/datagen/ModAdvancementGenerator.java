package net.lonk.agartha.datagen;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement whiteMonsterAdvancement = Advancement.Builder.advancement()
                .display(
                        ModItems.WHITE_MONSTER.get(),
                        Component.translatable("advancements.agartha.white_monster.title"),
                        Component.translatable("advancements.agartha.white_monster.description"),
                        null,
                        FrameType.CHALLENGE,
                        true, true, false
                )
                .addCriterion("obtain_white_monster", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHITE_MONSTER.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(AgarthaMod.MODID, "white_monster"), existingFileHelper);
    }
}