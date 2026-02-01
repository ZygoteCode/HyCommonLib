package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientSyncInteractionChains extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private SyncInteractionChain[] updates;

    public HsEventClientSyncInteractionChains(PacketHandler packetHandler, Packet packet, SyncInteractionChain[] updates) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.updates = updates;
    }

    public PacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public HsPlayer getPlayer() {
        return new HsPlayer(this.getPacketHandler().getAuth().getUuid());
    }

    public SyncInteractionChain[] getUpdates() {
        return this.updates;
    }

    public void setUpdates(SyncInteractionChain[] updates) {
        this.updates = updates;
    }
}