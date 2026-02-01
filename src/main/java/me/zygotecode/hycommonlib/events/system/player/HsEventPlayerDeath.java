package me.zygotecode.hycommonlib.events.system.player;

import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerDeath extends HsEventCancellable {
    private HsPlayer player;
    private DeathComponent deathDetails;

    public HsEventPlayerDeath(HsPlayer player, DeathComponent deathDetails) {
        this.player = player;
        this.deathDetails = deathDetails;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(HsPlayer player) {
        this.player = player;
    }

    public DeathComponent getDeathDetails() {
        return this.deathDetails;
    }

    public void setDeathDetails(DeathComponent deathDetails) {
        this.deathDetails = deathDetails;
    }
}