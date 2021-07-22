package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.item.recipe.CrusherRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeInit {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RexlTech.MODID);

    public static class Types {
        public static final IRecipeType<CrusherRecipe> CRUSHING = IRecipeType.register(RexlTech.MODID + "crushing");
    }

    public static class Serializers {

        public static final RegistryObject<IRecipeSerializer<?>> CRUSHING = RECIPE_SERIALIZER.register("crushing", CrusherRecipe.Serializer::new);

    }

}
