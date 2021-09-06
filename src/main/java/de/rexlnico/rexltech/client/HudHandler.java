package de.rexlnico.rexltech.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.config.RexlTechClientConfig;
import de.rexlnico.rexltech.item.Jetpack;
import de.rexlnico.rexltech.utils.Colors;
import de.rexlnico.rexltech.utils.helper.EnergyHelper;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HudHandler {
    private static final ResourceLocation HUD_TEXTURE = new ResourceLocation(RexlTech.MODID, "textures/gui/hud.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (Minecraft.isGuiEnabled()) {
                ItemStack chest = mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST);
                Item item = chest.getItem();
                if (!chest.isEmpty() && item instanceof Jetpack) {
                    Jetpack jetpack = (Jetpack) item;
                    HudPos pos = getHudPos();
                    if (pos != null) {
                        int xPos = (int) (pos.x / 0.33) - 18;
                        int yPos = (int) (pos.y / 0.33) - 78;

                        mc.getTextureManager().bindTexture(HUD_TEXTURE);

                        RenderSystem.pushMatrix();
                        RenderSystem.scaled(0.33, 0.33, 1.0);
                        MatrixStack stack = event.getMatrixStack();
                        RenderHelper.drawTexturedModalRect(stack.getLast().getMatrix(), xPos, yPos, 0, 0, 28, 156);
                        int i2 = getEnergyBarScaled(chest);
                        RenderHelper.drawTexturedModalRect(stack.getLast().getMatrix(), xPos, 166 - i2 + yPos - 10, 28, 156 - i2, 28, i2);
                        RenderSystem.popMatrix();

                        String fuel = Colors.GRAY + EnergyHelper.getEnergyString(chest.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0));
                        String engine = Colors.GRAY + "E: " + getStatusString(chest.getOrCreateTag().getBoolean("Active"));
                        String hover = Colors.GRAY + "H: " + getStatusString(chest.getOrCreateTag().getBoolean("Hover"));

                        if (pos.side == 1) {
                            mc.fontRenderer.drawStringWithShadow(stack, fuel, pos.x - 8 - mc.fontRenderer.getStringWidth(fuel), pos.y - 21, 16383998);
                            mc.fontRenderer.drawStringWithShadow(stack, engine, pos.x - 8 - mc.fontRenderer.getStringWidth(engine), pos.y - 6, 16383998);
                            mc.fontRenderer.drawStringWithShadow(stack, hover, pos.x - 8 - mc.fontRenderer.getStringWidth(hover), pos.y + 4, 16383998);
                        } else {
                            mc.fontRenderer.drawStringWithShadow(stack, fuel, pos.x + 6, pos.y - 21, 16383998);
                            mc.fontRenderer.drawStringWithShadow(stack, engine, pos.x + 6, pos.y - 6, 16383998);
                            mc.fontRenderer.drawStringWithShadow(stack, hover, pos.x + 6, pos.y + 4, 16383998);
                        }

                        mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
                    }
                }
            }
        }
    }

    private String getStatusString(boolean on) {
        return on ? Colors.GREEN + "Active" : Colors.RED + "Inactive";
    }

    private int getEnergyBarScaled(ItemStack stack) {
        int i = stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
        int j = stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
        return (int) (j != 0 && i != 0 ? (long) i * 156 / j : 0);
    }

    private HudPos getHudPos() {
        MainWindow window = Minecraft.getInstance().getMainWindow();

        switch (RexlTechClientConfig.jetpackHudPositionConfigValue.get()) {
            case 0:
                return new HudPos(10, 30, 0);
            case 1:
                return new HudPos(10, window.getScaledHeight() / 2, 0);
            case 2:
                return new HudPos(10, window.getScaledHeight() - 30, 0);
            case 3:
                return new HudPos(window.getScaledWidth() - 8, 30, 1);
            case 4:
                return new HudPos(window.getScaledWidth() - 8, window.getScaledHeight() / 2, 1);
            case 5:
                return new HudPos(window.getScaledWidth() - 8, window.getScaledHeight() - 30, 1);
        }

        return null;
    }

    private class HudPos {
        public int x;
        public int y;
        public int side;

        public HudPos(int x, int y, int side) {
            this.x = x;
            this.y = y;
            this.side = side;
        }
    }

    public enum HudPosition {
        LeftTop,
        LeftMiddle,
        LeftBottom,
        RightTop,
        RightMiddle,
        RightBottom;
    }

}
