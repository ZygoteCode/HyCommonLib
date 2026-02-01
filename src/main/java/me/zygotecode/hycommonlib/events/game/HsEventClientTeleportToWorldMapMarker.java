package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientTeleportToWorldMapMarker extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private String markerId;

    public HsEventClientTeleportToWorldMapMarker(PacketHandler packetHandler, Packet packet, String markerId) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.markerId = markerId;
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

    public String getMarkerId() {
        return this.markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }
}