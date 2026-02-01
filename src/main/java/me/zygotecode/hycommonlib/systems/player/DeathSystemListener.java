package me.zygotecode.hycommonlib.systems.player;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.player.HsEventPlayerDeath;
import me.zygotecode.hycommonlib.events.system.player.HsEventPlayerRespawn;
import me.zygotecode.hycommonlib.player.HsPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeathSystemListener extends DeathSystems.OnDeathSystem {
    @Override
    public void onComponentAdded(@Nonnull Ref ref, @Nonnull DeathComponent component, @Nonnull Store store, @Nonnull CommandBuffer commandBuffer) {
        DeathComponent deathComp = (DeathComponent)store.getComponent(ref, DeathComponent.getComponentType());
        Player player = (Player)store.getComponent(ref, Player.getComponentType());
        HsPlayer hsPlayer = new HsPlayer(player);
        HsServer.addDeadPlayer(hsPlayer.getUuid());

        HsEventPlayerDeath eventPlayerDeath = new HsEventPlayerDeath(hsPlayer, deathComp);
        HsEventManager.call(eventPlayerDeath);
    }

    @Override
    public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Player player = (Player)store.getComponent(ref, Player.getComponentType());
        HsPlayer hsPlayer = new HsPlayer(player);
        HsServer.removeDeadPlayer(hsPlayer.getUuid());

        HsEventPlayerRespawn eventPlayerRespawn = new HsEventPlayerRespawn(hsPlayer);
        HsEventManager.call(eventPlayerRespawn);
    }

    @Nullable
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }
}