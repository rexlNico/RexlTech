package de.rexlnico.rexltech.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

public class WorldUtil {

    public static boolean breakExtraBlock(ItemStack stack, World world, PlayerEntity player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (player.isCreative()) {
            if (block.removedByPlayer(state, world, pos, player, false, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
            }

            if (!world.isRemote) {
                ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
            }
            return true;
        }

        stack.onBlockDestroyed(world, state, pos, player);

        if (!world.isRemote) {
            int xp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
            if (xp == -1) {
                return false;
            }

            TileEntity tileEntity = world.getTileEntity(pos);
            if (block.removedByPlayer(state, world, pos, player, true, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
                block.harvestBlock(world, player, pos, state, tileEntity, stack);
                block.dropXpOnBlockBreak((ServerWorld) world, pos, xp);
            }

            ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
//            world.destroyBlock(pos, true, player);
        } else {

            world.playEvent(2001, pos, Block.getStateId(state));
            if (block.removedByPlayer(state, world, pos, player, true, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
            }
            stack.onBlockDestroyed(world, state, pos, player);

        }
        return true;
    }

    private static BlockRayTraceResult getMovingObjectPosWithReachDistance(World world, PlayerEntity player, double distance, boolean p1, boolean p2, boolean p3) {
        Vector3d playerRotation = player.getLookVec();
        Vector3d rayPath = playerRotation.scale(distance);
        Vector3d from = player.getEyePosition(0);
        Vector3d to = from.add(rayPath);

        RayTraceContext rayCtx = new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, null);
        return world.rayTraceBlocks(rayCtx);
//        return player.pick(distance, 0f, false);
    }

    public static BlockRayTraceResult getNearestBlockWithDefaultReachDistance(World world, PlayerEntity player) {
        return getNearestBlockWithDefaultReachDistance(world, player, false, true, false);
    }

    public static BlockRayTraceResult getNearestBlockWithDefaultReachDistance(World world, PlayerEntity player, boolean stopOnLiquids, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        return getMovingObjectPosWithReachDistance(world, player, 20d, stopOnLiquids, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
    }

}
