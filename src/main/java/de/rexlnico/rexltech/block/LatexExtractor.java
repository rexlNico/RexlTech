package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.container.LatexExtractorContainer;
import de.rexlnico.rexltech.tileentity.TileEntityLatexExtractor;
import de.rexlnico.rexltech.utils.init.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class LatexExtractor extends BaseMachineBlock {

    public LatexExtractor() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5, 6).setRequiresTool().harvestTool(ToolType.PICKAXE));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityLatexExtractor();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            if (player.getHeldItem(handIn).getItem().equals(ItemInit.WRENCH.get())) return ActionResultType.SUCCESS;
            TileEntityLatexExtractor tileEntity = (TileEntityLatexExtractor) worldIn.getTileEntity(pos);
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("screen.rexltech.latex_extractor");
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new LatexExtractorContainer(i, worldIn, pos, playerInventory, playerEntity);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
        }

        return ActionResultType.SUCCESS;
    }

}
