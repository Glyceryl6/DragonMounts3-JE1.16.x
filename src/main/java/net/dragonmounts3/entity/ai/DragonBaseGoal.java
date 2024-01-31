package net.dragonmounts3.entity.ai;

import net.dragonmounts3.entity.dragon.TameableDragonEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class DragonBaseGoal extends Goal {

    protected TameableDragonEntity dragon;
    protected World world;
    protected Random random;
    protected PlayerEntity rider;

    public DragonBaseGoal(TameableDragonEntity dragon) {
        this.dragon = dragon;
        this.world = dragon.level;
        this.random = dragon.getRandom();
        this.rider = dragon.getControllingPlayer();
    }

    protected boolean tryMoveToBlockPos(BlockPos pos, double speed) {
        return dragon.getNavigation().moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, speed);
    }

    protected double getFollowRange() {
        return dragon.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

}