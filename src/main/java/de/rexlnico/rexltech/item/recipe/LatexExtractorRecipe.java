package de.rexlnico.rexltech.item.recipe;

import com.google.gson.JsonObject;
import de.rexlnico.rexltech.tileentity.TileEntityLatexExtractor;
import de.rexlnico.rexltech.utils.init.RecipeInit;
import de.rexlnico.rexltech.utils.jei.JEIPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LatexExtractorRecipe implements IRecipe<IInventory> {

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final int latex;
    private final int workTime;
    private final BlockState result;

    public LatexExtractorRecipe(ResourceLocation id, Ingredient ingredient, int latex, @Nonnull Block result, int workTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.latex = latex;
        this.result = result.getDefaultState();
        this.workTime = workTime;
        if (!JEIPlugin.LATEX_EXTRACTOR_RECIPES.containsKey(ingredient.getMatchingStacks()[0].getItem())) {
            JEIPlugin.LATEX_EXTRACTOR_RECIPES.put(ingredient.getMatchingStacks()[0].getItem(), this);
        }
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    public int getWorkTime() {
        return workTime;
    }

    public BlockState getResult() {
        return result;
    }

    public int getLatexProduction() {
        return latex;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeInit.LATEX_EXTRACTOR_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeInit.LATEX_EXTRACTOR_TYPE;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public boolean isValid(TileEntityLatexExtractor tile) {
        if (tile.getWorld().isAirBlock(tile.getBlockPosInFront()))
            return false;
        return this.ingredient.test(new ItemStack(tile.getWorld().getBlockState(tile.getBlockPosInFront()).getBlock().asItem(), 1));
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<LatexExtractorRecipe> {

        @Override
        public LatexExtractorRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            int latexProduction = JSONUtils.getInt(json, "production", 1);
            Block block = JSONUtils.hasField(json, "leftover") ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(json, "leftover"))) != null ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(json, "leftover"))) : Blocks.AIR : Blocks.AIR;
            int workTime = JSONUtils.getInt(json, "work_time", 1200);
            return new LatexExtractorRecipe(recipeId, ingredient, latexProduction, block, workTime);
        }

        @Nullable
        @Override
        public LatexExtractorRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            int latexProduction = buffer.readInt();
            String result = buffer.readString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(result)) != null ? ForgeRegistries.BLOCKS.getValue(new ResourceLocation(result)) : Blocks.AIR;
            int workTime = buffer.readInt();
            return new LatexExtractorRecipe(recipeId, ingredient, latexProduction, block, workTime);
        }

        @Override
        public void write(PacketBuffer buffer, LatexExtractorRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeInt(recipe.latex);
            buffer.writeString(recipe.result.getBlock().getRegistryName().getPath());
            buffer.writeInt(recipe.workTime);
        }

    }
}
