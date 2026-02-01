package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.ItemQuantity;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.SmartMoveType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerSmartGiveCreativeItem extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private ItemQuantity item;
    private SmartMoveType moveType;

    public HsEventPlayerSmartGiveCreativeItem(PacketHandler packetHandler, Packet packet, ItemQuantity item, SmartMoveType moveType) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.item = item;
        this.moveType = moveType;
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

    public SmartMoveType getMoveType() {
        return this.moveType;
    }

    public void setMoveType(SmartMoveType moveType) {
        this.moveType = moveType;
    }
}