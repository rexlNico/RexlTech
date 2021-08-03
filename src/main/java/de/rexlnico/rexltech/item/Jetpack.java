package de.rexlnico.rexltech.item;

import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.client.JetpackModel;
import de.rexlnico.rexltech.utils.handler.InputHandler;
import de.rexlnico.rexltech.utils.init.SoundInit;
import de.rexlnico.rexltech.utils.item.EnergyArmorItem;
import de.rexlnico.rexltech.utils.item.MaterialHelper;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class Jetpack extends EnergyArmorItem implements IDyeableArmorItem {

    private final static int baseUsage = 300;
    private final static double accelVert = 0.13D;
    private final static double speedVert = 0.61D;
    private final static double speedSide = 0.15D;
    private final static double speedHover = 0.34D;
    private final static double speedHoverSlow = 0.03D;
    private final static double sprintSpeed = 1.5D;
    private final static double sprintSpeedVert = 1.25D;
    private final static double sprintFuel = 3.2D;

    private BipedModel<?> model;

    public Jetpack() {
        super(MaterialHelper.JETPACK_MATERIAL, EquipmentSlotType.CHEST, new Properties().group(ItemGroup.COMBAT).setNoRepair(), 10000000, 50000, 0);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        ItemStack chest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        Item item = chest.getItem();
        if (!chest.isEmpty() && item instanceof Jetpack) {
            if (isEngineOn(chest)) {
                boolean hover = isHovering(chest);
                if (InputHandler.isHoldingUp(player) || hover && !player.isOnGround()) {

                    double motionY = player.getMotion().y;
                    double hoverSpeed = InputHandler.isHoldingDown(player) ? speedHover : speedHoverSlow;
                    double currentAccel = accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                    double currentSpeedVertical = speedVert * (player.isInWater() ? 0.4D : 1.0D);

                    double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? baseUsage * sprintFuel : baseUsage;

                    if (!player.isCreative()) {
                        extractEnergy(stack, (int) usage, false);
                    }

                    if (getEnergyStored(stack) > 0 || player.isCreative()) {
                        double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? sprintSpeedVert : 1.0D;

                        if (InputHandler.isHoldingUp(player)) {
                            if (!hover) {
                                fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * verticalSprintMulti);
                            } else {
                                if (InputHandler.isHoldingDown(player)) {
                                    fly(player, Math.min(motionY + currentAccel, -speedHoverSlow));
                                } else {
                                    fly(player, Math.min(motionY + currentAccel, speedHover) * verticalSprintMulti);
                                }
                            }
                        } else {
                            fly(player, Math.min(motionY + currentAccel, -hoverSpeed));
                        }

                        double speedSideways = (player.isCrouching() ? speedSide * 0.5F : speedSide);
                        double speedForward = (player.isSprinting() ? speedSideways * sprintSpeed : speedSideways);

                        if (InputHandler.isHoldingForwards(player)) {
                            player.moveRelative(1, new Vector3d(0, 0, speedForward));
                        }

                        if (InputHandler.isHoldingBackwards(player)) {
                            player.moveRelative(1, new Vector3d(0, 0, -speedSideways * 0.8F));
                        }

                        if (InputHandler.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vector3d(speedSideways, 0, 0));
                        }

                        if (InputHandler.isHoldingRight(player)) {
                            player.moveRelative(1, new Vector3d(-speedSideways, 0, 0));
                        }
                        world.playMovingSound(player, player, SoundInit.JETPACK, SoundCategory.PLAYERS, 1, 1);
                        if (!world.isRemote) {
                            player.fallDistance = 0.0F;

//                            if (player instanceof ServerPlayerEntity) {
//                                ((ServerPlayerEntity) player).connection.aboveGroundTickCount = 0;
//                            }
                        }
                    }
                }
            }
        }
    }

    private static void fly(PlayerEntity player, double y) {
        Vector3d motion = player.getMotion();
        player.setMotion(motion.x, y, motion.z);
    }

    public static boolean isEngineOn(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Active");
    }

    public static boolean isHovering(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Hover");
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return super.getDurabilityForDisplay(stack);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!this.isInGroup(group)) {
            return;
        }
        ItemStack charged = new ItemStack(this);
        charged.getOrCreateTag().putDouble("Energy", getEnergyCapacity());
        charged.getOrCreateTag().putBoolean("Active", false);
        charged.getOrCreateTag().putBoolean("Hover", false);
        items.add(charged);

        ItemStack empty = new ItemStack(this);
        charged.getOrCreateTag().putBoolean("Active", true);
        charged.getOrCreateTag().putBoolean("Hover", false);
        items.add(empty);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        StringTextComponent engine = new StringTextComponent("Engine: ");
        StringTextComponent engineState = new StringTextComponent(stack.getOrCreateTag().getBoolean("Active") ? "Active" : "Inactive");
        engineState.setStyle(Style.EMPTY.setColor(stack.getOrCreateTag().getBoolean("Active") ? Color.fromHex("#40ff00") : Color.fromHex("#ff0000")));
        engine.appendSibling(engineState);
        tooltip.add(engine);
        StringTextComponent hover = new StringTextComponent("Hover: ");
        StringTextComponent hoverState = new StringTextComponent(stack.getOrCreateTag().getBoolean("Hover") ? "Active" : "Inactive");
        hoverState.setStyle(Style.EMPTY.setColor(stack.getOrCreateTag().getBoolean("Hover") ? Color.fromHex("#40ff00") : Color.fromHex("#ff0000")));
        hover.appendSibling(hoverState);
        tooltip.add(hover);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BipedModel<?> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType slot, BipedModel _default) {
        if (this.model == null)
            this.model = new JetpackModel();
        return this.model;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return type == null ? RexlTech.MODID + ":textures/armor/jetpack.png" : RexlTech.MODID + ":textures/armor/jetpack_overlay.png";
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        return true;
    }

    @Override
    public int getColor(ItemStack stack) {
        return ColorHelper.PackedColor.packColor(100, 0, 102, 255);
    }
}
