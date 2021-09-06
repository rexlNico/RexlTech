package de.rexlnico.rexltech.utils.helper;

import de.rexlnico.rexltech.item.recipe.LatexExtractorRecipe;
import de.rexlnico.rexltech.tileentity.TileEntityLatexExtractor;
import de.rexlnico.rexltech.utils.init.RecipeInit;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;

public class RecipeHelper {

    @Nullable
    public static LatexExtractorRecipe getLatexExtractorRecipe(TileEntityLatexExtractor tile) {
        for (IRecipe<?> recipe : tile.getWorld().getRecipeManager().getRecipesForType(RecipeInit.LATEX_EXTRACTOR_TYPE)) {
            if (recipe instanceof LatexExtractorRecipe) {
                final LatexExtractorRecipe latexExtractorRecipe = (LatexExtractorRecipe) recipe;
                if (latexExtractorRecipe.isValid(tile)) {
                    return latexExtractorRecipe;
                }
            }
        }
        return null;
    }

}
