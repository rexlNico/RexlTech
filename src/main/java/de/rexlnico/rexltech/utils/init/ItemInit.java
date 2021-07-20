package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.item.BaseEnergyCellBlockItem;
import de.rexlnico.rexltech.item.Drill;
import de.rexlnico.rexltech.item.Wrench;
import de.rexlnico.rexltech.itemgroup.MachineGroup;
import de.rexlnico.rexltech.itemgroup.ResourcesGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RexlTech.MODID);

    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().group(ResourcesGroup.RESOURCES_GROUP)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties().group(ResourcesGroup.RESOURCES_GROUP)));
    public static final RegistryObject<Item> DRILL = ITEMS.register("drill", Drill::new);
    public static final RegistryObject<BlockItem> COPPER_BLOCK = ITEMS.register("copper_block", () -> new BlockItem(BlockInit.COPPER_BLOCK.get(), new Item.Properties().group(ResourcesGroup.RESOURCES_GROUP)));
    public static final RegistryObject<BlockItem> COPPER_ORE = ITEMS.register("copper_ore", () -> new BlockItem(BlockInit.COPPER_ORE.get(), new Item.Properties().group(ResourcesGroup.RESOURCES_GROUP)));

    public static final RegistryObject<BlockItem> COAL_GENERATOR = ITEMS.register("coal_generator", () -> new BlockItem(BlockInit.COAL_GENERATOR.get(), new Item.Properties().group(MachineGroup.MACHINEGROUP)));
    public static final RegistryObject<BlockItem> BASIC_ENERGY_CELL = ITEMS.register("basic_energy_cell", () -> new BaseEnergyCellBlockItem(BlockInit.BASIC_ENERGY_CELL.get(), new Item.Properties().group(MachineGroup.MACHINEGROUP)));

    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", Wrench::new);
}
