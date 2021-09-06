package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.item.recipe.CrusherRecipe;
import de.rexlnico.rexltech.item.recipe.LatexExtractorRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeInit {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RexlTech.MODID);

    public static final IRecipeType<CrusherRecipe> CRUSHING_TYPE = IRecipeType.register(RexlTech.MODID + "crushing");
    public static final RegistryObject<IRecipeSerializer<?>> CRUSHING_SERIALIZER = RECIPE_SERIALIZER.register("crushing", CrusherRecipe.Serializer::new);

    public static final IRecipeType<LatexExtractorRecipe> LATEX_EXTRACTOR_TYPE = IRecipeType.register(RexlTech.MODID + "latex_extractor");
    public static final RegistryObject<IRecipeSerializer<?>> LATEX_EXTRACTOR_SERIALIZER = RECIPE_SERIALIZER.register("latex_extractor", LatexExtractorRecipe.Serializer::new);
}
