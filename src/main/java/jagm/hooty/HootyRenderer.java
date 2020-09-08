package jagm.hooty;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HootyRenderer extends EntityRenderer<HootyEntity> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(Hooty.MOD_ID, "textures/entity/hooty.png");
	protected final HootyModel<HootyEntity> hootyModel = new HootyModel<HootyEntity>(false);
	protected final HootyModel<HootyEntity> segmentModel = new HootyModel<HootyEntity>(true);

	public HootyRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.0F;
	}

	@Override
	public void render(HootyEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
		matrixStackIn.rotate(new Quaternion(0.0F, (180.0F + entityYaw) * ((float) Math.PI / 180F), 0.0F, false));
		matrixStackIn.rotate(new Quaternion(entityIn.rotationPitch * ((float) Math.PI / 180F), 0.0F, 0.0F, false));
		this.hootyModel.render(matrixStackIn, bufferIn.getBuffer(this.hootyModel.getRenderType(this.getEntityTexture(entityIn))), packedLightIn,
				OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.pop();
		boolean first = true;
		for (HootySegment segment : entityIn.getSegments()) {
			// Skip rendering the first segment because it clips into the head.
			if (first) {
				first = false;
				if (!entityIn.getHasRetracted() || entityIn.getSegments().size() <= 20) {
					continue;
				}
			}
			matrixStackIn.push();
			Vector3d v = entityIn.getMotion().scale(partialTicks).add(new Vector3d(entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ));
			matrixStackIn.translate(segment.getX() - v.x, segment.getY() - v.y, segment.getZ() - v.z);
			matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
			matrixStackIn.rotate(new Quaternion(0.0F, (180.0F + segment.getYaw()) * ((float) Math.PI / 180F), 0.0F, false));
			matrixStackIn.rotate(new Quaternion(segment.getPitch() * ((float) Math.PI / 180F), 0.0F, 0.0F, false));
			this.segmentModel.render(matrixStackIn, bufferIn.getBuffer(this.segmentModel.getRenderType(this.getEntityTexture(entityIn))), packedLightIn,
					OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStackIn.pop();
		}
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(HootyEntity entity) {
		return TEXTURE;
	}

}
