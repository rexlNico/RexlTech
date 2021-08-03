package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.tileentity.BaseTileEntityCable;
import de.rexlnico.rexltech.tileentity.TileEntityBasicEnergyCell;
import de.rexlnico.rexltech.tileentity.TileEntityCoalGenerator;
import de.rexlnico.rexltech.tileentity.TileEntityCrusher;
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

}
