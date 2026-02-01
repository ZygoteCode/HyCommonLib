package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerSwitchHotbarBlockSet extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private String itemId;

    public HsEventPlayerSwitchHotbarBlockSet(PacketHandler packetHandler, Packet packet, String itemId) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.itemId = itemId;
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

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}