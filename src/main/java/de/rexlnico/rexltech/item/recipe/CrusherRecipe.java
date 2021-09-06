package de.rexlnico.rexltech.item.recipe;

import com.google.gson.JsonObject;
import de.rexlnico.rexltech.utils.init.RecipeInit;
import de.rexlnico.rexltech.utils.jei.JEIPlugin;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CrusherRecipe extends SingleItemRecipe {

    public int processTime;

    public CrusherRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int processTime) {
        super(RecipeInit.CRUSHING_TYPE, RecipeInit.CRUSHING_SERIALIZER.get(), id, "", ingredient, result);
        this.processTime = processTime;
        if (!JEIPlugin.CRUSHER_RECIPES.containsKey(ingredient.getMatchingStacks()[0].getItem())) {
            JEIPlugin.CRUSHER_RECIPES.put(ingredient.getMatchingStacks()[0].getItem(), this);
        }
    }

    public int getOutputAmount() {
        return result.getCount();
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0));
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrusherRecipe> {

        @Override
        public CrusherRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getString(json, "result"));
            int count = JSONUtils.getInt(json, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
            int processTime = JSONUtils.getInt(json, "processTime");
            return new CrusherRecipe(recipeId, ingredient, result, processTime);
        }

        @Nullable
        @Override
        public CrusherRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            int processTime = buffer.readInt();
            return new CrusherRecipe(recipeId, ingredient, result, processTime);
        }

        @Override
        public void write(PacketBuffer buffer, CrusherRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
            buffer.writeInt(recipe.processTime);
        }

        public void comparingByValue() {

        }

    }
}
