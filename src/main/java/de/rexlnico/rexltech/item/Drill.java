package de.rexlnico.rexltech.item;

import de.rexlnico.rexltech.utils.WorldUtil;
import de.rexlnico.rexltech.utils.item.EnergyItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;

public class Drill extends EnergyItem {

    private static final int energyCapacity = 250000;
    private static final int input = 25000;
    private static final int energyCostPerBlock = 500;

    public Drill() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).addToolType(ToolType.PICKAXE, 4).setNoRepair().maxDamage(energyCapacity), energyCapacity, input, 0);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        boolean toReturn = false;
        if (this.getEnergyStored(stack) >= energyCostPerBlock) {

            BlockRayTraceResult ray = WorldUtil.getNearestBlockWithDefaultReachDistance(player.world, player);
            if (!player.isSneaking()) {
                toReturn = this.breakBlocks(stack, 1, player.world, pos, ray.getFace(), player);
            } else {
                toReturn = this.breakBlocks(stack, 0, player.world, pos, ray.getFace(), player);
            }

        }
        return toReturn;
    }

    public static int getEnergyCostPerBlock() {
        return energyCostPerBlock;
    }

//    @Override
//    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
//        if(enchantment.type == EnchantmentType.VANISHABLE){
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    private boolean breakBlocks(ItemStack stack, int radius, World world, BlockPos aPos, Direction side, PlayerEntity player) {
        int xRange = radius;
        int yRange = radius;
        int zRange = 0;

        if (side.getAxis() == Direction.Axis.Y) {
            zRange = radius;
            yRange = 0;
        }
        if (side.getAxis() == Direction.Axis.X) {
            xRange = 0;
            zRange = radius;
        }

        int use = energyCostPerBlock;
        if (this.getEnergyStored(stack) >= use) {
            this.tryHarvestBlock(world, aPos, false, stack, player, use);
        }else{
            return false;
        }
        if (radius > 0) {
            for (int xPos = aPos.getX() - xRange; xPos <= aPos.getX() + xRange; xPos++) {
                for (int yPos = aPos.getY() - yRange; yPos <= aPos.getY() + yRange; yPos++) {
                    for (int zPos = aPos.getZ() - zRange; zPos <= aPos.getZ() + zRange; zPos++) {
                        if (!(aPos.getX() == xPos && aPos.getY() == yPos && aPos.getZ() == zPos)) {
                            if (this.getEnergyStored(stack) >= use) {
                                BlockPos thePos = new BlockPos(xPos, yPos, zPos);
                                this.tryHarvestBlock(world, thePos, true, stack, player, use);
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static int getEnergyCapacity() {
        return energyCapacity;
    }

    private void tryHarvestBlock(World world, BlockPos pos, boolean isExtra, ItemStack stack, PlayerEntity player, int use) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = state.getBlockHardness(world, pos);
        boolean canHarvest = (ForgeHooks.canHarvestBlock(state, player, world, pos) || this.canHarvestBlock(stack, state)) && stack.getToolTypes().contains(state.getHarvestTool());
        if (hardness >= 0.0F && (!isExtra || canHarvest && !block.hasTileEntity(world.getBlockState(pos)))) {
            if (!player.isCreative()) {
                this.extractEnergy(stack, use, false);
            }
            world.destroyBlock(pos, !player.isCreative(), player);
        }
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        return super.canHarvestBlock(stack, state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return this.getEnergyStored(stack) >= energyCostPerBlock
                ? state.getBlock().getHarvestTool(state) == null || stack.getToolTypes().contains(state.getBlock().getHarvestTool(state))
                ? 10.0f
                : 1.0F
                : 0.1F;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !newStack.isItemEqual(oldStack);
    }

}
