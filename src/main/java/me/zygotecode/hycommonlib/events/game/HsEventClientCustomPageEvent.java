package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageEventType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientCustomPageEvent extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private CustomPageEventType type;
    private String data;

    public HsEventClientCustomPageEvent(PacketHandler packetHandler, Packet packet, CustomPageEventType type, String data) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.type = type;
        this.data = data;
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

    public CustomPageEventType getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setType(CustomPageEventType type) {
        this.type = type;
    }
}