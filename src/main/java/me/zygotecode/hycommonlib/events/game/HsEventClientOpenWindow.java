package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.window.WindowType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientOpenWindow extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private WindowType type;

    public HsEventClientOpenWindow(PacketHandler packetHandler, Packet packet, WindowType type) {
        this.packetHandler = packetHandler;
        this.packet = packet;
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

    public WindowType getType() {
        return this.type;
    }

    public void setType(WindowType type) {
        this.type = type;
    }
}