package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public int addEnergy(int energy, boolean simulate) {
        int after = getEnergyStored() + energy;
        if (after <= getMaxEnergyStored()) {
            if (!simulate) this.energy = after;
            return energy;
        }
        int remaining = after - getMaxEnergyStored();
        if (!simulate) this.energy = getMaxEnergyStored();
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

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        int before = this.maxExtract;
        this.maxExtract = Integer.MAX_VALUE;

        int toReturn = this.extractEnergy(maxExtract, simulate);

        this.maxExtract = before;
        return toReturn;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        capacity = nbt.getInt("capacity");
        energy = nbt.getInt("energy");
        maxExtract = nbt.getInt("extract");
        maxReceive = nbt.getInt("receive");
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setMaxEnergy(int capacity) {
        this.capacity = capacity;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }

}
