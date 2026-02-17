package mc.shiracraft.core.event;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.entity.ShopkeeperEntity;
import mc.shiracraft.core.registry.EntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Core.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EntityEvents {

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.SHOPKEEPER.get(), ShopkeeperEntity.createAttributes().build());
    }
}

