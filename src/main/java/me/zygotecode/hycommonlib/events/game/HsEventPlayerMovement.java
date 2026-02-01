package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerMovement extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private MovementStates movementStates;
    private HalfFloatPosition relativePosition;
    private Position absolutePosition;
    private Direction bodyOrientation;
    private Direction lookOrientation;
    private TeleportAck teleportAck;
    private Position wishMovement;
    private Vector3d velocity;
    private int mountedTo;
    private MovementStates riderMovementStates;

    public HsEventPlayerMovement(PacketHandler packetHandler, Packet packet, MovementStates movementStates, HalfFloatPosition relativePosition, Position absolutePosition, Direction bodyOrientation, Direction lookOrientation, TeleportAck teleportAck, Position wishMovement, Vector3d velocity, int mountedTo, MovementStates riderMovementStates) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.movementStates = movementStates;
        this.relativePosition = relativePosition;
        this.absolutePosition = absolutePosition;
        this.bodyOrientation = bodyOrientation;
        this.lookOrientation = lookOrientation;
        this.teleportAck = teleportAck;
        this.wishMovement = wishMovement;
        this.velocity = velocity;
        this.mountedTo = mountedTo;
        this.riderMovementStates = riderMovementStates;
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

    public MovementStates getMovementStates() {
        return this.movementStates;
    }

    public void setMovementStates(MovementStates movementStates) {
        this.movementStates = movementStates;
    }

    public HalfFloatPosition getRelativePosition() {
        return this.relativePosition;
    }

    public void setRelativePosition(HalfFloatPosition relativePosition) {
        this.relativePosition = relativePosition;
    }

    public Position getAbsolutePosition() {
        return this.absolutePosition;
    }

    public void setAbsolutePosition(Position absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public Direction getBodyOrientation() {
        return this.bodyOrientation;
    }

    public void setBodyOrientation(Direction bodyOrientation) {
        this.bodyOrientation = bodyOrientation;
    }

    public Direction getLookOrientation() {
        return this.lookOrientation;
    }

    public void setLookOrientation(Direction lookOrientation) {
        this.lookOrientation = lookOrientation;
    }

    public TeleportAck getTeleportAck() {
        return this.teleportAck;
    }

    public void setTeleportAck(TeleportAck teleportAck) {
        this.teleportAck = teleportAck;
    }

    public Position getWishMovement() {
        return this.wishMovement;
    }

    public void setWishMovement(Position wishMovement) {
        this.wishMovement = wishMovement;
    }

    public Vector3d getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public int getMountedTo() {
        return this.mountedTo;
    }

    public void setMountedTo(int mountedTo) {
        this.mountedTo = mountedTo;
    }

    public MovementStates getRiderMovementStates() {
        return this.riderMovementStates;
    }

    public void setRiderMovementStates(MovementStates riderMovementStates) {
        this.riderMovementStates = riderMovementStates;
    }
}