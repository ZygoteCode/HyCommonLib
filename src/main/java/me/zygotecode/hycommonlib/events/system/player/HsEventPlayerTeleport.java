package me.zygotecode.hycommonlib.events.system.player;

import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.location.HsLocation;
import me.zygotecode.hycommonlib.player.HsPlayer;
import me.zygotecode.hycommonlib.world.HsWorld;

public class HsEventPlayerTeleport extends HsEventCancellable {
    private HsPlayer player;
    private HsWorld targetWorld;
    private boolean isInAnotherWorld;
    private Teleport teleport;
    private HsLocation teleportLocation;

    public HsEventPlayerTeleport(HsPlayer player, HsWorld targetWorld, boolean isInAnotherWorld, Teleport teleport, HsLocation teleportLocation) {
        this.player = player;
        this.targetWorld = targetWorld;
        this.isInAnotherWorld = isInAnotherWorld;
        this.teleport = teleport;
        this.teleportLocation = teleportLocation;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(HsPlayer player) {
        this.player = player;
    }

    public HsWorld getTargetWorld() {
        return this.targetWorld;
    }

    public boolean isInAnotherWorld() {
        return this.isInAnotherWorld;
    }

    public Teleport getTeleport() {
        return this.teleport;
    }

    public HsLocation getTeleportLocation() {
        return this.teleportLocation;
    }
}