package me.zygotecode.hycommonlib.world;

import com.hypixel.hytale.codec.lookup.MapKeyMapCodec;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.common.semver.SemverRange;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.metrics.metric.HistoricMetric;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.protocol.packets.setup.ClientFeature;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.gameplay.*;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemEntityConfig;
import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.ClientEffectWorldSettings;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.WorldNotificationHandler;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.lighting.ChunkLightingManager;
import com.hypixel.hytale.server.core.universe.world.spawn.ISpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.universe.world.storage.provider.IChunkStorageProvider;
import com.hypixel.hytale.server.core.universe.world.storage.resources.IResourceStorageProvider;
import com.hypixel.hytale.server.core.universe.world.worldgen.provider.IWorldGenProvider;
import com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager;
import com.hypixel.hytale.server.core.universe.world.worldmap.provider.IWorldMapProvider;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.location.HsLocation;
import me.zygotecode.hycommonlib.location.HsPosition;
import me.zygotecode.hycommonlib.location.HsRotation;
import me.zygotecode.hycommonlib.player.HsPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class HsWorld {
    private UUID uuid;

    public HsWorld(World world) {
        this.uuid = world.getWorldConfig().getUuid();
    }

    public HsWorld(UUID uuid) {
        this.uuid = uuid;
    }

    public HsWorld(String name) {
        this.uuid = Universe.get().getWorld(name).getWorldConfig().getUuid();
    }

    public HsWorld(Store<EntityStore> store) {
        this.uuid = HsServer.getWorld(store).getWorldConfig().getUuid();
    }

    public World getHytaleWorld() {
        return Universe.get().getWorld(this.uuid);
    }

    public String getName() {
        return this.getHytaleWorld().getName();
    }

    public EntityStore getEntityStore() {
        return this.getHytaleWorld().getEntityStore();
    }

    public Store<EntityStore> getStore() {
        return this.getEntityStore().getStore();
    }

    public void run() {
        this.getHytaleWorld().run();
    }

    public void broadcastFeatures() {
        this.getHytaleWorld().broadcastFeatures();
    }

    public void movePlayersToWorld(World world, Collection<PlayerRef> players) {
        this.getHytaleWorld().drainPlayersTo(world, players);
    }

    public void movePlayersToWorld(HsWorld world, Collection<PlayerRef> players) {
        this.getHytaleWorld().drainPlayersTo(world.getHytaleWorld(), players);
    }

    public void movePlayersToWorld(World world) {
        Collection<PlayerRef> collection = new ArrayList<>();

        for (HsPlayer player: this.getPlayers()) {
            collection.add(player.getPlayerRef());
        }

        this.getHytaleWorld().drainPlayersTo(world, collection);
    }

    public void movePlayersToWorld(HsWorld world) {
        this.movePlayersToWorld(world.getHytaleWorld());
    }

    public void stop() {
        this.getHytaleWorld().stop();
    }

    public <T extends Entity> T spawnEntity(T entity, @Nonnull Vector3d position, Vector3f rotation) {
        return this.getHytaleWorld().spawnEntity(entity, position, rotation);
    }

    public <T extends Entity> T spawnEntity(T entity, @Nonnull HsPosition position, HsRotation rotation) {
        return this.getHytaleWorld().spawnEntity(entity, position.toVector(), rotation.toVector());
    }

    public <T extends Entity> T spawnEntity(T entity, @Nonnull HsLocation location) {
        return this.getHytaleWorld().spawnEntity(entity, location.getPosition().toVector(),  location.getRotation().toVector());
    }

    public WorldConfig getWorldConfig() {
        return this.getHytaleWorld().getWorldConfig();
    }

    public int getBlock(Vector3i pos) {
        return this.getHytaleWorld().getBlock(pos);
    }

    public WorldChunk getChunk(long l) {
        return this.getHytaleWorld().getChunk(l);
    }

    public ChunkStore getChunkStore() {
        return this.getHytaleWorld().getChunkStore();
    }

    public int getFluidId(int x, int y, int z) {
        return this.getHytaleWorld().getFluidId(x, y, z);
    }

    public ChunkLightingManager getChunkLighting() {
        return this.getHytaleWorld().getChunkLighting();
    }

    public BlockType getBlockType(int x, int y, int z) {
        return this.getHytaleWorld().getBlockType(x, y, z);
    }

    public BlockType getBlocKType(Vector3i pos) {
        return this.getHytaleWorld().getBlockType(pos);
    }

    public void execute(Runnable command) {
        this.getHytaleWorld().execute(command);
    }

    public int getTickStepNanos() {
        return this.getHytaleWorld().getTickStepNanos();
    }

    public int getTps() {
        return this.getHytaleWorld().getTps();
    }

    public long getTick() {
        return this.getHytaleWorld().getTick();
    }

    public WorldMapManager getWorldMapManager() {
        return this.getHytaleWorld().getWorldMapManager();
    }

    public HistoricMetric getBufferedTickLengthMetricSet() {
        return this.getHytaleWorld().getBufferedTickLengthMetricSet();
    }

    public Holder<ChunkStore> getBlockComponentHolder(int x, int y, int z) {
        return this.getHytaleWorld().getBlockComponentHolder(x, y, z);
    }

    public Path getSavePath() {
        return this.getHytaleWorld().getSavePath();
    }

    public int getPlayerCount() {
        return this.getHytaleWorld().getPlayerCount();
    }

    public WorldNotificationHandler getNotificationHandler() {
        return this.getHytaleWorld().getNotificationHandler();
    }

    public ArrayList<HsPlayer> getPlayers() {
        ArrayList<HsPlayer> players = new ArrayList<>();

        for (PlayerRef playerRef : this.getHytaleWorld().getPlayerRefs()) {
            players.add(new HsPlayer(playerRef.getUuid()));
        }

        return players;
    }

    public int getNighttimeDurationSeconds() {
        return this.getHytaleWorld().getNighttimeDurationSeconds();
    }

    public HytaleLogger getLogger() {
        return this.getHytaleWorld().getLogger();
    }

    public WorldChunk getNonTickingChunk(long index) {
        return this.getHytaleWorld().getNonTickingChunk(index);
    }

    public WorldChunk getChunkIfInMemory(long index) {
        return this.getHytaleWorld().getChunkIfInMemory(index);
    }

    public WorldChunk getChunkIfLoaded(long index) {
        return this.getHytaleWorld().getChunkIfLoaded(index);
    }

    public WorldChunk getChunkIfNonTicking(long index) {
        return this.getHytaleWorld().getChunkIfNonTicking(index);
    }

    public void setCompassUpdating(boolean updating) {
        this.getHytaleWorld().setCompassUpdating(updating);
    }

    public void setPaused(boolean paused) {
        this.getHytaleWorld().setPaused(paused);
    }

    public void setTicking(boolean ticking) {
        this.getHytaleWorld().setTicking(ticking);
    }

    public void setTps(int tps) {
        this.getHytaleWorld().setTps(tps);
    }

    public void setBlock(int x, int y, int z, String blockTypeKey) {
        this.getHytaleWorld().setBlock(x, y, z, blockTypeKey);
    }

    public void ciao(int x, int y, int z, String blockTypeKey, int settings) {
        this.getHytaleWorld().setBlock(x, y, z, blockTypeKey, settings);
    }

    public Map<ClientFeature, Boolean> getFeatures() {
        return this.getHytaleWorld().getFeatures();
    }

    public void enableFeature(ClientFeature feature, boolean enabled) {
        this.getHytaleWorld().registerFeature(feature, enabled);
    }

    public boolean isAlive() {
        return this.getHytaleWorld().isAlive();
    }

    public boolean isCompassUpdating() {
        return this.getHytaleWorld().isCompassUpdating();
    }

    public boolean isPaused() {
        return this.getHytaleWorld().isPaused();
    }

    public boolean isTicking() {
        return this.getHytaleWorld().isTicking();
    }

    public boolean isInThread() {
        return this.getHytaleWorld().isInThread();
    }

    public boolean isStarted() {
        return this.getHytaleWorld().isStarted();
    }

    public boolean isFeatureEnabled(ClientFeature feature) {
        return this.getHytaleWorld().isFeatureEnabled(feature);
    }

    public GameplayConfig getGameplayConfig() {
        return this.getHytaleWorld().getGameplayConfig();
    }

    public void breakBlock(int x, int y, int z, int settings) {
        this.getHytaleWorld().breakBlock(x, y, z, settings);
    }

    public void performBlockUpdate(int x, int y, int z) {
        this.getHytaleWorld().performBlockUpdate(x, y, z);
    }

    public void placeBlock(int x, int y, int z, BlockType blockType, int rotation) {
        this.getHytaleWorld().testPlaceBlock(x, y, z, blockType, rotation);
    }

    public void stopIndividualWorld() {
        this.getHytaleWorld().stopIndividualWorld();
    }

    public void interrupt() {
        this.getHytaleWorld().interrupt();
    }

    public void clearMetrics() {
        this.getHytaleWorld().clearMetrics();
    }

    public int getDaytimeDurationSeconds() {
        return this.getHytaleWorld().getDaytimeDurationSeconds();
    }

    public CombatConfig getCombatConfig() {
        return this.getGameplayConfig().getCombatConfig();
    }

    public CraftingConfig getCraftingConfig() {
        return this.getGameplayConfig().getCraftingConfig();
    }

    public CameraEffectsConfig getCameraEffectsConfig() {
        return  this.getGameplayConfig().getCameraEffectsConfig();
    }

    public String getCreativePlaySoundSet() {
        return this.getGameplayConfig().getCreativePlaySoundSet();
    }

    public int getCreativePlaySoundSetIndex() {
        return this.getGameplayConfig().getCreativePlaySoundSetIndex();
    }

    public DeathConfig getDeathConfig() {
        return this.getGameplayConfig().getDeathConfig();
    }

    public GatheringConfig getGatheringConfig() {
        return this.getGameplayConfig().getGatheringConfig();
    }

    public ItemDurabilityConfig getItemDurabilityConfig() {
        return this.getGameplayConfig().getItemDurabilityConfig();
    }

    public ItemEntityConfig getItemEntityConfig() {
        return this.getGameplayConfig().getItemEntityConfig();
    }

    public int getMaxEnvironmentalNPCSpawns() {
        return this.getGameplayConfig().getMaxEnvironmentalNPCSpawns();
    }

    public PlayerConfig getPlayerConfig() {
        return this.getGameplayConfig().getPlayerConfig();
    }

    public MapKeyMapCodec.TypeMap<Object> getPluginConfig() {
        return this.getGameplayConfig().getPluginConfig();
    }

    public RespawnConfig getRespawnConfig() {
        return this.getGameplayConfig().getRespawnConfig();
    }

    public boolean getShowItemPickupNotifications() {
        return this.getGameplayConfig().getShowItemPickupNotifications();
    }

    public SpawnConfig getSpawnConfig() {
        return this.getGameplayConfig().getSpawnConfig();
    }

    @Nonnull
    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.getWorldConfig().setUuid(uuid);
        this.uuid = uuid;
    }

    public boolean isDeleteOnUniverseStart() {
        return this.getWorldConfig().isDeleteOnUniverseStart();
    }

    public void setDeleteOnUniverseStart(boolean deleteOnUniverseStart) {
        this.getWorldConfig().setDeleteOnUniverseStart(deleteOnUniverseStart);
    }

    public boolean isDeleteOnRemove() {
        return this.getWorldConfig().isDeleteOnRemove();
    }

    public void setDeleteOnRemove(boolean deleteOnRemove) {
        this.getWorldConfig().setDeleteOnRemove(deleteOnRemove);
    }

    public boolean isSavingConfig() {
        return this.getWorldConfig().isSavingConfig();
    }

    public void setSavingConfig(boolean savingConfig) {
        this.getWorldConfig().setSavingConfig(savingConfig);
    }

    public String getDisplayName() {
        return this.getWorldConfig().getDisplayName();
    }

    public void setDisplayName(String name) {
        this.getWorldConfig().setDisplayName(name);
    }

    public long getSeed() {
        return this.getWorldConfig().getSeed();
    }

    public void setSeed(long seed) {
        this.getWorldConfig().setSeed(seed);
    }

    @Nullable
    public ISpawnProvider getSpawnProvider() {
        return this.getWorldConfig().getSpawnProvider();
    }

    public void setSpawnProvider(ISpawnProvider spawnProvider) {
        this.getWorldConfig().setSpawnProvider(spawnProvider);
    }

    public IWorldGenProvider getWorldGenProvider() {
        return this.getWorldConfig().getWorldGenProvider();
    }

    public void setWorldGenProvider(IWorldGenProvider worldGenProvider) {
        this.getWorldConfig().setWorldGenProvider(worldGenProvider);
    }

    public IWorldMapProvider getWorldMapProvider() {
        return this.getWorldConfig().getWorldMapProvider();
    }

    public void setWorldMapProvider(IWorldMapProvider worldMapProvider) {
        this.getWorldConfig().setWorldMapProvider(worldMapProvider);
    }

    public IChunkStorageProvider getChunkStorageProvider() {
        return this.getWorldConfig().getChunkStorageProvider();
    }

    public void setChunkStorageProvider(IChunkStorageProvider chunkStorageProvider) {
        this.getWorldConfig().setChunkStorageProvider(chunkStorageProvider);
    }

    @Nonnull
    public WorldConfig.ChunkConfig getChunkConfig() {
        return this.getWorldConfig().getChunkConfig();
    }

    public void setChunkConfig(@Nonnull WorldConfig.ChunkConfig chunkConfig) {
        this.getWorldConfig().setChunkConfig(chunkConfig);
    }

    public boolean isBlockTicking() {
        return this.getWorldConfig().isBlockTicking();
    }

    public void setBlockTicking(boolean ticking) {
        this.getWorldConfig().setBlockTicking(ticking);
    }

    public boolean isPvpEnabled() {
        return this.getWorldConfig().isPvpEnabled();
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.getWorldConfig().setPvpEnabled(pvpEnabled);
    }

    public boolean isFallDamageEnabled() {
        return this.getWorldConfig().isFallDamageEnabled();
    }

    public boolean isGameTimePaused() {
        return this.getWorldConfig().isGameTimePaused();
    }

    public void setGameTimePaused(boolean gameTimePaused) {
        this.getWorldConfig().setGameTimePaused(gameTimePaused);
    }

    public Instant getGameTime() {
        return this.getWorldConfig().getGameTime();
    }

    public void setGameTime(Instant gameTime) {
        this.getWorldConfig().setGameTime(gameTime);
    }

    public String getForcedWeather() {
        return this.getWorldConfig().getForcedWeather();
    }

    public void setForcedWeather(String forcedWeather) {
        this.getWorldConfig().setForcedWeather(forcedWeather);
    }

    public ClientEffectWorldSettings getClientEffects() {
        return this.getWorldConfig().getClientEffects();
    }

    public void setClientEffects(ClientEffectWorldSettings clientEffects) {
        this.getWorldConfig().setClientEffects(clientEffects);
    }

    @Nonnull
    public Map<PluginIdentifier, SemverRange> getRequiredPlugins() {
        return this.getWorldConfig().getRequiredPlugins();
    }

    public void setRequiredPlugins(Map<PluginIdentifier, SemverRange> requiredPlugins) {
        this.getWorldConfig().setRequiredPlugins(requiredPlugins);
    }

    public GameMode getGameMode() {
        return this.getWorldConfig().getGameMode();
    }

    public void setGameMode(GameMode gameMode) {
        this.getWorldConfig().setGameMode(gameMode);
    }

    public boolean isSpawningNPC() {
        return this.getWorldConfig().isSpawningNPC();
    }

    public void setSpawningNPC(boolean spawningNPC) {
        this.getWorldConfig().setSpawningNPC(spawningNPC);
    }

    public boolean isSpawnMarkersEnabled() {
        return this.getWorldConfig().isSpawnMarkersEnabled();
    }

    public void setIsSpawnMarkersEnabled(boolean spawnMarkersEnabled) {
        this.getWorldConfig().setIsSpawnMarkersEnabled(spawnMarkersEnabled);
    }

    public boolean isAllNPCFrozen() {
        return this.getWorldConfig().isAllNPCFrozen();
    }

    public void setIsAllNPCFrozen(boolean allNPCFrozen) {
        this.getWorldConfig().setIsAllNPCFrozen(allNPCFrozen);
    }

    public void setGameplayConfig(String gameplayConfig) {
        this.getWorldConfig().setGameplayConfig(gameplayConfig);
    }

    @Nullable
    public DeathConfig getDeathConfigOverride() {
        return this.getWorldConfig().getDeathConfigOverride();
    }

    @Nullable
    public Integer getDaytimeDurationSecondsOverride() {
        return this.getWorldConfig().getDaytimeDurationSecondsOverride();
    }

    @Nullable
    public Integer getNighttimeDurationSecondsOverride() {
        return this.getWorldConfig().getNighttimeDurationSecondsOverride();
    }

    public boolean isSavingPlayers() {
        return this.getWorldConfig().isSavingPlayers();
    }

    public void setSavingPlayers(boolean savingPlayers) {
        this.getWorldConfig().setSavingPlayers(savingPlayers);
    }

    public boolean canUnloadChunks() {
        return this.getWorldConfig().canUnloadChunks();
    }

    public void setCanUnloadChunks(boolean unloadingChunks) {
        this.getWorldConfig().setCanUnloadChunks(unloadingChunks);
    }

    public boolean canSaveChunks() {
        return this.getWorldConfig().canSaveChunks();
    }

    public void setCanSaveChunks(boolean savingChunks) {
        this.getWorldConfig().setCanSaveChunks(savingChunks);
    }

    public boolean shouldSaveNewChunks() {
        return this.getWorldConfig().shouldSaveNewChunks();
    }

    public void setSaveNewChunks(boolean saveNewChunks) {
        this.getWorldConfig().setSaveNewChunks(saveNewChunks);
    }

    public boolean isObjectiveMarkersEnabled() {
        return this.getWorldConfig().isObjectiveMarkersEnabled();
    }

    public void setObjectiveMarkersEnabled(boolean objectiveMarkersEnabled) {
        this.getWorldConfig().setObjectiveMarkersEnabled(objectiveMarkersEnabled);
    }

    public IResourceStorageProvider getResourceStorageProvider() {
        return this.getWorldConfig().getResourceStorageProvider();
    }

    public void setResourceStorageProvider(@Nonnull IResourceStorageProvider resourceStorageProvider) {
        this.getWorldConfig().setResourceStorageProvider(resourceStorageProvider);
    }
}