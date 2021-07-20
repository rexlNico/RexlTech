package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.tileentity.TileEntityBasicEnergyCell;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BasicEnergyCell extends BaseEnergyCell {

    public BasicEnergyCell() {
        super(AbstractBlock.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(6, 7).setRequiresTool().harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBasicEnergyCell();
    }

}
