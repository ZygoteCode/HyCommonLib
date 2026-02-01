package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.PlayerSkin;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerOptions extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private PlayerSkin skin;

    public HsEventPlayerOptions(PacketHandler packetHandler, Packet packet, PlayerSkin skin) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.skin = skin;
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

    public PlayerSkin getSkin() {
        return this.skin;
    }

    public void setSkin(PlayerSkin skin) {
        this.skin = skin;
    }
}