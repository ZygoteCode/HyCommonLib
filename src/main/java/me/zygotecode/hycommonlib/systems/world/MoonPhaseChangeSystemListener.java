package me.zygotecode.hycommonlib.systems.world;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.WorldEventSystem;
import com.hypixel.hytale.server.core.universe.world.events.ecs.MoonPhaseChangeEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.world.HsEventWorldMoonPhaseChange;

import javax.annotation.Nonnull;

public class MoonPhaseChangeSystemListener extends WorldEventSystem<EntityStore, MoonPhaseChangeEvent> {
    public MoonPhaseChangeSystemListener() {
        super(MoonPhaseChangeEvent.class);
    }

    @Override
    public void handle(
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull MoonPhaseChangeEvent event
    ) {
        HsEventWorldMoonPhaseChange worldMoonPhaseChangeEvent = new HsEventWorldMoonPhaseChange(store, commandBuffer, HsServer.getWorld(store), event.getNewMoonPhase());
        HsEventManager.call(worldMoonPhaseChangeEvent);
    }
}