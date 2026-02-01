package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerDropItemStack extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private int inventorySectionId;
    private int slotId;
    private int quantity;

    public HsEventPlayerDropItemStack(PacketHandler packetHandler, Packet packet, int inventorySectionId, int slotId, int quantity) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.inventorySectionId = inventorySectionId;
        this.slotId = slotId;
        this.quantity = quantity;
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

    public int getInventorySectionId() {
        return this.inventorySectionId;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setInventorySectionId(int inventorySectionId) {
        this.inventorySectionId = inventorySectionId;
    }

    public void setsSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}