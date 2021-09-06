package de.rexlnico.rexltech.utils.helper;

import com.mojang.authlib.GameProfile;
import de.rexlnico.rexltech.utils.networking.FakeServerHandler;
import net.minecraft.entity.Pose;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class FakePlayer extends net.minecraftforge.common.util.FakePlayer {

    private static GameProfile profile = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE78"), "[RexlTechFakePlayer]");

    @Nullable
    final private GameProfile owner;
    @Nullable
    final private String type;
    private final ItemStack[] cachedHandInventory = new ItemStack[2];
    private final ItemStack[] cachedArmorArray = new ItemStack[4];

    public FakePlayer(ServerWorld world, GameProfile owner, String type) {
        super(world, profile);
        this.owner = owner;
        this.type = type;
        connection = new FakeServerHandler(this);
//        setSize(0, 0);
        abilities.disableDamage = true;
    }

    public void setLocationEdge(BlockPos offset, Direction side) {
        double r = 0.2;
        double x = offset.getX() + 0.5 - side.getXOffset() * r;
        double y = offset.getY() + 0.5 - side.getYOffset() * r;
        double z = offset.getZ() + 0.5 - side.getZOffset() * r;

        int yaw;
        int pitch;

        switch (side) {
            case DOWN:
                pitch = 90;
                yaw = 0;
                break;
            case UP:
                pitch = -90;
                yaw = 0;
                break;
            case NORTH:
                yaw = 180;
                pitch = 0;
                break;
            case SOUTH:
                yaw = 0;
                pitch = 0;
                break;
            case WEST:
                yaw = 90;
                pitch = 0;
                break;
            case EAST:
                yaw = 270;
                pitch = 0;
                break;
            default:
                throw new RuntimeException("Invalid Side (" + side + ")");
        }

        setLocationAndAngles(x, y, z, yaw, pitch);
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return 0;
    }

    public RayTraceResult trace(double blockReachDistance) {
        Vector3d vec3d = new Vector3d(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ());
        Vector3d vec3d1 = getVectorForRotationPublic(this.getPitchYaw().x, this.getPitchYaw().y);
        Vector3d vec3d2 = Vector3d.ZERO.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, this));
    }

    public Vector3d getVectorForRotationPublic(float rotationPitch, float rotationYaw) {
        return this.getVectorForRotation(rotationPitch, rotationYaw);
    }

    public void clearInventory() {
        inventory.clear();
    }

    @Override
    public void setActiveHand(Hand hand) {
    }

    @Nonnull
    public Vector3d getPositionEyes(float partialTicks) {
        return getPositionEyes();
    }

    @Nonnull
    public Vector3d getPositionEyes() {
        return new Vector3d(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ());
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        StringTextComponent itextcomponent = new StringTextComponent("[");
        if (type != null) {
            itextcomponent.appendString(type);
        } else {
            itextcomponent.appendString("XUFakePlayer");
        }
        if (owner != null) {
            itextcomponent.appendString(" - ");
            itextcomponent.appendString(owner.getName());
        }
        itextcomponent.appendString("]");
        return itextcomponent;
    }

    public void updateCooldown() {
        this.ticksSinceLastSwing = 20090;
    }

//    public void updateAttributes() {
//        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
//            ItemStack itemstack;
//
//            switch (entityequipmentslot.getSlotType()) {
//                case HAND:
//                    itemstack = StackHelper.safeCopy(this.cachedHandInventory[entityequipmentslot.getIndex()]);
//                    break;
//                case ARMOR:
//                    itemstack = StackHelper.safeCopy(this.cachedArmorArray[entityequipmentslot.getIndex()]);
//                    break;
//                default:
//                    continue;
//            }
//
//            ItemStack newStack = this.getItemStackFromSlot(entityequipmentslot);
//
//            if (!ItemStack.areItemStacksEqual(newStack, itemstack)) {
//                if (StackHelper.isNonNull(itemstack)) {
//                    this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
//                }
//
//                if (StackHelper.isNonNull(newStack)) {
//                    this.getAttributeMap().applyAttributeModifiers(newStack.getAttributeModifiers(entityequipmentslot));
//                }
//
//                switch (entityequipmentslot.getSlotType()) {
//                    case HAND:
//                        this.cachedHandInventory[entityequipmentslot.getIndex()] = StackHelper.safeCopy(newStack);
//                        break;
//                    case ARMOR:
//                        this.cachedArmorArray[entityequipmentslot.getIndex()] = StackHelper.safeCopy(newStack);
//                }
//            }
//        }
//    }

}
