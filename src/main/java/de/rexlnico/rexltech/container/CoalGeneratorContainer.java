package de.rexlnico.rexltech.container;

import de.rexlnico.rexltech.tileentity.TileEntityCoalGenerator;
import de.rexlnico.rexltech.utils.container.BasicMachineContainer;
import de.rexlnico.rexltech.utils.container.CoalGeneratorSlotItemHandler;
import de.rexlnico.rexltech.utils.init.BlockInit;
import de.rexlnico.rexltech.utils.init.ContainerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class CoalGeneratorContainer extends BasicMachineContainer {

    public CoalGeneratorContainer(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ContainerInit.COAL_GENERATOR_CONTAINER.get(), windowID, world, pos, playerInventory, playerEntity);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new CoalGeneratorSlotItemHandler(handler, 0, 80, 41));
            });
        }
        trackProgress();
    }

    public void trackProgress() {
        TileEntityCoalGenerator tile = (TileEntityCoalGenerator) tileEntity;
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.burntime & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.burntime & 0xffff0000;
                tile.burntime = energyStored + (value & 0xffff);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.burntime >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.burntime & 0x0000ffff;
                tile.burntime = energyStored + (value << 16);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tile.maxBurntime & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.maxBurntime & 0xffff0000;
                tile.maxBurntime = energyStored + (value & 0xffff);
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (tile.maxBurntime >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = tile.maxBurntime & 0x0000ffff;
                tile.maxBurntime = energyStored + (value << 16);
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, BlockInit.COAL_GENERATOR.get());
    }

    public TileEntityCoalGenerator getTileEntity(){
        return (TileEntityCoalGenerator) tileEntity;
    }

}
