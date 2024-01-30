package net.dragonmounts.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dragonmounts.block.HatchableDragonEggBlock;
import net.dragonmounts.entity.dragon.HatchableDragonEggEntity;
import net.dragonmounts.init.DMBlocks;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;

import javax.annotation.Nonnull;

/**
 * @see net.minecraft.client.renderer.entity.FallingBlockRenderer
 */
public class DragonEggRenderer extends EntityRenderer<HatchableDragonEggEntity> {
    public DragonEggRenderer(EntityRendererManager entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public void render(@Nonnull HatchableDragonEggEntity entity, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int packedLight) {
        HatchableDragonEggBlock block = entity.getDragonType().getInstance(HatchableDragonEggBlock.class, DMBlocks.ENDER_DRAGON_EGG);
        BlockState blockstate = block.defaultBlockState();
        World world = entity.level;
        if (blockstate.getRenderShape() != BlockRenderType.INVISIBLE) {
            BlockPos pos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
            matrixStack.pushPose();
            float axis = entity.getRotationAxis();
            float angle = entity.getAmplitude();
            if (angle != 0) {
                angle = (float) (Math.sin(angle - partialTicks) * Math.PI / 45F);//... * 8 / 360
                double temp = Math.sin(angle);
                matrixStack.mulPose(new Quaternion((float) (Math.cos(axis) * temp), 0F, (float) (Math.sin(axis) * temp), (float) (Math.cos(angle))));
                /*It is equivalent (at least assuming so) to:
                matrixStack.mulPose(new Vector3f((float)Math.cos(axis), 0, (float)Math.sin(axis)).rotationDegrees((float) (Math.sin(entity.getAmplitude() - partialTicks) * 8F)));
                */
            }
            matrixStack.translate(-0.5D, 0.0D, -0.5D);
            BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            for (RenderType type : RenderType.chunkBufferLayers()) {
                if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
                    ForgeHooksClient.setRenderLayer(type);
                    dispatcher.getModelRenderer().renderModel(world, dispatcher.getBlockModel(blockstate), blockstate, pos, matrixStack, buffer.getBuffer(type), false, world.random, blockstate.getSeed(pos), OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
                }
            }
            ForgeHooksClient.setRenderLayer(null);
            matrixStack.popPose();
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull HatchableDragonEggEntity entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }
}