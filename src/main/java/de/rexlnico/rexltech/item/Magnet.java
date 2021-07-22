package de.rexlnico.rexltech.item;

import de.rexlnico.rexltech.utils.item.EnergyItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class Magnet extends EnergyItem {

    private static final int energyCapacity = 250000;
    private static final int input = 25000;
    private static final int energyCostTick = 50;
    private static final int range = 5;

    public Magnet() {
        super(new Properties().group(ItemGroup.TOOLS).maxStackSize(1).setNoRepair().maxDamage(energyCapacity), energyCapacity, input, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof PlayerEntity && !world.isRemote) {
                PlayerEntity player = (PlayerEntity) entity;
                List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(entity.getPosX() - range, entity.getPosY() - range, entity.getPosZ() - range, entity.getPosX() + range, entity.getPosY() + range, entity.getPosZ() + range));
                for (ItemEntity item : items) {
                    if (item.getPersistentData().getBoolean("PreventRemoteMovement")) {
                        continue;
                    }
                    if (item.isAlive() && !item.cannotPickup()) {
                        int energyForItem = energyCostTick * item.getItem().getCount();

                        if (this.getEnergyStored(stack) >= energyForItem) {
                            ItemStack oldItem = item.getItem().copy();

                            item.onCollideWithPlayer(player);

                            if (!player.isCreative()) {
                                if (!item.isAlive() || !ItemStack.areItemStacksEqual(item.getItem(), oldItem)) {
                                    this.extractEnergy(stack, energyForItem, false);
                                }
                            }
                        }
                    }
                }
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    public static int getEnergyCapacity() {
        return energyCapacity;
    }


    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return super.canHarvestBlock(stack, state);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !newStack.isItemEqual(oldStack);
    }

}
