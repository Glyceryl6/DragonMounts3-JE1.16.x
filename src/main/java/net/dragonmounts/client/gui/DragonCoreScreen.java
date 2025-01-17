package net.dragonmounts.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dragonmounts.inventory.DragonCoreContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static net.dragonmounts.DragonMounts.makeId;

/**
 * @see net.minecraft.client.gui.screen.inventory.ShulkerBoxScreen
 */
@OnlyIn(Dist.CLIENT)
public class DragonCoreScreen extends ContainerScreen<DragonCoreContainer> {
    private static final ResourceLocation TEXTURE_LOCATION = makeId("textures/gui/dragon_core.png");

    public DragonCoreScreen(DragonCoreContainer menu, PlayerInventory playerInventory, ITextComponent title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(@Nonnull MatrixStack matrices, int x, int y, float ticks) {
        this.renderBackground(matrices);
        super.render(matrices, x, y, ticks);
        this.renderTooltip(matrices, x, y);
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrices, float ticks, int x, int y) {
        //noinspection DataFlowIssue
        this.minecraft.getTextureManager().bind(TEXTURE_LOCATION);
        //noinspection deprecation
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(
                matrices,
                (this.width - this.imageWidth) >> 1,
                (this.height - this.imageHeight) >> 1,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );
    }
}
