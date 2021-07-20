package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.utils.tileentity.CustomSidedEnergyStorage;
import de.rexlnico.rexltech.utils.tileentity.IEnergySided;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseTileEntityEnergyCell extends TileEntity implements ITickableTileEntity, IEnergySided {

    public SideConfiguration[] sideConfiguration;
    public ArrayList<SideConfiguration> allowedSideConfigs;
    public CustomSidedEnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> energyLazyOptional = LazyOptional.of(() -> energyStorage);

    public BaseTileEntityEnergyCell(TileEntityType<?> tileEntityTypeIn, SideConfiguration[] allowedSideConfigs, int energyStorage, int energyIn, int energyOut) {
        super(tileEntityTypeIn);
        sideConfiguration = SideConfiguration.getDefaultSideConfig();
        this.allowedSideConfigs = new ArrayList<>(Arrays.asList(allowedSideConfigs));
        this.allowedSideConfigs.add(SideConfiguration.NONE);
        this.energyStorage = new CustomSidedEnergyStorage(this, energyStorage, energyIn, energyOut);
    }

    public BaseTileEntityEnergyCell(TileEntityType<?> tileEntityTypeIn, SideConfiguration[] allowedSideConfigs, int energyStorage, int energyInOut) {
        super(tileEntityTypeIn);
        sideConfiguration = SideConfiguration.getDefaultSideConfig();
        this.allowedSideConfigs = new ArrayList<>(Arrays.asList(allowedSideConfigs));
        this.allowedSideConfigs.add(SideConfiguration.NONE);
        this.energyStorage = new CustomSidedEnergyStorage(this, energyStorage, energyInOut);
    }

    @Override
    public void remove() {
        super.remove();
        energyLazyOptional.invalidate();
    }

    public void setSideConfig(Direction direction, SideConfiguration sideConfig) {
        sideConfiguration[direction.ordinal()] = sideConfig;
    }

    public SideConfiguration getNextConfiguration(Direction direction) {
        int i = 0;
        SideConfiguration side = sideConfiguration[direction.ordinal()];
        for (SideConfiguration all : allowedSideConfigs) {
            if (all == side) {
                if (i + 1 >= allowedSideConfigs.size()) {
                    return allowedSideConfigs.get(0);
                }
                return allowedSideConfigs.get(i + 1);
            }
            i++;
        }
        return SideConfiguration.NONE;
    }

    @Override
    protected void invalidateCaps() {
        energyLazyOptional.invalidate();
        super.invalidateCaps();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            energyStorage.setDirection(null);
            return energyLazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            if (sideConfiguration[side.getOpposite().ordinal()].output || sideConfiguration[side.getOpposite().ordinal()].input) {
                energyStorage.setDirection(side);
                return energyLazyOptional.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("energy", energyStorage.serializeNBT());
        SideConfiguration.writeToNBT(compound, sideConfiguration);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        energyStorage.deserializeNBT(nbt.getCompound("energy"));
        sideConfiguration = SideConfiguration.readFromNBTConfig(nbt);
        super.read(state, nbt);
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        onTick();
        sendOutPower();
        markDirty();
    }

    @Override
    public SideConfiguration[] getSideConfiguration() {
        return sideConfiguration;
    }

    public abstract void onTick();

    public void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                if (sideConfiguration[direction.ordinal()].output) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).map(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(capacity.get(), energyStorage.getMaxExtract()), false);
                                capacity.addAndGet(-received);
                                energyStorage.extractEnergy(received, false);
                                markDirty();
                                return capacity.get() > 0;
                            } else return true;
                        }).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        }
    }

}
