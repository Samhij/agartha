package net.lonk.agartha.datagen;

import net.lonk.agartha.AgarthaMod;
import net.lonk.agartha.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AgarthaMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile agarthiumOreModel = models().cubeColumn("agarthium_ore",
                modLoc("block/agarthium_ore_top"),
                modLoc("block/agarthium_ore_side"));

        blockWithItem(ModBlocks.AGARTHIUM_BLOCK);
        simpleBlockWithItem(ModBlocks.AGARTHIUM_ORE.get(), agarthiumOreModel);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
