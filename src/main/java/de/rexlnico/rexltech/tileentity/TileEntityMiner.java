package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.item.BaseUpgradeItem;
import de.rexlnico.rexltech.utils.init.ItemInit;
import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TileEntityMiner extends BaseTileEntityMachineBlock {

    private final static ItemStack TOOL = new ItemStack(Items.NETHERITE_PICKAXE);

    public BlockPos miningPos;
    public int cooldown;
    public static final int basEnergyUsageBreak = 50;
    public static final int basEnergyUsageMove = 20;
    public static final int baseEnergyIdle = 5;

    public TileEntityMiner() {
        super(TileEntityInit.MINER.get(), 18, new Integer[]{}, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17}, new SideConfiguration[]{SideConfiguration.OUTPUT}, 2500000, 250000, 0);
    }

    @Override
    public void setWorldAndPos(World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);
        if (miningPos == null)
            miningPos = Objects.requireNonNull(world).getChunk(pos).getPos().asBlockPos().add(0, pos.getY(), 0);
    }

    @Override
    public void onTick() {
        if (miningPos == null || isFinished()) return;
        if (energyStorage.getEnergyStored() != energyStorage.getMaxEnergyStored() && getEnergy(basEnergyUsageBreak) == 0 && getEnergy(basEnergyUsageMove) == 0 && getEnergy(baseEnergyIdle) == 0) {
            energyStorage.setEnergy(energyStorage.getMaxEnergyStored());
        }
        if (cooldown > 0) {
            if (energyStorage.getEnergyStored() > getEnergy(baseEnergyIdle)) {
                cooldown -= getTicks(1);
                energyStorage.extractEnergyInternal(getEnergy(baseEnergyIdle), false);
            }
        } else {
            if (Objects.requireNonNull(world).getBlockState(miningPos).getBlock() == Blocks.AIR) {
                if (energyStorage.getEnergyStored() > getEnergy(basEnergyUsageMove)) {
                    energyStorage.extractEnergyInternal(getEnergy(basEnergyUsageMove), false);
                    setNewPosition();
                    cooldown += 2;
                }
            } else if (world.getBlockState(miningPos).getBlock().getTags().contains(Tags.Blocks.ORES.getName())) {
                if (energyStorage.getEnergyStored() > getEnergy(basEnergyUsageBreak)) {
                    List<ItemStack> blockLoot = getBlockLoot(miningPos);
                    if (handler.canFitItemsInInventory(blockLoot)) {
                        energyStorage.extractEnergyInternal(getEnergy(basEnergyUsageBreak), false);
                        handler.insertItemsInternal(blockLoot);
                        breakBlock(miningPos);
                        setNewPosition();
                        cooldown += 10;
                    }
                }
            } else {
                if (energyStorage.getEnergyStored() > getEnergy(basEnergyUsageMove)) {
                    energyStorage.extractEnergyInternal(getEnergy(basEnergyUsageMove), false);
                    setNewPosition();
                    cooldown += 5;
                }
            }
        }
    }

    public int getFortuneLevel() {
        int luck = 0;
        for (int i = handler.getSlots() - 4; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty() && handler.getStackInSlot(i).getItem() == ItemInit.LUCK_UPGRADE.get()) {
                luck += 1;
            }
        }
        return Math.min(3, luck);
    }

    public boolean hasSilkTouch() {
        for (int i = handler.getSlots() - 4; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty() && handler.getStackInSlot(i).getItem() == ItemInit.SILK_TOUCH_UPGRADE.get()) {
                return true;
            }
        }
        return false;
    }

    public int getMiningHeight() {
        return miningPos.getY();
    }

    public void setMiningHeight(int height) {
        miningPos = new BlockPos(miningPos.getX(), height, miningPos.getZ());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT posCompound = new CompoundNBT();
        posCompound.putInt("x", miningPos.getX());
        posCompound.putInt("y", miningPos.getY());
        posCompound.putInt("z", miningPos.getZ());
        compound.put("miningPos", posCompound);
        return super.write(compound);
    }

    public ItemStack getTool() {
        ItemStack copy = TOOL.copy();
        int fortune = getFortuneLevel();
        boolean silkTouch = hasSilkTouch();
        if (fortune > 0 && !silkTouch) {
            copy.addEnchantment(Enchantments.FORTUNE, fortune);
        } else if (silkTouch) {
            copy.addEnchantment(Enchantments.SILK_TOUCH, 1);
        }
        return copy;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        CompoundNBT posCompound = nbt.getCompound("miningPos");
        miningPos = new BlockPos(posCompound.getInt("x"), posCompound.getInt("y"), posCompound.getInt("z"));
        super.read(state, nbt);
    }

    public List<ItemStack> getBlockLoot(BlockPos pos) {
        if (!Objects.requireNonNull(world).isRemote) {
            return world.getBlockState(pos).getDrops(new LootContext.Builder((ServerWorld) world).withRandom(world.getRandom()).withParameter(LootParameters.ORIGIN, Vector3d.ZERO).withParameter(LootParameters.TOOL, getTool()));
        }
        return new ArrayList<>();
    }

    public void breakBlock(BlockPos pos) {
        if (!Objects.requireNonNull(world).isRemote) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public void setNewPosition() {
        BlockPos end = Objects.requireNonNull(world).getChunk(pos).getPos().asBlockPos().add(16, 0, 16);
        if (miningPos != end) {
            if (miningPos.getX() < end.getX()) {
                miningPos = miningPos.add(1, 0, 0);
            } else {
                if (miningPos.getZ() < end.getZ()) {
                    miningPos = miningPos.add(-16, 0, 1);
                } else {
                    miningPos = miningPos.add(-16, -1, -16);
                }
            }
        }
    }

    public boolean isFinished() {
        return miningPos == Objects.requireNonNull(world).getChunk(pos).getPos().asBlockPos().add(16, 0, 16);
    }

    @Nonnull
    @Override
    public BaseUpgradeItem[] getAllowedAddons() {
        return new BaseUpgradeItem[]{ItemInit.SPEED_UPGRADE.get(), ItemInit.CREATIVE_ENERGY_UPGRADE.get(), ItemInit.LUCK_UPGRADE.get(), ItemInit.SILK_TOUCH_UPGRADE.get()};
    }

    @Override
    public int getLightValue() {
        BlockState blockState = Objects.requireNonNull(world).getBlockState(pos);
        return blockState.get(BaseMachineBlock.BURNING) ? 3 : 0;
    }

}
