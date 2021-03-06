package de.rexlnico.rexltech.item;

import de.rexlnico.rexltech.utils.tileentity.IWrenchBreakable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

import java.util.Objects;

public class Wrench extends Item {

    public Wrench() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos()).getBlock() instanceof IWrenchBreakable) {
            if (Objects.requireNonNull(context.getPlayer()).isSneaking()) {
                context.getWorld().destroyBlock(context.getPos(), true, context.getPlayer());
            }
        }
        return super.onItemUse(context);
    }
}
