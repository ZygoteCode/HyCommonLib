package me.zygotecode.hycommonlib.events.packet;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventServerPacket extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;

    public HsEventServerPacket(PacketHandler packetHandler, Packet packet) {
        this.packetHandler = packetHandler;
        this.packet = packet;
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
}