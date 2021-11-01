package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class CustomFluidStorage extends FluidTank {

    private TileEntity tile;
    private Fluid filter = null;
    public boolean input = true;
    public boolean output = true;

    public CustomFluidStorage(TileEntity tile, int capacity) {
        super(capacity);
        this.tile = tile;
    }

    public CustomFluidStorage(TileEntity tile, int capacity, Fluid filter) {
        super(capacity);
        this.filter = filter;
        this.tile = tile;
    }

    public void setAmount(int amount) {
        if (getFluid() != FluidStack.EMPTY) {
            fluid = new FluidStack(fluid.getFluid(), amount);
            tile.markDirty();
            sendTileUpdate();
        }
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        if (this.filter != null && !stack.getFluid().equals(filter))
            return false;
        return super.isFluidValid(stack);
    }

    public void sendTileUpdate() {
    }

    public int fillForced(FluidStack resource, FluidAction action) {
        tile.markDirty();
        sendTileUpdate();
        return super.fill(resource, action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        tile.markDirty();
        sendTileUpdate();
        return input ? super.fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        tile.markDirty();
        sendTileUpdate();
        return output ? super.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        tile.markDirty();
        sendTileUpdate();
        return output ? super.drain(resource, action) : FluidStack.EMPTY;
    }

    @Override
    protected void onContentsChanged() {
        tile.markDirty();
        sendTileUpdate();
        super.onContentsChanged();
    }
}
