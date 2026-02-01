package me.zygotecode.hycommonlib.systems.block;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.block.HsEventDamageBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DamageBlockSystemListener extends EntityEventSystem<EntityStore, DamageBlockEvent> {
    public DamageBlockSystemListener() {
        super(DamageBlockEvent.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull DamageBlockEvent event
    ) {
        HsEventDamageBlock damageBlockEvent = new HsEventDamageBlock(event.getDamage(), event.getBlockType(), event.getCurrentDamage(), event.getTargetBlock(), event.getItemInHand(), archetypeChunk, store, commandBuffer, HsServer.getPlayer(store));
        HsEventManager.call(damageBlockEvent);

        if (damageBlockEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setDamage(damageBlockEvent.getDamage());
        event.setTargetBlock(damageBlockEvent.getTargetBlock());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
