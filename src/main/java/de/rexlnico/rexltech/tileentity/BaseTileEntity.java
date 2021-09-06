package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseTileEntity extends TileEntity implements ITickableTileEntity {

    public SideConfiguration[] sideConfiguration;
    public ArrayList<SideConfiguration> allowedSideConfigs;

    public BaseTileEntity(TileEntityType<?> tileEntityTypeIn, SideConfiguration... allowedSideConfigs) {
        super(tileEntityTypeIn);
        sideConfiguration = SideConfiguration.getDefaultSideConfig();
        this.allowedSideConfigs = new ArrayList<>(Arrays.asList(allowedSideConfigs));
        this.allowedSideConfigs.add(SideConfiguration.NONE);
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

    public void changeBurningState(boolean value) {
        BlockState blockState = world.getBlockState(pos);
        world.setBlockState(pos, blockState.with(BaseMachineBlock.BURNING, value), Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
    }

    public abstract void setTanksStack(int id, FluidStack stack);

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        SideConfiguration.writeToNBT(compound, sideConfiguration);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        sideConfiguration = SideConfiguration.readFromNBTConfig(nbt);
        super.read(state, nbt);
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        onTick();
    }

    public abstract void onTick();

    public abstract int getLightValue();

}
