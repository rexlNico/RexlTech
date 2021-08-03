package de.rexlnico.rexltech.block;

import de.rexlnico.rexltech.tileentity.BaseTileEntityCable;
import de.rexlnico.rexltech.utils.tileentity.IWrenchBreakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.*;

public abstract class BaseCable extends Block implements IWrenchBreakable {
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty TILE = BooleanProperty.create("tile");
    private static final VoxelShape CABLE = makeCuboidShape(6.25, 6.25, 6.25, 9.75, 9.75, 9.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[]{makeCuboidShape(6.5, 6.5, 0, 9.5, 9.5, 7), makeCuboidShape(9.5, 6.5, 6.5, 16, 9.5, 9.5), makeCuboidShape(6.5, 6.5, 9.5, 9.5, 9.5, 16), makeCuboidShape(0, 6.5, 6.5, 6.5, 9.5, 9.5), makeCuboidShape(6.5, 9.5, 6.5, 9.5, 16, 9.5), makeCuboidShape(6.5, 0, 6.5, 9.5, 7, 9.5)};

    public BaseCable() {
        super(Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(5, 5).harvestTool(ToolType.PICKAXE).harvestLevel(0).setRequiresTool());
        setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false).with(TILE, false));
    }

    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(TILE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelShape = CABLE;
        if (state.get(NORTH) || canConnectEnergy(world, pos, Direction.NORTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[0]);
        if (state.get(EAST) || canConnectEnergy(world, pos, Direction.EAST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[1]);
        if (state.get(SOUTH) || canConnectEnergy(world, pos, Direction.SOUTH))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[2]);
        if (state.get(WEST) || canConnectEnergy(world, pos, Direction.WEST))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[3]);
        if (state.get(UP) || canConnectEnergy(world, pos, Direction.UP))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[4]);
        if (state.get(DOWN) || canConnectEnergy(world, pos, Direction.DOWN))
            voxelShape = VoxelShapes.or(voxelShape, MULTIPART[5]);
        return voxelShape;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        TileEntity tileEntity = world.getTileEntity(currentPos);
        if (tileEntity instanceof BaseTileEntityCable) {
            BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, currentPos, direction)) {
                    cable.energySides.add(direction);
                }
            }
        }

        return createCableState(world, currentPos);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);

    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return createCableState(context.getWorld(), context.getPos());
    }

    private BlockState createCableState(IWorld world, BlockPos pos) {
        final BlockState state = getDefaultState();
        boolean[] north = canAttach(state, world, pos, Direction.NORTH);
        boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
        boolean[] west = canAttach(state, world, pos, Direction.WEST);
        boolean[] east = canAttach(state, world, pos, Direction.EAST);
        boolean[] up = canAttach(state, world, pos, Direction.UP);
        boolean[] down = canAttach(state, world, pos, Direction.DOWN);
        boolean tile = false;
        if (north[0] || south[0] || west[0] || east[0] || up[0] || down[0]) {
            tile = true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof BaseTileEntityCable) {
                BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
                cable.remove();
            }
        }
        return state.with(NORTH, north[0]).with(SOUTH, south[0]).with(WEST, west[0]).with(EAST, east[0]).with(UP, up[0]).with(DOWN, down[0]).with(TILE, tile);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        super.onPlayerDestroy(worldIn, pos, state);
        if (worldIn.getTileEntity(pos) != null) worldIn.getTileEntity(pos).remove();
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        super.onExplosionDestroy(worldIn, pos, explosionIn);
        if (worldIn.getTileEntity(pos) != null) worldIn.getTileEntity(pos).remove();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (world.isRemote) return;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof BaseTileEntityCable) {
            BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
            for (Direction direction : Direction.values()) {
                if (cable.canExtractEnergy(direction)) {
                    cable.search(this, direction);
                }
            }
        } else {
            findCables(world, pos, pos);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof BaseTileEntityCable) {
            BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, pos, direction)) {
                    cable.energySides.add(direction);
                }
            }
        }
        super.onBlockAdded(state, world, pos, oldState, isMoving);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        findCables(world, pos, pos);
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    public boolean[] canAttach(BlockState state, IWorld world, BlockPos pos, Direction direction) {
        return new boolean[]{world.getBlockState(pos.offset(direction)).getBlock() == this || canConnectEnergy(world, pos, direction), canConnectEnergy(world, pos, direction)};
    }

    public boolean canConnectEnergy(IBlockReader world, BlockPos pos, Direction direction) {
        try {
            TileEntity tile = world.getTileEntity(pos.offset(direction));
            if (tile == null) return false;
            return (tile instanceof BaseTileEntityCable) || tile.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(state, worldIn, pos, context);
    }

    static final Map<BlockPos, Set<BlockPos>> CACHE = new HashMap<>();

    public void searchCables(IWorld world, BlockPos pos, BaseTileEntityCable first, Direction side) {
        if (!first.proxyMap.get(side).searchCache.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                if (blockPos.equals(first.getPos())) continue;
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof BaseTileEntityCable) {
                        BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
                        first.proxyMap.get(side).add(blockPos);
                    }
                    BaseCable cableBlock = (BaseCable) state.getBlock();
                    first.proxyMap.get(side).searchCache.add(pos);
                    cableBlock.searchCables(world, blockPos, first, side);
                }
            }
        }
    }

    public void findCables(IWorld world, BlockPos poss, BlockPos pos) {
        Set<BlockPos> ss = CACHE.get(poss);
        if (ss == null) {
            ss = new HashSet<>();
        }
        if (!ss.contains(pos)) {
            for (Direction direction : Direction.values()) {
                BlockPos blockPos = pos.offset(direction);
                BlockState state = world.getBlockState(blockPos);
                if (state.getBlock() == this) {
                    TileEntity tileEntity = world.getTileEntity(blockPos);
                    if (tileEntity instanceof BaseTileEntityCable) {
                        BaseTileEntityCable cable = (BaseTileEntityCable) tileEntity;
                        for (Direction side : Direction.values()) {
                            cable.proxyMap.get(side).cables().clear();
                            cable.search(this, side);
                        }
                    }
                    BaseCable cableBlock = (BaseCable) state.getBlock();
                    ss.add(pos);
                    CACHE.put(poss, ss);
                    cableBlock.findCables(world, poss, blockPos);
                }
            }
        }
        CACHE.clear();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        VoxelShape voxelshape = CABLE;
        voxelshape.forEachBox((x, y, z, xx, yy, zz) -> {
            double d1 = Math.min(1.0D, xx - x);
            double d2 = Math.min(1.0D, yy - y);
            double d3 = Math.min(1.0D, zz - z);
            int i = Math.max(2, MathHelper.ceil(d1 / 0.25D));
            int j = Math.max(2, MathHelper.ceil(d2 / 0.25D));
            int k = Math.max(2, MathHelper.ceil(d3 / 0.25D));
            for (int l = 0; l < i; ++l) {
                for (int i1 = 0; i1 < j; ++i1) {
                    for (int j1 = 0; j1 < k; ++j1) {
                        double d4 = ((double) l + 0.5D) / (double) i;
                        double d5 = ((double) i1 + 0.5D) / (double) j;
                        double d6 = ((double) j1 + 0.5D) / (double) k;
                        double d7 = d4 * d1 + x;
                        double d8 = d5 * d2 + y;
                        double d9 = d6 * d3 + z;
                        Minecraft.getInstance().particles.addEffect((new DiggingParticle((ClientWorld) world, (double) pos.getX() + d7, (double) pos.getY() + d8, (double) pos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D, state)).setBlockPos(pos));
                    }
                }
            }
        });
        return true;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, TILE);
        super.fillStateContainer(builder);
    }

    @Override
    public abstract void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn);
}
