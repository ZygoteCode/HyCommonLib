package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerMouseInteraction extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;

    private int activeSlot;
    private MouseButtonEvent mouseButton;
    private MouseMotionEvent mouseMotion;
    private long clientTimestamp;
    private String itemInHandId;
    private Vector2f screenPoint;
    private WorldInteraction worldInteraction;

    public HsEventPlayerMouseInteraction(PacketHandler packetHandler, Packet packet, int activeSlot, MouseButtonEvent mouseButton, MouseMotionEvent mouseMotion, long clientTimestamp, String itemInHandId, Vector2f screenPoint, WorldInteraction worldInteraction) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.activeSlot = activeSlot;
        this.mouseButton = mouseButton;
        this.mouseMotion = mouseMotion;
        this.clientTimestamp = clientTimestamp;
        this.itemInHandId = itemInHandId;
        this.screenPoint = screenPoint;
        this.worldInteraction = worldInteraction;
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

    public MouseButtonEvent getMouseButton() {
        return this.mouseButton;
    }

    public void setMouseButton(MouseButtonEvent mouseButton) {
        this.mouseButton = mouseButton;
    }

    public MouseMotionEvent getMouseMotion() {
        return this.mouseMotion;
    }

    public void setMouseMotion(MouseMotionEvent mouseMotion) {
        this.mouseMotion = mouseMotion;
    }

    public long getClientTimestamp() {
        return this.clientTimestamp;
    }

    public void setClientTimestamp(long clientTimestamp) {
        this.clientTimestamp = clientTimestamp;
    }

    public String getItemInHandId() {
        return this.itemInHandId;
    }

    public void setItemInHandId(String itemInHandId) {
        this.itemInHandId = itemInHandId;
    }

    public Vector2f getScreenPoint() {
        return this.screenPoint;
    }

    public void setScreenPoint(Vector2f screenPoint) {
        this.screenPoint = screenPoint;
    }

    public WorldInteraction getWorldInteraction() {
        return this.worldInteraction;
    }

    public void setWorldInteraction(WorldInteraction worldInteraction) {
        this.worldInteraction = worldInteraction;
    }
}