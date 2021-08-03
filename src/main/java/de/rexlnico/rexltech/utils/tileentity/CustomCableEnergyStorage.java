package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.util.Direction;

public abstract class CustomCableEnergyStorage extends CustomEnergyStorage {

    public Direction direction;

    public CustomCableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public CustomCableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public abstract int receiveEnergy(int maxReceive, boolean simulate);
}
