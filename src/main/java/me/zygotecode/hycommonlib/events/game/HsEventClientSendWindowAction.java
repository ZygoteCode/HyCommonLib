package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.window.WindowAction;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientSendWindowAction extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private WindowAction action;
    private int id;

    public HsEventClientSendWindowAction(PacketHandler packetHandler, Packet packet, WindowAction action, int id) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.action = action;
        this.id = id;
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

    public WindowAction getAction() {
        return this.action;
    }

    public void setAction(WindowAction action) {
        this.action = action;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}