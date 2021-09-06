package de.rexlnico.rexltech.utils.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GuiHelper {

    //from Tinkers Construct changing in future
    public static void renderFluidTank(MatrixStack matrices, ContainerScreen<?> screen, FluidStack stack, FluidTank tank, int x, int y, int width, int height, int depth) {
        if (!stack.isEmpty()) {
            int maxY = y + height;
            int fluidHeight = Math.min(height * tank.getFluidAmount() / tank.getCapacity(), height);
            renderTiledFluid(matrices, screen, stack, x, maxY - fluidHeight, width, fluidHeight, depth);
        }
    }

    //from Tinkers Construct changing in future
    public static void renderTiledFluid(MatrixStack matrices, ContainerScreen<?> screen, FluidStack stack, int x, int y, int width, int height, int depth) {
        if (!stack.isEmpty()) {
            TextureAtlasSprite fluidSprite = screen.getMinecraft().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(stack.getFluid().getAttributes().getStillTexture(stack));
            setColorRGBA(stack.getFluid().getAttributes().getColor(stack));
            renderTiledTextureAtlas(matrices, screen, fluidSprite, x, y, width, height, depth, stack.getFluid().getAttributes().isGaseous(stack));
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    //from Tinkers Construct changing in future
    public static void renderTiledTextureAtlas(MatrixStack matrices, ContainerScreen<?> screen, TextureAtlasSprite sprite, int x, int y, int width, int height, int depth, boolean upsideDown) {
        // start drawing sprites
        screen.getMinecraft().getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);

        // tile vertically
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        int spriteHeight = sprite.getHeight();
        int spriteWidth = sprite.getWidth();
        int startX = x + screen.getGuiLeft();
        int startY = y + screen.getGuiTop();
        do {
            int renderHeight = Math.min(spriteHeight, height);
            height -= renderHeight;
            float v2 = sprite.getInterpolatedV((16f * renderHeight) / spriteHeight);

            // we need to draw the quads per width too
            int x2 = startX;
            int widthLeft = width;
            Matrix4f matrix = matrices.getLast().getMatrix();
            // tile horizontally
            do {
                int renderWidth = Math.min(spriteWidth, widthLeft);
                widthLeft -= renderWidth;

                float u2 = sprite.getInterpolatedU((16f * renderWidth) / spriteWidth);
                if (upsideDown) {
                    buildSquare(matrix, builder, x2, x2 + renderWidth, startY, startY + renderHeight, depth, u1, u2, v2, v1);
                } else {
                    buildSquare(matrix, builder, x2, x2 + renderWidth, startY, startY + renderHeight, depth, u1, u2, v1, v2);
                }
                x2 += renderWidth;
            } while (widthLeft > 0);

            startY += renderHeight;
        } while (height > 0);

        // finish drawing sprites
        builder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(builder);
    }

    //from Tinkers Construct changing in future
    private static void buildSquare(Matrix4f matrix, BufferBuilder builder, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        builder.pos(matrix, x1, y2, z).tex(u1, v2).endVertex();
        builder.pos(matrix, x2, y2, z).tex(u2, v2).endVertex();
        builder.pos(matrix, x2, y1, z).tex(u2, v1).endVertex();
        builder.pos(matrix, x1, y1, z).tex(u1, v1).endVertex();
    }

    //from Tinkers Construct changing in future
    public static void setColorRGBA(int color) {
        float a = alpha(color) / 255.0F;
        float r = red(color) / 255.0F;
        float g = green(color) / 255.0F;
        float b = blue(color) / 255.0F;
        RenderSystem.color4f(r, g, b, a);
    }

    //from Tinkers Construct changing in future
    public static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    //from Tinkers Construct changing in future
    public static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    //from Tinkers Construct changing in future
    public static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    //from Tinkers Construct changing in future
    public static int blue(int c) {
        return (c) & 0xFF;
    }

}
