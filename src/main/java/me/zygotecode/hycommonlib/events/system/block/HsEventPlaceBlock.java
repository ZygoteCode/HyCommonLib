package me.zygotecode.hycommonlib.events.system.block;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlaceBlock extends HsEventCancellable {
    private ArchetypeChunk<EntityStore> archetypeChunk;
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;

    private ItemStack itemInHand;
    private Vector3i targetBlock;
    private RotationTuple rotation;
    private HsPlayer player;

    public HsEventPlaceBlock(ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, ItemStack itemInHand, Vector3i targetBlock, RotationTuple rotation, HsPlayer player) {
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.itemInHand = itemInHand;
        this.targetBlock = targetBlock;
        this.rotation = rotation;
        this.player = player;
    }

    public ArchetypeChunk<EntityStore> getArchetypeChunk() {
        return this.archetypeChunk;
    }

    public Store<EntityStore> getStore() {
        return this.store;
    }

    public CommandBuffer<EntityStore> getCommandBuffer() {
        return this.commandBuffer;
    }

    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    public Vector3i getTargetBlock() {
        return this.targetBlock;
    }

    public RotationTuple getRotation() {
        return this.rotation;
    }

    public void setTargetBlock(Vector3i targetBlock) {
        this.targetBlock = targetBlock;
    }

    public void setRotation(RotationTuple rotation) {
        this.rotation = rotation;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }
}