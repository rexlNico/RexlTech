package de.rexlnico.rexltech.container;

import de.rexlnico.rexltech.tileentity.TileEntityLatexExtractor;
import de.rexlnico.rexltech.utils.container.BasicMachineContainer;
import de.rexlnico.rexltech.utils.init.BlockInit;
import de.rexlnico.rexltech.utils.init.ContainerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LatexExtractorContainer extends BasicMachineContainer {

    public LatexExtractorContainer(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ContainerInit.LATEX_EXTRACTOR_CONTAINER.get(), windowID, world, pos, playerInventory, playerEntity);
    }

    public TileEntityLatexExtractor getTileEntity() {
        return (TileEntityLatexExtractor) tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, BlockInit.LATEX_EXTRACTOR.get());
    }
}
