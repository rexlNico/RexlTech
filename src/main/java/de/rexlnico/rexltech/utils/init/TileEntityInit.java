package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RexlTech.MODID);

    public static final RegistryObject<TileEntityType<?>> COAL_GENERATOR_TILE_ENTITY = TILE_ENTITIES.register("coal_generator", () -> TileEntityType.Builder.create(TileEntityCoalGenerator::new, BlockInit.COAL_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> BASIC_ENERGY_CELL = TILE_ENTITIES.register("basic_energy_cell", () -> TileEntityType.Builder.create(TileEntityBasicEnergyCell::new, BlockInit.BASIC_ENERGY_CELL.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> CRUSHER = TILE_ENTITIES.register("crusher", () -> TileEntityType.Builder.create(TileEntityCrusher::new, BlockInit.CRUSHER.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> ENERGY_CABLE = TILE_ENTITIES.register("energy_cable", () -> TileEntityType.Builder.create(BaseTileEntityCable::new, BlockInit.BASIC_ENERGY_CABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> ITEM_OUTPUT = TILE_ENTITIES.register("item_output", () -> TileEntityType.Builder.create(TileEntityItemOutput::new, BlockInit.ITEM_OUTPUT.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> MINER_BLOCK = TILE_ENTITIES.register("miner_block", () -> TileEntityType.Builder.create(TileEntityMiner::new, BlockInit.MINER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<?>> LATEX_EXTRACTOR = TILE_ENTITIES.register("latex_extractor", () -> TileEntityType.Builder.create(TileEntityLatexExtractor::new, BlockInit.LATEX_EXTRACTOR.get()).build(null));

}
