package de.rexlnico.rexltech.item.recipe;

import com.google.gson.JsonObject;
import de.rexlnico.rexltech.utils.init.RecipeInit;
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

    public CrusherRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        super(RecipeInit.Types.CRUSHING, RecipeInit.Serializers.CRUSHING.get(), id, "", ingredient, result);
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
            int count = JSONUtils.getInt(json, "count");
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
            return new CrusherRecipe(recipeId, ingredient, result);
        }

        @Nullable
        @Override
        public CrusherRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack result = buffer.readItemStack();
            return new CrusherRecipe(recipeId, ingredient, result);
        }

        @Override
        public void write(PacketBuffer buffer, CrusherRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }

}
