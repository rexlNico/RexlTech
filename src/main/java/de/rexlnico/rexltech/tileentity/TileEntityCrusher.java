package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.Crusher;
import de.rexlnico.rexltech.item.BaseUpgradeItem;
import de.rexlnico.rexltech.item.recipe.CrusherRecipe;
import de.rexlnico.rexltech.utils.init.ItemInit;
import de.rexlnico.rexltech.utils.init.RecipeInit;
import de.rexlnico.rexltech.utils.init.SoundInit;
import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntityCrusher extends BaseTileEntityMachineBlock {

    public int progress;
    public int maxProgress;
    public static final int basEnergyUsage = 100;

    public TileEntityCrusher() {
        super(TileEntityInit.CRUSHER.get(), 2, new Integer[]{0}, new Integer[]{1}, new SideConfiguration[]{SideConfiguration.INPUT, SideConfiguration.OUTPUT, SideConfiguration.IN_AND_OUT}, 100000, 10000, 0);
    }

    @Override
    public void setWorldAndPos(World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);
        for (CrusherRecipe crusherRecipe : world.getRecipeManager().getRecipesForType(RecipeInit.CRUSHING_TYPE)) {
            handler.addAllowedItem(0, crusherRecipe.getIngredients().get(0).getMatchingStacks()[0].getItem());
        }
    }

    @Override
    public void onTick() {

        if (!handler.getStackInSlot(0).isEmpty()) {
            CrusherRecipe recipe = getRecipe();
            if (recipe == null) return;
            ItemStack currentOutput = handler.getStackInSlot(1);
            ItemStack newOutput = getOutput(recipe);
            if (!currentOutput.isEmpty()) {
                if (currentOutput.getItem() == newOutput.getItem()) {
                    int newCount = currentOutput.getCount() + newOutput.getCount();
                    if (newCount <= newOutput.getMaxStackSize()) {
                        if (progress <= 0) {
                            if (!handler.getStackInSlot(0).isEmpty()) {
                                maxProgress = recipe.processTime;
                                progress = maxProgress;
                            } else {
                                maxProgress = 0;
                                progress = 0;
                            }
                        }
                        if (progress > 0) {
                            int usage = getEnergy(basEnergyUsage);
                            int added = energyStorage.extractEnergyInternal(usage, true);
                            if (added == usage) {
                                progress = progress - getTicks(1);
                                energyStorage.extractEnergyInternal(usage, false);
                            }
                        }
                        if (progress <= 0) {
                            handler.getStackInSlot(0).shrink(1);
                            handler.getStackInSlot(1).grow(newOutput.getCount());
                            maxProgress = 0;
                            progress = 0;
                            if (!handler.getStackInSlot(0).isEmpty()) {
                                maxProgress = recipe.processTime;
                                progress = maxProgress;
                            }
                        }
                    }
                }
            } else {
                int newCount = newOutput.getCount();
                if (newCount <= newOutput.getMaxStackSize()) {
                    if (progress <= 0) {
                        if (!handler.getStackInSlot(0).isEmpty()) {
                            maxProgress = recipe.processTime;
                            progress = maxProgress;
                        } else {
                            maxProgress = 0;
                            progress = 0;
                        }
                    }
                    if (progress > 0) {
                        int usage = getEnergy(basEnergyUsage);
                        int added = energyStorage.extractEnergyInternal(usage, true);
                        if (added == usage) {
                            progress = progress - getTicks(1);
                            energyStorage.extractEnergyInternal(usage, false);
                        }
                    }
                    if (progress <= 0) {
                        handler.getStackInSlot(0).shrink(1);
                        handler.setStackInSlot(1, newOutput);
                        maxProgress = 0;
                        progress = 0;
                        if (!handler.getStackInSlot(0).isEmpty()) {
                            maxProgress = recipe.processTime;
                            progress = maxProgress;
                        }
                    }
                }
            }
        } else {
            if (progress > 0 || maxProgress > 0) {
                progress = 0;
                maxProgress = 0;
            }
        }
        if (energyStorage.getEnergyStored() != energyStorage.getMaxEnergyStored() && getEnergy(basEnergyUsage) == 0) {
            energyStorage.setEnergy(energyStorage.getMaxEnergyStored());
        }
        if (progress > 0) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundInit.CRUSHER, SoundCategory.BLOCKS, 1, 1, true);
        }
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(Crusher.BURNING) != progress > 0) {
            changeBurningState(progress > 0);
        }
    }

    public CrusherRecipe getRecipe() {
        if (world == null || handler.getStackInSlot(0).isEmpty()) return null;
        return world.getRecipeManager().getRecipe(RecipeInit.CRUSHING_TYPE, handler, world).orElse(null);
    }

    private ItemStack getOutput(CrusherRecipe recipe) {
        if (recipe != null) {
            return recipe.getCraftingResult(handler);
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public BaseUpgradeItem[] getAllowedAddons() {
        return new BaseUpgradeItem[]{ItemInit.SPEED_UPGRADE.get(), ItemInit.CREATIVE_ENERGY_UPGRADE.get()};
    }

    @Override
    public int getLightValue() {
        BlockState blockState = world.getBlockState(pos);
        return blockState.get(Crusher.BURNING) ? 5 : 0;
    }

}
