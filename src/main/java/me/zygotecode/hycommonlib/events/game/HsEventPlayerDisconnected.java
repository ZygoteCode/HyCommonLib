package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.connection.DisconnectType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerDisconnected extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private String reason;
    private DisconnectType type;

    public HsEventPlayerDisconnected(PacketHandler packetHandler, Packet packet, String reason, DisconnectType type) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.reason = reason;
        this.type = type;
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

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DisconnectType getType() {
        return this.type;
    }

    public void setType(DisconnectType type) {
        this.type = type;
    }
}