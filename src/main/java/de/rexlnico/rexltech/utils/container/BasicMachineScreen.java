package de.rexlnico.rexltech.utils.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rexlnico.rexltech.RexlTech;
import de.rexlnico.rexltech.block.BaseMachineBlock;
import de.rexlnico.rexltech.tileentity.BaseTileEntityMachineBlock;
import de.rexlnico.rexltech.utils.helper.EnergyHelper;
import de.rexlnico.rexltech.utils.networking.PacketHandler;
import de.rexlnico.rexltech.utils.networking.SideConfigChangePacket;
import de.rexlnico.rexltech.utils.tileentity.SideConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicMachineScreen<T extends BasicMachineContainer> extends ContainerScreen<T> {

    private final ResourceLocation CONFIG = new ResourceLocation(RexlTech.MODID, "textures/gui/configuration_gui.png");
    private final ResourceLocation ADDONS = new ResourceLocation(RexlTech.MODID, "textures/gui/addon_gui.png");
    public final ResourceLocation GUI;
    private static final List<PlayerEntity> isConfigOpen = new ArrayList<>();
    private static final int sideOffset = 4;
    private static final int offset = 20;

    private int xEnergyOffset = 0;
    private int yEnergyOffset = 0;

    private ResourceLocation TOP;
    private ResourceLocation FRONT;
    private ResourceLocation SIDE;
    private ResourceLocation BOTTOM;

    public BasicMachineScreen(BasicMachineContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super((T) container, inv, titleIn);
        String regName = container.getTileEntity().getBlockState().getBlock().getRegistryName().toString().replaceFirst(RexlTech.MODID + ":", "");
        GUI = new ResourceLocation(RexlTech.MODID, "textures/gui/" + regName + ".png");
        TOP = new ResourceLocation(RexlTech.MODID + ":textures/blocks/" + regName + "_top.png");
        BOTTOM = new ResourceLocation(RexlTech.MODID + ":textures/blocks/" + regName + "_bottom.png");
        FRONT = new ResourceLocation(RexlTech.MODID + ":textures/blocks/" + regName + "_front.png");
        SIDE = new ResourceLocation(RexlTech.MODID + ":textures/blocks/" + regName + "_side.png");
    }

    @Override
    public void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        drawString(matrixStack, Minecraft.getInstance().fontRenderer, title, (this.xSize / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(title.getString()) / 2) - 5, 6, 0x808080);
    }

    public void setEnergyOffset(int x, int y) {
        xEnergyOffset = x;
        yEnergyOffset = y;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

        if (container.getTileEntity().getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            drawGuiContainerEnergy(matrixStack);
            drawGuiContainerAddons(matrixStack);
        }

        this.minecraft.getTextureManager().bindTexture(CONFIG);
        if (isConfigOpen.contains(playerInventory.player)) {
            drawConfig(matrixStack);
        } else {
            this.blit(matrixStack, this.guiLeft + this.xSize, this.guiTop + 4, 88, 0, 22, 22);
        }
    }

    public void drawGuiContainerAddons(MatrixStack matrixStack) {
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(ADDONS);
        this.blit(matrixStack, relX - 31, relY, 0, 0, 31, 93);
    }

    public void drawGuiContainerEnergy(MatrixStack matrixStack) {
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int energy = getEnergyStoredScaled(58);
        this.blit(matrixStack, relX + 153 + xEnergyOffset, relY + 74 - energy + yEnergyOffset, 176, 58 + 14 - energy, 14, energy);
    }

    @Override
    public int getXSize() {
        return super.getXSize() + (isConfigOpen.contains(playerInventory.player) ? 88 : 22);
    }

    @Override
    public int getYSize() {
        return (isConfigOpen.contains(playerInventory.player) ? 89 + 4 : super.getYSize());
    }

    private void drawConfig(MatrixStack matrixStack) {
        this.minecraft.getTextureManager().bindTexture(CONFIG);
        this.blit(matrixStack, this.guiLeft + this.xSize, this.guiTop + 4, 0, 0, 88, 89);
        this.minecraft.getTextureManager().bindTexture(FRONT);
        blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45, 0, 0, 16, 16, 16, 16);
        this.minecraft.getTextureManager().bindTexture(SIDE);
        blit(matrixStack, this.guiLeft + this.xSize + 38 + offset + sideOffset, this.guiTop + 45, 0, 0, 16, 16, 16, 16);
        blit(matrixStack, this.guiLeft + this.xSize + 38 - offset + sideOffset, this.guiTop + 45, 0, 0, 16, 16, 16, 16);
        blit(matrixStack, this.guiLeft + this.xSize + 38 + offset + sideOffset, this.guiTop + 45 + offset, 0, 0, 16, 16, 16, 16);
        this.minecraft.getTextureManager().bindTexture(TOP);
        blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45 - offset, 0, 0, 16, 16, 16, 16);
        this.minecraft.getTextureManager().bindTexture(BOTTOM);
        blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45 + offset, 0, 0, 16, 16, 16, 16);
        drawOverlay(matrixStack);
    }

    private void drawOverlay(MatrixStack matrixStack) {
        this.minecraft.getTextureManager().bindTexture(CONFIG);
        Direction facing = container.getTileEntity().getBlockState().get(BaseMachineBlock.FACING);
        if (container.getTileEntity().sideConfiguration == null) {
            return;
        }
        drawSideOverlay(matrixStack, Direction.UP, container.getTileEntity().sideConfiguration[Direction.UP.ordinal()]);
        drawSideOverlay(matrixStack, Direction.DOWN, container.getTileEntity().sideConfiguration[Direction.DOWN.ordinal()]);
        if (facing == Direction.NORTH) {
            drawSideOverlay(matrixStack, Direction.SOUTH, container.getTileEntity().sideConfiguration[Direction.SOUTH.ordinal()]);
            drawSideOverlay(matrixStack, Direction.EAST, container.getTileEntity().sideConfiguration[Direction.WEST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.WEST, container.getTileEntity().sideConfiguration[Direction.EAST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.NORTH, container.getTileEntity().sideConfiguration[Direction.NORTH.ordinal()]);
        } else if (facing == Direction.SOUTH) {
            drawSideOverlay(matrixStack, Direction.SOUTH, container.getTileEntity().sideConfiguration[Direction.NORTH.ordinal()]);
            drawSideOverlay(matrixStack, Direction.NORTH, container.getTileEntity().sideConfiguration[Direction.SOUTH.ordinal()]);
            drawSideOverlay(matrixStack, Direction.EAST, container.getTileEntity().sideConfiguration[Direction.EAST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.WEST, container.getTileEntity().sideConfiguration[Direction.WEST.ordinal()]);
        } else if (facing == Direction.EAST) {
            drawSideOverlay(matrixStack, Direction.NORTH, container.getTileEntity().sideConfiguration[Direction.EAST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.EAST, container.getTileEntity().sideConfiguration[Direction.NORTH.ordinal()]);
            drawSideOverlay(matrixStack, Direction.SOUTH, container.getTileEntity().sideConfiguration[Direction.WEST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.WEST, container.getTileEntity().sideConfiguration[Direction.SOUTH.ordinal()]);
        } else if (facing == Direction.WEST) {
            drawSideOverlay(matrixStack, Direction.NORTH, container.getTileEntity().sideConfiguration[Direction.WEST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.WEST, container.getTileEntity().sideConfiguration[Direction.NORTH.ordinal()]);
            drawSideOverlay(matrixStack, Direction.SOUTH, container.getTileEntity().sideConfiguration[Direction.EAST.ordinal()]);
            drawSideOverlay(matrixStack, Direction.EAST, container.getTileEntity().sideConfiguration[Direction.SOUTH.ordinal()]);
        }
    }

    private void drawSideOverlay(MatrixStack matrixStack, Direction facing, SideConfiguration config) {
        if (config == null || config.equals(SideConfiguration.NONE)) return;
        if (facing == Direction.UP) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45 - offset, config.textureX, config.textureY, 16, 16);
        } else if (facing == Direction.DOWN) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45 + offset, config.textureX, config.textureY, 16, 16);
        } else if (facing == Direction.EAST) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset + offset, this.guiTop + 45, config.textureX, config.textureY, 16, 16);
        } else if (facing == Direction.SOUTH) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset + offset, this.guiTop + 45 + offset, config.textureX, config.textureY, 16, 16);
        } else if (facing == Direction.WEST) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset - offset, this.guiTop + 45, config.textureX, config.textureY, 16, 16);
        } else if (facing == Direction.NORTH) {
            blit(matrixStack, this.guiLeft + this.xSize + 38 + sideOffset, this.guiTop + 45, config.textureX, config.textureY, 16, 16);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= this.guiLeft + this.xSize && mouseX <= this.guiLeft + this.xSize + 22) {
            if (mouseY >= this.guiTop + 4 && mouseY <= this.guiTop + 26) {
                playerInventory.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 1);
                if (isConfigOpen.contains(playerInventory.player)) {
                    isConfigOpen.remove(playerInventory.player);
                } else {
                    isConfigOpen.add(playerInventory.player);
                }
            }
        }
        if (isConfigOpen.contains(playerInventory.player)) {
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset + 16) {
                if (mouseY >= this.guiTop + 45 - offset && mouseY <= this.guiTop + 45 - offset + 16) {
                    nextConfig(Direction.UP);
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset + 16) {
                if (mouseY >= this.guiTop + 45 + offset && mouseY <= this.guiTop + 45 + offset + 16) {
                    nextConfig(Direction.DOWN);
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset - offset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset - offset + 16) {
                if (mouseY >= this.guiTop + 45 && mouseY <= this.guiTop + 45 + 16) {
                    nextConfig(getFacingFromSide(Direction.EAST));
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset + offset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset + offset + 16) {
                if (mouseY >= this.guiTop + 45 && mouseY <= this.guiTop + 45 + 16) {
                    nextConfig(getFacingFromSide(Direction.WEST));
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset + offset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset + offset + 16) {
                if (mouseY >= this.guiTop + 45 + offset && mouseY <= this.guiTop + 45 + offset + 16) {
                    nextConfig(getFacingFromSide(Direction.SOUTH));
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            if (mouseX >= this.guiLeft + this.xSize + 38 + sideOffset && mouseX <= this.guiLeft + this.xSize + 38 + sideOffset + 16) {
                if (mouseY >= this.guiTop + 45 && mouseY <= this.guiTop + 45 + 16) {
                    nextConfig(getFacingFromSide(Direction.NORTH));
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private Direction getFacingFromSide(Direction side) {
        Direction facing = container.getTileEntity().getWorld().getBlockState(container.getTileEntity().getPos()).get(BaseMachineBlock.FACING);
        if (side == Direction.UP || side == Direction.DOWN) {
            return side;
        }
        if (facing == Direction.NORTH) {
            return side;
        } else if (facing == Direction.SOUTH) {
            if (side == Direction.SOUTH) return Direction.NORTH;
            if (side == Direction.NORTH) return Direction.SOUTH;
            if (side == Direction.EAST) return Direction.WEST;
            if (side == Direction.WEST) return Direction.EAST;
        } else if (facing == Direction.EAST) {
            if (side == Direction.NORTH) return Direction.EAST;
            if (side == Direction.EAST) return Direction.SOUTH;
            if (side == Direction.SOUTH) return Direction.WEST;
            if (side == Direction.WEST) return Direction.NORTH;
        } else if (facing == Direction.WEST) {
            if (side == Direction.NORTH) return Direction.WEST;
            if (side == Direction.WEST) return Direction.SOUTH;
            if (side == Direction.SOUTH) return Direction.EAST;
            if (side == Direction.EAST) return Direction.NORTH;
        }
        return side;
    }

    private void nextConfig(Direction direction) {
        playerInventory.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, 0.7f);
        PacketHandler.INSTANCE.sendToServer(new SideConfigChangePacket(container.getTileEntity().getPos(), direction.ordinal()));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        int relX = (this.width - this.xSize) / 2 - 1;
        int relY = (this.height - this.ySize) / 2 - 1;
        if (container.getTileEntity().getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            if (mouseX >= relX + 153 && mouseX <= relX + 168) {
                if (mouseY >= relY + 16 && mouseY <= relY + 75) {
                    renderTooltip(matrixStack, getEnergyHover(), mouseX, mouseY);
                }
            }
        }
    }

    public TextComponent getEnergyHover() {
        StringTextComponent textComponent = EnergyHelper.getEnergyStorageTextComponent(container.getEnergy(), container.getMaxEnergy());
        textComponent.setStyle(Style.EMPTY.setColor(Color.fromInt(4210752)));
        return textComponent;
    }

    public int getEnergyStoredScaled(int pixels) {
        int i = ((BaseTileEntityMachineBlock) container.getTileEntity()).energyStorage.getEnergyStored();
        int j = ((BaseTileEntityMachineBlock) container.getTileEntity()).energyStorage.getMaxEnergyStored();
        return i != 0 && j != 0 ? i * pixels / j : 0;
    }

    public int getBurnScaled(int pixels, int burntime, int maxBurntime) {
        int i = burntime;
        int j = maxBurntime;
        return i != 0 && j != 0 ? i * pixels / j : 0;
    }

    public int getProgressScaled(int pixels, int burntime, int maxBurntime) {
        int i = burntime;
        int j = maxBurntime;
        return i != 0 && j != 0 ? i * pixels / j : pixels;
    }

}
