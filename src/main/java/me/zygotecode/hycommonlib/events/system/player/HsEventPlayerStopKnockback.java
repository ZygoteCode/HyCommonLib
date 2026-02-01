package me.zygotecode.hycommonlib.events.system.player;

import com.hypixel.hytale.server.core.modules.entity.player.KnockbackSimulation;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerStopKnockback extends HsEventCancellable {
    private HsPlayer player;
    private KnockbackSimulation knockback;

    public HsEventPlayerStopKnockback(HsPlayer player, KnockbackSimulation knockback) {
        this.player = player;
        this.knockback = knockback;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }

    public KnockbackSimulation getKnockback() {
        return this.knockback;
    }
}