package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.InventoryActionType;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerInventoryAction extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private byte actionData;
    private int inventorySectionId;
    private InventoryActionType inventoryActionType;

    public HsEventPlayerInventoryAction(PacketHandler packetHandler, Packet packet, byte actionData, int inventorySectionId,  InventoryActionType inventoryActionType) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.actionData = actionData;
        this.inventorySectionId = inventorySectionId;
        this.inventoryActionType = inventoryActionType;
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

    public byte getActionData() {
        return this.actionData;
    }

    public int getInventorySectionId() {
        return this.inventorySectionId;
    }

    public InventoryActionType getInventoryActionType() {
        return this.inventoryActionType;
    }

    public void setActionData(byte actionData) {
        this.actionData = actionData;
    }

    public void setInventorySectionId(int inventorySectionId) {
        this.inventorySectionId = inventorySectionId;
    }

    public void setInventoryActionType(InventoryActionType inventoryActionType) {
        this.inventoryActionType = inventoryActionType;
    }
}