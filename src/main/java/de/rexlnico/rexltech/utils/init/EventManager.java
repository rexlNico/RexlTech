package de.rexlnico.rexltech.utils.init;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.config.RexlTechOreConfig;
import de.rexlnico.rexltech.item.Drill;
import de.rexlnico.rexltech.tileentity.BaseTileEntityMachineBlock;
import de.rexlnico.rexltech.tileentity.TileEntityLatexExtractor;
import de.rexlnico.rexltech.world.gen.RubberTreeGeneration;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RexlTech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class EventManager {

    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof BaseMachineBlock && event.getWorld().getTileEntity(event.getPos()) instanceof BaseTileEntityMachineBlock) {
            InventoryHelper.dropItems((World) event.getWorld(), event.getPos(), ((BaseTileEntityMachineBlock) event.getWorld().getTileEntity(event.getPos())).handler.getStack());
        }
        if (event.getPlayer().getHeldItem(Hand.MAIN_HAND).getItem() == ItemInit.DRILL.get()) {
            event.getPlayer().getHeldItem(Hand.MAIN_HAND).getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                if (handler.getEnergyStored() < Drill.getEnergyCostPerBlock()) {
                    if (event.getWorld().getBlockState(event.getPos()).getRequiresTool()) {
                        event.setCanceled(true);
                        event.getWorld().destroyBlock(event.getPos(), false, event.getPlayer());
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void biomeLoadingEvent(BiomeLoadingEvent event) {
        RubberTreeGeneration.generateTrees(event);
    }

    @SubscribeEvent
    public static void worldTickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.type == TickEvent.Type.WORLD && event.world.getGameTime() % 40 == 0 && TileEntityLatexExtractor.EXTRACTION.containsKey(event.world.getDimensionType())) {
            TileEntityLatexExtractor.EXTRACTION.get(event.world.getDimensionType()).values().forEach(blockPosFluidExtractionProgressHashMap -> blockPosFluidExtractionProgressHashMap.keySet().forEach(pos -> event.world.sendBlockBreakProgress(blockPosFluidExtractionProgressHashMap.get(pos).getBreakID(), pos, blockPosFluidExtractionProgressHashMap.get(pos).getProgress())));
        }
    }

    public static void addOres(final BiomeLoadingEvent event) {
        if (RexlTechOreConfig.copper_amount.get() > 0) {
            addOre(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockInit.COPPER_ORE.get().getDefaultState(), RexlTechOreConfig.copper_veinSize.get(), RexlTechOreConfig.copper_minHeight.get(), RexlTechOreConfig.copper_maxHeight.get(), RexlTechOreConfig.copper_amount.get());
        }
    }

    public static void addOre(final BiomeLoadingEvent event, RuleTest rule, BlockState state, int veinSize, int minHeight, int maxHeight, int amount) {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(rule, state, veinSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))).square().count(amount));
    }

}
