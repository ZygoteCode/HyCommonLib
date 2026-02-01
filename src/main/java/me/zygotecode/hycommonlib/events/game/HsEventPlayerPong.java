package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.InstantData;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.connection.PongType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerPong extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;

    private PongType type;
    private int id;
    private short packetQueueSize;
    private InstantData time;

    public HsEventPlayerPong(PacketHandler packetHandler, Packet packet, PongType type, int id, short packetQueueSize, InstantData time) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.type = type;
        this.id = id;
        this.packetQueueSize = packetQueueSize;
        this.time = time;
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

    public PongType getType() {
        return this.type;
    }

    public void setType(PongType type) {
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getPacketQueueSize() {
        return this.packetQueueSize;
    }

    public void setPacketQueueSize(short packetQueueSize) {
        this.packetQueueSize = packetQueueSize;
    }

    public InstantData getTime() {
        return this.time;
    }

    public void setTime(InstantData time) {
        this.time = time;
    }
}