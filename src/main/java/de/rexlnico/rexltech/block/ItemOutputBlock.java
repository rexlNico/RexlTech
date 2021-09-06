package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.tileentity.TileEntityItemOutput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class ItemOutputBlock extends Block {

    public ItemOutputBlock() {
        super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(2).hardnessAndResistance(5).doesNotBlockMovement().sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityItemOutput();
    }

}
