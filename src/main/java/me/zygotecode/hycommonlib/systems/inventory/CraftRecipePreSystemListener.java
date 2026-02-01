package me.zygotecode.hycommonlib.systems.inventory;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.inventory.HsEventPlayerPreCraftRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftRecipePreSystemListener extends EntityEventSystem<EntityStore, CraftRecipeEvent.Pre> {
    public CraftRecipePreSystemListener() {
        super(CraftRecipeEvent.Pre.class);
    }

    @Override
    public void handle(
            int index,
            @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull CraftRecipeEvent.Pre event
    ) {
        HsEventPlayerPreCraftRecipe preCraftRecipeEvent = new HsEventPlayerPreCraftRecipe(archetypeChunk, store, commandBuffer, HsServer.getPlayer(store), event.getCraftedRecipe(), event.getQuantity());
        HsEventManager.call(preCraftRecipeEvent);

        if (preCraftRecipeEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}