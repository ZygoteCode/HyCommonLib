package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.serveraccess.Access;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientSetServerAccess extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private Access access;
    private String password;

    public HsEventClientSetServerAccess(PacketHandler packetHandler, Packet packet, Access access, String password) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.access = access;
        this.password = password;
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

    public Access getAccess() {
        return this.access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}