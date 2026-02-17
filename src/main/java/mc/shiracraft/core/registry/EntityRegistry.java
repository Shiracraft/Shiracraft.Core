package mc.shiracraft.core.registry;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.entity.ShopkeeperEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Core.MOD_ID);

    public static final RegistryObject<EntityType<ShopkeeperEntity>> SHOPKEEPER = ENTITY_TYPES.register(
            ShopkeeperEntity.ENTITY_ID,
            () -> EntityType.Builder.of(ShopkeeperEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .clientTrackingRange(8)
                    .updateInterval(3)
                    .build(ShopkeeperEntity.ENTITY_ID)
    );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
