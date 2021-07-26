package de.rexlnico.rexltech.utils.jei;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.item.recipe.CrusherRecipe;
import de.rexlnico.rexltech.item.recipe.CrusherRecipeCategory;
import de.rexlnico.rexltech.screen.CrusherScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.*;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final Map<Item, CrusherRecipe> CRUSHER_RECIPES = new HashMap<>();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(RexlTech.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        registration.addRecipeCategories(new CrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        registration.addRecipes(getSortedCrusherRecipes(), new ResourceLocation(RexlTech.MODID, "crushing"));
    }

    private Collection<CrusherRecipe> getSortedCrusherRecipes(){
        List<CrusherRecipe> coll = new ArrayList<>(CRUSHER_RECIPES.values());
        coll.sort(Comparator.comparing(CrusherRecipe::getOutputAmount));
        return coll;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IModPlugin.super.registerRecipeTransferHandlers(registration);
        registration.addRecipeTransferHandler(new CrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()), new ResourceLocation(RexlTech.MODID, "crushing"));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
        registration.addRecipeClickArea(CrusherScreen.class, 75, 33, 24, 17, new ResourceLocation(RexlTech.MODID, "crushing"));
    }
}
