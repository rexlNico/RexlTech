package de.rexlnico.rexltech.utils.item;

import de.rexlnico.rexltech.utils.tileentity.CustomSidedEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderEnergyCell implements ICapabilitySerializable<CompoundNBT> {

    IEnergyStorage energyStorage;
    LazyOptional<IEnergyStorage> lazyOptional = LazyOptional.of(() -> energyStorage);

    public CapabilityProviderEnergyCell(CustomSidedEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    public CapabilityProviderEnergyCell() {
        energyStorage = new EnergyStorage(0, 0);
    }

    public void invalidate() {
        lazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyOptional.cast();
        }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (energyStorage instanceof CustomSidedEnergyStorage) {
            return ((CustomSidedEnergyStorage) energyStorage).serializeNBT();
        }
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.isEmpty()) {
            ((CustomSidedEnergyStorage) energyStorage).deserializeNBT(nbt);
        } else {
            energyStorage = new EnergyStorage(0, 0);
        }
    }
}
