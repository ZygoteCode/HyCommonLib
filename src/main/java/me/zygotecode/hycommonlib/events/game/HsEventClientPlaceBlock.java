package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.BlockRotation;
import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventClientPlaceBlock extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;

    private int placedBlockId;
    private BlockPosition blockPosition;
    private BlockRotation blockRotation;

    public HsEventClientPlaceBlock(PacketHandler packetHandler, Packet packet, int placedBlockId, BlockPosition blockPosition, BlockRotation blockRotation) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.placedBlockId = placedBlockId;
        this.blockPosition = blockPosition;
        this.blockRotation = blockRotation;
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

    public int getPlacedBlockId() {
        return this.placedBlockId;
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    public BlockRotation getBlockRotation() {
        return this.blockRotation;
    }

    public void setPlacedBlockId(int placedBlockId) {
        this.placedBlockId = placedBlockId;
    }

    public void setBlockPosition(BlockPosition blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setBlockRotation(BlockRotation blockRotation) {
        this.blockRotation = blockRotation;
    }
}