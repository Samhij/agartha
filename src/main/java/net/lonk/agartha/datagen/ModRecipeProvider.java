package net.lonk.agartha.datagen;

import net.lonk.agartha.block.ModBlocks;
import net.lonk.agartha.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        oreSmelting(writer, List.of(ModBlocks.AGARTHIUM_ORE.get()), RecipeCategory.MISC, ModItems.AGARTHAN_RESIDUE.get(), 0.25f, 200, "agarthium");
        oreBlasting(writer, List.of(ModBlocks.AGARTHIUM_ORE.get()), RecipeCategory.MISC, ModItems.AGARTHAN_RESIDUE.get(), 0.25f, 100, "agarthium");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.AGARTHIUM_BLOCK.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.AGARTHIUM.get())
                .unlockedBy("has_agarthium", has(ModItems.AGARTHIUM.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AGARTHIUM.get(), 9)
                .requires(ModBlocks.AGARTHIUM_BLOCK.get())
                .unlockedBy("has_agarthium", has(ModItems.AGARTHIUM.get()))
                .save(writer, ModItems.AGARTHIUM.getId() + "_from_agarthium_block");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AGARTHIUM.get())
                .requires(ModItems.AGARTHAN_RESIDUE.get(), 4)
                .requires(Items.AMETHYST_SHARD, 4)
                .unlockedBy("has_agarthan_residue", has(ModItems.AGARTHAN_RESIDUE.get()))
                .save(writer, ModItems.AGARTHIUM.getId() + "_from_agarthan_residue");
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
