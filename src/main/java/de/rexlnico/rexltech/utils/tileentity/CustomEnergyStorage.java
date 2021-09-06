package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    private TileEntity tile;

    public CustomEnergyStorage(TileEntity tile, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.tile = tile;
    }

    public CustomEnergyStorage(TileEntity tile, int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.tile = tile;
    }

    public int addEnergy(int energy, boolean simulate) {
        int after = getEnergyStored() + energy;
        if (after <= getMaxEnergyStored()) {
            if (!simulate) this.energy = after;
            return energy;
        }
        int remaining = after - getMaxEnergyStored();
        if (!simulate) this.energy = getMaxEnergyStored();
        markDirty();
        return energy - remaining;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("capacity", getMaxEnergyStored());
        tag.putInt("energy", getEnergyStored());
        tag.putInt("extract", maxExtract);
        tag.putInt("receive", maxReceive);
        return tag;
    }

    public void markDirty() {
        tile.markDirty();
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        int before = this.maxExtract;
        this.maxExtract = Integer.MAX_VALUE;

        int toReturn = this.extractEnergy(maxExtract, simulate);

        this.maxExtract = before;
        markDirty();
        return toReturn;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        markDirty();
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        markDirty();
        return super.extractEnergy(maxExtract, simulate);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        capacity = nbt.getInt("capacity");
        energy = nbt.getInt("energy");
        maxExtract = nbt.getInt("extract");
        maxReceive = nbt.getInt("receive");
    }

    public void setEnergy(int energy) {
        markDirty();
        this.energy = energy;
    }

    public void setMaxEnergy(int capacity) {
        markDirty();
        this.capacity = capacity;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

}
