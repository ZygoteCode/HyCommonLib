package me.zygotecode.hycommonlib.systems.player;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.player.KnockbackPredictionSystems;
import com.hypixel.hytale.server.core.modules.entity.player.KnockbackSimulation;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.player.HsEventPlayerStartKnockback;
import me.zygotecode.hycommonlib.events.system.player.HsEventPlayerStopKnockback;
import me.zygotecode.hycommonlib.player.HsPlayer;

import javax.annotation.Nonnull;

public class KnockbackPredictionSystemListener extends KnockbackPredictionSystems.InitKnockback {
    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull KnockbackSimulation component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = (Player)store.getComponent(ref, Player.getComponentType());
        HsPlayer hsPlayer = new HsPlayer(player);

        HsEventPlayerStartKnockback startKnockbackEvent = new  HsEventPlayerStartKnockback(hsPlayer, component);
        HsEventManager.call(startKnockbackEvent);
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull KnockbackSimulation knockbackSimulationComponent, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = (Player)store.getComponent(ref, Player.getComponentType());
        HsPlayer hsPlayer = new HsPlayer(player);

        HsEventPlayerStopKnockback stopKnockbackEvent = new  HsEventPlayerStopKnockback(hsPlayer, knockbackSimulationComponent);
        HsEventManager.call(stopKnockbackEvent);
    }

    @Nonnull
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }
}