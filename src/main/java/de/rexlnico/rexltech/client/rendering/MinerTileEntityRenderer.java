package de.rexlnico.rexltech.client.rendering;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.rexlnico.rexltech.client.model.MinerModel;
import de.rexlnico.rexltech.tileentity.TileEntityMiner;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class MinerTileEntityRenderer extends TileEntityRenderer<TileEntityMiner> {

    MinerModel minerModel = new MinerModel();
    ResourceLocation modelTexture = new ResourceLocation("");

    public MinerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityMiner tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        boolean USE_ENTITY_MODEL_TRANSFORMATIONS = true;

        if (USE_ENTITY_MODEL_TRANSFORMATIONS) {
            matrixStack.scale(-1, -1, 1);
            matrixStack.translate(0.0D, -1.501F, 0.0D);
        }


        matrixStack.translate(0, 0, 0);

        IVertexBuilder renderBuffer = buffer.getBuffer(minerModel.getRenderType(modelTexture));
        minerModel.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F); // white, fully opaque
        matrixStack.pop();

    }

    @Override
    public boolean isGlobalRenderer(TileEntityMiner te) {
        return true;
    }
}
