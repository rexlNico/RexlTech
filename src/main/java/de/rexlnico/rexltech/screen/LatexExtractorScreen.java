package de.rexlnico.rexltech.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rexlnico.rexltech.container.LatexExtractorContainer;
import de.rexlnico.rexltech.utils.container.BasicMachineScreen;
import de.rexlnico.rexltech.utils.helper.EnergyHelper;
import de.rexlnico.rexltech.utils.helper.GuiHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

public class LatexExtractorScreen extends BasicMachineScreen<LatexExtractorContainer> {


    public LatexExtractorScreen(LatexExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        GuiHelper.renderFluidTank(matrixStack, this, container.getTileEntity().latexTank.getFluid(), container.getTileEntity().latexTank, 154, 17, 12, 48, 100);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(matrixStack, relX + 154, relY + 16, 177, 0, 14, 50);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int relX = (this.width - this.xSize) / 2 - 1;
        int relY = (this.height - this.ySize) / 2 - 1;
        if (mouseX >= relX + 154 && mouseX <= relX + 167) {
            if (mouseY >= relY + 17 && mouseY <= relY + 67) {
                StringTextComponent textComponent = EnergyHelper.getFluidStorageTextComponent(container.getTileEntity().latexTank.getFluidAmount(), container.getTileEntity().latexTank.getCapacity());
                textComponent.setStyle(Style.EMPTY.setColor(Color.fromInt(4210752)));
                renderTooltip(matrixStack, textComponent, mouseX, mouseY);
            }
        }
    }

}
