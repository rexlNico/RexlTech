package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, RexlTech.MODID);

    public static final ResourceLocation LATEX_FLUID_TEXTURE = new ResourceLocation(RexlTech.MODID, "fluids/latex_still");
    public static final ResourceLocation LATEX_FLOWING_TEXTURE = new ResourceLocation(RexlTech.MODID, "fluids/latex_flowing");
    public static final RegistryObject<FlowingFluid> LATEX_FLUID = FLUIDS.register("latex_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.LATEX_PROPERTIES));
    public static final RegistryObject<FlowingFluid> LATEX_FLOWING = FLUIDS.register("latex_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.LATEX_PROPERTIES));
    public static RegistryObject<FlowingFluidBlock> LATEX_FLUID_BLOCK = BlockInit.BLOCKS.register("latex_fluid_block", () -> new FlowingFluidBlock(LATEX_FLUID,
            Block.Properties.create(Material.WATER)
                    .doesNotBlockMovement()
                    .hardnessAndResistance(100.0F)
                    .noDrops().speedFactor(0.0f).jumpFactor(0.35f)));
    public static RegistryObject<BucketItem> LATEX_FLUID_BUCKET = ItemInit.ITEMS.register("latex_fluid_bucket",
            () -> new BucketItem(LATEX_FLUID,
                    new Item.Properties()
                            .containerItem(Items.BUCKET)
                            .maxStackSize(1)
                            .group(ItemGroup.MISC)));
    public static final ForgeFlowingFluid.Properties LATEX_PROPERTIES = new ForgeFlowingFluid.Properties(
            LATEX_FLUID,
            LATEX_FLOWING,
            FluidAttributes.builder(LATEX_FLUID_TEXTURE, LATEX_FLOWING_TEXTURE).viscosity(50))
            .bucket(LATEX_FLUID_BUCKET)
            .tickRate(20)
            .block(LATEX_FLUID_BLOCK);

}
