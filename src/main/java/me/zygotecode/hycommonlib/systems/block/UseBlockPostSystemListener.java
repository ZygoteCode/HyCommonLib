package me.zygotecode.hycommonlib.systems.block;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.block.HsEventPostUseBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UseBlockPostSystemListener extends EntityEventSystem<EntityStore, UseBlockEvent.Post> {
    public UseBlockPostSystemListener() {
        super(UseBlockEvent.Post.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull UseBlockEvent.Post event
    ) {
        HsEventPostUseBlock postUseBlockEvent = new HsEventPostUseBlock(archetypeChunk, store, commandBuffer, event.getBlockType(), event.getTargetBlock(), event.getContext(), event.getInteractionType(), HsServer.getPlayer(store));
        HsEventManager.call(postUseBlockEvent);
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
