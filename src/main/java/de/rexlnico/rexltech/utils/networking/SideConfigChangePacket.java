package de.rexlnico.rexltech.utils.networking;

import de.rexlnico.rexltech.tileentity.BaseTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SideConfigChangePacket {

    private BlockPos blockPos;
    private int side;

    public SideConfigChangePacket(PacketBuffer buf) {
        blockPos = buf.readBlockPos();
        side = buf.readInt();
    }

    public SideConfigChangePacket(BlockPos pos, int side) {
        this.blockPos = pos;
        this.side = side;
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(side);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender().world.getTileEntity(blockPos) instanceof BaseTileEntity) {
                BaseTileEntity tileEntity = (BaseTileEntity) ctx.get().getSender().world.getTileEntity(blockPos);
                tileEntity.sideConfiguration[side] = tileEntity.getNextConfiguration(Direction.values()[side]);
                tileEntity.markDirty();
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
