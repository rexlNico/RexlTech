package de.rexlnico.rexltech.utils.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WorldHelper {

    private static BlockRayTraceResult getMovingObjectPosWithReachDistance(World world, PlayerEntity player, double distance) {
        Vector3d playerRotation = player.getLookVec();
        Vector3d rayPath = playerRotation.scale(distance);
        Vector3d from = player.getEyePosition(0);
        Vector3d to = from.add(rayPath);

        RayTraceContext rayCtx = new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, null);
        return world.rayTraceBlocks(rayCtx);
    }

    public static BlockRayTraceResult getNearestBlockWithDefaultReachDistance(World world, PlayerEntity player) {
        return getNearestBlockWithDefaultReachDistance(world, player, 20d);
    }

    public static BlockRayTraceResult getNearestBlockWithDefaultReachDistance(World world, PlayerEntity player, double distance) {
        return getMovingObjectPosWithReachDistance(world, player, distance);
    }

}
