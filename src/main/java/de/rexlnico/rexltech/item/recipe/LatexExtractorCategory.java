package de.rexlnico.rexltech.item.recipe;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.container.LatexExtractorContainer;
import de.rexlnico.rexltech.utils.init.BlockInit;
import de.rexlnico.rexltech.utils.init.FluidInit;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LatexExtractorCategory implements IRecipeCategory<LatexExtractorRecipe>, IRecipeTransferHandler<LatexExtractorContainer> {

    private final IGuiHelper helper;
    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable tankOverlay;

    public LatexExtractorCategory(IGuiHelper helper) {
        this.helper = helper;
        ResourceLocation backgroundTexture = new ResourceLocation(RexlTech.MODID + ":textures/jei/latex_extractor.png");
        this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.LATEX_EXTRACTOR.get()));
        this.background = helper.drawableBuilder(backgroundTexture, 0, 27, 76, 50).addPadding(0, 0, 0, 74).build();
        this.tankOverlay = helper.createDrawable(backgroundTexture, 1, 207, 12, 48);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(RexlTech.MODID, "latex_extractor");
    }

    @Override
    public Class<? extends LatexExtractorRecipe> getRecipeClass() {
        return LatexExtractorRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("screen.rexltech.latex_extractor").getString();
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
    public void setIngredients(LatexExtractorRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.getIngredient().getMatchingStacks()));
        ingredients.setOutput(VanillaTypes.FLUID, new FluidStack(FluidInit.LATEX_FLUID.get(), recipe.getLatexProduction()));
        if (recipe.getResult().getBlock() != Blocks.AIR)
            ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.getResult().getBlock()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LatexExtractorRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.init(0, true, 0, 16);
        if (ingredients.getOutputs(VanillaTypes.ITEM).size() > 0) {
            items.init(1, false, 27, 34);
            items.setBackground(1, helper.getSlotDrawable());
            items.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        }
        IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
        fluids.init(1, false, 57, 1, 12, 48, Math.max(50, ingredients.getOutputs(VanillaTypes.FLUID).get(0).get(0).getAmount()), false, tankOverlay);
        items.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        fluids.set(1, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
    }

    @Override
    public void draw(LatexExtractorRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.DARK_GRAY + "Production: ", 80, 6, 0xFFFFFF);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, TextFormatting.DARK_GRAY + "" + recipe.getLatexProduction() + "mb/tick", 80, 6 + (Minecraft.getInstance().fontRenderer.FONT_HEIGHT + 2) * 1, 0xFFFFFF);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(LatexExtractorRecipe recipe, double mouseX, double mouseY) {
        if (mouseX >= 78 && mouseX <= 140 && mouseY >= 5 && mouseY <= 25)
            return Arrays.asList(new StringTextComponent("Production rate"));
        return new ArrayList<>();
    }

    @Override
    public Class<LatexExtractorContainer> getContainerClass() {
        return LatexExtractorContainer.class;
    }

}
