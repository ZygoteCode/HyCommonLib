package me.zygotecode.hycommonlib.systems.block;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.block.HsEventPlaceBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaceBlockSystemListener extends EntityEventSystem<EntityStore, PlaceBlockEvent> {
    public PlaceBlockSystemListener() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull PlaceBlockEvent event
    ) {
        HsEventPlaceBlock placeBlockEvent = new HsEventPlaceBlock(archetypeChunk, store, commandBuffer, event.getItemInHand(), event.getTargetBlock(), event.getRotation(), HsServer.getPlayer(store));
        HsEventManager.call(placeBlockEvent);

        if (placeBlockEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setTargetBlock(placeBlockEvent.getTargetBlock());
        event.setRotation(placeBlockEvent.getRotation());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}