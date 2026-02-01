package me.zygotecode.hycommonlib.events.system.player;

import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerRespawn extends HsEventCancellable {
    private HsPlayer player;

    public HsEventPlayerRespawn(HsPlayer player) {
        this.player = player;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(HsPlayer player) {
        this.player = player;
    }
}