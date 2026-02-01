package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.ItemQuantity;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerDropCreativeItem extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private ItemQuantity item;

    public HsEventPlayerDropCreativeItem(PacketHandler packetHandler, Packet packet, ItemQuantity item) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.item = item;
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

    public ItemQuantity getItem() {
        return this.item;
    }

    public void setItem(ItemQuantity item) {
        this.item = item;
    }
}