package net.dragonmounts3.registry;

import net.dragonmounts3.api.IDragonTypified;
import net.dragonmounts3.client.variant.VariantAppearance;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static it.unimi.dsi.fastutil.Arrays.MAX_ARRAY_SIZE;
import static net.dragonmounts3.DragonMounts.MOD_ID;
import static net.dragonmounts3.DragonMounts.prefix;

public class DragonVariant extends ForgeRegistryEntry<DragonVariant> implements IDragonTypified {
    public static final String DATA_PARAMETER_KEY = "Variant";
    public static final ResourceLocation DEFAULT_KEY = prefix("ender_female");
    public static final Registry REGISTRY = new Registry(MOD_ID, "dragon_variant", new RegistryBuilder<DragonVariant>().setDefaultKey(DEFAULT_KEY));
    public static final IDataSerializer<DragonVariant> SERIALIZER = new IDataSerializer<DragonVariant>() {
        public void write(PacketBuffer buffer, @Nonnull DragonVariant value) {
            buffer.writeVarInt(REGISTRY.getID(value));
        }

        @Nonnull
        public DragonVariant read(@Nonnull PacketBuffer buffer) {
            return REGISTRY.getValue(buffer.readVarInt());
        }

        @Nonnull
        public DragonVariant copy(@Nonnull DragonVariant value) {
            return value;
        }
    };

    public static DragonVariant byName(String name) {
        return REGISTRY.getValue(new ResourceLocation(name));
    }

    public final DragonType type;
    private int index = -1;
    private VariantAppearance appearance;

    public DragonVariant(DragonType type) {
        this.type = type;
    }

    public final ResourceLocation getSerializedName() {
        ResourceLocation key = this.getRegistryName();
        return key == null ? DEFAULT_KEY : key;
    }

    @Override
    public final DragonType getDragonType() {
        return this.type;
    }

    public VariantAppearance getAppearance(VariantAppearance defaultValue) {
        return this.appearance == null ? defaultValue : this.appearance;
    }

    @SuppressWarnings("UnusedReturnValue")
    public VariantAppearance setAppearance(VariantAppearance value) {
        VariantAppearance old = this.appearance;
        this.appearance = value;
        return old;
    }

    /**
     * Simplified {@link it.unimi.dsi.fastutil.objects.ReferenceArrayList}
     */
    @ParametersAreNonnullByDefault
    public static final class Manager implements IDragonTypified {
        public static final int DEFAULT_INITIAL_CAPACITY = 8;
        public final DragonType type;
        private DragonVariant[] variants = {};
        private int size;

        public Manager(DragonType type) {
            this.type = type;
        }

        private void grow(int capacity) {
            if (capacity <= this.variants.length)
                return;
            if (this.variants.length > 0)
                capacity = (int) Math.max(Math.min((long) this.variants.length + (this.variants.length >> 1), MAX_ARRAY_SIZE), capacity);
            else if (capacity < DEFAULT_INITIAL_CAPACITY)
                capacity = DEFAULT_INITIAL_CAPACITY;
            final DragonVariant[] array = new DragonVariant[capacity];
            System.arraycopy(this.variants, 0, array, 0, size);
            this.variants = array;
            assert this.size <= this.variants.length;
        }

        @SuppressWarnings("UnusedReturnValue")
        private boolean add(final DragonVariant variant) {
            if (variant.type != this.type || variant.index >= 0) return false;
            this.grow(this.size + 1);
            variant.index = this.size;
            this.variants[this.size++] = variant;
            assert this.size <= this.variants.length;
            return true;
        }

        @SuppressWarnings("UnusedReturnValue")
        private boolean remove(final DragonVariant variant) {
            if (variant.type != this.type || variant.index < 0) return false;
            if (variant.index >= this.size)
                throw new IndexOutOfBoundsException("Index (" + variant.index + ") is greater than or equal to list size (" + this.size + ")");
            this.size--;
            if (variant.index != this.size) {
                System.arraycopy(this.variants, variant.index + 1, this.variants, variant.index, this.size - variant.index);
            }
            variant.index = -1;
            this.variants[this.size] = null;
            assert this.size <= this.variants.length;
            return true;
        }

        private void clear() {
            for (int i = 0; i < this.size; ++i) {
                this.variants[i].index = -1;
                this.variants[i] = null;
            }
            this.size = 0;
        }

        public DragonVariant draw(Random random, @Nullable DragonVariant current) {
            switch (this.size) {
                case 0: return current;
                case 1: return this.variants[0];
            }
            if (current == null || current.type != this.type) {
                return this.variants[random.nextInt(this.size)];
            }
            if (this.size == 2) return this.variants[(current.index ^ 1) & 1];//current.index == 0 ? 1 : 0
            int index = random.nextInt(this.size - 1);
            return this.variants[index < current.index ? index : index + 1];
        }

        public int size() {
            return this.size;
        }

        @Override
        public DragonType getDragonType() {
            return this.type;
        }
    }

    public static class Registry extends DeferredRegistry<DragonVariant> implements IForgeRegistry.AddCallback<DragonVariant>, IForgeRegistry.ClearCallback<DragonVariant> {
        public Registry(String namespace, String name, RegistryBuilder<DragonVariant> builder) {
            super(namespace, name, DragonVariant.class, builder);
        }

        @Override
        public void onAdd(IForgeRegistryInternal<DragonVariant> owner, RegistryManager stage, int id, DragonVariant obj, @Nullable DragonVariant oldObj) {
            if (owner == this.registry) {//public -> protected
                if (oldObj != null) {
                    oldObj.type.variants.remove(oldObj);
                }
                obj.type.variants.add(obj);
            }
        }

        @Override
        public void onClear(IForgeRegistryInternal<DragonVariant> owner, RegistryManager stage) {
            if (owner == this.registry) {//public -> protected
                for (DragonType type : DragonType.REGISTRY) {
                    type.variants.clear();
                }
            }
        }
    }
}
