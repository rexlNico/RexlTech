package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.tileentity.BaseTileEntityEnergyCell;
import de.rexlnico.rexltech.utils.init.ItemInit;
import de.rexlnico.rexltech.utils.tileentity.IWrenchBreakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BaseEnergyCell extends Block implements IWrenchBreakable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BaseEnergyCell(Properties properties) {
        super(properties);
        setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            if (player.getHeldItem(handIn).getItem().equals(ItemInit.WRENCH.get())) {
                BaseTileEntityEnergyCell tileEntity = (BaseTileEntityEnergyCell) worldIn.getTileEntity(pos);
                tileEntity.setSideConfig(hit.getFace(), tileEntity.getNextConfiguration(hit.getFace()));
            } else {
                BaseTileEntityEnergyCell tileEntity = (BaseTileEntityEnergyCell) worldIn.getTileEntity(pos);
                StringTextComponent textComponent = new StringTextComponent("[Configuration]");
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[UP] "+tileEntity.sideConfiguration[Direction.UP.ordinal()].name());
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[DOWN] "+tileEntity.sideConfiguration[Direction.DOWN.ordinal()].name());
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[NORTH] "+tileEntity.sideConfiguration[Direction.NORTH.ordinal()].name());
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[SOUTH] "+tileEntity.sideConfiguration[Direction.SOUTH.ordinal()].name());
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[EAST] "+tileEntity.sideConfiguration[Direction.EAST.ordinal()].name());
                player.sendMessage(textComponent, null);
                textComponent = new StringTextComponent("[WEST] "+tileEntity.sideConfiguration[Direction.WEST.ordinal()].name());
                player.sendMessage(textComponent, null);
            }
        }
        return ActionResultType.SUCCESS;
    }

}
