package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.block.*;
import de.rexlnico.rexltech.growable.RubberTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RexlTech.MODID);

    //Ores
    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5, 6).sound(SoundType.METAL).setRequiresTool()));
    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new Block(AbstractBlock.Properties.from(Blocks.IRON_ORE).harvestTool(ToolType.PICKAXE).setRequiresTool()));

    //Machines
    public static final RegistryObject<Block> COAL_GENERATOR = BLOCKS.register("coal_generator", CoalGenerator::new);
    public static final RegistryObject<Block> BASIC_ENERGY_CELL = BLOCKS.register("basic_energy_cell", BasicEnergyCell::new);
    public static final RegistryObject<Block> CRUSHER = BLOCKS.register("crusher", Crusher::new);
    public static final RegistryObject<Block> LATEX_EXTRACTOR = BLOCKS.register("latex_extractor", LatexExtractor::new);
    public static final RegistryObject<Block> ITEM_OUTPUT = BLOCKS.register("item_output", ItemOutputBlock::new);
    public static final RegistryObject<Block> MINER_BLOCK = BLOCKS.register("miner_block", MinerBlock::new);

    //Cables
    public static final RegistryObject<Block> BASIC_ENERGY_CABLE = BLOCKS.register("basic_energy_cable", BasicEnergyCable::new);

    //Rubber Tree
    public static final RegistryObject<Block> RUBBER_LEAVES = BLOCKS.register("rubber_leaves", () -> new LeavesBlock(AbstractBlock.Properties.from(Blocks.OAK_LEAVES).tickRandomly().notSolid()));
    public static final RegistryObject<Block> RUBBER_LOG = BLOCKS.register("rubber_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> RUBBER_WOOD = BLOCKS.register("rubber_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_RUBBER_LOG = BLOCKS.register("stripped_rubber_log", () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_RUBBER_WOOD = BLOCKS.register("stripped_rubber_wood", () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> RUBBER_PLANKS = BLOCKS.register("rubber_planks", () -> new Block(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> RUBBER_SAPLING = BLOCKS.register("rubber_sapling", () -> new SaplingBlock(new RubberTree(), AbstractBlock.Properties.from(Blocks.OAK_SAPLING).tickRandomly()));

}
