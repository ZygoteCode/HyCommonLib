package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.PickupLocation;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientSyncPlayerPreferences extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;

    private boolean allowNPCDetection;
    private PickupLocation armorItemsPreferredPickupLocation;
    private PickupLocation miscItemsPreferredPickupLocation;
    private boolean respondToHit;
    private boolean showEntityMarkers;
    private PickupLocation solidBlockItemsPreferredPickupLocation;
    private PickupLocation usableItemsPreferredPickupLocation;
    private PickupLocation weaponAndToolItemsPreferredPickupLocation;

    public HsEventClientSyncPlayerPreferences(PacketHandler packetHandler, Packet packet, boolean allowNPCDetection, PickupLocation armorItemsPreferredPickupLocation, PickupLocation miscItemsPreferredPickupLocation, boolean respondToHit, boolean showEntityMarkers, PickupLocation solidBlockItemsPreferredPickupLocation, PickupLocation usableItemsPreferredPickupLocation, PickupLocation weaponAndToolItemsPreferredPickupLocation) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.allowNPCDetection = allowNPCDetection;
        this.armorItemsPreferredPickupLocation = armorItemsPreferredPickupLocation;
        this.miscItemsPreferredPickupLocation = miscItemsPreferredPickupLocation;
        this.respondToHit = respondToHit;
        this.showEntityMarkers = showEntityMarkers;
        this.solidBlockItemsPreferredPickupLocation = solidBlockItemsPreferredPickupLocation;
        this.usableItemsPreferredPickupLocation = usableItemsPreferredPickupLocation;
        this.weaponAndToolItemsPreferredPickupLocation = weaponAndToolItemsPreferredPickupLocation;
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

    public boolean isAllowNPCDetection() {
        return this.allowNPCDetection;
    }

    public void setAllowNPCDetection(boolean allowNPCDetection) {
        this.allowNPCDetection = allowNPCDetection;
    }

    public PickupLocation getArmorItemsPreferredPickupLocation() {
        return this.armorItemsPreferredPickupLocation;
    }

    public void setArmorItemsPreferredPickupLocation(PickupLocation armorItemsPreferredPickupLocation) {
        this.armorItemsPreferredPickupLocation = armorItemsPreferredPickupLocation;
    }

    public PickupLocation getMiscItemsPreferredPickupLocation() {
        return this.miscItemsPreferredPickupLocation;
    }

    public void setMiscItemsPreferredPickupLocation(PickupLocation miscItemsPreferredPickupLocation) {
        this.miscItemsPreferredPickupLocation = miscItemsPreferredPickupLocation;
    }

    public boolean isRespondToHit() {
        return this.respondToHit;
    }

    public void setRespondToHit(boolean respondToHit) {
        this.respondToHit = respondToHit;
    }

    public boolean isShowEntityMarkers() {
        return this.showEntityMarkers;
    }

    public void setShowEntityMarkers(boolean showEntityMarkers) {
        this.showEntityMarkers = showEntityMarkers;
    }

    public PickupLocation getSolidBlockItemsPreferredPickupLocation() {
        return this.solidBlockItemsPreferredPickupLocation;
    }

    public void setSolidBlockItemsPreferredPickupLocation(PickupLocation solidBlockItemsPreferredPickupLocation) {
        this.solidBlockItemsPreferredPickupLocation = solidBlockItemsPreferredPickupLocation;
    }

    public PickupLocation getUsableItemsPreferredPickupLocation() {
        return this.usableItemsPreferredPickupLocation;
    }

    public void setUsableItemsPreferredPickupLocation(PickupLocation usableItemsPreferredPickupLocation) {
        this.usableItemsPreferredPickupLocation = usableItemsPreferredPickupLocation;
    }

    public PickupLocation getWeaponAndToolItemsPreferredPickupLocation() {
        return this.weaponAndToolItemsPreferredPickupLocation;
    }

    public void setWeaponAndToolItemsPreferredPickupLocation(PickupLocation weaponAndToolItemsPreferredPickupLocation) {
        this.weaponAndToolItemsPreferredPickupLocation = weaponAndToolItemsPreferredPickupLocation;
    }
}