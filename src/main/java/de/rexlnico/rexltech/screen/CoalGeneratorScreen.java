package de.rexlnico.rexltech.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rexlnico.rexltech.container.CoalGeneratorContainer;
import de.rexlnico.rexltech.utils.container.BasicMachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CoalGeneratorScreen extends BasicMachineScreen<CoalGeneratorContainer> {


    public CoalGeneratorScreen(CoalGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;

        int burn = getBurnScaled(14, container.getTileEntity().burntime, container.getTileEntity().maxBurntime);
        this.blit(matrixStack, relX + 81, relY + 36 - burn, 176, 14 - burn, 14, burn);
    }


}
