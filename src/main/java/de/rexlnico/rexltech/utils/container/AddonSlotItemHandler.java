package de.rexlnico.rexltech.utils.container;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class AddonSlotItemHandler extends SlotItemHandler {


    private final List<Item> allowedAddons;

    public AddonSlotItemHandler(IItemHandler itemHandler, List<Item> allowedAddons, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.allowedAddons = allowedAddons;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(!allowedAddons.contains(stack.getItem())) return false;
        return super.isItemValid(stack);
    }

}
