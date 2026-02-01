package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerMoveItemStack extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private int quantity;
    private int fromSectionId;
    private int toSectionId;
    private int fromSlotId;
    private int toSlotId;

    public HsEventPlayerMoveItemStack(PacketHandler packetHandler, Packet packet, int quantity, int fromSectionId,  int toSectionId, int fromSlotId, int toSlotId) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.quantity = quantity;
        this.fromSectionId = fromSectionId;
        this.toSectionId = toSectionId;
        this.fromSlotId = fromSlotId;
        this.toSlotId = toSlotId;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFromSectionId() {
        return this.fromSectionId;
    }

    public void setFromSectionId(int fromSectionId) {
        this.fromSectionId = fromSectionId;
    }

    public int getToSectionId() {
        return this.toSectionId;
    }

    public void setToSectionId(int toSectionId) {
        this.toSectionId = toSectionId;
    }

    public int getFromSlotId() {
        return this.fromSlotId;
    }

    public void setFromSlotId(int fromSlotId) {
        this.fromSlotId = fromSlotId;
    }

    public int getToSlotId() {
        return this.toSlotId;
    }

    public void setToSlotId(int toSlotId) {
        this.toSlotId = toSlotId;
    }
}