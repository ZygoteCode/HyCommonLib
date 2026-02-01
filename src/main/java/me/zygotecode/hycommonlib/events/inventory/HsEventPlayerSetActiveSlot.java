package me.zygotecode.hycommonlib.events.inventory;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerSetActiveSlot extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private int activeSlot;
    private int inventorySectionId;

    public HsEventPlayerSetActiveSlot(PacketHandler packetHandler, Packet packet, int activeSlot, int inventorySectionId) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.activeSlot = activeSlot;
        this.inventorySectionId = inventorySectionId;
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

    public int getActiveSlot() {
        return this.activeSlot;
    }

    public void setActiveSlot(int activeSlot) {
        this.activeSlot = activeSlot;
    }

    public int getInventorySectionId() {
        return this.inventorySectionId;
    }

    public void setInventorySectionId(int inventorySectionId) {
        this.inventorySectionId = inventorySectionId;
    }
}