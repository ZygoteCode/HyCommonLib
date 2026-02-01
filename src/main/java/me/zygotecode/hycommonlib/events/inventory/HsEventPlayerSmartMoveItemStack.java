package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.SmartMoveType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerSmartMoveItemStack extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private int fromSectionId;
    private int fromSlotId;
    private int quantity;
    private SmartMoveType moveType;

    public HsEventPlayerSmartMoveItemStack(PacketHandler packetHandler, Packet packet, int fromSectionId, int fromSlotId, int quantity, SmartMoveType moveType) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.fromSectionId = fromSectionId;
        this.fromSlotId = fromSlotId;
        this.quantity = quantity;
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

    public int getFromSectionId() {
        return this.fromSectionId;
    }

    public void setFromSectionId(int fromSectionId) {
        this.fromSectionId = fromSectionId;
    }

    public int getFromSlotId() {
        return this.fromSlotId;
    }

    public void setFromSlotId(int fromSlotId) {
        this.fromSlotId = fromSlotId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SmartMoveType getMoveType() {
        return this.moveType;
    }

    public void setMoveType(SmartMoveType moveType) {
        this.moveType = moveType;
    }
}