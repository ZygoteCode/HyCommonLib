package me.zygotecode.hycommonlib.systems.player;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entity.teleport.TeleportSystems;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.system.player.HsEventPlayerTeleport;
import me.zygotecode.hycommonlib.location.HsLocation;
import me.zygotecode.hycommonlib.player.HsPlayer;
import me.zygotecode.hycommonlib.world.HsWorld;

import javax.annotation.Nonnull;

public class TeleportSystemListener extends TeleportSystems.PlayerMoveSystem {
    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull Teleport teleport, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        World targetWorld = teleport.getWorld();
        boolean isInAnotherWorld = false;

        if (targetWorld != null && !targetWorld.equals(((EntityStore)store.getExternalData()).getWorld())) {
            isInAnotherWorld = true;
        }

        Player player = (Player)store.getComponent(ref, Player.getComponentType());
        HsPlayer hsPlayer = new HsPlayer(player);
        HsLocation hsLocation = new HsLocation(teleport.getPosition().x, teleport.getPosition().y, teleport.getPosition().z, teleport.getHeadRotation().x,  teleport.getHeadRotation().y, teleport.getHeadRotation().z);

        HsEventPlayerTeleport eventPlayerTeleport = new HsEventPlayerTeleport(hsPlayer, new HsWorld(targetWorld == null ? player.getWorld() : targetWorld), isInAnotherWorld, teleport, hsLocation);
        HsEventManager.call(eventPlayerTeleport);
    }

    @Nonnull
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }
}
