package net.dragonmounts3.entity.dragon;

import net.dragonmounts3.DragonMountsConfig;
import net.dragonmounts3.api.DragonType;
import net.dragonmounts3.api.IDragonFood;
import net.dragonmounts3.api.IMutableDragonTypified;
import net.dragonmounts3.block.entity.DragonCoreBlockEntity;
import net.dragonmounts3.client.DragonAnimator;
import net.dragonmounts3.entity.ai.DragonBodyController;
import net.dragonmounts3.entity.ai.DragonMovementController;
import net.dragonmounts3.entity.dragon.helper.DragonBodyHelper;
import net.dragonmounts3.entity.dragon.helper.DragonHelper;
import net.dragonmounts3.init.*;
import net.dragonmounts3.inventory.DragonInventory;
import net.dragonmounts3.item.DragonArmorItem;
import net.dragonmounts3.item.DragonScalesItem;
import net.dragonmounts3.item.IDragonContainer;
import net.dragonmounts3.item.TieredShearsItem;
import net.dragonmounts3.network.SFeedDragonPacket;
import net.dragonmounts3.network.SSyncDragonAgePacket;
import net.dragonmounts3.util.DragonFood;
import net.dragonmounts3.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SaddleItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static net.dragonmounts3.network.DMPacketHandler.CHANNEL;
import static net.minecraft.entity.ai.attributes.Attributes.FLYING_SPEED;
import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;
import static net.minecraftforge.fml.network.PacketDistributor.PLAYER;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY;

/**
 * @see net.minecraft.entity.passive.horse.MuleEntity
 * @see net.minecraft.entity.passive.horse.HorseEntity
 */
@ParametersAreNonnullByDefault
public class TameableDragonEntity extends TameableEntity implements IEquipable, IForgeShearable, IMutableDragonTypified, IFlyingAnimal {
    // base attributes
    public static final double BASE_GROUND_SPEED = 0.4;
    public static final double BASE_AIR_SPEED = 0.9;
    public static final double BASE_TOUGHNESS = 30.0D;
    public static final float RESISTANCE = 10.0f;
    public static final double BASE_FOLLOW_RANGE = 70;
    public static final double BASE_FOLLOW_RANGE_FLYING = BASE_FOLLOW_RANGE * 2;
    public static final int HOME_RADIUS = 64;
    public static final double LIFTOFF_THRESHOLD = 10;
    private static final Logger LOGGER = LogManager.getLogger();

