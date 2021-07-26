package de.rexlnico.rexltech.container;

import de.rexlnico.rexltech.tileentity.TileEntityCrusher;
import de.rexlnico.rexltech.utils.container.BasicMachineContainer;
import de.rexlnico.rexltech.utils.container.CustomOutputSlotItemHandler;
import de.rexlnico.rexltech.utils.container.CustomSlotItemHandler;
import de.rexlnico.rexltech.utils.init.BlockInit;
import de.rexlnico.rexltech.utils.init.ContainerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class CrusherContainer extends BasicMachineContainer {

    public CrusherContainer(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ContainerInit.CRUSHER_CONTAINER.get(), windowID, world, pos, playerInventory, playerEntity);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new CustomSlotItemHandler(handler, 0, 53, 34));
                addSlot(new CustomOutputSlotItemHandler(handler, 1, 107, 34));
            });
        }
        trackProgress();
    }

    public void trackProgress() {
        TileEntityCrusher tile = (TileEntityCrusher) tileEntity;
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.progress & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.progress & 0xffff0000;
                tile.progress = energyStored + (value & 0xffff);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.progress >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.progress & 0x0000ffff;
                tile.progress = energyStored + (value << 16);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.maxProgress & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.maxProgress & 0xffff0000;
                tile.maxProgress = energyStored + (value & 0xffff);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.maxProgress >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.maxProgress & 0x0000ffff;
                tile.maxProgress = energyStored + (value << 16);
            }
        });
    }

    public TileEntityCrusher getTileEntity(){
        return (TileEntityCrusher) tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, BlockInit.CRUSHER.get());
    }
}
