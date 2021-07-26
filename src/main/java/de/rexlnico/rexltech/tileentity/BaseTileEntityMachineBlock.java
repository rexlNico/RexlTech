package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.item.BaseUpgradeItem;
import de.rexlnico.rexltech.utils.tileentity.CustomEnergyStorage;
import de.rexlnico.rexltech.utils.tileentity.CustomItemStackHandler;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseTileEntityMachineBlock extends TileEntity implements ITickableTileEntity {

    public SideConfiguration[] sideConfiguration;
    public ArrayList<SideConfiguration> allowedSideConfigs;
    public CustomItemStackHandler handler;
    public CustomEnergyStorage energyStorage;
    private LazyOptional<ItemStackHandler> sidedLazyOptional = LazyOptional.of(() -> handler);
    private LazyOptional<ItemStackHandler> lazyOptional = LazyOptional.of(() -> handler.itemStackHandler);
    private LazyOptional<IEnergyStorage> energyLazyOptional = LazyOptional.of(() -> energyStorage);
    public boolean addons = true;

    public BaseTileEntityMachineBlock(TileEntityType<?> tileEntityTypeIn, int slots, Integer[] inputs, Integer[] outputs, SideConfiguration[] allowedSideConfigs, int energyStorage, int energyIn, int energyOut) {
        super(tileEntityTypeIn);
        handler = new CustomItemStackHandler(this, slots + 4, inputs, outputs);
        sideConfiguration = SideConfiguration.getDefaultSideConfig();
        this.allowedSideConfigs = new ArrayList<>(Arrays.asList(allowedSideConfigs));
        this.allowedSideConfigs.add(SideConfiguration.NONE);
        this.energyStorage = new CustomEnergyStorage(energyStorage, energyIn, energyOut);
    }

    public BaseTileEntityMachineBlock(TileEntityType<?> tileEntityTypeIn, int slots, boolean addons, Integer[] inputs, Integer[] outputs, SideConfiguration[] allowedSideConfigs, int energyStorage, int energyIn, int energyOut) {
        super(tileEntityTypeIn);
        this.addons = addons;
        handler = new CustomItemStackHandler(this, slots + (this.addons ? 4 : 0), inputs, outputs);
        sideConfiguration = SideConfiguration.getDefaultSideConfig();
        this.allowedSideConfigs = new ArrayList<>(Arrays.asList(allowedSideConfigs));
        this.allowedSideConfigs.add(SideConfiguration.NONE);
        this.energyStorage = new CustomEnergyStorage(energyStorage, energyIn, energyOut);
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

    public int getTicks(int baseRemove) {
        int remove = baseRemove;
        for (int i = handler.getSlots() - 4; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                remove = BaseUpgradeItem.getTickTimeRemove(remove, handler.getStackInSlot(i));
            }
        }
        return remove;
    }

    public int getEnergy(int baseEnergy) {
        int remove = baseEnergy;
        for (int i = handler.getSlots() - 4; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                remove = BaseUpgradeItem.getEnergyUsage(remove, handler.getStackInSlot(i));
            }
        }
        return remove;
    }

    @Override
    protected void invalidateCaps() {
        lazyOptional.invalidate();
        sidedLazyOptional.invalidate();
        energyLazyOptional.invalidate();
        super.invalidateCaps();
    }

    public void changeBurningState(boolean value) {
        BlockState blockState = world.getBlockState(pos);
        world.setBlockState(pos, blockState.with(BaseMachineBlock.BURNING, value), Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            handler.setDirection(null);
            return lazyOptional.cast();
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyLazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (sideConfiguration[side.ordinal()].input || sideConfiguration[side.ordinal()].output) {
                handler.setDirection(side);
                return sidedLazyOptional.cast();
            }
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("addons", addons);
        compound.put("inventory", handler.serializeNBT());
        compound.put("energy", energyStorage.serializeNBT());
        SideConfiguration.writeToNBT(compound, sideConfiguration);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        addons = nbt.getBoolean("addons");
        handler.deserializeNBT(nbt.getCompound("inventory"));
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

    public abstract void onTick();

    public void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
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

    @Nullable
    public abstract Item[] getAllowedAddons();

    public List<Item> getAllowedAddonsList() {
        return Arrays.asList(getAllowedAddons());
    }

    public abstract int getLightValue();

}
