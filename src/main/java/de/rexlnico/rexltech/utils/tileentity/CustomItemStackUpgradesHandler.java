package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CustomItemStackUpgradesHandler extends ItemStackHandler {

    private TileEntity tile;
    public List<Item> allowedUpgrades;

    public CustomItemStackUpgradesHandler(TileEntity tile, int size, Item... allowedUpgrades) {
        super(size);
        this.allowedUpgrades = Arrays.asList(allowedUpgrades);
        this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!allowedUpgrades.contains(stack.getItem())) return stack;
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

    public List<Item> getAllowedUpgrades() {
        return allowedUpgrades;
    }
}
