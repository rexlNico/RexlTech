package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.EnergyStorage;

public class CustomSidedEnergyStorage extends EnergyStorage {

    private final IEnergySided energySided;
    private Direction direction;

    public CustomSidedEnergyStorage(IEnergySided sided, int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
        this.energySided = sided;
    }

    public CustomSidedEnergyStorage(IEnergySided sided,int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.energySided = sided;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(direction == null) return super.extractEnergy(maxExtract, simulate);
        if(!energySided.getSideConfiguration()[direction.ordinal()].output) return 0;
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(direction == null) return super.receiveEnergy(maxReceive, simulate);
        if(!energySided.getSideConfiguration()[direction.ordinal()].input) return 0;
        return super.receiveEnergy(maxReceive, simulate);
    }

    public int addEnergy(int energy, boolean simulate){
        int after = getEnergyStored() + energy;
        if(after <= getMaxEnergyStored()){
            if(!simulate) this.energy = after;
            return energy;
        }
        int remaining = after - getMaxEnergyStored();
        if(!simulate) this.energy = getMaxEnergyStored();
        return energy - remaining;
    }

    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("capacity", getMaxEnergyStored());
        tag.putInt("energy", getEnergyStored());
        tag.putInt("extract", maxExtract);
        tag.putInt("receive", maxReceive);
        return tag;
    }

    public void deserializeNBT(CompoundNBT nbt)
    {
        capacity = nbt.getInt("capacity");
        energy = nbt.getInt("energy");
        maxExtract = nbt.getInt("extract");
        maxReceive = nbt.getInt("receive");
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }
    public void setMaxEnergy(int capacity){
        this.capacity = capacity;
    }

    public int getMaxReceive(){
        return maxReceive;
    }

    public int getMaxExtract(){
        return maxExtract;
    }

}
