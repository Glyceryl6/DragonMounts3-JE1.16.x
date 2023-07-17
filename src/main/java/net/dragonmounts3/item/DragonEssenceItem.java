package net.dragonmounts3.item;

import net.dragonmounts3.api.DragonType;
import net.dragonmounts3.api.IDragonTypified;
import net.dragonmounts3.entity.dragon.DragonLifeStage;
import net.dragonmounts3.entity.dragon.TameableDragonEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dragonmounts3.DragonMounts.ITEM_TRANSLATION_KEY_PREFIX;

public class DragonEssenceItem extends Item implements IDragonTypified, IDragonContainer {
    private static final String TRANSLATION_KEY = ITEM_TRANSLATION_KEY_PREFIX + "dragon_essence";

    protected DragonType type;

    public DragonEssenceItem(DragonType type, Properties properties) {
        super(properties.stacksTo(1));
        this.type = type;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World level, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.NONE);
        ItemStack stack = player.getItemInHand(hand);
        RayTraceResult.Type resultType = rayTraceResult.getType();
        if (resultType == RayTraceResult.Type.MISS) {
            return ActionResult.pass(stack);
        } else if (resultType == RayTraceResult.Type.BLOCK) {
            if (!level.isClientSide) {
                BlockPos pos = rayTraceResult.getBlockPos().relative(rayTraceResult.getDirection());
                TameableDragonEntity dragon = this.release(stack.getTag(), level, this.type);
                dragon.setDragonType(this.type, true);
                dragon.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                level.addFreshEntity(dragon);
                if (!player.abilities.instabuild) {
                    stack.shrink(1);
                }
                return ActionResult.success(stack);
            }
            return ActionResult.consume(stack);
        }
        return ActionResult.fail(stack);
    }

    @Override
    public TameableDragonEntity release(CompoundNBT compound, World level, @Nullable DragonType type) {
        TameableDragonEntity dragon = IDragonContainer.super.release(compound, level, type);
        dragon.setLifeStage(DragonLifeStage.NEWBORN, true, true);
        return dragon;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
        components.add(this.type.getText());
    }

    @Nonnull
    @Override
    public String getDescriptionId() {
        return TRANSLATION_KEY;
    }

    @Override
    public DragonType getDragonType() {
        return this.type;
    }
}
