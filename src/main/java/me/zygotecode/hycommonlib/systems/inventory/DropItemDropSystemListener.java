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
import me.zygotecode.hycommonlib.events.system.inventory.HsEventPlayerDropItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DropItemDropSystemListener extends EntityEventSystem<EntityStore, DropItemEvent.Drop> {
    public DropItemDropSystemListener() {
        super(DropItemEvent.Drop.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull DropItemEvent.Drop event
    ) {
        HsEventPlayerDropItem dropItemEvent = new HsEventPlayerDropItem(archetypeChunk, store, commandBuffer, HsServer.getPlayer(store), event.getItemStack(), event.getThrowSpeed());
        HsEventManager.call(dropItemEvent);

        if (dropItemEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setItemStack(dropItemEvent.getItemStack());
        event.setThrowSpeed(dropItemEvent.getThrowSpeed());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
