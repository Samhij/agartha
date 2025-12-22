package net.lonk.agartha.datagen;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AgarthaMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.WHITE_MONSTER);
        simpleItem(ModItems.YAKUB_SPAWN_EGG);

        simpleItem(ModItems.AGARTHAN_RESIDUE);
        simpleItem(ModItems.AGARTHIUM);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(AgarthaMod.MODID, "item/" + item.getId().getPath()));
    }
}
