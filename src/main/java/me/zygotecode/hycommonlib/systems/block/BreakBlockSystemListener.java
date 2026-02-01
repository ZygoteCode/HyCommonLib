package me.zygotecode.hycommonlib.systems.block;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.block.HsEventBreakBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BreakBlockSystemListener extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    public BreakBlockSystemListener() {
        super(BreakBlockEvent.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull BreakBlockEvent event
    ) {
        HsEventBreakBlock breakBlockEvent = new HsEventBreakBlock(archetypeChunk, store, commandBuffer, event.getBlockType(), event.getTargetBlock(), event.getItemInHand(), HsServer.getPlayer(store));
        HsEventManager.call(breakBlockEvent);

        if (breakBlockEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setTargetBlock(breakBlockEvent.getTargetBlock());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
