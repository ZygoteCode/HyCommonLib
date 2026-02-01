package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientReady extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private boolean readyForChunks;
    private boolean readyForGameplay;

    public HsEventClientReady(PacketHandler packetHandler, Packet packet, boolean readyForChunks, boolean readyForGameplay) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.readyForChunks = readyForChunks;
        this.readyForGameplay = readyForGameplay;
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

    public boolean isReadyForGameplay() {
        return this.readyForGameplay;
    }

    public boolean isReadyForChunks() {
        return this.readyForChunks;
    }

    public void setReadyForGameplay(boolean readyForGameplay) {
        this.readyForGameplay = readyForGameplay;
    }

    public void setReadyForChunks(boolean readyForChunks) {
        this.readyForChunks = readyForChunks;
    }
}