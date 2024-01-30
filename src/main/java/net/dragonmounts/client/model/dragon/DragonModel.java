package net.dragonmounts.client.model.dragon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.dragonmounts.client.variant.VariantAppearance;
import net.dragonmounts.client.variant.VariantAppearances;
import net.dragonmounts.entity.dragon.TameableDragonEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DragonModel extends EntityModel<TameableDragonEntity> {
    private static final Matrix4f INVERSE_SCALE = Matrix4f.createScaleMatrix(-1, 1, 1);
    private static final Matrix3f INVERSE_NORMS = new Matrix3f(INVERSE_SCALE);
    public final DragonHeadModelPart head;
    public final DragonNeckModelPart neck;
    public final DragonBodyModelPart body;
    public final DragonTailModelPart tail;
    public final DragonWingModelPart wing;
    public final ModelHolder<DragonLegConfig, DragonLegModelPart> foreLeftLeg;
    public final ModelHolder<DragonLegConfig, DragonLegModelPart> foreRightLeg;
    public final ModelHolder<DragonLegConfig, DragonLegModelPart> hindLeftLeg;
    public final ModelHolder<DragonLegConfig, DragonLegModelPart> hindRightLeg;

    public DragonModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.head = new DragonHeadModelPart(this);
        this.neck = new DragonNeckModelPart(this);
        this.body = new DragonBodyModelPart(this);
        this.tail = new DragonTailModelPart(this);
        this.wing = new DragonWingModelPart(this);
        List<DragonLegConfig> configs = Arrays.asList(DragonLegConfig.DEFAULT, DragonLegConfig.SKELETON);
        this.foreLeftLeg = new ModelHolder<>(this, DragonLegModelPart.Fore::new, configs);
        this.foreRightLeg = new ModelHolder<>(this, DragonLegModelPart.Fore::new, configs);
        this.hindLeftLeg = new ModelHolder<>(this, DragonLegModelPart.Hind::new, configs);
        this.hindRightLeg = new ModelHolder<>(this, DragonLegModelPart.Hind::new, configs);
    }

    @Override
    public void prepareMobModel(@Nonnull TameableDragonEntity dragon, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hasSideTailScale = dragon.getVariant().getAppearance(VariantAppearances.ENDER_FEMALE).hasSideTailScale(dragon);
        this.tail.leftScale.visible = this.tail.rightScale.visible = hasSideTailScale;
        this.tail.middleScale.visible = !hasSideTailScale;
        this.head.scaleX = this.head.scaleY = this.head.scaleZ = 0.92F;
        dragon.animator.setPartialTicks(partialTick);

    }

    @Override
    public void setupAnim(@Nonnull TameableDragonEntity dragon, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        dragon.animator.setLook(netHeadYaw, headPitch);
        dragon.animator.setMovement(limbSwing, limbSwingAmount * dragon.getScale());
        dragon.animator.animate(this);
    }

    @Override
    public void renderToBuffer(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderHead(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.neck.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.tail.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderWings(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderLegs(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderOnShoulder(VariantAppearance appearance, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight, float size) {
        matrixStack.pushPose();
        float scale = size * appearance.renderScale;
        matrixStack.scale(-scale, -scale, scale);
        boolean hasSideTailScale = appearance.hasSideTailScaleOnShoulder();
        this.tail.leftScale.visible = this.tail.rightScale.visible = hasSideTailScale;
        this.tail.middleScale.visible = !hasSideTailScale;
        this.head.scaleX = this.head.scaleY = this.head.scaleZ = 0.92F;
        this.renderToBuffer(matrixStack, buffer.getBuffer(appearance.getBodyOnShoulder()), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.renderToBuffer(matrixStack, buffer.getBuffer(appearance.getGlowOnShoulder()), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    protected void renderHead(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderWings(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        matrixStack.scale(1.1F, 1.1F, 1.1F);
        this.wing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.last().pose().multiply(INVERSE_SCALE);
        matrixStack.last().normal().mul(INVERSE_NORMS);
        this.wing.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
    }

    protected void renderLegs(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        this.foreRightLeg.getCurrent().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.hindRightLeg.getCurrent().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.last().pose().multiply(INVERSE_SCALE);
        matrixStack.last().normal().mul(INVERSE_NORMS);
        this.foreLeftLeg.getCurrent().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.hindLeftLeg.getCurrent().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
    }
}