package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Direction;
import com.hypixel.hytale.protocol.MovementStates;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.Position;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerMountMovement extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private Position absolutePosition;
    private MovementStates movementStates;
    private Direction bodyOrientation;

    public HsEventPlayerMountMovement(PacketHandler packetHandler, Packet packet, Position absolutePosition, MovementStates movementStates, Direction bodyOrientation) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.absolutePosition = absolutePosition;
        this.movementStates = movementStates;
        this.bodyOrientation = bodyOrientation;
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

    public Position getAbsolutePosition() {
        return this.absolutePosition;
    }

    public MovementStates getMovementStates() {
        return this.movementStates;
    }

    public Direction getBodyOrientation() {
        return this.bodyOrientation;
    }

    public void setAbsolutePosition(Position absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public void setMovementStates(MovementStates movementStates) {
        this.movementStates = movementStates;
    }

    public void setBodyOrientation(Direction bodyOrientation) {
        this.bodyOrientation = bodyOrientation;
    }
}