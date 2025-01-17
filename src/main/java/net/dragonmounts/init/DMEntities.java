package net.dragonmounts.init;

import net.dragonmounts.DragonMounts;
import net.dragonmounts.entity.CarriageEntity;
import net.dragonmounts.entity.dragon.HatchableDragonEggEntity;
import net.dragonmounts.entity.dragon.TameableDragonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.dragonmounts.entity.dragon.TameableDragonEntity.ADULT_DIMENSIONS_HEIGHT;
import static net.dragonmounts.entity.dragon.TameableDragonEntity.ADULT_DIMENSIONS_WIDTH;
import static net.minecraft.entity.EntityType.Builder.of;

@Mod.EventBusSubscriber(modid = DragonMounts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DMEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DragonMounts.create(ForgeRegistries.ENTITIES);
    public static final RegistryObject<EntityType<CarriageEntity>> CARRIAGE = register("carriage", of(CarriageEntity::new, EntityClassification.MISC), 0.8F, 0.8F);
    public static final RegistryObject<EntityType<HatchableDragonEggEntity>> HATCHABLE_DRAGON_EGG = register("dragon_egg", of(HatchableDragonEggEntity::new, EntityClassification.MISC), 0.875F, 1.0F);
    public static final RegistryObject<EntityType<TameableDragonEntity>> TAMEABLE_DRAGON = register("dragon", of(TameableDragonEntity::construct, EntityClassification.CREATURE), ADULT_DIMENSIONS_WIDTH, ADULT_DIMENSIONS_HEIGHT);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder, float width, float height) {
        return ENTITY_TYPES.register(name, () -> builder.sized(width, height).build(name));
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(HATCHABLE_DRAGON_EGG.get(), HatchableDragonEggEntity.registerAttributes().build());
        event.put(TAMEABLE_DRAGON.get(), TameableDragonEntity.registerAttributes().build());
    }
}