    // data value IDs
    private static final DataParameter<String> DATA_DRAGON_TYPE = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> DATA_FLYING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_AGE_LOCKED = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_BREATHING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_ALT_BREATHING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GOING_DOWN = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ALLOW_OTHER_PLAYERS = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BOOSTING = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOVER_CANCELLED = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> Y_LOCKED = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ALT_TEXTURE = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FOLLOW_YAW = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> DATA_BREEDER = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<String> VARIANT = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> DATA_REPO_COUNT = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> DATA_SHEARED = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_ADJUDICATOR_STONE = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_ELDER_STONE = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    //private static final DataParameter<Boolean> SLEEP = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> DATA_BREATH_WEAPON_TARGET = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.STRING);
    private static final DataParameter<Integer> DATA_BREATH_WEAPON_MODE = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.INT);
    public static final DataParameter<ItemStack> DATA_SADDLE_ITEM = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<ItemStack> DATA_ARMOR_ITEM = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<ItemStack> DATA_CHEST_ITEM = EntityDataManager.defineId(TameableDragonEntity.class, DataSerializers.ITEM_STACK);
    public static final String AGE_DATA_PARAMETER_KEY = "Age";
    public static final String AGE_LOCKED_DATA_PARAMETER_KEY = "AgeLocked";
    public static final String FLYING_DATA_PARAMETER_KEY = "Flying";
    public static final String SADDLE_DATA_PARAMETER_KEY = "Saddle";
    public static final String SHEARED_DATA_PARAMETER_KEY = "ShearCooldown";
    private final DragonBodyHelper dragonBodyHelper = new DragonBodyHelper(this);
    private final Map<Class<?>, DragonHelper> helpers = new HashMap<>();
    protected final GroundPathNavigator groundNavigation;
    protected final FlyingPathNavigator flyingNavigation;
    public EnderCrystalEntity healingEnderCrystal;
    /**
     * Set to null in {@link Entity#onRemovedFromWorld()} to avoid memory leaks
     */
    protected DragonAnimator animator;
    /**
     * Set to null in {@link Entity#onRemovedFromWorld()} to avoid memory leaks
     */
    protected DragonInventory inventory = new DragonInventory(this);
    protected DragonType type = DragonType.ENDER;
    protected DragonLifeStage stage = DragonLifeStage.ADULT;
    protected ItemStack saddle = ItemStack.EMPTY;
    protected ItemStack armor = ItemStack.EMPTY;
    protected ItemStack chest = ItemStack.EMPTY;
    protected boolean hasChest = false;
    protected boolean isSaddled = false;
    protected int fallingTicks = 0;
    private int shearCooldown;
    public int roarTicks;
    protected int ticksSinceLastAttack;
    private boolean isUsingBreathWeapon;
    private boolean altBreathing;
    private boolean isGoingDown;
    private boolean isUnHovered;
    private boolean yLocked;
    private boolean followYaw;

    public TameableDragonEntity(EntityType<? extends TameableDragonEntity> type, World world) {
        super(type, world);
        this.maxUpStep = 1.0F;
        this.blocksBuilding = true;
        Objects.requireNonNull(this.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(DragonMountsConfig.SERVER.base_health.get());
        this.animator = this.level.isClientSide ? new DragonAnimator(this) : null;
        this.moveControl = new DragonMovementController(this);
        this.groundNavigation = new GroundPathNavigator(this, level);
        this.groundNavigation.setCanFloat(true);
        this.flyingNavigation = new FlyingPathNavigator(this, level);
        this.flyingNavigation.setCanFloat(true);
        this.navigation = this.groundNavigation;
    }

    public TameableDragonEntity(World world) {
        this(DMEntities.TAMEABLE_DRAGON.get(), world);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, DragonMountsConfig.SERVER.base_health.get())
                .add(FLYING_SPEED, BASE_AIR_SPEED)
                .add(MOVEMENT_SPEED, BASE_GROUND_SPEED)
                .add(Attributes.ATTACK_DAMAGE, DragonMountsConfig.SERVER.base_damage.get())
                .add(Attributes.FOLLOW_RANGE, BASE_FOLLOW_RANGE)
                .add(Attributes.KNOCKBACK_RESISTANCE, RESISTANCE)
                .add(Attributes.ARMOR, DragonMountsConfig.SERVER.base_armor.get())
                .add(Attributes.ARMOR_TOUGHNESS, BASE_TOUGHNESS);
    }

    public DragonInventory getInventory() {
        return this.inventory;
    }

    public void setSaddle(@Nonnull ItemStack stack, boolean sync) {
        boolean original = this.isSaddled;
        this.isSaddled = stack.getItem() instanceof SaddleItem;
        if (!original && this.isSaddled && !this.level.isClientSide) {
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }
        if (!stack.isEmpty()) {
            stack.setCount(1);
        }
        this.saddle = stack;
        if (sync && !this.level.isClientSide) {
            this.entityData.set(DATA_SADDLE_ITEM, stack.copy());
        }
    }

    public void setArmor(@Nonnull ItemStack stack, boolean sync) {
        if (!stack.isEmpty()) {
            stack.setCount(1);
        }
        this.armor = stack;
        if (this.level.isClientSide) return;
        ModifiableAttributeInstance attribute = this.getAttribute(Attributes.ARMOR);
        if (attribute != null) {
            attribute.removeModifier(DragonArmorItem.MODIFIER_UUID);
            Item item = stack.getItem();
            if (item instanceof DragonArmorItem) {
                attribute.addTransientModifier(new AttributeModifier(
                        DragonArmorItem.MODIFIER_UUID,
                        "Dragon armor bonus",
                        ((DragonArmorItem) item).getProtection(),
                        AttributeModifier.Operation.ADDITION
                ));
            }
        }
        if (sync) {
            this.entityData.set(DATA_ARMOR_ITEM, stack.copy());
        }
    }

    public void setChest(@Nonnull ItemStack stack, boolean sync) {
        this.hasChest = stack.getItem().is(Tags.Items.CHESTS_WOODEN);
        if (!this.hasChest) {
            this.inventory.dropContents(true);
        }
        if (!stack.isEmpty()) {
            stack.setCount(1);
        }
        this.chest = stack;
        if (sync && !this.level.isClientSide) {
            this.entityData.set(DATA_CHEST_ITEM, stack.copy());
        }
    }

    public ItemStack getSaddle() {
        return this.saddle;
    }

    public ItemStack getArmor() {
        return this.armor;
    }

    public ItemStack getChest() {
        return this.chest;
    }

    public void inventoryChanged() {
    }

    public void onWingsDown(float speed) {
        if (!this.isInWater()) {
            // play wing sounds
            this.level.playLocalSound(
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP,
                    SoundCategory.VOICE,
                    (1 - speed) * this.getVoicePitch(),
                    (0.5f - speed * 0.2f) * this.getSoundVolume(),
                    true
            );
        }
    }

    /**
     * Returns the int-precision distance to solid ground.
     * Describe an inclusive limit to reduce iterations.
     */
    public double getAltitude(int limit) {
        BlockPos.Mutable pos = blockPosition().mutable().move(0, -1, 0);
        //BlockPos.Mutable min = this.level.dimensionType().minY(); -> 0
        int i = 0;
        while (i <= limit && pos.getY() > 0 && !this.level.getBlockState(pos).getMaterial().isSolid()) {
            pos.setY(((int) this.getY()) - ++i);
        }
        return i;
    }

    /**
     * Returns the distance to the ground while the entity is flying.
     */
    public double getAltitude() {
        return getAltitude(this.level.getMaxBuildHeight());
    }

    public boolean isHighEnough(int height) {
        return this.getAltitude(height) >= height;
    }

    /**
     * Returns the entity's health relative to the maximum health.
     *
     * @return health normalized between 0 and 1
     */
    public float getHealthRelative() {
        return this.getHealth() / this.getMaxHealth();
    }

    public int getMaxDeathTime() {
        return 120;
    }

    public DragonAnimator getAnimator() {
        return this.animator;
    }

    public boolean isFlying() {
        return this.entityData.get(DATA_FLYING);
    }

    protected void setFlying(boolean flying) {
        this.entityData.set(DATA_FLYING, flying);
        this.navigation = flying ? this.flyingNavigation : this.groundNavigation;
    }

    public void createDragonCore(ItemStack stack) {
        Block block = DMBlocks.DRAGON_CORE.get();
        BlockPos pos = this.blockPosition();
        boolean flag = true;
        if (this.level.isEmptyBlock(pos)) {
            BlockState state = block.defaultBlockState().setValue(HorizontalBlock.FACING, this.getDirection());
            if (this.level.setBlock(pos, state, 3)) {
                TileEntity entity = this.level.getBlockEntity(pos);
                if (entity instanceof DragonCoreBlockEntity) {
                    ((DragonCoreBlockEntity) entity).setItem(0, stack);
                    flag = false;
                }
            }
        }
        if (flag) {
            ItemStack result = new ItemStack(block.asItem());
            CompoundNBT compound = new CompoundNBT();
            CompoundNBT blockEntityTag = new CompoundNBT();
            ListNBT items = new ListNBT();
            CompoundNBT object = new CompoundNBT();
            object.putByte("Slot", (byte) 0);
            stack.save(object);
            items.add(object);
            blockEntityTag.put("Items", items);
            compound.put("BlockEntityTag", blockEntityTag);
            result.setTag(compound);
            ItemEntity item = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), result);
            this.level.addFreshEntity(item);

        }
    }

    //----------Entity----------

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_DRAGON_TYPE, DragonType.ENDER.getSerializedName());
        this.entityData.define(DATA_FLYING, false);
        this.entityData.define(DATA_SHEARED, false);
        this.entityData.define(DATA_AGE_LOCKED, false);
        this.entityData.define(DATA_SADDLE_ITEM, ItemStack.EMPTY);
        this.entityData.define(DATA_ARMOR_ITEM, ItemStack.EMPTY);
        this.entityData.define(DATA_CHEST_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(DragonType.DATA_PARAMETER_KEY, this.entityData.get(DATA_DRAGON_TYPE));
        compound.putString(DragonLifeStage.DATA_PARAMETER_KEY, this.stage.getSerializedName());
        compound.putBoolean(FLYING_DATA_PARAMETER_KEY, this.entityData.get(DATA_FLYING));
        compound.putBoolean(AGE_LOCKED_DATA_PARAMETER_KEY, this.entityData.get(DATA_AGE_LOCKED));
        compound.putInt(SHEARED_DATA_PARAMETER_KEY, this.isSheared() ? this.shearCooldown : 0);
        ListNBT items = this.inventory.createTag();
        if (!items.isEmpty()) {
            compound.put("Items", items);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        int age = this.age;
        DragonLifeStage stage = this.stage;
        if (compound.contains(DragonLifeStage.DATA_PARAMETER_KEY)) {
            this.setLifeStage(DragonLifeStage.byName(compound.getString(DragonLifeStage.DATA_PARAMETER_KEY)), false, false);
        }
        super.readAdditionalSaveData(compound);
        if (!this.firstTick && (this.age != age || stage != this.stage)) {
            CHANNEL.send(TRACKING_ENTITY.with(() -> this), new SSyncDragonAgePacket(this));
        }
        this.setDragonType(DragonType.byName(compound.getString(DragonType.DATA_PARAMETER_KEY)));
        if (compound.contains(FLYING_DATA_PARAMETER_KEY)) {
            this.entityData.set(DATA_FLYING, compound.getBoolean(FLYING_DATA_PARAMETER_KEY));
        }
        if (compound.contains(SADDLE_DATA_PARAMETER_KEY)) {
            this.setSaddle(ItemStack.of(compound.getCompound(SADDLE_DATA_PARAMETER_KEY)), true);
        }
        if (compound.contains(SHEARED_DATA_PARAMETER_KEY)) {
            this.setSheared(compound.getInt(SHEARED_DATA_PARAMETER_KEY));
        }
        if (compound.contains(AGE_LOCKED_DATA_PARAMETER_KEY)) {
            this.entityData.set(DATA_AGE_LOCKED, compound.getBoolean(AGE_LOCKED_DATA_PARAMETER_KEY));
        }
        if (compound.contains("Items")) {
            this.inventory.fromTag(compound.getList("Items", 10));
        }
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> key) {
        if (DATA_DRAGON_TYPE.equals(key)) {
            this.type = DragonType.byName(this.entityData.get(DATA_DRAGON_TYPE));
        } else if (DATA_SADDLE_ITEM.equals(key)) {
            this.saddle = this.entityData.get(DATA_SADDLE_ITEM);
            this.isSaddled = this.saddle.getItem() instanceof SaddleItem;
        } else if (DATA_ARMOR_ITEM.equals(key)) {
            this.armor = this.entityData.get(DATA_ARMOR_ITEM);
        } else if (DATA_CHEST_ITEM.equals(key)) {
            this.chest = this.entityData.get(DATA_CHEST_ITEM);
            this.hasChest = this.chest.getItem().getItem().is(Tags.Items.CHESTS_WOODEN);
        } else {
            super.onSyncedDataUpdated(key);
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        this.animator = null;
        this.inventory = null;
    }

    @Override
    public void aiStep() {
        int age = this.age;
        if (this.level.isClientSide) {
            super.aiStep();
            this.animator.tick();
            if (!this.isAgeLocked()) {
                if (age < 0) {
                    ++this.age;
                } else if (age > 0) {
                    --this.age;
                }
            }
        } else {
            if (this.shearCooldown > 0) {
                this.setSheared(this.shearCooldown - 1);
            }
            if (this.isAgeLocked()) {
                this.age = 0;
                super.aiStep();
                this.age = age;
            } else {
                super.aiStep();
            }
            if (this.isOnGround()) {
                this.fallingTicks = 0;
                this.setFlying(false);
            } else {
                this.setFlying(++this.fallingTicks > LIFTOFF_THRESHOLD && !this.isBaby() && this.getControllingPassenger() != null);
                /*if (this.isFlying() != flag) {
                    getEntityAttribute(FOLLOW_RANGE).setBaseValue(getDragonSpeed());
                }*/
            }
        }
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity entity) {
        return null;
    }

    public boolean hasChest() {
        return this.hasChest;
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public float getScale() {
        return DragonLifeStage.getSize(this.stage, this.age);
    }

    @Nonnull
    @Override
    public EntitySize getDimensions(Pose pose) {
        return this.getType().getDimensions().scale(DragonLifeStage.getSize(this.stage, this.stage.duration >> 1));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return DragonFood.test(stack.getItem());
    }

    @Nonnull
    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if (this.level.isClientSide) {
            if (DragonFood.test(item) || item.is(Tags.Items.CHESTS_WOODEN) || item instanceof SaddleItem || item instanceof DragonArmorItem || this.isOwnedBy(player)) {
                return ActionResultType.CONSUME;
            }
            return ActionResultType.PASS;
        }
        IDragonFood food = DragonFood.get(item);
        if (food != null) {
            if (!food.isEatable(this, player, stack, hand)) return ActionResultType.FAIL;
            food.eat(this, player, stack, hand);
            CHANNEL.send(TRACKING_ENTITY.with(() -> this), new SFeedDragonPacket(this, item));
            return ActionResultType.SUCCESS;
        }
        if (this.isOwnedBy(player)) {
            if (this.getSaddle().isEmpty() && item instanceof SaddleItem) {
                this.isSaddled = true;//avoid playing sound
                this.setSaddle(stack.copy(), true);
                item.interactLivingEntity(stack, player, this, hand);//play sound, shrink stack
                return ActionResultType.SUCCESS;
            }
            if (this.getArmor().isEmpty() && item instanceof DragonArmorItem) {
                this.setArmor(stack.split(1), true);
                return ActionResultType.SUCCESS;
            }
            if (this.getChest().isEmpty() && item.is(Tags.Items.CHESTS_WOODEN)) {
                this.setArmor(stack.split(1), true);
                return ActionResultType.SUCCESS;
            }
            ActionResultType result = item.interactLivingEntity(stack, player, this, hand);
            if (result.consumesAction()) return result;
            boolean flag = player.isSecondaryUseActive();
            if (this.isBaby() && !flag) {
                /*this.setAttackTarget(null);
                this.getNavigator().clearPath();
                this.getAISit().setSitting(false);*/
                this.startRiding(player, true);
                return ActionResultType.SUCCESS;
            }
            if (!this.isSaddled || flag || this.isVehicle()) {
                NetworkHooks.openGui((ServerPlayerEntity) player, this.inventory, buffer -> buffer.writeVarInt(this.getId()));
            } else {
                player.yRot = this.yRot;
                player.xRot = this.xRot;
                player.startRiding(this);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    /*@Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }*/

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level.isClientSide && this.isTame()) {
            this.setLifeStage(DragonLifeStage.NEWBORN, true, false);
            this.createDragonCore(IDragonContainer.fill(this, DMItems.DRAGON_ESSENCE.get(this.getDragonType()), true));
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.inventory.dropContents(false);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        Entity entity = source.getEntity();
        return (entity != null && (entity == this || this.hasPassenger(entity))) || super.isInvulnerableTo(source) || this.getDragonType().isInvulnerableTo(source);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        CHANNEL.send(PLAYER.with(() -> player), new SSyncDragonAgePacket(this));
    }

    public void setLifeStage(DragonLifeStage stage, boolean reset, boolean sync) {
        if (this.stage == stage) return;
        this.stage = stage;
        if (reset) {
            this.refreshAge();
        }
        this.refreshDimensions();
        this.reapplyPosition();
        if (sync && !this.level.isClientSide) {
            CHANNEL.send(TRACKING_ENTITY.with(() -> this), new SSyncDragonAgePacket(this));
        }
    }

    public final DragonLifeStage getLifeStage() {
        return this.stage;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(DMItems.DRAGON_SPAWN_EGG.get(this.getDragonType()));
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singletonList(this.getArmor());
    }

    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> passengers = this.getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public void onPassengerTurned(Entity entity) {
        entity.setYBodyRot(this.yRot);
        float angle = MathHelper.wrapDegrees(entity.yRot - this.yRot);
        float clamped = MathHelper.clamp(angle, -60.0f, 60.0f);
        entity.yRotO += clamped - angle;
        entity.yRot += clamped - angle;
        entity.setYHeadRot(entity.yRot);
    }

    @Override
    public void positionRider(Entity passenger) {
        int index = this.getPassengers().indexOf(passenger);
        if (index >= 0) {
            Vector3d pos = this.type.passengerOffset.apply(index, /*this.isInSittingPose()*/false)
                    .scale(this.getScale())
                    .yRot((float) Math.toRadians(-this.yBodyRot))
                    .add(this.position());
            passenger.setPos(pos.x, pos.y, pos.z);
            if (index == 0) {
                this.onPassengerTurned(passenger);
                passenger.xRotO = passenger.xRot;
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.type.passengerOffset.apply(0, this.isInSittingPose()).y * this.getScale();
    }

    @Override
    public float getEyeHeight(Pose pose) {
        float height = super.getStandingEyeHeight(pose, this.getDimensions(pose));
        return this.isInSittingPose() ? height * 0.8f : height;
    }

    //----------LivingEntity----------

    //TODO: Override getItemBySlot

    @Override
    public void travel(Vector3d travelVector) {
        boolean flying = isFlying();
        float speed = (float) (flying ? this.getAttributeValue(FLYING_SPEED) * 0.25f : this.getAttributeValue(MOVEMENT_SPEED));
        this.setSpeed(speed);
        if (this.canBeControlledByRider()) {// Were being controlled; override ai movement
            LivingEntity driver = (LivingEntity) this.getControllingPassenger();
            if (driver == null) return;
            double moveForward = Math.sqrt(driver.zza * driver.zza + driver.xxa * driver.xxa);
            // rotate head to match driver.
            float yaw = driver.yHeadRot;
            if (moveForward > 0) {// rotate in the direction of the drivers controls
                yaw += (float) MathHelper.atan2(driver.zza, driver.xxa) * (180f / MathUtil.PI) - 90;
            }
            this.yHeadRot = yaw;
            this.xRot *= 0.68f;
            // rotate body towards the head
            this.yRot = (MathHelper.rotateIfNecessary(this.yRot, this.yHeadRot, 45));
            if (this.isControlledByLocalInstance()) {// Client applies motion
                boolean jumping = Minecraft.getInstance().options.keyJump.isDown();
                boolean descending = DMKeyBindings.DESCENT.isDown();
                if (flying) {
                    double moveY = (jumping ? 1 : 0) + (descending ? -1 : 0);
                    if (moveForward > 0) {
                        //moveForward *= travelVector.z;
                        if (DragonMountsConfig.CLIENT.camera_control.get()) {
                            moveY = MathHelper.clamp(moveY - driver.xRot / 90.0d, -1, 1);
                        }
                    } else {
                        moveForward = 0;
                    }
                    travelVector = new Vector3d(travelVector.x, moveY, moveForward);
                } else {
                    travelVector = new Vector3d(travelVector.x, travelVector.y, moveForward);
                    if (jumping) {
                        this.jumpFromGround();
                    }
                }
            } else if (driver instanceof PlayerEntity) {// other clients receive animations
                this.calculateEntityAnimation(this, true);
                this.setDeltaMovement(Vector3d.ZERO);
                return;
            }
        }
        if (flying) {
            // Move relative to yaw - handled in the move controller or by driver
            this.moveRelative(speed, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getDeltaMovement().lengthSqr() < 0.1) // we're not actually going anywhere, bob up and down.
                this.setDeltaMovement(this.getDeltaMovement().add(0, Math.sin(this.tickCount / 4f) * 0.03, 0));
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9f)); // smoothly slow down
            this.calculateEntityAnimation(this, true);
        } else {
            super.travel(travelVector);
        }
    }


    //----------MobEntity----------

    @Nonnull
    @Override
    protected DragonBodyController createBodyControl() {
        return new DragonBodyController(this);
    }

    @Override
    public int getMaxHeadYRot() {
        return 90;
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    //TODO: Override setSlot

    //----------AgeableEntity----------

    public void applyPacket(SSyncDragonAgePacket packet) {
        this.age = packet.age;
        this.setLifeStage(packet.stage, false, false);
    }

    protected void refreshAge() {
        switch (this.stage) {
            case NEWBORN:
            case INFANT:
                this.age = -this.stage.duration;
                break;
            case JUVENILE:
            case PREJUVENILE:
                this.age = this.stage.duration;
                break;
            default:
                this.age = 0;
        }
    }

    public void setAgeLocked(boolean locked) {
        this.entityData.set(DATA_AGE_LOCKED, locked);
    }

    public boolean isAgeLocked() {
        return this.entityData.get(DATA_AGE_LOCKED);
    }

    @Override
    protected void ageBoundaryReached() {
        this.setLifeStage(DragonLifeStage.byId(this.stage.ordinal() + 1), true, false);
    }

    /**
     * Call the field {@link TameableDragonEntity#age} directly for internal use.
     */
    public void setAgeAsync(int age) {
        this.age = age;
    }

    @Override
    public void setAge(int age) {
        if (this.age == age) return;
        if (this.level.isClientSide) {
            this.age = age;
            return;
        }
        if (this.age < 0 && age >= 0 || this.age > 0 && age <= 0) {
            this.ageBoundaryReached();
        } else {
            this.age = age;
        }
        CHANNEL.send(TRACKING_ENTITY.with(() -> this), new SSyncDragonAgePacket(this));
    }

    @Override
    public void ageUp(int delta, boolean forced) {
        int backup = this.age;
        //Notice:                                ↓↓           ↓↓                             ↓↓           ↓↓
        if (!this.isAgeLocked() && (this.age < 0 && (this.age += delta) >= 0 || this.age > 0 && (this.age -= delta) <= 0)) {
            this.ageBoundaryReached();
            if (forced) {
                this.forcedAge += backup < 0 ? -backup : backup;
            }
        }
    }

    @Override
    public final int getAge() {
        return this.age;
    }

    @Override
    public void setBaby(boolean value) {
        this.setLifeStage(value ? DragonLifeStage.NEWBORN : DragonLifeStage.ADULT, true, true);
    }

    public void refreshForcedAgeTimer() {
        if (this.forcedAgeTimer <= 0) {
            this.forcedAgeTimer = 40;
        }
    }

    //----------IMutableDragonTypified----------

    public void setDragonType(DragonType type, boolean reset) {
        AttributeModifierManager manager = this.getAttributes();
        manager.removeAttributeModifiers(this.getDragonType().getAttributeModifiers());
        this.entityData.set(DATA_DRAGON_TYPE, type.getSerializedName());
        manager.addTransientAttributeModifiers(type.getAttributeModifiers());
        if (reset) {
            ModifiableAttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
            if (health != null) {
                this.setHealth((float) health.getValue());
            }
        }
    }

    @Override
    public void setDragonType(DragonType type) {
        this.setDragonType(type, false);
    }

    @Override
    public DragonType getDragonType() {
        return this.type;
    }

    //----------IForgeShearable----------

    public boolean isSheared() {
        return this.entityData.get(DATA_SHEARED);
    }

    public void setSheared(int cooldown) {
        this.shearCooldown = cooldown;
        this.entityData.set(DATA_SHEARED, cooldown > 0);
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack stack, World world, BlockPos pos) {
        Item item = stack.getItem();
        return this.isAlive() && this.stage.ordinal() >= 2 && !this.isSheared() && item instanceof TieredShearsItem && ((TieredShearsItem) item).getTier().getLevel() >= 3;
    }

    //----------IEquipable----------

    @Override
    public boolean isSaddled() {
        return this.isSaddled;
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void equipSaddle(@Nullable SoundCategory category) {
        if (category != null) {
            this.level.playSound(null, this, SoundEvents.HORSE_SADDLE, category, 0.5F, 1.0F);
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack stack, World world, BlockPos pos, int fortune) {
        DragonScalesItem scale = DMItems.DRAGON_SCALES.get(this.getDragonType());
        if (scale != null) {
            this.setSheared(2500 + this.random.nextInt(1000));
            this.playSound(DMSounds.DRAGON_GROWL.get(), 1.0F, 1.0F);
            return Collections.singletonList(new ItemStack(scale, 2 + this.random.nextInt(3)));
        }
        return Collections.emptyList();
    }
}