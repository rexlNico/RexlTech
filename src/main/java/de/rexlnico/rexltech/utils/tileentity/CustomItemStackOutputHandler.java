package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CustomItemStackOutputHandler extends ItemStackHandler {

    TileEntity tile;

    public CustomItemStackOutputHandler(TileEntity tile, int size) {
        super(size);
        this.tile = tile;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
        tile.markDirty();
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        tile.markDirty();
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.markDirty();
        super.onContentsChanged(slot);
    }
    
    @Override
    public void setSize(int size) {
        tile.markDirty();
        super.setSize(size);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        tile.markDirty();
        super.setStackInSlot(slot, stack);
    }
}
