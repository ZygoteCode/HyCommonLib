package me.zygotecode.hycommonlib.player;

import com.hypixel.hytale.builtin.mounts.MountPlugin;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.inventory.UpdatePlayerInventory;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.auth.PlayerAuthentication;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.StatModifiersManager;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.effect.ActiveEntityEffect;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.HiddenPlayersManager;
import com.hypixel.hytale.server.core.entity.entities.player.HotbarManager;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerConfigData;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerRespawnPointData;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerWorldData;
import com.hypixel.hytale.server.core.entity.entities.player.hud.HudManager;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.windows.WindowManager;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SortType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.Invulnerable;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.player.ChunkTracker;
import com.hypixel.hytale.server.core.modules.entity.player.KnockbackSimulation;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.zygotecode.hycommonlib.HsServer;
import me.zygotecode.hycommonlib.world.HsWorld;
import me.zygotecode.hycommonlib.location.HsLocation;
import me.zygotecode.hycommonlib.location.HsPosition;
import me.zygotecode.hycommonlib.location.HsRotation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.hypixel.hytale.server.core.util.TempAssetIdUtil;

public class HsPlayer {
    private final UUID uuid;

    public HsPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public HsPlayer(String username) {
        this.uuid = Universe.get().getPlayerByUsername(username, NameMatching.EXACT_IGNORE_CASE).getUuid();
    }

    public HsPlayer(PlayerRef playerRef) {
        this.uuid = playerRef.getUuid();
    }

    @SuppressWarnings("removal")
    public HsPlayer(Player player) {
        this.uuid = player.getReference().getStore().getComponent(player.getReference(), UUIDComponent.getComponentType()).getUuid();
    }

    public HsPlayer(Store<EntityStore> store) {
        this.uuid = HsServer.getPlayer(store).getUuid();
    }

    public PlayerAuthentication getPlayerAuthentication() {
        return this.getPacketHandler().getAuth();
    }

    public PacketHandler getPacketHandler() {
        return this.getPlayerRef().getPacketHandler();
    }

