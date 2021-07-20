package de.rexlnico.rexltech.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BaseEnergyCellBlockItem extends BlockItem {

    public BaseEnergyCellBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder.maxStackSize(1));
    }

//    @Nullable
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
//        return new CapabilityProviderEnergyCell();
//    }

}
