package de.rexlnico.rexltech.utils.container;

import de.rexlnico.rexltech.utils.tileentity.CustomItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CustomOutputSlotItemHandler extends SlotItemHandler {

    private CustomItemStackHandler handler;
    private int index;

    public CustomOutputSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
        if(itemHandler instanceof CustomItemStackHandler){
            handler = (CustomItemStackHandler) itemHandler;
        }
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

}
