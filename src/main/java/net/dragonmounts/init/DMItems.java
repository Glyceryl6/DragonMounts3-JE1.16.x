package net.dragonmounts.init;

import mcp.MethodsReturnNonnullByDefault;
import net.dragonmounts.DragonMounts;
import net.dragonmounts.api.DragonScaleArmorSuit;
import net.dragonmounts.api.DragonScaleMaterial;
import net.dragonmounts.api.DragonScaleTier;
import net.dragonmounts.api.IDragonScaleArmorEffect;
import net.dragonmounts.block.HatchableDragonEggBlock;
import net.dragonmounts.client.renderer.DMItemStackTileEntityRenderer;
import net.dragonmounts.item.*;
import net.dragonmounts.registry.DragonType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.dragonmounts.api.DragonScaleArmorSuit.DRAGONMOUNTS_TOOL_TAB;
import static net.dragonmounts.init.DMItemGroups.*;

@MethodsReturnNonnullByDefault
public class DMItems {
    public static final DeferredRegister<Item> ITEMS = DragonMounts.create(ForgeRegistries.ITEMS);
    public static final IDispenseItemBehavior EQUIPMENT_BEHAVIOR = new OptionalDispenseBehavior() {
        @Nonnull
        protected ItemStack execute(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
            this.setSuccess(ArmorItem.dispenseArmor(source, stack));
            return stack;
        }
    };
    public static final Supplier<Callable<ItemStackTileEntityRenderer>> GET_DMISTER = () -> DMItemStackTileEntityRenderer::getInstance;
    //Scales Start
    public static final DragonScalesItem FOREST_DRAGON_SCALES = createDragonScalesItem("forest_dragon_scales", DragonTypes.FOREST, item());
    public static final DragonScalesItem FIRE_DRAGON_SCALES = createDragonScalesItem("fire_dragon_scales", DragonTypes.FIRE, item());
    public static final DragonScalesItem ICE_DRAGON_SCALES = createDragonScalesItem("ice_dragon_scales", DragonTypes.ICE, item());
    public static final DragonScalesItem WATER_DRAGON_SCALES = createDragonScalesItem("water_dragon_scales", DragonTypes.WATER, item());
    public static final DragonScalesItem AETHER_DRAGON_SCALES = createDragonScalesItem("aether_dragon_scales", DragonTypes.AETHER, item());
    public static final DragonScalesItem NETHER_DRAGON_SCALES = createDragonScalesItem("nether_dragon_scales", DragonTypes.NETHER, item());
    public static final DragonScalesItem ENDER_DRAGON_SCALES = createDragonScalesItem("ender_dragon_scales", DragonTypes.ENDER, item());
    public static final DragonScalesItem SUNLIGHT_DRAGON_SCALES = createDragonScalesItem("sunlight_dragon_scales", DragonTypes.SUNLIGHT, item());
    public static final DragonScalesItem ENCHANT_DRAGON_SCALES = createDragonScalesItem("enchant_dragon_scales", DragonTypes.ENCHANT, item());
    public static final DragonScalesItem STORM_DRAGON_SCALES = createDragonScalesItem("storm_dragon_scales", DragonTypes.STORM, item());
    public static final DragonScalesItem TERRA_DRAGON_SCALES = createDragonScalesItem("terra_dragon_scales", DragonTypes.TERRA, item());
    public static final DragonScalesItem ZOMBIE_DRAGON_SCALES = createDragonScalesItem("zombie_dragon_scales", DragonTypes.ZOMBIE, item());
    public static final DragonScalesItem MOONLIGHT_DRAGON_SCALES = createDragonScalesItem("moonlight_dragon_scales", DragonTypes.MOONLIGHT, item());
    public static final DragonScalesItem SCULK_DRAGON_SCALES = createDragonScalesItem("sculk_dragon_scales", DragonTypes.SCULK, item().fireResistant());
    //Dragon Armor
    public static final DragonArmorItem IRON_DRAGON_ARMOR = createDragonArmorItem("iron_dragon_armor", DragonArmorItem.TEXTURE_PREFIX + "iron.png", 5, tool().stacksTo(1));
    public static final DragonArmorItem GOLDEN_DRAGON_ARMOR = createDragonArmorItem("golden_dragon_armor", DragonArmorItem.TEXTURE_PREFIX + "gold.png", 7, tool().stacksTo(1));
    public static final DragonArmorItem DIAMOND_DRAGON_ARMOR = createDragonArmorItem("diamond_dragon_armor", DragonArmorItem.TEXTURE_PREFIX + "diamond.png", 11, tool().stacksTo(1));
    public static final DragonArmorItem EMERALD_DRAGON_ARMOR = createDragonArmorItem("emerald_dragon_armor", DragonArmorItem.TEXTURE_PREFIX + "emerald.png", 11, tool().stacksTo(1));
    public static final DragonArmorItem NETHERITE_DRAGON_ARMOR = createDragonArmorItem("netherite_dragon_armor", DragonArmorItem.TEXTURE_PREFIX + "netherite.png", 15, tool().stacksTo(1).fireResistant());
    //Dragon Scale Swords
    public static final DragonScaleSwordItem AETHER_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("aether_dragon_sword", DragonScaleTier.AETHER, tool());
    public static final DragonScaleSwordItem WATER_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("water_dragon_sword", DragonScaleTier.WATER, tool());
    public static final DragonScaleSwordItem ICE_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("ice_dragon_sword", DragonScaleTier.ICE, tool());
    public static final DragonScaleSwordItem FIRE_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("fire_dragon_sword", DragonScaleTier.FIRE, tool());
    public static final DragonScaleSwordItem FOREST_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("forest_dragon_sword", DragonScaleTier.FOREST, tool());
    public static final DragonScaleSwordItem NETHER_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("nether_dragon_sword", DragonScaleTier.NETHER, tool());
    public static final DragonScaleSwordItem ENDER_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("ender_dragon_sword", DragonScaleTier.ENDER, tool());
    public static final DragonScaleSwordItem ENCHANT_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("enchant_dragon_sword", DragonScaleTier.ENCHANT, tool());
    public static final DragonScaleSwordItem SUNLIGHT_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("sunlight_dragon_sword", DragonScaleTier.SUNLIGHT, tool());
    public static final DragonScaleSwordItem MOONLIGHT_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("moonlight_dragon_sword", DragonScaleTier.MOONLIGHT, tool());
    public static final DragonScaleSwordItem STORM_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("storm_dragon_sword", DragonScaleTier.STORM, tool());
    public static final DragonScaleSwordItem TERRA_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("terra_dragon_sword", DragonScaleTier.TERRA, tool());
    public static final DragonScaleSwordItem ZOMBIE_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("zombie_dragon_sword", DragonScaleTier.ZOMBIE, tool());
    public static final DragonScaleSwordItem SCULK_DRAGON_SCALE_SWORD = createDragonScaleSwordItem("sculk_dragon_sword", DragonScaleTier.SCULK, tool().fireResistant());
    //Dragon Scale Axes
    public static final DragonScaleAxeItem AETHER_DRAGON_SCALE_AXE = createDragonScaleAxeItem("aether_dragon_axe", DragonScaleTier.AETHER, tool());
    public static final DragonScaleAxeItem WATER_DRAGON_SCALE_AXE = createDragonScaleAxeItem("water_dragon_axe", DragonScaleTier.WATER, tool());
    public static final DragonScaleAxeItem ICE_DRAGON_SCALE_AXE = createDragonScaleAxeItem("ice_dragon_axe", DragonScaleTier.ICE, tool());
    public static final DragonScaleAxeItem FIRE_DRAGON_SCALE_AXE = createDragonScaleAxeItem("fire_dragon_axe", DragonScaleTier.FIRE, tool());
    public static final DragonScaleAxeItem FOREST_DRAGON_SCALE_AXE = createDragonScaleAxeItem("forest_dragon_axe", DragonScaleTier.FOREST, tool());
    public static final DragonScaleAxeItem NETHER_DRAGON_SCALE_AXE = createDragonScaleAxeItem("nether_dragon_axe", DragonScaleTier.NETHER, 6.0F, -2.9F, tool());
    public static final DragonScaleAxeItem ENDER_DRAGON_SCALE_AXE = createDragonScaleAxeItem("ender_dragon_axe", DragonScaleTier.ENDER, 6.0F, -2.9F, tool());
    public static final DragonScaleAxeItem ENCHANT_DRAGON_SCALE_AXE = createDragonScaleAxeItem("enchant_dragon_axe", DragonScaleTier.ENCHANT, tool());
    public static final DragonScaleAxeItem SUNLIGHT_DRAGON_SCALE_AXE = createDragonScaleAxeItem("sunlight_dragon_axe", DragonScaleTier.SUNLIGHT, tool());
    public static final DragonScaleAxeItem MOONLIGHT_DRAGON_SCALE_AXE = createDragonScaleAxeItem("moonlight_dragon_axe", DragonScaleTier.MOONLIGHT, tool());
    public static final DragonScaleAxeItem STORM_DRAGON_SCALE_AXE = createDragonScaleAxeItem("storm_dragon_axe", DragonScaleTier.STORM, tool());
    public static final DragonScaleAxeItem TERRA_DRAGON_SCALE_AXE = createDragonScaleAxeItem("terra_dragon_axe", DragonScaleTier.TERRA, tool());
    public static final DragonScaleAxeItem ZOMBIE_DRAGON_SCALE_AXE = createDragonScaleAxeItem("zombie_dragon_axe", DragonScaleTier.ZOMBIE, tool());
    public static final DragonScaleAxeItem SCULK_DRAGON_SCALE_AXE = createDragonScaleAxeItem("sculk_dragon_axe", DragonScaleTier.SCULK, tool().fireResistant());
    //Dragon Scale Bows
    public static final DragonScaleBowItem AETHER_DRAGON_SCALE_BOW = createDragonScaleBowItem("aether_dragon_scale_bow", DragonScaleTier.AETHER, tool());
    public static final DragonScaleBowItem WATER_DRAGON_SCALE_BOW = createDragonScaleBowItem("water_dragon_scale_bow", DragonScaleTier.WATER, tool());
    public static final DragonScaleBowItem ICE_DRAGON_SCALE_BOW = createDragonScaleBowItem("ice_dragon_scale_bow", DragonScaleTier.ICE, tool());
    public static final DragonScaleBowItem FIRE_DRAGON_SCALE_BOW = createDragonScaleBowItem("fire_dragon_scale_bow", DragonScaleTier.FIRE, tool());
    public static final DragonScaleBowItem FOREST_DRAGON_SCALE_BOW = createDragonScaleBowItem("forest_dragon_scale_bow", DragonScaleTier.FOREST, tool());
    public static final DragonScaleBowItem NETHER_DRAGON_SCALE_BOW = createDragonScaleBowItem("nether_dragon_scale_bow", DragonScaleTier.NETHER, tool());
    public static final DragonScaleBowItem ENDER_DRAGON_SCALE_BOW = createDragonScaleBowItem("ender_dragon_scale_bow", DragonScaleTier.ENDER, tool());
    public static final DragonScaleBowItem ENCHANT_DRAGON_SCALE_BOW = createDragonScaleBowItem("enchant_dragon_scale_bow", DragonScaleTier.ENCHANT, tool());
    public static final DragonScaleBowItem SUNLIGHT_DRAGON_SCALE_BOW = createDragonScaleBowItem("sunlight_dragon_scale_bow", DragonScaleTier.SUNLIGHT, tool());
    public static final DragonScaleBowItem MOONLIGHT_DRAGON_SCALE_BOW = createDragonScaleBowItem("moonlight_dragon_scale_bow", DragonScaleTier.MOONLIGHT, tool());
    public static final DragonScaleBowItem STORM_DRAGON_SCALE_BOW = createDragonScaleBowItem("storm_dragon_scale_bow", DragonScaleTier.STORM, tool());
    public static final DragonScaleBowItem TERRA_DRAGON_SCALE_BOW = createDragonScaleBowItem("terra_dragon_scale_bow", DragonScaleTier.TERRA, tool());
    public static final DragonScaleBowItem ZOMBIE_DRAGON_SCALE_BOW = createDragonScaleBowItem("zombie_dragon_scale_bow", DragonScaleTier.ZOMBIE, tool());
    public static final DragonScaleBowItem SCULK_DRAGON_SCALE_BOW = createDragonScaleBowItem("sculk_dragon_scale_bow", DragonScaleTier.SCULK, tool().fireResistant());
    //Dragon Scale Shields
    public static final DragonScaleShieldItem AETHER_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("aether_dragon_scale_shield", DragonScaleMaterial.AETHER, tool());
    public static final DragonScaleShieldItem WATER_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("water_dragon_scale_shield", DragonScaleMaterial.WATER, tool());
    public static final DragonScaleShieldItem ICE_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("ice_dragon_scale_shield", DragonScaleMaterial.ICE, tool());
    public static final DragonScaleShieldItem FIRE_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("fire_dragon_scale_shield", DragonScaleMaterial.FIRE, tool());
    public static final DragonScaleShieldItem FOREST_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("forest_dragon_scale_shield", DragonScaleMaterial.FOREST, tool());
    public static final DragonScaleShieldItem NETHER_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("nether_dragon_scale_shield", DragonScaleMaterial.NETHER, tool());
    public static final DragonScaleShieldItem ENDER_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("ender_dragon_scale_shield", DragonScaleMaterial.ENDER, tool());
    public static final DragonScaleShieldItem ENCHANT_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("enchant_dragon_scale_shield", DragonScaleMaterial.ENCHANT, tool());
    public static final DragonScaleShieldItem SUNLIGHT_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("sunlight_dragon_scale_shield", DragonScaleMaterial.SUNLIGHT, tool());
    public static final DragonScaleShieldItem MOONLIGHT_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("moonlight_dragon_scale_shield", DragonScaleMaterial.MOONLIGHT, tool());
    public static final DragonScaleShieldItem STORM_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("storm_dragon_scale_shield", DragonScaleMaterial.STORM, tool());
    public static final DragonScaleShieldItem TERRA_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("terra_dragon_scale_shield", DragonScaleMaterial.TERRA, tool());
    public static final DragonScaleShieldItem ZOMBIE_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("zombie_dragon_scale_shield", DragonScaleMaterial.ZOMBIE, tool());
    public static final DragonScaleShieldItem SCULK_DRAGON_SCALE_SHIELD = createDragonScaleShieldItem("sculk_dragon_scale_shield", DragonScaleMaterial.SCULK, tool().fireResistant());
    //Dragon Scale Tools - Aether
    public static final DragonScaleShovelItem AETHER_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("aether_dragon_shovel", DragonScaleTier.AETHER, tool());
    public static final DragonScalePickaxeItem AETHER_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("aether_dragon_pickaxe", DragonScaleTier.AETHER, tool());
    public static final DragonScaleHoeItem AETHER_DRAGON_SCALE_HOE = createDragonScaleHoeItem("aether_dragon_hoe", DragonScaleTier.AETHER, tool());
    //Dragon Scale Tools - Water
    public static final DragonScaleShovelItem WATER_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("water_dragon_shovel", DragonScaleTier.WATER, tool());
    public static final DragonScalePickaxeItem WATER_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("water_dragon_pickaxe", DragonScaleTier.WATER, tool());
    public static final DragonScaleHoeItem WATER_DRAGON_SCALE_HOE = createDragonScaleHoeItem("water_dragon_hoe", DragonScaleTier.WATER, tool());
    //Dragon Scale Tools - Ice
    public static final DragonScaleShovelItem ICE_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("ice_dragon_shovel", DragonScaleTier.ICE, tool());
    public static final DragonScalePickaxeItem ICE_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("ice_dragon_pickaxe", DragonScaleTier.ICE, tool());
    public static final DragonScaleHoeItem ICE_DRAGON_SCALE_HOE = createDragonScaleHoeItem("ice_dragon_hoe", DragonScaleTier.ICE, tool());
    //Dragon Scale Tools - Fire
    public static final DragonScaleShovelItem FIRE_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("fire_dragon_shovel", DragonScaleTier.FIRE, tool());
    public static final DragonScalePickaxeItem FIRE_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("fire_dragon_pickaxe", DragonScaleTier.FIRE, tool());
    public static final DragonScaleHoeItem FIRE_DRAGON_SCALE_HOE = createDragonScaleHoeItem("fire_dragon_hoe", DragonScaleTier.FIRE, tool());
    //Dragon Scale Tools - Forest
    public static final DragonScaleShovelItem FOREST_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("forest_dragon_shovel", DragonScaleTier.FOREST, tool());
    public static final DragonScalePickaxeItem FOREST_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("forest_dragon_pickaxe", DragonScaleTier.FOREST, tool());
    public static final DragonScaleHoeItem FOREST_DRAGON_SCALE_HOE = createDragonScaleHoeItem("forest_dragon_hoe", DragonScaleTier.FOREST, tool());
    //Dragon Scale Tools - Nether
    public static final DragonScaleShovelItem NETHER_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("nether_dragon_shovel", DragonScaleTier.NETHER, tool());
    public static final DragonScalePickaxeItem NETHER_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("nether_dragon_pickaxe", DragonScaleTier.NETHER, tool());
    public static final DragonScaleHoeItem NETHER_DRAGON_SCALE_HOE = createDragonScaleHoeItem("nether_dragon_hoe", DragonScaleTier.NETHER, tool());
    //Dragon Scale Tools - Ender
    public static final DragonScaleShovelItem ENDER_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("ender_dragon_shovel", DragonScaleTier.ENDER, tool());
    public static final DragonScalePickaxeItem ENDER_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("ender_dragon_pickaxe", DragonScaleTier.ENDER, tool());
    public static final DragonScaleHoeItem ENDER_DRAGON_SCALE_HOE = createDragonScaleHoeItem("ender_dragon_hoe", DragonScaleTier.ENDER, tool());
    //Dragon Scale Tools - Enchant
    public static final DragonScaleShovelItem ENCHANT_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("enchant_dragon_shovel", DragonScaleTier.ENCHANT, tool());
    public static final DragonScalePickaxeItem ENCHANT_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("enchant_dragon_pickaxe", DragonScaleTier.ENCHANT, tool());
    public static final DragonScaleHoeItem ENCHANT_DRAGON_SCALE_HOE = createDragonScaleHoeItem("enchant_dragon_hoe", DragonScaleTier.ENCHANT, tool());
    //Dragon Scale Tools - Sunlight
    public static final DragonScaleShovelItem SUNLIGHT_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("sunlight_dragon_shovel", DragonScaleTier.SUNLIGHT, tool());
    public static final DragonScalePickaxeItem SUNLIGHT_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("sunlight_dragon_pickaxe", DragonScaleTier.SUNLIGHT, tool());
    public static final DragonScaleHoeItem SUNLIGHT_DRAGON_SCALE_HOE = createDragonScaleHoeItem("sunlight_dragon_hoe", DragonScaleTier.SUNLIGHT, tool());
    //Dragon Scale Tools - Moonlight
    public static final DragonScaleShovelItem MOONLIGHT_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("moonlight_dragon_shovel", DragonScaleTier.MOONLIGHT, tool());
    public static final DragonScalePickaxeItem MOONLIGHT_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("moonlight_dragon_pickaxe", DragonScaleTier.MOONLIGHT, tool());
    public static final DragonScaleHoeItem MOONLIGHT_DRAGON_SCALE_HOE = createDragonScaleHoeItem("moonlight_dragon_hoe", DragonScaleTier.MOONLIGHT, tool());
    //Dragon Scale Tools - Storm
    public static final DragonScaleShovelItem STORM_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("storm_dragon_shovel", DragonScaleTier.STORM, tool());
    public static final DragonScalePickaxeItem STORM_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("storm_dragon_pickaxe", DragonScaleTier.STORM, tool());
    public static final DragonScaleHoeItem STORM_DRAGON_SCALE_HOE = createDragonScaleHoeItem("storm_dragon_hoe", DragonScaleTier.STORM, tool());
    //Dragon Scale Tools - Terra
    public static final DragonScaleShovelItem TERRA_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("terra_dragon_shovel", DragonScaleTier.TERRA, tool());
    public static final DragonScalePickaxeItem TERRA_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("terra_dragon_pickaxe", DragonScaleTier.TERRA, tool());
    public static final DragonScaleHoeItem TERRA_DRAGON_SCALE_HOE = createDragonScaleHoeItem("terra_dragon_hoe", DragonScaleTier.TERRA, tool());
    //Dragon Scale Tools - Zombie
    public static final DragonScaleShovelItem ZOMBIE_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("zombie_dragon_shovel", DragonScaleTier.ZOMBIE, tool());
    public static final DragonScalePickaxeItem ZOMBIE_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("zombie_dragon_pickaxe", DragonScaleTier.ZOMBIE, tool());
    public static final DragonScaleHoeItem ZOMBIE_DRAGON_SCALE_HOE = createDragonScaleHoeItem("zombie_dragon_hoe", DragonScaleTier.ZOMBIE, tool());
    //Dragon Scale Tools - Sculk
    public static final DragonScaleShovelItem SCULK_DRAGON_SCALE_SHOVEL = createDragonScaleShovelItem("sculk_dragon_shovel", DragonScaleTier.SCULK, tool().fireResistant());
    public static final DragonScalePickaxeItem SCULK_DRAGON_SCALE_PICKAXE = createDragonScalePickaxeItem("sculk_dragon_pickaxe", DragonScaleTier.SCULK, tool().fireResistant());
    public static final DragonScaleHoeItem SCULK_DRAGON_SCALE_HOE = createDragonScaleHoeItem("sculk_dragon_hoe", DragonScaleTier.SCULK, tool().fireResistant());
    //Dragon Scale Armors
    public static final DragonScaleArmorSuit AETHER_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "aether_dragon_scale_helmet",
            "aether_dragon_scale_chestplate",
            "aether_dragon_scale_leggings",
            "aether_dragon_scale_boots",
            DragonScaleMaterial.AETHER,
            DMArmorEffects.AETHER,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit WATER_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "water_dragon_scale_helmet",
            "water_dragon_scale_chestplate",
            "water_dragon_scale_leggings",
            "water_dragon_scale_boots",
            DragonScaleMaterial.WATER,
            DMArmorEffects.WATER,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit ICE_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "ice_dragon_scale_helmet",
            "ice_dragon_scale_chestplate",
            "ice_dragon_scale_leggings",
            "ice_dragon_scale_boots",
            DragonScaleMaterial.ICE,
            DMArmorEffects.ICE,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit FIRE_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "fire_dragon_scale_helmet",
            "fire_dragon_scale_chestplate",
            "fire_dragon_scale_leggings",
            "fire_dragon_scale_boots",
            DragonScaleMaterial.FIRE,
            DMArmorEffects.FIRE,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit FOREST_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "forest_dragon_scale_helmet",
            "forest_dragon_scale_chestplate",
            "forest_dragon_scale_leggings",
            "forest_dragon_scale_boots",
            DragonScaleMaterial.FOREST,
            DMArmorEffects.FOREST,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit NETHER_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "nether_dragon_scale_helmet",
            "nether_dragon_scale_chestplate",
            "nether_dragon_scale_leggings",
            "nether_dragon_scale_boots",
            DragonScaleMaterial.NETHER,
            DMArmorEffects.NETHER,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit ENDER_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "ender_dragon_scale_helmet",
            "ender_dragon_scale_chestplate",
            "ender_dragon_scale_leggings",
            "ender_dragon_scale_boots",
            DragonScaleMaterial.ENDER,
            DMArmorEffects.ENDER,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit ENCHANT_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "enchant_dragon_scale_helmet",
            "enchant_dragon_scale_chestplate",
            "enchant_dragon_scale_leggings",
            "enchant_dragon_scale_boots",
            DragonScaleMaterial.ENCHANT,
            DMArmorEffects.ENCHANT,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit SUNLIGHT_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "sunlight_dragon_scale_helmet",
            "sunlight_dragon_scale_chestplate",
            "sunlight_dragon_scale_leggings",
            "sunlight_dragon_scale_boots",
            DragonScaleMaterial.SUNLIGHT,
            DMArmorEffects.SUNLIGHT,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit MOONLIGHT_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "moonlight_dragon_scale_helmet",
            "moonlight_dragon_scale_chestplate",
            "moonlight_dragon_scale_leggings",
            "moonlight_dragon_scale_boots",
            DragonScaleMaterial.MOONLIGHT,
            DMArmorEffects.MOONLIGHT,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit STORM_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "storm_dragon_scale_helmet",
            "storm_dragon_scale_chestplate",
            "storm_dragon_scale_leggings",
            "storm_dragon_scale_boots",
            DragonScaleMaterial.STORM,
            DMArmorEffects.STORM,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit TERRA_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "terra_dragon_scale_helmet",
            "terra_dragon_scale_chestplate",
            "terra_dragon_scale_leggings",
            "terra_dragon_scale_boots",
            DragonScaleMaterial.TERRA,
            DMArmorEffects.TERRA,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit ZOMBIE_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "zombie_dragon_scale_helmet",
            "zombie_dragon_scale_chestplate",
            "zombie_dragon_scale_leggings",
            "zombie_dragon_scale_boots",
            DragonScaleMaterial.ZOMBIE,
            DMArmorEffects.ZOMBIE,
            DRAGONMOUNTS_TOOL_TAB
    );
    public static final DragonScaleArmorSuit SCULK_DRAGON_SCALE_ARMORS = createDragonScaleArmors(
            "sculk_dragon_scale_helmet",
            "sculk_dragon_scale_chestplate",
            "sculk_dragon_scale_leggings",
            "sculk_dragon_scale_boots",
            DragonScaleMaterial.SCULK,
            null,
            $ -> new Properties().tab(TOOL_TAB).fireResistant()
    );
    //Dragon Spawn Eggs
    public static final DragonSpawnEggItem AETHER_DRAGON_SPAWN_EGG = createDragonSpawnEgg("aether_dragon_spawn_egg", DragonTypes.AETHER, 0x05C3D2, 0x281EE8, item());
    public static final DragonSpawnEggItem ENCHANT_DRAGON_SPAWN_EGG = createDragonSpawnEgg("enchant_dragon_spawn_egg", DragonTypes.ENCHANT, 0xCC0DE0, 0xFFFFFF, item());
    public static final DragonSpawnEggItem ENDER_DRAGON_SPAWN_EGG = createDragonSpawnEgg("ender_dragon_spawn_egg", DragonTypes.ENDER, 0x08080C, 0x79087E, item());
    public static final DragonSpawnEggItem FIRE_DRAGON_SPAWN_EGG = createDragonSpawnEgg("fire_dragon_spawn_egg", DragonTypes.FIRE, 0x620508, 0xF7A502, item());
    public static final DragonSpawnEggItem FOREST_DRAGON_SPAWN_EGG = createDragonSpawnEgg("forest_dragon_spawn_egg", DragonTypes.FOREST, 0x0C9613, 0x036408, item());
    public static final DragonSpawnEggItem ICE_DRAGON_SPAWN_EGG = createDragonSpawnEgg("ice_dragon_spawn_egg", DragonTypes.ICE, 0xFFFFFF, 0x02D0EE, item());
    public static final DragonSpawnEggItem MOONLIGHT_DRAGON_SPAWN_EGG = createDragonSpawnEgg("moonlight_dragon_spawn_egg", DragonTypes.MOONLIGHT, 0x00164E, 0xFEFEFE, item());
    public static final DragonSpawnEggItem NETHER_DRAGON_SPAWN_EGG = createDragonSpawnEgg("nether_dragon_spawn_egg", DragonTypes.NETHER, 0x632F1B, 0xE7A621, item());
    public static final DragonSpawnEggItem SCULK_DRAGON_SPAWN_EGG = createDragonSpawnEgg("sculk_dragon_spawn_egg", DragonTypes.SCULK, 0x0F4649, 0x39D6E0, item());
    public static final DragonSpawnEggItem SKELETON_DRAGON_SPAWN_EGG = createDragonSpawnEgg("skeleton_dragon_spawn_egg", DragonTypes.SKELETON, 0xFFFFFF, 0x474F51, item());
    public static final DragonSpawnEggItem STORM_DRAGON_SPAWN_EGG = createDragonSpawnEgg("storm_dragon_spawn_egg", DragonTypes.STORM, 0x010B0F, 0x0FA8CE, item());
    public static final DragonSpawnEggItem SUNLIGHT_DRAGON_SPAWN_EGG = createDragonSpawnEgg("sunlight_dragon_spawn_egg", DragonTypes.SUNLIGHT, 0xF4950D, 0xF4E10D, item());
    public static final DragonSpawnEggItem TERRA_DRAGON_SPAWN_EGG = createDragonSpawnEgg("terra_dragon_spawn_egg", DragonTypes.TERRA, 0x674517, 0xE6B10D, item());
    public static final DragonSpawnEggItem WATER_DRAGON_SPAWN_EGG = createDragonSpawnEgg("water_dragon_spawn_egg", DragonTypes.WATER, 0x546FAD, 0x2B427E, item());
    public static final DragonSpawnEggItem WITHER_DRAGON_SPAWN_EGG = createDragonSpawnEgg("wither_dragon_spawn_egg", DragonTypes.WITHER, 0x8A9999, 0x474F51, item());
    public static final DragonSpawnEggItem ZOMBIE_DRAGON_SPAWN_EGG = createDragonSpawnEgg("zombie_dragon_spawn_egg", DragonTypes.ZOMBIE, 0x66664B, 0xB6D035, item());
    //?
    public static final VariantSwitcherItem VARIANT_SWITCHER = createItem("variant_switcher", new VariantSwitcherItem(item()));
    //Shears
    public static final TieredShearsItem DIAMOND_SHEARS = createTieredShearsItem("diamond_shears", ItemTier.DIAMOND, item());
    public static final TieredShearsItem NETHERITE_SHEARS = createTieredShearsItem("netherite_shears", ItemTier.NETHERITE, item().fireResistant());
    //Carriages
    public static final CarriageItem ACACIA_CARRIAGE = createItem("acacia_carriage", new CarriageItem(CarriageTypes.ACACIA, item()));
    public static final CarriageItem BIRCH_CARRIAGE = createItem("birch_carriage", new CarriageItem(CarriageTypes.BIRCH, item()));
    public static final CarriageItem DARK_OAK_CARRIAGE = createItem("dark_oak_carriage", new CarriageItem(CarriageTypes.DARK_OAK, item()));
    public static final CarriageItem JUNGLE_CARRIAGE = createItem("jungle_carriage", new CarriageItem(CarriageTypes.JUNGLE, item()));
    public static final CarriageItem OAK_CARRIAGE = createItem("oak_carriage", new CarriageItem(CarriageTypes.OAK, item()));
    public static final CarriageItem SPRUCE_CARRIAGE = createItem("spruce_carriage", new CarriageItem(CarriageTypes.SPRUCE, item()));
    //Dragon Amulets
    public static final AmuletItem<Entity> AMULET = createItem("amulet", new AmuletItem<>(Entity.class, item()));
    public static final DragonAmuletItem FOREST_DRAGON_AMULET = createDragonAmuletItem("forest_dragon_amulet", DragonTypes.FOREST, none());
    public static final DragonAmuletItem FIRE_DRAGON_AMULET = createDragonAmuletItem("fire_dragon_amulet", DragonTypes.FIRE, none());
    public static final DragonAmuletItem ICE_DRAGON_AMULET = createDragonAmuletItem("ice_dragon_amulet", DragonTypes.ICE, none());
    public static final DragonAmuletItem WATER_DRAGON_AMULET = createDragonAmuletItem("water_dragon_amulet", DragonTypes.WATER, none());
    public static final DragonAmuletItem AETHER_DRAGON_AMULET = createDragonAmuletItem("aether_dragon_amulet", DragonTypes.AETHER, none());
    public static final DragonAmuletItem NETHER_DRAGON_AMULET = createDragonAmuletItem("nether_dragon_amulet", DragonTypes.NETHER, none());
    public static final DragonAmuletItem ENDER_DRAGON_AMULET = createDragonAmuletItem("ender_dragon_amulet", DragonTypes.ENDER, none());
    public static final DragonAmuletItem SUNLIGHT_DRAGON_AMULET = createDragonAmuletItem("sunlight_dragon_amulet", DragonTypes.SUNLIGHT, none());
    public static final DragonAmuletItem ENCHANT_DRAGON_AMULET = createDragonAmuletItem("enchant_dragon_amulet", DragonTypes.ENCHANT, none());
    public static final DragonAmuletItem STORM_DRAGON_AMULET = createDragonAmuletItem("storm_dragon_amulet", DragonTypes.STORM, none());
    public static final DragonAmuletItem TERRA_DRAGON_AMULET = createDragonAmuletItem("terra_dragon_amulet", DragonTypes.TERRA, none());
    public static final DragonAmuletItem ZOMBIE_DRAGON_AMULET = createDragonAmuletItem("zombie_dragon_amulet", DragonTypes.ZOMBIE, none());
    public static final DragonAmuletItem MOONLIGHT_DRAGON_AMULET = createDragonAmuletItem("moonlight_dragon_amulet", DragonTypes.MOONLIGHT, none());
    public static final DragonAmuletItem SCULK_DRAGON_AMULET = createDragonAmuletItem("sculk_dragon_amulet", DragonTypes.SCULK, none().fireResistant());
    public static final DragonAmuletItem SKELETON_DRAGON_AMULET = createDragonAmuletItem("skeleton_dragon_amulet", DragonTypes.SKELETON, none());
    public static final DragonAmuletItem WITHER_DRAGON_AMULET = createDragonAmuletItem("wither_dragon_amulet", DragonTypes.WITHER, none());
    //Dragon Essences
    public static final DragonEssenceItem FOREST_DRAGON_ESSENCE = createDragonEssenceItem("forest_dragon_essence", DragonTypes.FOREST, none());
    public static final DragonEssenceItem FIRE_DRAGON_ESSENCE = createDragonEssenceItem("fire_dragon_essence", DragonTypes.FIRE, none());
    public static final DragonEssenceItem ICE_DRAGON_ESSENCE = createDragonEssenceItem("ice_dragon_essence", DragonTypes.ICE, none());
    public static final DragonEssenceItem WATER_DRAGON_ESSENCE = createDragonEssenceItem("water_dragon_essence", DragonTypes.WATER, none());
    public static final DragonEssenceItem AETHER_DRAGON_ESSENCE = createDragonEssenceItem("aether_dragon_essence", DragonTypes.AETHER, none());
    public static final DragonEssenceItem NETHER_DRAGON_ESSENCE = createDragonEssenceItem("nether_dragon_essence", DragonTypes.NETHER, none());
    public static final DragonEssenceItem ENDER_DRAGON_ESSENCE = createDragonEssenceItem("ender_dragon_essence", DragonTypes.ENDER, none());
    public static final DragonEssenceItem SUNLIGHT_DRAGON_ESSENCE = createDragonEssenceItem("sunlight_dragon_essence", DragonTypes.SUNLIGHT, none());
    public static final DragonEssenceItem ENCHANT_DRAGON_ESSENCE = createDragonEssenceItem("enchant_dragon_essence", DragonTypes.ENCHANT, none());
    public static final DragonEssenceItem STORM_DRAGON_ESSENCE = createDragonEssenceItem("storm_dragon_essence", DragonTypes.STORM, none());
    public static final DragonEssenceItem TERRA_DRAGON_ESSENCE = createDragonEssenceItem("terra_dragon_essence", DragonTypes.TERRA, none());
    public static final DragonEssenceItem ZOMBIE_DRAGON_ESSENCE = createDragonEssenceItem("zombie_dragon_essence", DragonTypes.ZOMBIE, none());
    public static final DragonEssenceItem MOONLIGHT_DRAGON_ESSENCE = createDragonEssenceItem("moonlight_dragon_essence", DragonTypes.MOONLIGHT, none());
    public static final DragonEssenceItem SCULK_DRAGON_ESSENCE = createDragonEssenceItem("sculk_dragon_essence", DragonTypes.SCULK, none().fireResistant());
    public static final DragonEssenceItem SKELETON_DRAGON_ESSENCE = createDragonEssenceItem("skeleton_dragon_essence", DragonTypes.SKELETON, none());
    public static final DragonEssenceItem WITHER_DRAGON_ESSENCE = createDragonEssenceItem("wither_dragon_essence", DragonTypes.WITHER, none());
    public static final DragonWhistleItem DRAGON_WHISTLE = createItem("dragon_whistle", new DragonWhistleItem(item()));
    //Blocks
    public static final BlockItem DRAGON_CORE = createItem("dragon_core", new BlockItem(DMBlocks.DRAGON_CORE, none().rarity(Rarity.RARE).setISTER(GET_DMISTER)));
    public static final DragonNestItem DRAGON_NEST = createItem("dragon_nest", new DragonNestItem(DMBlocks.DRAGON_NEST, block()));

    static <T extends Item> T createItem(String name, T item) {
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonAmuletItem createDragonAmuletItem(String name, DragonType type, Properties props) {
        DragonAmuletItem item = new DragonAmuletItem(type, props);
        type.bindInstance(DragonAmuletItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonArmorItem createDragonArmorItem(String name, String texture, int protection, Properties props) {
        DragonArmorItem item = new DragonArmorItem(new ResourceLocation(DragonMounts.MOD_ID, texture), protection, props);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleAxeItem createDragonScaleAxeItem(String name, DragonScaleTier tier, float attackDamageModifier, float attackSpeedModifier, Properties props) {
        DragonScaleAxeItem item = new DragonScaleAxeItem(tier, attackDamageModifier, attackSpeedModifier, props);
        tier.type.bindInstance(DragonScaleAxeItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleAxeItem createDragonScaleAxeItem(String name, DragonScaleTier tier, Properties props) {
        return createDragonScaleAxeItem(name, tier, 5.0F, -2.8F, props);
    }

    static DragonScaleBowItem createDragonScaleBowItem(String name, DragonScaleTier tier, Properties props) {
        DragonScaleBowItem item = new DragonScaleBowItem(tier, props);
        tier.type.bindInstance(DragonScaleBowItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonEssenceItem createDragonEssenceItem(String name, DragonType type, Properties props) {
        DragonEssenceItem item = new DragonEssenceItem(type, props);
        type.bindInstance(DragonEssenceItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleHoeItem createDragonScaleHoeItem(String name, DragonScaleTier tier, Properties props) {
        DragonScaleHoeItem item = new DragonScaleHoeItem(tier, (int) -tier.getAttackDamageBonus(), tier.getAttackDamageBonus() - 3.0F, props);
        tier.type.bindInstance(DragonScaleHoeItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScalePickaxeItem createDragonScalePickaxeItem(String name, DragonScaleTier tier, Properties props) {
        DragonScalePickaxeItem item = new DragonScalePickaxeItem(tier, 1, -2.8F, props);
        tier.type.bindInstance(DragonScalePickaxeItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleArmorSuit createDragonScaleArmors(
            String helmet,
            String chestplate,
            String leggings,
            String boots,
            DragonScaleMaterial material,
            IDragonScaleArmorEffect effect,
            Function<EquipmentSlotType, Properties> factory
    ) {
        DragonScaleArmorSuit suit = new DragonScaleArmorSuit(material, effect, factory);
        material.type.bindInstance(DragonScaleArmorSuit.class, suit);
        ITEMS.register(helmet, suit.helmet::getItem);
        ITEMS.register(chestplate, suit.chestplate::getItem);
        ITEMS.register(leggings, suit.leggings::getItem);
        ITEMS.register(boots, suit.boots::getItem);
        return suit;
    }

    static DragonScalesItem createDragonScalesItem(String name, DragonType type, Properties props) {
        DragonScalesItem item = new DragonScalesItem(type, props);
        type.bindInstance(DragonScalesItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleShieldItem createDragonScaleShieldItem(String name, DragonScaleMaterial material, Properties props) {
        DragonScaleShieldItem item = new DragonScaleShieldItem(material, props);
        material.getDragonType().bindInstance(DragonScaleShieldItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleShovelItem createDragonScaleShovelItem(String name, DragonScaleTier tier, Properties props) {
        DragonScaleShovelItem item = new DragonScaleShovelItem(tier, 1.5F, -3.0F, props);
        tier.type.bindInstance(DragonScaleShovelItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonScaleSwordItem createDragonScaleSwordItem(String name, DragonScaleTier tier, Properties props) {
        DragonScaleSwordItem item = new DragonScaleSwordItem(tier, 3, -2.0F, props);
        tier.type.bindInstance(DragonScaleSwordItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static DragonSpawnEggItem createDragonSpawnEgg(String name, DragonType type, int background, int highlight, Properties props) {
        DragonSpawnEggItem item = new DragonSpawnEggItem(type, background, highlight, props);
        type.bindInstance(DragonSpawnEggItem.class, item);
        ITEMS.register(name, item::getItem);
        return item;
    }

    static TieredShearsItem createTieredShearsItem(String name, IItemTier tier, Properties props) {
        TieredShearsItem item = new TieredShearsItem(tier, props);
        ITEMS.register(name, item::getItem);
        return item;
    }

    public static void subscribeEvents(IEventBus forgeBus) {
        forgeBus.addListener(DMArmorEffects::xpBonus);
        forgeBus.addListener(DMArmorEffects::meleeChanneling);
        forgeBus.addListener(DMArmorEffects::riposte);
        forgeBus.addListener(HatchableDragonEggBlock::interact);
    }
}