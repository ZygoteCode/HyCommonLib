package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.ItemQuantity;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerSetCreativeItem extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private ItemQuantity item;
    private int inventorySectionId;
    private boolean override;
    private int slotId;

    public HsEventPlayerSetCreativeItem(PacketHandler packetHandler, Packet packet, ItemQuantity item, int inventorySectionId, boolean override, int slotId) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.item = item;
        this.inventorySectionId = inventorySectionId;
        this.override = override;
        this.slotId = slotId;
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

    public int getInventorySectionId() {
        return this.inventorySectionId;
    }

    public boolean isOverride() {
        return this.override;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public void setItem(ItemQuantity item) {
        this.item = item;
    }

    public void setInventorySectionId(int inventorySectionId) {
        this.inventorySectionId = inventorySectionId;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
}