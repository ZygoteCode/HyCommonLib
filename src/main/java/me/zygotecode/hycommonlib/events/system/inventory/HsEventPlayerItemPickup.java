package me.zygotecode.hycommonlib.events.system.inventory;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerItemPickup extends HsEventCancellable {
    private ArchetypeChunk<EntityStore> archetypeChunk;
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;

    private HsPlayer player;
    private ItemStack itemStack;

    public HsEventPlayerItemPickup(ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, HsPlayer player, ItemStack itemStack) {
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.player = player;
        this.itemStack = itemStack;
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

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}