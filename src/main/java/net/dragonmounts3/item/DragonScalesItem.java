package net.dragonmounts3.item;

import net.dragonmounts3.registry.DragonType;
import net.dragonmounts3.registry.IDragonTypified;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dragonmounts3.DragonMounts.getItemTranslationKey;

public class DragonScalesItem extends Item implements IDragonTypified {
    private static final String TRANSLATION_KEY = getItemTranslationKey("dragon_scales");

    protected DragonType type;

    public DragonScalesItem(DragonType type, Properties properties) {
        super(properties);
        this.type = type;
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