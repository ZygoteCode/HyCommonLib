package me.zygotecode.hycommonlib.systems.inventory;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.inventory.HsEventPlayerRequestDropItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DropItemRequestSystemListener extends EntityEventSystem<EntityStore, DropItemEvent.PlayerRequest> {
    public DropItemRequestSystemListener() {
        super(DropItemEvent.PlayerRequest.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull DropItemEvent.PlayerRequest event
    ) {
        HsEventPlayerRequestDropItem requestDropItemEvent = new HsEventPlayerRequestDropItem(archetypeChunk, store, commandBuffer, HsServer.getPlayer(store), event.getInventorySectionId(), event.getSlotId());
        HsEventManager.call(requestDropItemEvent);

        if (requestDropItemEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}