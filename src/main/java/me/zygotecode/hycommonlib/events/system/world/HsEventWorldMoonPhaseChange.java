package me.zygotecode.hycommonlib.events.system.world;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.world.HsWorld;

public class HsEventWorldMoonPhaseChange extends HsEventCancellable {
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;
    private HsWorld world;
    private int newMoonPhase;

    public HsEventWorldMoonPhaseChange(Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, HsWorld world, int newMoonPhase) {
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.world = world;
        this.newMoonPhase = newMoonPhase;
    }

    public Store<EntityStore> getStore() {
        return this.store;
    }

    public CommandBuffer<EntityStore> getCommandBuffer() {
        return this.commandBuffer;
    }

    public HsWorld getWorld() {
        return this.world;
    }

    public int getNewMoonPhase() {
        return this.newMoonPhase;
    }
}
