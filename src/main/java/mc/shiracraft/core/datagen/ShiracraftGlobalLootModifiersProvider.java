package mc.shiracraft.core.datagen;

import mc.shiracraft.core.Core;
import mc.shiracraft.core.loot.AddItemModifier;
import mc.shiracraft.core.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ShiracraftGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    public ShiracraftGlobalLootModifiersProvider(PackOutput output) {
        super(output, Core.MOD_ID);
    }

    @Override
    protected void start() {
        addSlotCoinDrops();
    }

    private void addSlotCoinDrops() {
        // Add slot coin drops for any mob killed by a player with 20% chance
        // Drops 1-3 coins randomly
        // TODO: Make drop chance configurable
        add("slot_coin_from_mob", new AddItemModifier(new LootItemCondition[] {
                LootItemKilledByPlayerCondition.killedByPlayer().build(),
                LootItemRandomChanceCondition.randomChance(0.2f).build()
        }, ItemRegistry.SLOT_COIN.get()));

        // Add slot coin drops for common chest types
        // Drops 3 coins every time (no random chance, guaranteed)
        addChestLootModifier("slot_coin_from_village_chest", "chests/village/village_weaponsmith");
        addChestLootModifier("slot_coin_from_desert_pyramid", "chests/desert_pyramid");
        addChestLootModifier("slot_coin_from_jungle_temple", "chests/jungle_temple");
        addChestLootModifier("slot_coin_from_stronghold_corridor", "chests/stronghold_corridor");
        addChestLootModifier("slot_coin_from_dungeon", "chests/simple_dungeon");
        addChestLootModifier("slot_coin_from_mineshaft", "chests/abandoned_mineshaft");
        addChestLootModifier("slot_coin_from_end_city", "chests/end_city_treasure");
        addChestLootModifier("slot_coin_from_nether_fortress", "chests/nether_bridge");
        addChestLootModifier("slot_coin_from_bastion", "chests/bastion_treasure");
    }

    private void addChestLootModifier(String name, String lootTableId) {
        add(name, new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft", lootTableId)).build()
        }, ItemRegistry.SLOT_COIN.get(), 3, true));
    }
}
