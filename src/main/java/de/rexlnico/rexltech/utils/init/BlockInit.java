package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.block.BasicEnergyCell;
import de.rexlnico.rexltech.block.CoalGenerator;
import de.rexlnico.rexltech.block.Crusher;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RexlTech.MODID);

    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5, 6).sound(SoundType.METAL).setRequiresTool()));
    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    public static final RegistryObject<Block> COAL_GENERATOR = BLOCKS.register("coal_generator", CoalGenerator::new);
    public static final RegistryObject<Block> BASIC_ENERGY_CELL = BLOCKS.register("basic_energy_cell", BasicEnergyCell::new);
    public static final RegistryObject<Block> CRUSHER = BLOCKS.register("crusher", Crusher::new);


}
