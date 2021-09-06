package de.rexlnico.rexltech.utils.networking;

import de.rexlnico.rexltech.tileentity.BaseTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidTankUpdatePacket {

    private BlockPos blockPos;
    private int tankID;
    private FluidStack fluidStack;

    public FluidTankUpdatePacket(PacketBuffer buf) {
        blockPos = buf.readBlockPos();
        tankID = buf.readInt();
        fluidStack = FluidStack.loadFluidStackFromNBT(buf.readCompoundTag());
    }

    public FluidTankUpdatePacket(BlockPos pos, FluidStack fluidStack) {
        this.blockPos = pos;
        this.tankID = 1;
        this.fluidStack = fluidStack;
    }

    public FluidTankUpdatePacket(BlockPos pos, int tankID, FluidStack fluidStack) {
        this.blockPos = pos;
        this.tankID = tankID;
        this.fluidStack = fluidStack;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(tankID);
        buf.writeCompoundTag(fluidStack.writeToNBT(new CompoundNBT()));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world.getTileEntity(blockPos) instanceof BaseTileEntity) {
                BaseTileEntity tileEntity = (BaseTileEntity) Minecraft.getInstance().world.getTileEntity(blockPos);
                tileEntity.setTanksStack(tankID, fluidStack);
                tileEntity.markDirty();
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
