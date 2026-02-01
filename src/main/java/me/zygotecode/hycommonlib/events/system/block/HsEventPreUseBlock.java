package me.zygotecode.hycommonlib.events.system.block;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPreUseBlock extends HsEventCancellable {
    private ArchetypeChunk<EntityStore> archetypeChunk;
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;

    private BlockType blockType;
    private Vector3i targetBlock;
    private InteractionContext context;
    private InteractionType interactionType;
    private HsPlayer player;

    public HsEventPreUseBlock(ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, BlockType blockType, Vector3i targetBlock, InteractionContext context, InteractionType interactionType, HsPlayer player) {
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.blockType = blockType;
        this.targetBlock = targetBlock;
        this.context = context;
        this.interactionType = interactionType;
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

    public BlockType getBlockType() {
        return this.blockType;
    }

    public Vector3i getTargetBlock() {
        return this.targetBlock;
    }

    public InteractionContext getInteractionContext() {
        return this.context;
    }

    public InteractionType getInteractionType() {
        return this.interactionType;
    }

    public HsPlayer getPlayer() {
        return this.player;
    }
}