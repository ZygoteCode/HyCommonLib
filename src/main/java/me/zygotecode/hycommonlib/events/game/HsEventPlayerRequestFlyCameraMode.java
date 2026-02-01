package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerRequestFlyCameraMode extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private boolean entering;

    public HsEventPlayerRequestFlyCameraMode(PacketHandler packetHandler, Packet packet, boolean entering) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.entering = entering;
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

    public boolean isEntering() {
        return this.entering;
    }

    public void setEntering(boolean entering) {
        this.entering = entering;
    }
}