package de.rexlnico.rexltech.utils.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public abstract class CustomCableEnergyStorage extends CustomEnergyStorage {

    public Direction direction;

    public CustomCableEnergyStorage(TileEntity tile, int capacity, int maxTransfer) {
        super(tile, capacity, maxTransfer);
    }

    public CustomCableEnergyStorage(TileEntity tile, int capacity, int maxReceive, int maxExtract) {
        super(tile, capacity, maxReceive, maxExtract);
    }

    @Override
    public abstract int receiveEnergy(int maxReceive, boolean simulate);
}