    public String getUsername() {
        return this.getPlayerRef().getUsername();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public HostAddress getReferralSource() {
        return this.getPlayerAuthentication().getReferralSource();
    }

    public PlayerRef getPlayerRef() {
        return Universe.get().getPlayer(uuid);
    }

    public Holder<EntityStore> getHolder() {
        return this.getPlayerRef().getHolder();
    }

    public HsWorld getWorld() {
        return new HsWorld(this.getHytalePlayer().getWorld());
    }

    public Player getHytalePlayer() {
        return this.getStore().getComponent(this.getRef(), Player.getComponentType());
    }

    public Ref<EntityStore> getRef() {
        return this.getPlayerRef().getReference();
    }
    
    public Ref<EntityStore> getReference() {
        return this.getRef();
    }

    public Store<EntityStore> getStore() {
        return this.getRef().getStore();
    }

    public String getDisplayName() {
        return this.getHytalePlayer().getDisplayName();
    }

    public Inventory getInventory() {
        return this.getHytalePlayer().getInventory();
    }

    public int getClientViewRadius() {
        return this.getHytalePlayer().getClientViewRadius();
    }

    public GameMode getGameMode() {
        return this.getHytalePlayer().getGameMode();
    }

    public HotbarManager getHotbarManager() {
        return this.getHytalePlayer().getHotbarManager();
    }

    public double getFallDistance() {
        return this.getHytalePlayer().getCurrentFallDistance();
    }

    public HudManager getHudManager() {
        return this.getHytalePlayer().getHudManager();
    }

    public int getMountEntityId() {
        return this.getHytalePlayer().getMountEntityId();
    }

    public PageManager getPageManager() {
        return this.getHytalePlayer().getPageManager();
    }

    public StatModifiersManager getStatModifiersManager() {
        return this.getHytalePlayer().getStatModifiersManager();
    }

    public WorldMapTracker getWorldMapTracker() {
        return this.getHytalePlayer().getWorldMapTracker();
    }

    public long getSinceLastSpawnNanoSeconds() {
        return this.getHytalePlayer().getSinceLastSpawnNanos();
    }

    public WindowManager getWindowManager() {
        return this.getHytalePlayer().getWindowManager();
    }

    public PlayerConfigData getPlayerConfigData() {
        return this.getHytalePlayer().getPlayerConfigData();
    }

    public int getViewRadius() {
        return this.getHytalePlayer().getViewRadius();
    }

    @SuppressWarnings("removal")
    public int getNetworkId() {
        return this.getHytalePlayer().getNetworkId();
    }

    public HsLocation getLocation() {
        TransformComponent tc = (TransformComponent)this.getStore().getComponent(this.getRef(), TransformComponent.getComponentType());
        HeadRotation hr = (HeadRotation)this.getStore().getComponent(this.getRef(), HeadRotation.getComponentType());

        Vector3d pos = tc.getPosition().clone();
        Vector3f rot = hr.getRotation().clone();

        double currentX = pos.getX();
        double currentY = pos.getY();
        double currentZ = pos.getZ();

        float currentPitch = rot.getX();
        float currentYaw = rot.getY();
        float currentRoll = rot.getZ();

        return new HsLocation(currentX, currentY, currentZ, currentPitch, currentYaw, currentRoll);
    }

    public HsPosition getPosition() {
        TransformComponent tc = (TransformComponent)this.getStore().getComponent(this.getRef(), TransformComponent.getComponentType());
        Vector3d pos = tc.getPosition().clone();

        double currentX = pos.getX();
        double currentY = pos.getY();
        double currentZ = pos.getZ();

        return new HsPosition(currentX, currentY, currentZ);
    }

    public HsRotation getRotation() {
        HeadRotation hr = (HeadRotation)this.getStore().getComponent(this.getRef(), HeadRotation.getComponentType());
        Vector3f rot = hr.getRotation().clone();

        float currentPitch = rot.getX();
        float currentYaw = rot.getY();
        float currentRoll = rot.getZ();

        return new HsRotation(currentPitch, currentYaw, currentRoll);
    }

    public void setLocation(HsLocation location) {
        Transform t = new Transform(location.getPosX(), location.getPosY(), location.getPosZ(), location.getPitch(), location.getYaw(), location.getRoll());
        this.getHytalePlayer().getWorld().getEntityStore().getStore().addComponent(this.getRef(), Teleport.getComponentType(), Teleport.createForPlayer(this.getWorld().getHytaleWorld(), t));
    }

    public void setPosition(HsPosition position) {
        HsRotation rotation = this.getRotation();
        Transform t = new Transform(position.getPosX(), position.getPosY(), position.getPosZ(), rotation.getPitch(), rotation.getYaw(), rotation.getRoll());
        this.getHytalePlayer().getWorld().getEntityStore().getStore().addComponent(this.getRef(), Teleport.getComponentType(), Teleport.createForPlayer(this.getWorld().getHytaleWorld(), t));
    }

    public void setRotation(HsRotation rotation) {
        HsLocation location = this.getLocation();
        Transform t = new Transform(location.getPosX(), location.getPosY(), location.getPosZ(), rotation.getPitch(), rotation.getYaw(), rotation.getRoll());
        this.getHytalePlayer().getWorld().getEntityStore().getStore().addComponent(this.getRef(), Teleport.getComponentType(), Teleport.createForPlayer(this.getWorld().getHytaleWorld(), t));
    }

    public void setPosition(double posX, double posY, double posZ) {
        this.setPosition(new HsPosition(posX, posY, posZ));
    }

    public void setRotation(float pitch, float yaw, float roll) {
        this.setRotation(new HsRotation(pitch, yaw, roll));
    }

    public void setLocation(double posX, double posY, double posZ, float pitch, float yaw, float roll) {
        this.setLocation(new HsLocation(posX, posY, posZ, pitch, yaw, roll));
    }

    public double getPosX() {
        return this.getPosition().getPosX();
    }

    public double getPosY() {
        return this.getPosition().getPosY();
    }

    public double getPosZ() {
        return this.getPosition().getPosZ();
    }

    public double getPitch() {
        return this.getRotation().getPitch();
    }

    public double getYaw() {
        return this.getRotation().getYaw();
    }

    public double getRoll() {
        return this.getRotation().getRoll();
    }

    public void setPosX(double posX) {
        HsLocation location = this.getLocation();
        this.setPosition(posX, location.getPosY(), location.getPosZ());
    }

    public void setPosY(double posY) {
        HsLocation location = this.getLocation();
        this.setPosition(location.getPosX(), posY, location.getPosZ());
    }

    public void setPosZ(double posZ) {
        HsLocation location = this.getLocation();
        this.setPosition(location.getPosX(), location.getPosY(), posZ);
    }

    public void setPitch(float pitch) {
        HsRotation rotation = this.getRotation();
        this.setRotation(pitch, rotation.getYaw(), rotation.getRoll());
    }

    public void setYaw(float yaw) {
        HsRotation rotation = this.getRotation();
        this.setRotation(rotation.getPitch(), yaw, rotation.getRoll());
    }

    public void setRoll(float roll) {
        HsRotation rotation = this.getRotation();
        this.setRotation(rotation.getPitch(), rotation.getYaw(), roll);
    }

    public void showEventTitle(String title, String text, boolean isMajor) {
        EventTitleUtil.showEventTitleToPlayer(
                getPlayerRef(),
                Message.raw(text),
                Message.raw(title),
                isMajor
        );
    }

    public EntityStatMap getStats() {
        ComponentType<EntityStore, EntityStatMap> statMapType = EntityStatsModule.get().getEntityStatMapComponentType();
        EntityStatMap stats = getStore().getComponent(getRef(), statMapType);
        return stats;
    }

    public void setStat(int index, float value) {
        this.getStats().setStatValue(index, value);
    }

    public void addStat(int index, float value) {
        this.getStats().addStatValue(index, value);
    }

    public void subtractStat(int index, float value) {
        this.getStats().subtractStatValue(index, value);
    }

    public void setStatToMin(int index) {
        this.getStats().minimizeStatValue(index);
    }

    public void setStatToMax(int index) {
        this.getStats().maximizeStatValue(index);
    }

    public float getStatValue(int index) {
        return this.getStats().get(index).get();
    }

    public float getMaxStatValue(int index) {
        return this.getStats().get(index).getMax();
    }

    public float getMinStatValue(int index) {
        return this.getStats().get(index).getMin();
    }

    public float getStatPercentage(int index) {
        return this.getStats().get(index).asPercentage();
    }

    public EntityStatValue getStat(int index) {
        return this.getStats().get(index);
    }

    public EntityStatValue getHealthStat() {
        return this.getStat(DefaultEntityStatTypes.getHealth());
    }

    public EntityStatValue getOxygenStat() {
        return this.getStat(DefaultEntityStatTypes.getOxygen());
    }

    public EntityStatValue getStaminaStat() {
        return this.getStat(DefaultEntityStatTypes.getStamina());
    }

    public EntityStatValue getManaStat() {
        return this.getStat(DefaultEntityStatTypes.getMana());
    }

    public EntityStatValue getSignatureEnergyStat() {
        return this.getStat(DefaultEntityStatTypes.getSignatureEnergy());
    }

    public EntityStatValue getAmmoStat() {
        return this.getStat(DefaultEntityStatTypes.getAmmo());
    }

    public void setHealth(float value) {
        this.setStat(DefaultEntityStatTypes.getHealth(), value);
    }

    public float getHealth() {
        return this.getHealthStat().get();
    }

    public float getMinHealth() {
        return this.getHealthStat().getMin();
    }

    public float getMaxHealth() {
        return this.getHealthStat().getMax();
    }

    public void setHealthToMax() {
        this.setStat(DefaultEntityStatTypes.getHealth(), this.getMaxHealth());
    }

    public void setHealthToMin() {
        this.setStat(DefaultEntityStatTypes.getHealth(), this.getMinHealth());
    }

    public void kill() {
        this.setHealth(0.0F);
    }

    public float getHealthPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getHealth());
    }

    public void setOxygen(float value) {
        this.setStat(DefaultEntityStatTypes.getOxygen(), value);
    }

    public float getOxygen() {
        return this.getOxygenStat().get();
    }

    public float getMinOxygen() {
        return this.getOxygenStat().getMin();
    }

    public float getMaxOxygen() {
        return this.getOxygenStat().getMax();
    }

    public void setOxygenToMax() {
        this.setStat(DefaultEntityStatTypes.getOxygen(), this.getMaxOxygen());
    }

    public void setOxygenToMin() {
        this.setStat(DefaultEntityStatTypes.getOxygen(), this.getMinOxygen());
    }

    public float getOxygenPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getOxygen());
    }

    public void setStamina(float value) {
        this.setStat(DefaultEntityStatTypes.getStamina(), value);
    }

    public float getStamina() {
        return this.getStaminaStat().get();
    }

    public float getMinStamina() {
        return this.getStaminaStat().getMin();
    }

    public float getMaxStamina() {
        return this.getStaminaStat().getMax();
    }

    public void setStaminaToMax() {
        this.setStat(DefaultEntityStatTypes.getStamina(), this.getMaxStamina());
    }

    public void setStaminaToMin() {
        this.setStat(DefaultEntityStatTypes.getStamina(), this.getMinStamina());
    }

    public float getStaminaPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getStamina());
    }

    public void setMana(float value) {
        this.setStat(DefaultEntityStatTypes.getMana(), value);
    }

    public float getMana() {
        return this.getManaStat().get();
    }

    public float getMinMana() {
        return this.getManaStat().getMin();
    }

    public float getMaxMana() {
        return this.getManaStat().getMax();
    }

    public void setManaToMax() {
        this.setStat(DefaultEntityStatTypes.getMana(), this.getMaxMana());
    }

    public void setManaToMin() {
        this.setStat(DefaultEntityStatTypes.getMana(), this.getMinMana());
    }

    public float getManaPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getMana());
    }

    public void setSignatureEnergy(float value) {
        this.setStat(DefaultEntityStatTypes.getSignatureEnergy(), value);
    }

    public float getSignatureEnergy() {
        return this.getSignatureEnergyStat().get();
    }

    public float getMinSignatureEnergy() {
        return this.getSignatureEnergyStat().getMin();
    }

    public float getMaxSignatureEnergy() {
        return this.getSignatureEnergyStat().getMax();
    }

    public void setSignatureEnergyToMax() {
        this.setStat(DefaultEntityStatTypes.getSignatureEnergy(), this.getMaxSignatureEnergy());
    }

    public void setSignatureEnergyToMin() {
        this.setStat(DefaultEntityStatTypes.getSignatureEnergy(), this.getMinSignatureEnergy());
    }

    public float getSignatureEnergyPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getSignatureEnergy());
    }

    public void setAmmo(float value) {
        this.setStat(DefaultEntityStatTypes.getAmmo(), value);
    }

    public float getAmmo() {
        return this.getAmmoStat().get();
    }

    public float getMinAmmo() {
        return this.getAmmoStat().getMin();
    }

    public float getMaxAmmo() {
        return this.getAmmoStat().getMax();
    }

    public void setAmmoToMax() {
        this.setStat(DefaultEntityStatTypes.getAmmo(), this.getMaxAmmo());
    }

    public void setAmmoToMin() {
        this.setStat(DefaultEntityStatTypes.getAmmo(), this.getMinAmmo());
    }

    public float getAmmoPercentage() {
        return this.getStatPercentage(DefaultEntityStatTypes.getAmmo());
    }

    public MovementStates getMovementStates() {
        MovementStatesComponent component = getStore().getComponent(getRef(), MovementStatesComponent.getComponentType());
        return component.getMovementStates();
    }

    public boolean isIdle() {
        return this.getMovementStates().idle;
    }

    public void setIdle(boolean value) {
        this.getMovementStates().idle = value;
    }

    public boolean isHorizontalIdle() {
        return this.getMovementStates().horizontalIdle;
    }

    public void setHorizontalIdle(boolean value) {
        this.getMovementStates().horizontalIdle = value;
    }

    public boolean isJumping() {
        return this.getMovementStates().jumping;
    }

    public void setJumping(boolean value) {
        this.getMovementStates().jumping = value;
    }

    public boolean isFlying() {
        return this.getMovementStates().flying;
    }

    public void setFlying(boolean value) {
        this.getMovementStates().flying = value;
    }

    public boolean isWalking() {
        return this.getMovementStates().walking;
    }

    public void setWalking(boolean value) {
        this.getMovementStates().walking = value;
    }

    public boolean isRunning() {
        return this.getMovementStates().running;
    }

    public void setRunning(boolean value) {
        this.getMovementStates().running = value;
    }

    public boolean isSprinting() {
        return this.getMovementStates().sprinting;
    }

    public void setSprinting(boolean value) {
        this.getMovementStates().sprinting = value;
    }

    public boolean isCrouching() {
        return this.getMovementStates().crouching;
    }

    public void setCrouching(boolean value) {
        this.getMovementStates().crouching = value;
    }

    public boolean isForcedCrouching() {
        return this.getMovementStates().forcedCrouching;
    }

    public void setForcedCrouching(boolean value) {
        this.getMovementStates().forcedCrouching = value;
    }

    public boolean isFalling() {
        return this.getMovementStates().falling;
    }

    public void setFalling(boolean value) {
        this.getMovementStates().falling = value;
    }

    public boolean isClimbing() {
        return this.getMovementStates().climbing;
    }

    public void setClimbing(boolean value) {
        this.getMovementStates().climbing = value;
    }

    public boolean isInFluid() {
        return this.getMovementStates().inFluid;
    }

    public void setInFluid(boolean value) {
        this.getMovementStates().inFluid = value;
    }

    public boolean isSwimming() {
        return this.getMovementStates().swimming;
    }

    public void setSwimming(boolean value) {
        this.getMovementStates().swimming = value;
    }

    public boolean isSwimJumping() {
        return this.getMovementStates().swimJumping;
    }

    public void setSwimJumping(boolean value) {
        this.getMovementStates().swimJumping = value;
    }

    public boolean isOnGround() {
        return this.getMovementStates().onGround;
    }

    public void setOnGround(boolean value) {
        this.getMovementStates().onGround = value;
    }

    public boolean isMantling() {
        return this.getMovementStates().mantling;
    }

    public void setMantling(boolean value) {
        this.getMovementStates().mantling = value;
    }

    public boolean isSliding() {
        return this.getMovementStates().sliding;
    }

    public void setSliding(boolean value) {
        this.getMovementStates().sliding = value;
    }

    public boolean isMounting() {
        return this.getMovementStates().mounting;
    }

    public void setMounting(boolean value) {
        this.getMovementStates().mounting = value;
    }

    public boolean isRolling() {
        return this.getMovementStates().rolling;
    }

    public void setRolling(boolean value) {
        this.getMovementStates().rolling = value;
    }

    public boolean isSitting() {
        return this.getMovementStates().sitting;
    }

    public void setSitting(boolean value) {
        this.getMovementStates().sitting = value;
    }

    public boolean isGliding() {
        return this.getMovementStates().gliding;
    }

    public void setGliding(boolean value) {
        this.getMovementStates().gliding = value;
    }

    public boolean isSleeping() {
        return this.getMovementStates().sleeping;
    }

    public void setSleeping(boolean value) {
        this.getMovementStates().sleeping = value;
    }

    public void setClientViewRadius(int clientViewRadius) {
        this.getHytalePlayer().setClientViewRadius(clientViewRadius);
    }

    public void setInventory(Inventory inventory) {
        this.getHytalePlayer().setInventory(inventory);
    }

    public void setFirstSpawn(boolean firstSpawn) {
        this.getHytalePlayer().setFirstSpawn(firstSpawn);
    }

    public void setCurrentFallDistance(double currentFallDistance) {
        this.getHytalePlayer().setCurrentFallDistance(currentFallDistance);
    }

    public void setNetworkId(int id) {
        this.getHytalePlayer().setNetworkId(id);
    }

    public void setMountEntityId(int mountEntityId) {
        this.getHytalePlayer().setMountEntityId(mountEntityId);
    }

    public void setLastSpawnTimeNanos(long time) {
        this.getHytalePlayer().setLastSpawnTimeNanos(time);
    }

    public boolean hasSpawnProtection() {
        return this.getHytalePlayer().hasSpawnProtection();
    }

    public boolean hasPermission(String id) {
        return this.getHytalePlayer().hasPermission(id);
    }

    public boolean hasPermission(String id, boolean def) {
        return this.getHytalePlayer().hasPermission(id, def);
    }

    public boolean isWaitingForClientReady() {
        return this.getHytalePlayer().isWaitingForClientReady();
    }

    public void moveToWorld(World world) {
        this.getHytalePlayer().loadIntoWorld(world);
    }

    public void moveToWorld(HsWorld world) {
        this.getHytalePlayer().loadIntoWorld(world.getHytaleWorld());
    }

    public boolean wasRemoved() {
        return this.getHytalePlayer().wasRemoved();
    }

    public void removeFromWorld() {
        this.getHytalePlayer().unloadFromWorld();
    }

    public void invalidateEquipmentNetwork() {
        this.getHytalePlayer().invalidateEquipmentNetwork();
    }

    public boolean isCollidable() {
        return this.getHytalePlayer().isCollidable();
    }

    public void setInventory(Inventory inventory, boolean ensureCapacity) {
        this.getHytalePlayer().setInventory(inventory, ensureCapacity);
    }

    public void setInventory(Inventory inventory, boolean ensureCapacity, List<ItemStack> remainder) {
        this.getHytalePlayer().setInventory(inventory, ensureCapacity, remainder);
    }

    public void sendMessage(String message) {
        this.getHytalePlayer().sendMessage(Message.raw(message));
    }

    public void sendMessage(Message message) {
        this.getHytalePlayer().sendMessage(message);
    }

    public void startClientReadyTimeout() {
        this.getHytalePlayer().startClientReadyTimeout();
    }

    public void resetVelocity(Velocity velocity) {
        this.getHytalePlayer().resetVelocity(velocity);
    }

    public boolean hasConsumeChanged() {
        return this.getPlayerConfigData().consumeHasChanged();
    }

    public void setBlockIdVersion(int blockIdVersion) {
        this.getPlayerConfigData().setBlockIdVersion(blockIdVersion);
    }

    public int getBlockIdVersion() {
        return this.getPlayerConfigData().getBlockIdVersion();
    }

    public void setWorld(String world) {
        this.getPlayerConfigData().setWorld(world);
    }

    public void setWorld(World world) {
        this.getPlayerConfigData().setWorld(world.getName());
    }

    public void setWorld(HsWorld world) {
        this.getPlayerConfigData().setWorld(world.getHytaleWorld().getName());
    }

    public Set<UUID> getDiscoveredInstances() {
        return this.getPlayerConfigData().getDiscoveredInstances();
    }

    public void setDiscoveredInstances(Set<UUID> discoveredInstances) {
        this.getPlayerConfigData().setDiscoveredInstances(discoveredInstances);
    }

    public void setKnownRecipes(Set<String> knownRecipes) {
        this.getPlayerConfigData().setKnownRecipes(knownRecipes);
    }

    public Set<String> getKnownRecipes() {
        return this.getPlayerConfigData().getKnownRecipes();
    }

    public void setDiscoveredZones(Set<String> discoveredZones) {
        this.getPlayerConfigData().setDiscoveredZones(discoveredZones);
    }

    public Set<String> getDiscoveredZones() {
        return this.getPlayerConfigData().getDiscoveredZones();
    }

    public ChunkTracker getChunkTracker() {
        return this.getPlayerRef().getChunkTracker();
    }

    public Vector3f getHeadRotation() {
        return this.getPlayerRef().getHeadRotation();
    }

    public String getLanguage() {
        return this.getPlayerRef().getLanguage();
    }

    public Transform getTransform() {
        return this.getPlayerRef().getTransform();
    }

    public UUID getWorldUuid() {
        return this.getPlayerRef().getWorldUuid();
    }

    public HiddenPlayersManager getHiddenPlayersManager() {
        return this.getPlayerRef().getHiddenPlayersManager();
    }

    public boolean isPlayerHidden() {
        return this.getHiddenPlayersManager().isPlayerHidden(this.getUuid());
    }

    public void hidePlayer() {
        this.getHiddenPlayersManager().hidePlayer(this.getUuid());
    }

    public void unhidePlayer() {
        this.getHiddenPlayersManager().showPlayer(this.getUuid());
    }

    public void showPlayer() {
        this.unhidePlayer();
    }

    public void setLanguage(String language) {
        this.getPlayerRef().setLanguage(language);
    }

    public boolean isValid() {
        return this.getPlayerRef().isValid();
    }

    public void referToServer(String host, int port) {
        this.getPlayerRef().referToServer(host, port);
    }

    public void referToServer(String host, int port, byte[] data) {
        this.getPlayerRef().referToServer(host, port, data);
    }

    public void moveToServer(String host, int port) {
        this.referToServer(host, port);
    }

    public void moveToServer(String host, int port, byte[] data) {
        this.referToServer(host, port, data);
    }

    public void updateLocation(World world, Transform transform, Vector3f headRotation) {
        this.getPlayerRef().updatePosition(world, transform, headRotation);
    }

    public void updateLocation(Transform transform, Vector3f headRotation) {
        this.getPlayerRef().updatePosition(this.getWorld().getHytaleWorld(), transform, headRotation);
    }

    public void updateLocation(World world, HsPosition position, HsRotation rotation) {
        this.updateLocation(world, position.toTransform(), rotation.toVector());
    }

    public void updateLocation(HsPosition position, HsRotation rotation) {
        this.updateLocation(position.toTransform(), rotation.toVector());
    }

    public void updateLocation(World world, HsLocation location) {
        this.updateLocation(world, location.getPosition().toTransform(), location.getRotation().toVector());
    }

    public void updateLocation(HsLocation location) {
        this.updateLocation(location.getPosition().toTransform(), location.getRotation().toVector());
    }

    public void updatePosition(World world, Transform transform) {
        this.updateLocation(world, transform, this.getHeadRotation());
    }

    public void updatePosition(Transform transform) {
        this.updateLocation(this.getWorld().getHytaleWorld(), transform, this.getHeadRotation());
    }

    public void updatePosition(World world, HsPosition position) {
        this.updatePosition(world, position.toTransform());
    }

    public void updatePosition(HsPosition position) {
        this.updatePosition(position.toTransform());
    }

    public void updateRotation(Vector3f rotation) {
        this.updateLocation(this.getPosition().toTransform(), rotation);
    }

    public Archetype<EntityStore> getArchetype() {
        return this.getStore().getArchetype(this.getRef());
    }

    public <T extends Component<EntityStore>> void putComponent(@Nonnull ComponentType<EntityStore, T> componentType, @Nonnull T component) {
        this.getStore().putComponent(this.getRef(), componentType, component);
    }

    public <T extends Component<EntityStore>> void removeComponent(@Nonnull ComponentType<EntityStore, T> componentType) {
        this.getStore().removeComponent(this.getRef(), componentType);
    }

    public <T extends Component<EntityStore>> void tryRemoveComponent(@Nonnull ComponentType<EntityStore, T> componentType) {
        this.getStore().tryRemoveComponent(this.getRef(), componentType);
    }

    public boolean isComponentPresent(@Nonnull ComponentType<EntityStore, ?> componentType) {
       return this.getArchetype().contains(componentType);
    }

    public void updateRotation(HsRotation rotation) {
        this.updateRotation(rotation.toVector());
    }

    public void setInvulnerable(boolean invulnerable) {
        if (invulnerable) {
            this.putComponent(Invulnerable.getComponentType(), Invulnerable.INSTANCE);
        } else {
            this.tryRemoveComponent(Invulnerable.getComponentType());
        }
    }

    public boolean isInvulnerable() {
        return this.isComponentPresent(Invulnerable.getComponentType());
    }

    public ItemStack getItemInHand() {
        return this.getInventory().getItemInHand();
    }

    public ItemStack getActiveHotbarItem() {
        return this.getInventory().getActiveHotbarItem();
    }

    public byte getActiveSlot(int inventorySectionId) {
        return this.getInventory().getActiveSlot(inventorySectionId);
    }

    public ItemContainer getArmor() {
        return this.getInventory().getArmor();
    }

    public ItemContainer getBackpack() {
        return this.getInventory().getBackpack();
    }

    public ItemContainer getTools() {
        return this.getInventory().getTools();
    }

    public byte getActiveHotbarSlot() {
        return this.getInventory().getActiveHotbarSlot();
    }

    public ItemContainer getHotbar() {
        return this.getInventory().getHotbar();
    }

    public ItemStack getUtilityItem() {
        return this.getInventory().getUtilityItem();
    }

    public ItemContainer getInventoryStorage() {
        return this.getInventory().getStorage();
    }

    public ItemStack getToolsItem() {
        return this.getInventory().getToolsItem();
    }

    public ItemContainer getSectionById(int id) {
        return this.getInventory().getSectionById(id);
    }

    public byte getActiveToolsSlot() {
        return this.getInventory().getActiveToolsSlot();
    }

    public byte getActiveUtilitySlot() {
        return this.getInventory().getActiveUtilitySlot();
    }

    public ItemContainer getUtility() {
        return this.getInventory().getUtility();
    }

    public CombinedItemContainer getCombinedArmorHotbarStorage() {
        return this.getInventory().getCombinedArmorHotbarStorage();
    }

    public CombinedItemContainer getCombinedArmorHotbarUtilityStorage() {
        return this.getInventory().getCombinedArmorHotbarUtilityStorage();
    }

    public CombinedItemContainer getCombinedStorageFirst() {
        return this.getInventory().getCombinedStorageFirst();
    }

    public CombinedItemContainer getCombinedHotbarFirst() {
        return this.getInventory().getCombinedHotbarFirst();
    }

    public CombinedItemContainer getCombinedBackpackStorageHotbar() {
        return this.getInventory().getCombinedBackpackStorageHotbar();
    }

    public CombinedItemContainer getCombinedEverything() {
        return this.getInventory().getCombinedEverything();
    }

    public void setActiveHotbarSlot(byte slot) {
        this.getInventory().setActiveHotbarSlot(slot);
    }

    public void setActiveSlot(int inventorySectionId, byte slot) {
        this.getInventory().setActiveSlot(inventorySectionId, slot);
    }

    public void setEntity(LivingEntity entity) {
        this.getInventory().setEntity(entity);
    }

    public void setSortType(SortType sortType) {
        this.getInventory().setSortType(sortType);
    }

    public void setActiveToolsSlot(byte slot) {
        this.getInventory().setActiveToolsSlot(slot);
    }

    public void setActiveUtilitySlot(byte slot) {
        this.getInventory().setActiveUtilitySlot(slot);
    }

    public void setUsingToolsItem(boolean value) {
        this.getInventory().setUsingToolsItem(value);
    }

    public void clearInventory() {
        this.getInventory().clear();
    }

    public boolean isUsingToolsItem() {
        return this.getInventory().usingToolsItem();
    }

    public void sortStorage(SortType sortType) {
        this.getInventory().sortStorage(sortType);
    }

    public boolean isInventoryConsumeIsDirty() {
        return this.getInventory().consumeIsDirty();
    }

    public boolean isInventoryConsumeNeedsSaving() {
        return this.getInventory().consumeNeedsSaving();
    }

    public void resizeBackpack(short capacity, List<ItemStack> remainder) {
        this.getInventory().resizeBackpack(capacity, remainder);
    }

    public List<ItemStack> dropAllItemStacks() {
        return this.getInventory().dropAllItemStacks();
    }

    public void smartMoveItem(int fromSectionId, int fromSlotId, int quantity, SmartMoveType type) {
        this.getInventory().smartMoveItem(fromSectionId, fromSlotId, quantity, type);
    }

    public void moveItem(int fromSectionId, int fromSlotId, int quantity, int toSectionId, int toSlotId) {
        this.getInventory().moveItem(fromSectionId, fromSlotId, quantity, toSectionId, toSlotId);
    }

    public boolean containsBrokenItem() {
        return this.getInventory().containsBrokenItem();
    }

    public UpdatePlayerInventory getInventoryPacket() {
        return this.getInventory().toPacket();
    }

    public void markInventoryChanged() {
        this.getInventory().markChanged();
    }

    public void unregisterInventory() {
        this.getInventory().unregister();
    }

    public ItemStack getItemInSlot(short slot) {
        return this.getInventoryStorage().getItemStack(slot);
    }

    public void addItemInInventory(ItemStack item) {
        this.getInventoryStorage().addItemStack(item);
    }

    public void removeItemFromInventory(ItemStack item) {
        this.getInventoryStorage().removeItemStack(item);
    }

    public void removeItemFromInventory(short slot) {
        this.getInventoryStorage().removeItemStackFromSlot(slot);
    }

    public short getInventoryCapacity() {
        return this.getInventoryStorage().getCapacity();
    }

    public boolean isInventoryEmpty() {
        return this.getInventoryStorage().isEmpty();
    }

    public void setGameMode(GameMode gameMode) {
        Player.setGameMode(this.getRef(), gameMode, this.getStore());
    }

    public void setGameModeAdventure() {
        this.setGameMode(GameMode.Adventure);
    }

    public void setGameModeCreative() {
        this.setGameMode(GameMode.Creative);
    }

    public void disconnectPlayer(String message) {
        this.getPacketHandler().disconnect(message);
    }

    public void sendInventory() {
        this.getHytalePlayer().sendInventory();
    }

    public void updateInventory() {
        this.sendInventory();
    }

    @SuppressWarnings("removal")
    public void playSound3D(String soundEventId, Vector3d position) {
        SoundUtil.playSoundEvent3dToPlayer(
                this.getRef(),
                TempAssetIdUtil.getSoundEventIndex(soundEventId),
                SoundCategory.UI,
                position,
                this.getStore()
        );
    }

    public void playSound3D(String soundEventId, HsPosition position) {
        this.playSound3D(soundEventId, new Vector3d(position.getPosX(), position.getPosY(), position.getPosZ()));
    }

    public void playSound3D(String soundEventId) {
        this.playSound3D(soundEventId, this.getPosition());
    }

    @SuppressWarnings("removal")
    public void playSound2D(String soundEventId) {
        SoundUtil.playSoundEvent2d(
                this.getRef(),
                TempAssetIdUtil.getSoundEventIndex(soundEventId),
                SoundCategory.UI,
                this.getStore()
        );
    }

    public void dismountFromMount() {
        MountPlugin.dismountNpc(this.getStore(), this.getMountEntityId());
    }

    public PlayerWorldData getPlayerWorldData(String worldName) {
        return this.getPlayerConfigData().getPerWorldData(worldName);
    }

    public PlayerWorldData getPlayerCurrentWorldData() {
        return this.getPlayerWorldData(this.getWorld().getName());
    }

    public List<PlayerDeathPositionData> getDeathPositions() {
        return this.getPlayerCurrentWorldData().getDeathPositions();
    }

    public HsPosition getLastPosition() {
        return new HsPosition(this.getPlayerCurrentWorldData().getLastPosition());
    }

    public MapMarker[] getWorldMapMarkers() {
        return this.getPlayerCurrentWorldData().getWorldMapMarkers();
    }

    public PlayerRespawnPointData[] getRespawnPoints() {
        return this.getPlayerCurrentWorldData().getRespawnPoints();
    }

    public SavedMovementStates getLastMovementStates() {
        return this.getPlayerCurrentWorldData().getLastMovementStates();
    }

    public void removeLastDeath(String markerId) {
        this.getPlayerCurrentWorldData().removeLastDeath(markerId);
    }

    public void setPlayerConfigData(PlayerConfigData playerConfigData) {
        this.getPlayerCurrentWorldData().setPlayerConfigData(playerConfigData);
    }

    public void setWorldMapMarkers(MapMarker[] mapMarkers) {
        this.getPlayerCurrentWorldData().setWorldMapMarkers(mapMarkers);
    }

    public void setLastPosition(Transform lastPosition) {
        this.getPlayerCurrentWorldData().setLastPosition(lastPosition);
    }

    public void setLastPosition(HsPosition lastPosition) {
        this.getPlayerCurrentWorldData().setLastPosition(lastPosition.toTransform());
    }

    public void setRespawnPoints(PlayerRespawnPointData[] respawnPoints) {
        this.getPlayerCurrentWorldData().setRespawnPoints(respawnPoints);
    }

    public void setLastMovementStates(MovementStates movementStates, boolean save) {
        this.getPlayerCurrentWorldData().setLastMovementStates(movementStates, save);
    }

    public HsPosition getRespawnPosition(String worldName) {
        CompletableFuture<Transform> future = this.getHytalePlayer().getRespawnPosition(this.getRef(), worldName, this.getStore());
        Transform result = future.join();
        return result == null ? null : new HsPosition(result);
    }

    public HsPosition getRespawnPosition() {
        return this.getRespawnPosition(this.getWorld().getName());
    }

    public EffectControllerComponent getEffectController() {
        return this.getStore().getComponent(this.getRef(), EffectControllerComponent.getComponentType());
    }

    public boolean applyEffect(String assetEffect) {
        com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect effect = com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect.getAssetMap().getAsset(assetEffect);
        return this.getEffectController().addEffect(this.getRef(), effect, this.getStore());
    }

    public void removeEffect(int entityEffectIndex) {
        EffectControllerComponent controller = this.getStore().getComponent(this.getRef(), EffectControllerComponent.getComponentType());
        this.getEffectController().removeEffect(this.getRef(), entityEffectIndex, this.getStore());
    }

    public EntityEffectUpdate[] consumeEffectChanges() {
        return this.getEffectController().consumeChanges();
    }

    public void addActiveEntityEffects(ActiveEntityEffect[] activeEntityEffects) {
        this.getEffectController().addActiveEntityEffects(activeEntityEffects);
    }

    public void clearEffectChanges() {
        this.getEffectController().clearChanges();
    }

    public Int2ObjectMap<ActiveEntityEffect> getActiveEffects() {
        return this.getEffectController().getActiveEffects();
    }

    public int[] getActiveEffectIndexes() {
        return this.getEffectController().getActiveEffectIndexes();
    }

    public EntityEffectUpdate[] createInitEffectUpdates() {
        return this.getEffectController().createInitUpdates();
    }

    public void clearEffects() {
        this.getEffectController().clearEffects(this.getRef(), this.getStore());
    }

    public Velocity getVelocity() {
        return this.getStore().getComponent(this.getRef(), Velocity.getComponentType());
    }

    public void setVelocity(double x, double y, double z) {
        this.getVelocity().set(x, y, z);
    }

    public void setVelocity(Vector3d vector) {
        this.getVelocity().set(vector);
    }

    public void setVelocity(HsPosition hsPosition) {
        this.getVelocity().set(hsPosition.toVector());
    }

    public boolean isAlive() {
        return !HsServer.isPlayerDead(this.uuid);
    }

    public boolean isDead() {
        return !this.isAlive();
    }

    public void respawn() {
        this.getStore().removeComponent(this.getRef(), DeathComponent.getComponentType());
        HsServer.removeDeadPlayer(this.uuid);
    }

    public void removeKnockback() {
        this.getStore().removeComponent(this.getRef(), KnockbackSimulation.getComponentType());
    }
}