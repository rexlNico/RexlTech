package de.rexlnico.rexltech.tileentity;

import de.rexlnico.rexltech.block.CoalGenerator;
import de.rexlnico.rexltech.config.RexlTechGeneratorConfig;
import de.rexlnico.rexltech.utils.init.TileEntityInit;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraftforge.common.ForgeHooks;

public class TileEntityCoalGenerator extends BaseTileEntityMachineBlock {

    public TileEntityCoalGenerator() {
        super(TileEntityInit.COAL_GENERATOR_TILE_ENTITY.get(), 1, new Integer[]{0}, new Integer[]{}, new SideConfiguration[]{SideConfiguration.INPUT}, RexlTechGeneratorConfig.coal_generator_storage.get(), 0, RexlTechGeneratorConfig.coal_generator_output.get());
        for (Item item : AbstractFurnaceTileEntity.getBurnTimes().keySet()) {
            if (item.getRegistryName().toString().contains("coal")) {
                handler.addAllowedItem(0, item);
            }
        }
    }

    public int burntime = 0;
    public int maxBurntime = 0;

    @Override
    public void onTick() {
        if (burntime > 0) {
            int added = energyStorage.addEnergy(RexlTechGeneratorConfig.coal_generator_production.get(), true);
            if (added == RexlTechGeneratorConfig.coal_generator_production.get()) {
                burntime--;
                energyStorage.addEnergy(RexlTechGeneratorConfig.coal_generator_production.get(), false);
            }
        }
        if (burntime <= 0) {
            if (!handler.getStackInSlot(0).isEmpty()) {
                ItemStack itemStack = handler.getStackInSlot(0);
                System.out.println(itemStack.getBurnTime());
                if (ForgeHooks.getBurnTime(itemStack) > 0 && itemStack.getItem().getRegistryName().toString().contains("coal")) {
                    maxBurntime = ForgeHooks.getBurnTime(itemStack);
                    burntime = maxBurntime;
                    handler.getStackInSlot(0).shrink(1);
                }
            }
        }
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(CoalGenerator.BURNING) != burntime > 0) {
            changeBurningState(burntime > 0);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("burn_time", burntime);
        compound.putInt("max_burn_time", maxBurntime);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        burntime = nbt.getInt("burn_time");
        maxBurntime = nbt.getInt("max_burn_time");
        super.read(state, nbt);
    }

}
