package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.tileentity.BaseTileEntityCable;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import java.util.List;

public class BasicEnergyCable extends BaseCable {

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BaseTileEntityCable(2500);
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("description.basic_energy_cable", 2500).setStyle(Style.EMPTY.setColor(Color.fromHex("#34ebe1"))));
    }
}
