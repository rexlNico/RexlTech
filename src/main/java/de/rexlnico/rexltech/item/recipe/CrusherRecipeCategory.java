package de.rexlnico.rexltech.item.recipe;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.container.CrusherContainer;
import de.rexlnico.rexltech.utils.init.BlockInit;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CrusherRecipeCategory implements IRecipeCategory<CrusherRecipe>, IRecipeTransferHandler<CrusherContainer> {

    protected final IDrawable background;
    private final IDrawable icon;
    public CrusherRecipeCategory(IGuiHelper helper) {
        ResourceLocation backgroundTexture = new ResourceLocation(RexlTech.MODID+":textures/jei/crusher.png");
        this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.CRUSHER.get()));
        this.background = helper.createDrawable(backgroundTexture, 0, 0,  72, 18);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(RexlTech.MODID, "crushing");
    }

    @Override
    public Class<? extends CrusherRecipe> getRecipeClass() {
        return CrusherRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("screen.rexltech.crusher").getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(CrusherRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrusherRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, true, 0, 0);
        items.init(1, false, 54, 0);
        items.set(ingredients);
    }

    @Override
    public Class<CrusherContainer> getContainerClass() {
        return CrusherContainer.class;
    }

}
