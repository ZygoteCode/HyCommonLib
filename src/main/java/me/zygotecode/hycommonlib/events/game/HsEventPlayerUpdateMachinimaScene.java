package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.protocol.packets.machinima.SceneUpdateType;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerUpdateMachinimaScene extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private String sceneName;
    private String player;
    private float frame;
    private byte[] scene;
    private SceneUpdateType updateType;

    public HsEventPlayerUpdateMachinimaScene(PacketHandler packetHandler, Packet packet, String sceneName, String player, float frame, byte[] scene, SceneUpdateType updateType) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.sceneName = sceneName;
        this.player = player;
        this.frame = frame;
        this.scene = scene;
        this.updateType = updateType;
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

    public String getScenePlayer() {
        return this.player;
    }

    public void setScenePlayer(String player) {
        this.player = player;
    }

    public float getFrame() {
        return this.frame;
    }

    public void setFrame(float frame) {
        this.frame = frame;
    }

    public byte[] getScene() {
        return this.scene;
    }

    public void setScene(byte[] scene) {
        this.scene = scene;
    }

    public SceneUpdateType getUpdateType() {
        return this.updateType;
    }

    public void setUpdateType(SceneUpdateType updateType) {
        this.updateType = updateType;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}