package me.zygotecode.hycommonlib.systems.inventory;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.InteractivelyPickupItemEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.inventory.HsEventPlayerItemPickup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPickupSystemListener extends EntityEventSystem<EntityStore, InteractivelyPickupItemEvent> {
    public ItemPickupSystemListener() {
        super(InteractivelyPickupItemEvent.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull InteractivelyPickupItemEvent event
    ) {
        HsEventPlayerItemPickup itemPickupEvent = new HsEventPlayerItemPickup(archetypeChunk, store, commandBuffer, HsServer.getPlayer(store), event.getItemStack());
        HsEventManager.call(itemPickupEvent);

        if (itemPickupEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setItemStack(itemPickupEvent.getItemStack());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
