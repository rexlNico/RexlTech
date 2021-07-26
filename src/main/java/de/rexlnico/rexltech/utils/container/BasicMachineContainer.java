package de.rexlnico.rexltech.utils.container;

import de.rexlnico.rexltech.tileentity.BaseTileEntityMachineBlock;
import de.rexlnico.rexltech.utils.tileentity.CustomEnergyStorage;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Direction;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class BasicMachineContainer extends Container {

    public BaseTileEntityMachineBlock tileEntity;
    public PlayerEntity playerEntity;
    public IItemHandler playerInventory;

    protected BasicMachineContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(type, id);
        tileEntity = (BaseTileEntityMachineBlock) world.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(this::addConfigSlots);
        }
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 84);
        trackPower();
        trackSideConfig();
    }

    public void addConfigSlots(IItemHandler handler) {
        int slots = handler.getSlots();
        addSlot(new AddonSlotItemHandler(handler, tileEntity.getAllowedAddonsList(), slots - 4, -24, 7));
        addSlot(new AddonSlotItemHandler(handler, tileEntity.getAllowedAddonsList(), slots - 3, -24, 28));
        addSlot(new AddonSlotItemHandler(handler, tileEntity.getAllowedAddonsList(), slots - 2, -24, 49));
        addSlot(new AddonSlotItemHandler(handler, tileEntity.getAllowedAddonsList(), slots - 1, -24, 70));
    }

    public void trackSideConfig() {
        for (Direction direction : Direction.values()) {
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return getTileEntity().sideConfiguration[direction.ordinal()].ordinal() & 0xffff;
                }

                @Override
                public void set(int value) {
                    getTileEntity().sideConfiguration[direction.ordinal()] = SideConfiguration.values()[value & 0xffff];
                }
            });
        }
    }

    public void trackPower() {
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergy() & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getEnergyStored() & 0xffff0000;
                    ((CustomEnergyStorage) handler).setEnergy(energyStored + (value & 0xffff));
                });
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (getEnergy() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getEnergyStored() & 0x0000ffff;
                    ((CustomEnergyStorage) handler).setEnergy(energyStored + (value << 16));
                });
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getMaxEnergy() & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getMaxEnergyStored() & 0xffff0000;
                    ((CustomEnergyStorage) handler).setMaxEnergy(energyStored + (value & 0xffff));
                });
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (getMaxEnergy() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(handler -> {
                    int energyStored = handler.getMaxEnergyStored() & 0x0000ffff;
                    ((CustomEnergyStorage) handler).setMaxEnergy(energyStored + (value << 16));
                });
            }
        });
    }

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getMaxEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }

    @Override
    public abstract boolean canInteractWith(PlayerEntity playerIn);

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    public void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public abstract BaseTileEntityMachineBlock getTileEntity();

}
