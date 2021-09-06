package de.rexlnico.rexltech.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MinerModel extends Model {

    public ModelRenderer parentModel;
    public ModelRenderer childModel;

    public MinerModel() {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
        textureHeight = 32;

        parentModel = new ModelRenderer(this, 0, 0);
        parentModel.setRotationPoint(0, 0, 0);
        parentModel.addBox(-8f, -8f, -8f, 16, 16, 16);

        childModel = new ModelRenderer(this, 0, 0);
        childModel.setRotationPoint(0, 0, 0);
        childModel.addBox(-0.5f, -1.5f, -0.5f, 1, 3, 2);

        parentModel.addChild(childModel);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        parentModel.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
