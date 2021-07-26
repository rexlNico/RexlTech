package de.rexlnico.rexltech.utils.tileentity;

import de.rexlnico.rexltech.tileentity.BaseTileEntityMachineBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.*;

public class CustomItemStackHandler extends ItemStackHandler implements IInventory {

    BaseTileEntityMachineBlock baseTileEntityMachineBlock;
    public ItemStackHandler itemStackHandler;
    Direction direction;
    public List<Integer> inputs;
    public List<Integer> outputs;
    public HashMap<Integer, ArrayList<Item>> allowedItems;

    public CustomItemStackHandler(BaseTileEntityMachineBlock baseTileEntityMachineBlock, int size, Integer[] inputs, Integer[] outputs) {
        super(size);
        this.baseTileEntityMachineBlock = baseTileEntityMachineBlock;
        this.inputs = new ArrayList<>(Arrays.asList(inputs));
        this.outputs = new ArrayList<>(Arrays.asList(outputs));
        itemStackHandler = new ItemStackHandler(stacks);
        this.allowedItems = new HashMap<>();
        for (int i = 0; i < size; i++) {
            allowedItems.put(i, new ArrayList<>());
        }
    }

    public void setDirection(Direction side) {
        this.direction = direction;
    }

    public void addAllowedItemsFromStacks(int slot, List<ItemStack> allowedItems) {
        for (ItemStack stack : allowedItems) {
            this.addAllowedItem(slot, stack.getItem());
        }
    }

    public void addAllowedItemsFromStacks(int slot, Set<ItemStack> allowedItems) {
        for (ItemStack stack : allowedItems) {
            this.addAllowedItem(slot, stack.getItem());
        }
    }

    public void addAllowedItems(int slot, List<Item> allowedItems) {
        if (!this.allowedItems.containsKey(slot)) this.allowedItems.put(slot, new ArrayList<>());
        this.allowedItems.get(slot).addAll(allowedItems);
    }

    public void addAllowedItems(int slot, Item... allowedItems) {
        if (!this.allowedItems.containsKey(slot)) this.allowedItems.put(slot, new ArrayList<>());
        this.allowedItems.get(slot).addAll(Arrays.asList(allowedItems));
    }

    public void addAllowedItems(int slot, ArrayList<Item> allowedItems) {
        if (!this.allowedItems.containsKey(slot)) this.allowedItems.put(slot, new ArrayList<>());
        this.allowedItems.get(slot).addAll(allowedItems);
    }

    public void addAllowedItem(int slot, Item allowedItem) {
        if (!this.allowedItems.containsKey(slot)) this.allowedItems.put(slot, new ArrayList<>());
        this.allowedItems.get(slot).add(allowedItem);
    }

    public void addAllowedItemFromStack(int slot, ItemStack allowedItem) {
        if (!this.allowedItems.containsKey(slot)) this.allowedItems.put(slot, new ArrayList<>());
        this.addAllowedItem(slot, allowedItem.getItem());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return allowedItems.get(slot).contains(stack.getItem());
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (direction == null) return itemStackHandler.insertItem(slot, stack, simulate);
        if (!inputs.contains(slot)) return stack;
        if (!allowedItems.get(slot).contains(stack.getItem())) return stack;
        if (!baseTileEntityMachineBlock.sideConfiguration[direction.ordinal()].input) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        itemStackHandler = new ItemStackHandler(stacks);
    }

    public NonNullList<ItemStack> getStack() {
        return stacks;
    }


    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (direction == null) return itemStackHandler.extractItem(slot, amount, simulate);
        if (!outputs.contains(slot)) return ItemStack.EMPTY;
        if (!baseTileEntityMachineBlock.sideConfiguration[direction.ordinal()].output) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSizeInventory() {
        return getSlots() - 4;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        getStackInSlot(index).shrink(count);
        return getStackInSlot(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index).copy();
        setStackInSlot(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {
        baseTileEntityMachineBlock.markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSlots() - 4; i++) {
            setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
