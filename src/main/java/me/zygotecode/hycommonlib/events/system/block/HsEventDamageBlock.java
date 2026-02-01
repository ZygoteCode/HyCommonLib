package me.zygotecode.hycommonlib.events.system.block;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventDamageBlock extends HsEventCancellable {
    private float damage;
    private BlockType blockType;
    private float currentDamage;
    private Vector3i targetBlock;
    private ItemStack itemInHand;
    private ArchetypeChunk<EntityStore> archetypeChunk;
    private Store<EntityStore> store;
    private CommandBuffer<EntityStore> commandBuffer;
    private HsPlayer player;

    public HsEventDamageBlock(float damage, BlockType blockType, float currentDamage, Vector3i targetBlock, ItemStack itemInHand, ArchetypeChunk<EntityStore> archetypeChunk, Store<EntityStore> store, CommandBuffer<EntityStore> commandBuffer, HsPlayer player) {
        this.damage = damage;
        this.blockType = blockType;
        this.currentDamage = currentDamage;
        this.targetBlock = targetBlock;
        this.itemInHand = itemInHand;
        this.archetypeChunk = archetypeChunk;
        this.store = store;
        this.commandBuffer = commandBuffer;
        this.player = player;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public float getCurrentDamage() {
        return this.currentDamage;
    }

    public Vector3i getTargetBlock() {
        return this.targetBlock;
    }

    public void setTargetBlock(Vector3i targetBlock) {
        this.targetBlock = targetBlock;
    }

    public ItemStack getItemInHand() {
        return this.itemInHand;
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

    public HsPlayer getPlayer() {
        return this.player;
    }
}