package de.rexlnico.rexltech.block;

import com.mojang.authlib.GameProfile;
import de.rexlnico.rexltech.tileentity.TileEntityMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class MinerBlock extends Block {
    public MinerBlock() {
        super(Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).setRequiresTool().harvestLevel(2).hardnessAndResistance(5).doesNotBlockMovement().sound(SoundType.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMiner();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        ((TileEntityMiner) worldIn.getTileEntity(pos)).setOwner(new GameProfile(placer.getUniqueID(), placer.getName().toString()));
    }
    
}
