package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.CustomCableEnergyStorage;
import de.rexlnico.rexltech.utils.tileentity.EnergyProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class BaseTileEntityCable extends TileEntity {

    public final Map<Direction, EnergyProxy> proxyMap = new HashMap<>();
    public final Set<Direction> energySides = new HashSet<>();
    public CustomCableEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyOptionalEnergy = LazyOptional.of(() -> energyStorage);

    public BaseTileEntityCable(int transfer) {
        super(TileEntityInit.ENERGY_CABLE.get());
        this.energyStorage = new CustomCableEnergyStorage(transfer, transfer) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                markDirty();
                return BaseTileEntityCable.this.receiveEnergy(maxReceive, simulate);
            }
        };
        for (Direction side : Direction.values()) {
            this.proxyMap.put(side, new EnergyProxy());
        }
    }

    public BaseTileEntityCable() {
        super(TileEntityInit.ENERGY_CABLE.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        this.proxyMap.forEach((direction, linkedCables) -> {
            CompoundNBT nbt = new CompoundNBT();
            linkedCables.write(nbt);
            nbt.putInt("direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("linked_cables", list);
        compound.put("energy", energyStorage.serializeNBT());
        return super.write(compound);
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        Direction direction = energyStorage.direction;
        if (this.world == null || world.isRemote || direction == null || !energyStorage.canReceive())
            return 0;
        int received = 0;
        received += pushEnergy(this.world, maxReceive, simulate, direction, this);
        for (BlockPos cablePos : this.proxyMap.get(direction).cables()) {
            int amount = maxReceive - received;
            if (amount <= 0) break;
            TileEntity cableTile = this.world.getTileEntity(cablePos);
            if (cableTile instanceof BaseTileEntityCable) {
                BaseTileEntityCable cable = (BaseTileEntityCable) cableTile;
                received += cable.pushEnergy(this.world, amount, simulate, direction, this);
            }
        }
        return received;
    }

    private int pushEnergy(World world, int maxReceive, boolean simulate, Direction direction, BaseTileEntityCable cable) {
        int received = 0;
        for (Direction side : this.energySides) {
            int amount = Math.min(maxReceive - received, energyStorage.getMaxExtract());
            if (amount <= 0) break;
            if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side)) continue;
            BlockPos pos = this.pos.offset(side);
            if (direction != null && cable.getPos().offset(direction).equals(pos)) continue;
            TileEntity tile = world.getTileEntity(pos);
            if (tile.getCapability(CapabilityEnergy.ENERGY).map((IEnergyStorage::canReceive)).orElse(false)) {
                received += tile.getCapability(CapabilityEnergy.ENERGY).map(iEnergyStorage -> iEnergyStorage.receiveEnergy(amount, simulate)).orElse(0);
            }
        }
        return received;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        ListNBT list = compound.getList("linked_cables", Constants.NBT.TAG_COMPOUND);
        IntStream.range(0, list.size()).mapToObj(list::getCompound).forEach(nbt -> {
            Direction direction = Direction.values()[nbt.getInt("direction")];
            this.proxyMap.put(direction, new EnergyProxy().read(nbt));
        });
        energyStorage.deserializeNBT(compound.getCompound("energy"));
    }

    public void search(Block block, Direction side) {
        this.proxyMap.get(side).search(block, this, side).clear();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY && energyStorage != null) {
            energyStorage.direction = side;
            return this.lazyOptionalEnergy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        this.lazyOptionalEnergy.invalidate();
    }

    public boolean canExtractEnergy(Direction side) {
        return side != null || energyStorage.canExtract();
    }

}
