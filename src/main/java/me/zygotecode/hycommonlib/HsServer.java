package me.zygotecode.hycommonlib;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.RequestFlyCameraMode;
import com.hypixel.hytale.protocol.packets.connection.Disconnect;
import com.hypixel.hytale.protocol.packets.connection.Pong;
import com.hypixel.hytale.protocol.packets.entities.MountMovement;
import com.hypixel.hytale.protocol.packets.interaction.DismountNPC;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.protocol.packets.interface_.ChatMessage;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageEvent;
import com.hypixel.hytale.protocol.packets.interface_.UpdateLanguage;
import com.hypixel.hytale.protocol.packets.inventory.*;
import com.hypixel.hytale.protocol.packets.machinima.RequestMachinimaActorModel;
import com.hypixel.hytale.protocol.packets.machinima.UpdateMachinimaScene;
import com.hypixel.hytale.protocol.packets.player.*;
import com.hypixel.hytale.protocol.packets.serveraccess.SetServerAccess;
import com.hypixel.hytale.protocol.packets.serveraccess.UpdateServerAccess;
import com.hypixel.hytale.protocol.packets.setup.PlayerOptions;
import com.hypixel.hytale.protocol.packets.setup.RequestAssets;
import com.hypixel.hytale.protocol.packets.setup.ViewRadius;
import com.hypixel.hytale.protocol.packets.window.ClientOpenWindow;
import com.hypixel.hytale.protocol.packets.window.CloseWindow;
import com.hypixel.hytale.protocol.packets.window.SendWindowAction;
import com.hypixel.hytale.protocol.packets.world.SetPaused;
import com.hypixel.hytale.protocol.packets.worldmap.TeleportToWorldMapMarker;
import com.hypixel.hytale.protocol.packets.worldmap.TeleportToWorldMapPosition;
import com.hypixel.hytale.protocol.packets.worldmap.UpdateWorldMapVisible;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandRegistration;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldConfig;
import com.hypixel.hytale.server.core.universe.world.WorldConfigProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.zygotecode.hycommonlib.event.HsEventRegistrar;
import me.zygotecode.hycommonlib.eventapi.HsEventManager;
import me.zygotecode.hycommonlib.events.game.*;
import me.zygotecode.hycommonlib.events.inventory.*;
import me.zygotecode.hycommonlib.systems.block.*;
import me.zygotecode.hycommonlib.systems.inventory.*;
import me.zygotecode.hycommonlib.events.packet.HsEventClientPacket;
import me.zygotecode.hycommonlib.events.packet.HsEventServerPacket;
import me.zygotecode.hycommonlib.player.HsPlayer;
import me.zygotecode.hycommonlib.systems.player.DeathSystemListener;
import me.zygotecode.hycommonlib.systems.player.KnockbackPredictionSystemListener;
import me.zygotecode.hycommonlib.systems.player.TeleportSystemListener;
import me.zygotecode.hycommonlib.systems.world.MoonPhaseChangeSystemListener;
import me.zygotecode.hycommonlib.world.HsWorld;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class HsServer {
    private static HsEventRegistrar eventRegistrar;
    private static ArrayList<UUID> deadPlayers;

    public static void initialize() {
        eventRegistrar = new HsEventRegistrar(Universe.get().getEventRegistry());
        deadPlayers = new ArrayList<UUID>();

        Universe.get().getEntityStoreRegistry().registerSystem(new DeathSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new TeleportSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new KnockbackPredictionSystemListener());

        Universe.get().getEntityStoreRegistry().registerSystem(new DamageBlockSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new PlaceBlockSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new BreakBlockSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new UseBlockPreSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new UseBlockPostSystemListener());

        Universe.get().getEntityStoreRegistry().registerSystem(new DropItemDropSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new DropItemRequestSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new ItemPickupSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new CraftRecipePreSystemListener());
        Universe.get().getEntityStoreRegistry().registerSystem(new CraftRecipePostSystemListener());

        Universe.get().getEntityStoreRegistry().registerSystem(new MoonPhaseChangeSystemListener());

        PacketAdapters.registerOutbound((PacketHandler handler, Packet packet) -> {
            HsEventServerPacket event = new HsEventServerPacket(handler, packet);
            HsEventManager.call(event);

            packet = event.getPacket();
            return event.isCancelled();
        });

        PacketAdapters.registerInbound((PacketHandler handler, Packet packet) -> {
            HsEventClientPacket event = new HsEventClientPacket(handler, packet);
            HsEventManager.call(event);

            if (packet instanceof ChatMessage chatPacket) {
                HsEventChatMessage chatMessageEvent = new HsEventChatMessage(handler, packet, chatPacket.message);
                HsEventManager.call(chatMessageEvent);

                if (chatMessageEvent.isCancelled()) {
                    return true;
                }

                chatPacket.message = chatMessageEvent.getMessage();
            } else if (packet instanceof Disconnect disconnectPacket) {
                HsEventPlayerDisconnected disconnectEvent = new HsEventPlayerDisconnected(handler, packet, disconnectPacket.reason, disconnectPacket.type);
                HsEventManager.call(disconnectEvent);

                if (disconnectEvent.isCancelled()) {
                    return true;
                }

                disconnectPacket.reason = disconnectEvent.getReason();
                disconnectPacket.type = disconnectEvent.getType();
            } else if (packet instanceof Pong pongPacket) {
                HsEventPlayerPong pongEvent = new HsEventPlayerPong(handler, packet, pongPacket.type, pongPacket.id, pongPacket.packetQueueSize, pongPacket.time);
                HsEventManager.call(pongEvent);

                if (pongEvent.isCancelled()) {
                    return true;
                }

                pongPacket.type = pongEvent.getType();
                pongPacket.id = pongEvent.getId();
                pongPacket.packetQueueSize = pongEvent.getPacketQueueSize();
                pongPacket.time = pongEvent.getTime();
            } else if (packet instanceof ClientMovement movementPacket) {
                HsEventPlayerMovement movementEvent = new HsEventPlayerMovement(handler, packet, movementPacket.movementStates, movementPacket.relativePosition, movementPacket.absolutePosition, movementPacket.bodyOrientation, movementPacket.lookOrientation, movementPacket.teleportAck, movementPacket.wishMovement, movementPacket.velocity, movementPacket.mountedTo, movementPacket.riderMovementStates);
                HsEventManager.call(movementEvent);

                if (movementEvent.isCancelled()) {
                    return true;
                }

                movementPacket.movementStates =  movementEvent.getMovementStates();
                movementPacket.relativePosition = movementEvent.getRelativePosition();
                movementPacket.absolutePosition = movementEvent.getAbsolutePosition();
                movementPacket.bodyOrientation = movementEvent.getBodyOrientation();
                movementPacket.lookOrientation = movementEvent.getLookOrientation();
                movementPacket.teleportAck = movementEvent.getTeleportAck();
                movementPacket.wishMovement = movementEvent.getWishMovement();
                movementPacket.velocity = movementEvent.getVelocity();
                movementPacket.mountedTo = movementEvent.getMountedTo();
                movementPacket.riderMovementStates = movementEvent.getRiderMovementStates();
            } else if (packet instanceof RequestAssets requestAssetsPacket) {
                HsEventClientRequestAssets requestAssetsEvent = new HsEventClientRequestAssets(handler, packet, requestAssetsPacket.assets);
                HsEventManager.call(requestAssetsEvent);

                if (requestAssetsEvent.isCancelled()) {
                    return true;
                }

                requestAssetsPacket.assets = requestAssetsEvent.getAssets();
            } else if (packet instanceof PlayerOptions playerOptionsPacket) {
                HsEventPlayerOptions playerOptionsEvent = new HsEventPlayerOptions(handler, packet, playerOptionsPacket.skin);
                HsEventManager.call(playerOptionsEvent);

                if (playerOptionsEvent.isCancelled()) {
                    return true;
                }

                playerOptionsPacket.skin = playerOptionsEvent.getSkin();
            } else if (packet instanceof CustomPageEvent customPageEventPacket) {
                HsEventClientCustomPageEvent customEvent = new HsEventClientCustomPageEvent(handler, packet, customPageEventPacket.type, customPageEventPacket.data);
                HsEventManager.call(customEvent);

                if (customEvent.isCancelled()) {
                    return true;
                }

                customPageEventPacket.data = customEvent.getData();
                customPageEventPacket.type = customEvent.getType();
            } else if (packet instanceof ViewRadius viewRadiusPacket) {
                HsEventPlayerViewRadius viewRadiusEvent = new HsEventPlayerViewRadius(handler, packet, viewRadiusPacket.value);
                HsEventManager.call(viewRadiusEvent);

                if (viewRadiusEvent.isCancelled()) {
                    return true;
                }

                viewRadiusPacket.value = viewRadiusEvent.getValue();
            } else if (packet instanceof UpdateLanguage updateLanguagePacket) {
                HsEventPlayerUpdateLanguage updateLanguageEvent = new HsEventPlayerUpdateLanguage(handler, packet, updateLanguagePacket.language);
                HsEventManager.call(updateLanguageEvent);

                if (updateLanguageEvent.isCancelled()) {
                    return true;
                }

                updateLanguagePacket.language = updateLanguageEvent.getLanguage();
            } else if (packet instanceof MouseInteraction mouseInteractionPacket) {
                HsEventPlayerMouseInteraction mouseInteractionEvent = new HsEventPlayerMouseInteraction(handler, packet, mouseInteractionPacket.activeSlot, mouseInteractionPacket.mouseButton, mouseInteractionPacket.mouseMotion, mouseInteractionPacket.clientTimestamp, mouseInteractionPacket.itemInHandId, mouseInteractionPacket.screenPoint, mouseInteractionPacket.worldInteraction);
                HsEventManager.call(mouseInteractionEvent);

                if (mouseInteractionEvent.isCancelled()) {
                    return true;
                }

                mouseInteractionPacket.activeSlot = mouseInteractionEvent.getActiveSlot();
                mouseInteractionPacket.mouseButton = mouseInteractionEvent.getMouseButton();
                mouseInteractionPacket.mouseMotion = mouseInteractionEvent.getMouseMotion();
                mouseInteractionPacket.clientTimestamp = mouseInteractionEvent.getClientTimestamp();
                mouseInteractionPacket.itemInHandId = mouseInteractionEvent.getItemInHandId();
                mouseInteractionPacket.screenPoint = mouseInteractionEvent.getScreenPoint();
                mouseInteractionPacket.worldInteraction = mouseInteractionEvent.getWorldInteraction();
            } else if (packet instanceof UpdateServerAccess updateServerAccessPacket) {
                HsEventClientUpdateServerAccess updateServerAccessEvent = new HsEventClientUpdateServerAccess(handler, packet, updateServerAccessPacket.access, updateServerAccessPacket.hosts);
                HsEventManager.call(updateServerAccessEvent);

                if (updateServerAccessEvent.isCancelled()) {
                    return true;
                }

                updateServerAccessPacket.access = updateServerAccessEvent.getAccess();
                updateServerAccessPacket.hosts = updateServerAccessEvent.getHosts();
            } else if (packet instanceof SetServerAccess setServerAccessPacket) {
                HsEventClientSetServerAccess setServerAccessEvent = new HsEventClientSetServerAccess(handler, packet, setServerAccessPacket.access, setServerAccessPacket.password);
                HsEventManager.call(setServerAccessEvent);

                if (setServerAccessEvent.isCancelled()) {
                    return true;
                }

                setServerAccessPacket.access = setServerAccessEvent.getAccess();
                setServerAccessPacket.password = setServerAccessEvent.getPassword();
            } else if (packet instanceof ClientOpenWindow clientOpenWindowPacket) {
                HsEventClientOpenWindow openWindowEvent = new HsEventClientOpenWindow(handler, packet, clientOpenWindowPacket.type);
                HsEventManager.call(openWindowEvent);

                if (openWindowEvent.isCancelled()) {
                    return true;
                }

                clientOpenWindowPacket.type = openWindowEvent.getType();
            } else if (packet instanceof SendWindowAction sendWindowActionPacket) {
                HsEventClientSendWindowAction sendWindowActionEvent = new HsEventClientSendWindowAction(handler, packet, sendWindowActionPacket.action, sendWindowActionPacket.id);
                HsEventManager.call(sendWindowActionEvent);

                if (sendWindowActionEvent.isCancelled()) {
                    return true;
                }

                sendWindowActionPacket.action = sendWindowActionEvent.getAction();
                sendWindowActionPacket.id = sendWindowActionEvent.getId();
            } else if (packet instanceof CloseWindow closeWindowPacket) {
                HsEventClientCloseWindow closeWindowEvent = new HsEventClientCloseWindow(handler, packet, closeWindowPacket.id);
                HsEventManager.call(closeWindowEvent);

                if (closeWindowEvent.isCancelled()) {
                    return true;
                }

                closeWindowPacket.id = closeWindowEvent.getId();
            } else if (packet instanceof DismountNPC dismountNPCPacket) {
                HsEventPlayerDismountNPC dismountNpcEvent = new HsEventPlayerDismountNPC(handler, packet);
                HsEventManager.call(dismountNpcEvent);

                if (dismountNpcEvent.isCancelled()) {
                    return true;
                }
            } else if (packet instanceof RequestMachinimaActorModel requestMachinimaActorModelPacket) {
                HsEventPlayerRequestMachinimaActorModel requestMachinimaActorModelEvent = new HsEventPlayerRequestMachinimaActorModel(handler, packet, requestMachinimaActorModelPacket.actorName, requestMachinimaActorModelPacket.modelId, requestMachinimaActorModelPacket.sceneName);
                HsEventManager.call(requestMachinimaActorModelEvent);

                if (requestMachinimaActorModelEvent.isCancelled()) {
                    return true;
                }

                requestMachinimaActorModelPacket.actorName = requestMachinimaActorModelEvent.getActorName();
                requestMachinimaActorModelPacket.modelId = requestMachinimaActorModelEvent.getModelId();
                requestMachinimaActorModelPacket.sceneName = requestMachinimaActorModelEvent.getSceneName();
            } else if (packet instanceof UpdateMachinimaScene updateMachinimaScenePacket) {
                HsEventPlayerUpdateMachinimaScene updateMachinimaSceneEvent = new HsEventPlayerUpdateMachinimaScene(handler, packet, updateMachinimaScenePacket.sceneName, updateMachinimaScenePacket.player, updateMachinimaScenePacket.frame, updateMachinimaScenePacket.scene, updateMachinimaScenePacket.updateType);
                HsEventManager.call(updateMachinimaSceneEvent);

                if (updateMachinimaSceneEvent.isCancelled()) {
                    return true;
                }

                updateMachinimaScenePacket.sceneName = updateMachinimaSceneEvent.getSceneName();
                updateMachinimaScenePacket.player = updateMachinimaSceneEvent.getScenePlayer();
                updateMachinimaScenePacket.frame = updateMachinimaSceneEvent.getFrame();
                updateMachinimaScenePacket.updateType = updateMachinimaSceneEvent.getUpdateType();
                updateMachinimaScenePacket.scene = updateMachinimaSceneEvent.getScene();
            } else if (packet instanceof ClientReady clientReadyPacket) {
                HsEventClientReady clientReadyEvent = new HsEventClientReady(handler, packet, clientReadyPacket.readyForChunks, clientReadyPacket.readyForGameplay);
                HsEventManager.call(clientReadyEvent);

                if (clientReadyEvent.isCancelled()) {
                    return true;
                }

                clientReadyPacket.readyForChunks = clientReadyEvent.isReadyForChunks();
                clientReadyPacket.readyForGameplay = clientReadyEvent.isReadyForGameplay();
            } else if (packet instanceof MountMovement mountMovementPacket) {
                HsEventPlayerMountMovement mountMovementEvent = new HsEventPlayerMountMovement(handler, packet, mountMovementPacket.absolutePosition, mountMovementPacket.movementStates, mountMovementPacket.bodyOrientation);
                HsEventManager.call(mountMovementEvent);

                if (mountMovementEvent.isCancelled()) {
                    return true;
                }

                mountMovementPacket.movementStates = mountMovementEvent.getMovementStates();
                mountMovementPacket.absolutePosition = mountMovementEvent.getAbsolutePosition();
                mountMovementPacket.bodyOrientation = mountMovementEvent.getBodyOrientation();
            } else if (packet instanceof SyncPlayerPreferences syncPlayerPreferencesPacket) {
                HsEventClientSyncPlayerPreferences syncPlayerPreferencesEvent = new HsEventClientSyncPlayerPreferences(handler, packet, syncPlayerPreferencesPacket.allowNPCDetection, syncPlayerPreferencesPacket.armorItemsPreferredPickupLocation, syncPlayerPreferencesPacket.miscItemsPreferredPickupLocation, syncPlayerPreferencesPacket.respondToHit, syncPlayerPreferencesPacket.showEntityMarkers, syncPlayerPreferencesPacket.solidBlockItemsPreferredPickupLocation, syncPlayerPreferencesPacket.usableItemsItemsPreferredPickupLocation, syncPlayerPreferencesPacket.weaponAndToolItemsPreferredPickupLocation);
                HsEventManager.call(syncPlayerPreferencesEvent);

                if (syncPlayerPreferencesEvent.isCancelled()) {
                    return true;
                }

                syncPlayerPreferencesPacket.allowNPCDetection = syncPlayerPreferencesEvent.isAllowNPCDetection();
                syncPlayerPreferencesPacket.armorItemsPreferredPickupLocation = syncPlayerPreferencesEvent.getArmorItemsPreferredPickupLocation();
                syncPlayerPreferencesPacket.miscItemsPreferredPickupLocation = syncPlayerPreferencesEvent.getMiscItemsPreferredPickupLocation();
                syncPlayerPreferencesPacket.respondToHit = syncPlayerPreferencesEvent.isRespondToHit();
                syncPlayerPreferencesPacket.showEntityMarkers = syncPlayerPreferencesEvent.isShowEntityMarkers();
                syncPlayerPreferencesPacket.solidBlockItemsPreferredPickupLocation = syncPlayerPreferencesEvent.getSolidBlockItemsPreferredPickupLocation();
                syncPlayerPreferencesPacket.usableItemsItemsPreferredPickupLocation = syncPlayerPreferencesEvent.getUsableItemsPreferredPickupLocation();
                syncPlayerPreferencesPacket.weaponAndToolItemsPreferredPickupLocation = syncPlayerPreferencesEvent.getWeaponAndToolItemsPreferredPickupLocation();
            } else if (packet instanceof ClientPlaceBlock clientPlaceBlockPacket) {
                HsEventClientPlaceBlock clientPlaceBlockEvent = new HsEventClientPlaceBlock(handler, packet, clientPlaceBlockPacket.placedBlockId, clientPlaceBlockPacket.position, clientPlaceBlockPacket.rotation);
                HsEventManager.call(clientPlaceBlockEvent);

                if (clientPlaceBlockEvent.isCancelled()) {
                    return true;
                }

                clientPlaceBlockPacket.placedBlockId = clientPlaceBlockEvent.getPlacedBlockId();
                clientPlaceBlockPacket.position = clientPlaceBlockEvent.getBlockPosition();
                clientPlaceBlockPacket.rotation = clientPlaceBlockEvent.getBlockRotation();
            } else if (packet instanceof RemoveMapMarker removeMapMarkerPacket) {
                HsEventPlayerRemoveMapMarker removeMapMarkerEvent = new HsEventPlayerRemoveMapMarker(handler, packet, removeMapMarkerPacket.markerId);
                HsEventManager.call(removeMapMarkerEvent);

                if (removeMapMarkerEvent.isCancelled()) {
                    return true;
                }

                removeMapMarkerPacket.markerId = removeMapMarkerEvent.getMarkerId();
            } else if (packet instanceof UpdateWorldMapVisible updateWorldMapVisiblePacket) {
                HsEventClientUpdateWorldMapVisible updateWorldMapVisibleEvent = new HsEventClientUpdateWorldMapVisible(handler, packet, updateWorldMapVisiblePacket.visible);
                HsEventManager.call(updateWorldMapVisibleEvent);

                if (updateWorldMapVisibleEvent.isCancelled()) {
                    return true;
                }

                updateWorldMapVisiblePacket.visible = updateWorldMapVisibleEvent.isVisible();
            } else if (packet instanceof TeleportToWorldMapMarker teleportToWorldMapMarkerPacket) {
                HsEventClientTeleportToWorldMapMarker teleportToWorldMapMarkerEvent = new HsEventClientTeleportToWorldMapMarker(handler, packet, teleportToWorldMapMarkerPacket.id);
                HsEventManager.call(teleportToWorldMapMarkerEvent);

                if (teleportToWorldMapMarkerEvent.isCancelled()) {
                    return true;
                }

                teleportToWorldMapMarkerPacket.id = teleportToWorldMapMarkerEvent.getMarkerId();
            } else if (packet instanceof TeleportToWorldMapPosition teleportToWorldMapPositionPacket) {
                HsEventClientTeleportToWorldMapPosition teleportToWorldMapPositionEvent = new HsEventClientTeleportToWorldMapPosition(handler, packet, teleportToWorldMapPositionPacket.x, teleportToWorldMapPositionPacket.y);
                HsEventManager.call(teleportToWorldMapPositionEvent);

                if (teleportToWorldMapPositionEvent.isCancelled()) {
                    return true;
                }

                teleportToWorldMapPositionPacket.x = teleportToWorldMapPositionEvent.getPosX();
                teleportToWorldMapPositionPacket.y = teleportToWorldMapPositionEvent.getPosY();
            } else if (packet instanceof SyncInteractionChains syncInteractionChainsPacket) {
                HsEventClientSyncInteractionChains syncInteractionChainsEvent = new HsEventClientSyncInteractionChains(handler, packet, syncInteractionChainsPacket.updates);
                HsEventManager.call(syncInteractionChainsEvent);

                if (syncInteractionChainsEvent.isCancelled()) {
                    return true;
                }

                syncInteractionChainsPacket.updates = syncInteractionChainsEvent.getUpdates();
            } else if (packet instanceof SetPaused setPausedPacket) {
                HsEventPlayerSetPaused setPausedEvent = new HsEventPlayerSetPaused(handler, packet, setPausedPacket.paused);
                HsEventManager.call(setPausedEvent);

                if (setPausedEvent.isCancelled()) {
                    return true;
                }

                setPausedPacket.paused = setPausedEvent.isPaused();
            } else if (packet instanceof RequestFlyCameraMode requestFlyCameraModePacket) {
                HsEventPlayerRequestFlyCameraMode requestFlyCameraModeEvent = new HsEventPlayerRequestFlyCameraMode(handler, packet, requestFlyCameraModePacket.entering);
                HsEventManager.call(requestFlyCameraModeEvent);

                if (requestFlyCameraModeEvent.isCancelled()) {
                    return true;
                }

                requestFlyCameraModePacket.entering = requestFlyCameraModeEvent.isEntering();
            } else if (packet instanceof SetCreativeItem setCreativeItemPacket) {
                HsEventPlayerSetCreativeItem setCreativeItemEvent = new HsEventPlayerSetCreativeItem(handler, packet, setCreativeItemPacket.item, setCreativeItemPacket.inventorySectionId, setCreativeItemPacket.override, setCreativeItemPacket.slotId);
                HsEventManager.call(setCreativeItemEvent);

                if (setCreativeItemEvent.isCancelled()) {
                    return true;
                }

                setCreativeItemPacket.inventorySectionId =  setCreativeItemEvent.getInventorySectionId();
                setCreativeItemPacket.override = setCreativeItemEvent.isOverride();
                setCreativeItemPacket.slotId = setCreativeItemEvent.getSlotId();
                setCreativeItemPacket.item = setCreativeItemEvent.getItem();
            } else if (packet instanceof DropCreativeItem dropCreativeItemPacket) {
                HsEventPlayerDropCreativeItem dropCreativeItemEvent = new HsEventPlayerDropCreativeItem(handler, packet, dropCreativeItemPacket.item);
                HsEventManager.call(dropCreativeItemEvent);

                if (dropCreativeItemEvent.isCancelled()) {
                    return true;
                }

                dropCreativeItemPacket.item =  dropCreativeItemEvent.getItem();
            } else if (packet instanceof SmartGiveCreativeItem smartGiveCreativeItemPacket) {
                HsEventPlayerSmartGiveCreativeItem smartGiveCreativeItemEvent = new HsEventPlayerSmartGiveCreativeItem(handler, packet, smartGiveCreativeItemPacket.item, smartGiveCreativeItemPacket.moveType);
                HsEventManager.call(smartGiveCreativeItemEvent);

                if (smartGiveCreativeItemEvent.isCancelled()) {
                    return true;
                }

                smartGiveCreativeItemPacket.item = smartGiveCreativeItemEvent.getItem();
                smartGiveCreativeItemPacket.moveType = smartGiveCreativeItemEvent.getMoveType();
            } else if (packet instanceof DropItemStack dropItemStackPacket) {
                HsEventPlayerDropItemStack dropItemStackEvent = new HsEventPlayerDropItemStack(handler, packet, dropItemStackPacket.inventorySectionId, dropItemStackPacket.slotId, dropItemStackPacket.quantity);
                HsEventManager.call(dropItemStackEvent);

                if (dropItemStackEvent.isCancelled()) {
                    return true;
                }

                dropItemStackPacket.inventorySectionId = dropItemStackEvent.getInventorySectionId();
                dropItemStackPacket.quantity = dropItemStackEvent.getQuantity();
                dropItemStackPacket.slotId = dropItemStackEvent.getSlotId();
            } else if (packet instanceof MoveItemStack moveItemStackPacket) {
                HsEventPlayerMoveItemStack moveItemStackEvent = new HsEventPlayerMoveItemStack(handler, packet, moveItemStackPacket.quantity, moveItemStackPacket.fromSectionId, moveItemStackPacket.toSectionId, moveItemStackPacket.fromSlotId, moveItemStackPacket.toSlotId);
                HsEventManager.call(moveItemStackEvent);

                if (moveItemStackEvent.isCancelled()) {
                    return true;
                }

                moveItemStackPacket.quantity =  moveItemStackEvent.getQuantity();
                moveItemStackPacket.fromSectionId = moveItemStackEvent.getFromSectionId();
                moveItemStackPacket.toSectionId = moveItemStackEvent.getToSectionId();
                moveItemStackPacket.fromSlotId = moveItemStackEvent.getFromSlotId();
                moveItemStackPacket.toSlotId =  moveItemStackEvent.getToSlotId();
            } else if (packet instanceof SmartMoveItemStack smartMoveItemStackPacket) {
                HsEventPlayerSmartMoveItemStack smartMoveItemStackEvent = new HsEventPlayerSmartMoveItemStack(handler, packet, smartMoveItemStackPacket.fromSectionId, smartMoveItemStackPacket.fromSlotId, smartMoveItemStackPacket.quantity, smartMoveItemStackPacket.moveType);
                HsEventManager.call(smartMoveItemStackEvent);

                if (smartMoveItemStackEvent.isCancelled()) {
                    return true;
                }

                smartMoveItemStackPacket.fromSectionId = smartMoveItemStackEvent.getFromSectionId();
                smartMoveItemStackPacket.fromSlotId = smartMoveItemStackEvent.getFromSlotId();
                smartMoveItemStackPacket.quantity = smartMoveItemStackEvent.getQuantity();
                smartMoveItemStackPacket.moveType = smartMoveItemStackEvent.getMoveType();
            } else if (packet instanceof SetActiveSlot setActiveSlotPacket) {
                HsEventPlayerSetActiveSlot setActiveSlotEvent = new HsEventPlayerSetActiveSlot(handler, packet, setActiveSlotPacket.activeSlot, setActiveSlotPacket.inventorySectionId);
                HsEventManager.call(setActiveSlotEvent);

                if (setActiveSlotEvent.isCancelled()) {
                    return true;
                }

                setActiveSlotPacket.activeSlot = setActiveSlotEvent.getActiveSlot();
                setActiveSlotPacket.inventorySectionId = setActiveSlotEvent.getInventorySectionId();
            } else if (packet instanceof SwitchHotbarBlockSet switchHotbarBlockSetPacket) {
                HsEventPlayerSwitchHotbarBlockSet switchHotbarBlockSetEvent = new HsEventPlayerSwitchHotbarBlockSet(handler, packet, switchHotbarBlockSetPacket.itemId);
                HsEventManager.call(switchHotbarBlockSetEvent);

                if (switchHotbarBlockSetEvent.isCancelled()) {
                    return true;
                }

                switchHotbarBlockSetPacket.itemId = switchHotbarBlockSetEvent.getItemId();
            } else if (packet instanceof InventoryAction inventoryActionPacket) {
                HsEventPlayerInventoryAction inventoryActionEvent = new HsEventPlayerInventoryAction(handler, packet, inventoryActionPacket.actionData, inventoryActionPacket.inventorySectionId, inventoryActionPacket.inventoryActionType);
                HsEventManager.call(inventoryActionEvent);

                if (inventoryActionEvent.isCancelled()) {
                    return true;
                }

                inventoryActionPacket.inventorySectionId = inventoryActionEvent.getInventorySectionId();
                inventoryActionPacket.inventoryActionType = inventoryActionEvent.getInventoryActionType();
                inventoryActionPacket.actionData = inventoryActionEvent.getActionData();
            }

            packet = event.getPacket();
            return event.isCancelled();
        });
    }

    public static void initialize(Object listener) {
        initialize();
        registerEvents(listener);
    }

    public static void initialize(Object[] listeners) {
        initialize();
        registerEvents(listeners);
    }

    public static HsWorld getWorld(String name) {
        return new HsWorld(Universe.get().getWorld(name));
    }

    public static HsWorld getWorld(UUID uuid) {
        return new HsWorld(uuid);
    }

    public static HsWorld getWorld(World world) {
        return new HsWorld(world);
    }

    public static HsWorld getWorld(Store<EntityStore> store) {
        for (HsWorld world: getWorlds()) {
            if (world.getStore().equals(store)) {
                return world;
            }
        }

        return null;
    }

    public static HsPlayer getPlayer(String username) {
        return new HsPlayer(Universe.get().getPlayerByUsername(username, NameMatching.EXACT_IGNORE_CASE).getUuid());
    }

    public static HsPlayer getPlayer(UUID uuid) {
        return new HsPlayer(uuid);
    }

    public static HsPlayer getPlayer(Player player) {
        return new HsPlayer(player);
    }

    public static HsPlayer getPlayer(PlayerRef playerRef) {
        return new HsPlayer(playerRef.getUuid());
    }

    public static HsPlayer getPlayer(Store<EntityStore> store) {
        for (HsPlayer player: getPlayers()) {
            if (player.getStore().equals(store)) {
                return player;
            }
        }

        return null;
    }

    public static ArrayList<HsWorld> getWorlds() {
        ArrayList<HsWorld> worlds = new ArrayList<>();

        for (World world : Universe.get().getWorlds().values()) {
            worlds.add(new HsWorld(world));
        }

        return worlds;
    }

    public static Universe getUniverse() {
        return Universe.get();
    }

    public static ArrayList<HsPlayer> getPlayers() {
        ArrayList<HsPlayer> players = new ArrayList<>();

        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            players.add(new HsPlayer(playerRef.getUuid()));
        }

        return players;
    }

    public static void registerEvents(Object listener) {
        eventRegistrar.registerEvents(listener);
        HsEventManager.register(listener);
    }

    public static void registerEvents(Object[] listeners) {
        for (Object listener: listeners) {
            registerEvents(listener);
        }
    }

    /*public static void broadcastPackets(Packet... packets) {
        Universe.get().broadcastPacket(packets);
    }

    public static void broadcastPacket(Packet packet) {
        Universe.get().broadcastPacket(packet);
    }

    public static void broadcastPacketNoCache(Packet packet) {
        Universe.get().broadcastPacketNoCache(packet);
    }*/

    public static void disconnectAllPlayers() {
        Universe.get().disconnectAllPLayers();
    }

    public static int getPlayerCount() {
        return Universe.get().getPlayerCount();
    }

    public static void shutdownAllWorlds() {
        Universe.get().shutdownAllWorlds();
    }

    public static void addWorld(String name) {
        Universe.get().addWorld(name);
    }

    public static void removeWorld(String name) {
        Universe.get().removeWorld(name);
    }

    public static World getDefaultWorld() {
        return Universe.get().getDefaultWorld();
    }

    public static void sendMessage(String message) {
        Universe.get().sendMessage(Message.raw(message));
    }

    public static void sendMessage(Message message) {
        Universe.get().sendMessage(message);
    }

    public static boolean isWorldLoadable(String name) {
        return Universe.get().isWorldLoadable(name);
    }

    public static void makeWorld(String name, Path savePath, WorldConfig worldConfig, boolean start) {
        Universe.get().makeWorld(name, savePath, worldConfig, start);
    }

    public static void makeWorld(String name, Path savePath, WorldConfig worldConfig) {
        Universe.get().makeWorld(name, savePath, worldConfig);
    }

    public static WorldConfigProvider getWorldConfigProvider() {
        return Universe.get().getWorldConfigProvider();
    }

    public static void log(Level level, String message) {
        Universe.get().getLogger().at(level).log(message);
    }

    public static void logInfo(String message) {
        log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        log(Level.WARNING, message);
    }

    public static void logSevere(String message) {
        log(Level.SEVERE, message);
    }

    public static void addDeadPlayer(UUID uuid) {
        deadPlayers.add(uuid);
    }

    public static void removeDeadPlayer(UUID uuid) {
        deadPlayers.remove(uuid);
    }

    public static boolean isPlayerDead(UUID uuid) {
        return deadPlayers.contains(uuid);
    }

    public static void registerCommand(AbstractCommand command) {
        Universe.get().getCommandRegistry().registerCommand(command);
    }

    public static void registerCommand(CommandRegistration registration) {
        Universe.get().getCommandRegistry().register(registration);
    }
}