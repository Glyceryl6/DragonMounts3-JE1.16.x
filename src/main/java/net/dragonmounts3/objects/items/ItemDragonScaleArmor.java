package net.dragonmounts3.objects.items;

import net.dragonmounts3.objects.DragonType;
import net.dragonmounts3.objects.IDragonTypified;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dragonmounts3.DragonMounts.MOD_ID;
import static net.dragonmounts3.DragonMounts.getItemTranslationKey;

public class ItemDragonScaleArmor extends ArmorItem implements IDragonTypified {
    private static final String[] TRANSLATION_KEY_SUFFIX = new String[]{"boots", "leggings", "chestplate", "helmet"};
    private final String translationKey;

    protected DragonType type;

    public ItemDragonScaleArmor(
            DragonScaleMaterial material,
            EquipmentSlotType slot,
            Properties properties
    ) {
        super(material, slot, properties);
        this.type = material.getDragonType();
        this.translationKey = getItemTranslationKey("dragon_scale_" + TRANSLATION_KEY_SUFFIX[slot.getIndex()]);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> components, @Nonnull ITooltipFlag flag) {
        components.add(this.type.getText());
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return MOD_ID + ":textures/models/armor/" + this.type.name().toLowerCase() + (slot == EquipmentSlotType.LEGS ? "_layer_2.png" : "_layer_1.png");
    }

    @Nonnull
    @Override
    public String getDescriptionId() {
        return this.translationKey;
    }

    @Override
    public DragonType getDragonType() {
        return this.type;
    }
}