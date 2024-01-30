package net.dragonmounts3.api;

import net.dragonmounts3.entity.dragon.DragonLifeStage;
import net.dragonmounts3.entity.dragon.TameableDragonEntity;
import net.dragonmounts3.util.math.MathUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

@FunctionalInterface
public interface IDragonFood {
    void eat(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack, Hand hand);

    default boolean isEatable(TameableDragonEntity dragon, PlayerEntity player, ItemStack stack, Hand hand) {
        return true;
    }

    default void act(TameableDragonEntity dragon, Item item) {
        if (item == Items.AIR) return;
        World level = dragon.level;
        if (dragon.getLifeStage() != DragonLifeStage.ADULT) {
            dragon.refreshForcedAgeTimer();
        }
        //noinspection DataFlowIssue
        Vector3d pos = dragon.animator.getThroatPosition(0, 0, -4);
        if (pos == null) return;
        level.playLocalSound(pos.x, pos.y, pos.z, item.getEatingSound(), SoundCategory.NEUTRAL, 1F, 0.75F, false);
        if (item == Items.HONEY_BOTTLE) return;
        if (item instanceof BucketItem) {
            level.playLocalSound(pos.x, pos.y, pos.z, item.getDrinkingSound(), SoundCategory.NEUTRAL, 0.25F, 0.75F, false);
            if (item == Items.COD_BUCKET) {
                item = Items.COD;
            } else if (item == Items.SALMON_BUCKET) {
                item = Items.SALMON;
            } else {
                item = Items.TROPICAL_FISH;
            }
        }
        ItemStack stack = new ItemStack(item);
        Random random = dragon.getRandom();
        for (int i = 0; i < 8; ++i) {
            Vector3d speed = new Vector3d((random.nextFloat() - 0.5D) * 0.1D, random.nextFloat() * 0.1D + 0.1D, 0.0D).xRot(-dragon.xRot * MathUtil.PI / 180F).yRot(-dragon.yRot * MathUtil.PI / 180F);
            level.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), pos.x, pos.y, pos.z, speed.x, speed.y + 0.05D, speed.z);
        }
    }
}
