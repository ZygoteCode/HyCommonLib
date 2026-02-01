package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Asset;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientRequestAssets extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private Asset[] assets;
    
    public HsEventClientRequestAssets(PacketHandler packetHandler, Packet packet, Asset[] assets) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.assets = assets;
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

    public Asset[] getAssets() {
        return this.assets;
    }

    public void setAssets(Asset[] assets) {
        this.assets = assets;
    }
}