package jagm.hooty;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HootyModel<T extends HootyEntity> extends EntityModel<Entity> {

	private final ModelRenderer body;

	public HootyModel(boolean segment) {
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, segment ? 16 : 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue,
			float alpha) {
		body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

}
