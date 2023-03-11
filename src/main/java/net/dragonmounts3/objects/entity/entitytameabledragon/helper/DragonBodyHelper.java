package net.dragonmounts3.objects.entity.entitytameabledragon.helper;

import net.dragonmounts3.objects.entity.entitytameabledragon.EntityTameableDragon;
import net.dragonmounts3.util.math.MathX;
import net.minecraft.entity.ai.controller.BodyController;

public class DragonBodyHelper extends BodyController {

    private final EntityTameableDragon dragon;
    private float prevRotationYawHead;
    private int turnTicks;

    public DragonBodyHelper(EntityTameableDragon dragon) {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void clientTick() {
        double deltaX = this.dragon.getX() - this.dragon.xo;
        double deltaY = this.dragon.getZ() - this.dragon.zo;
        double dist = deltaX * deltaX + deltaY * deltaY;
        float maxHeadBodyAngleDifference = 90.0F;
        // rotate instantly if flying, sitting or moving
        if (this.dragon.isFlying() || dragon.isTame() || dist > 0.0001) {
            this.dragon.yBodyRot = dragon.yRot;
            this.dragon.yHeadRot = MathX.updateRotation(this.dragon.yBodyRot, this.dragon.yHeadRot, maxHeadBodyAngleDifference);
            this.prevRotationYawHead = dragon.yHeadRot;
            this.turnTicks = 0;
            return;
        }

        double changeInHeadYaw = Math.abs(this.dragon.yHeadRot - this.prevRotationYawHead);
        if (changeInHeadYaw > 15) {
            this.turnTicks = 0;
            this.prevRotationYawHead = this.dragon.yHeadRot;
        } else {
            this.turnTicks++;
            if (this.turnTicks > 20) {
                maxHeadBodyAngleDifference = Math.max(1 - (float) (this.turnTicks - 20) / 20, 0) * 75;
            }
        }

        float yHeadRot = this.dragon.getYHeadRot();
        this.dragon.yBodyRot = MathX.constrainAngle(this.dragon.yBodyRot, yHeadRot, maxHeadBodyAngleDifference);
        this.dragon.yRot = this.dragon.yBodyRot;
    }

}