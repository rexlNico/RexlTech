package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.CustomItemStackOutputHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityItemOutput extends TileEntity implements ITickableTileEntity {

    public TileEntity controller;
    public CustomItemStackOutputHandler itemStackHandler = new CustomItemStackOutputHandler(this, 4);
    LazyOptional<IItemHandler> lazyOptional = LazyOptional.of(() -> itemStackHandler);

    public TileEntityItemOutput() {
        super(TileEntityInit.ITEM_OUTPUT.get());
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        sendOutItems();
    }

    private void sendOutItems() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                itemStackHandler.extractItem(i, stack.getCount(), false);
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return lazyOptional.cast();
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return lazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (controller != null)
            compound.put("Controller", NBTUtil.writeBlockPos(controller.getPos()));
        compound.put("Inventory", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("Controller"))
            controller = world.getTileEntity(NBTUtil.readBlockPos(nbt.getCompound("Controller")));
        itemStackHandler.deserializeNBT(nbt.getCompound("Inventory"));
        super.read(state, nbt);
    }
}
