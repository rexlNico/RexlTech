package de.rexlnico.rexltech.utils.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CoalGeneratorSlotItemHandler extends SlotItemHandler {


    public CoalGeneratorSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(!(ForgeHooks.getBurnTime(stack) > 0) || !stack.getItem().getRegistryName().toString().contains("coal")) return false;
        return super.isItemValid(stack);
    }

}
