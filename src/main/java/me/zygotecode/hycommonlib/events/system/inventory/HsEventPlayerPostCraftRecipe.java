package me.zygotecode.hycommonlib.events.system.inventory;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerPostCraftRecipe extends HsEventCancellable {
    private ArchetypeChunk<EntityStore> archetypeChunk;
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;
    private HsPlayer player;

    private CraftingRecipe craftingRecipe;
    private int quantity;

    public HsEventPlayerPostCraftRecipe(ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, HsPlayer player, CraftingRecipe craftingRecipe, int quantity) {
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.player = player;
        this.craftingRecipe = craftingRecipe;
        this.quantity = quantity;
    }

    public ArchetypeChunk<EntityStore> getArchetypeChunk() {
        return this.archetypeChunk;
    }

    public Store<EntityStore> getStore() {
        return this.store;
    }

    public CommandBuffer<EntityStore> getCommandBuffer() {
        return this.commandBuffer;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }

    public CraftingRecipe getCraftingRecipe() {
        return this.craftingRecipe;
    }

    public int getQuantity() {
        return this.quantity;
    }
}