package me.zygotecode.hycommonlib.events.game;

import com.hypixel.hytale.protocol.Packet;
import com.hypixel.hytale.server.core.io.PacketHandler;
import me.zygotecode.hycommonlib.eventapi.events.callables.HsEventCancellable;
import me.zygotecode.hycommonlib.player.HsPlayer;

public class HsEventPlayerRequestMachinimaActorModel extends HsEventCancellable {
    private PacketHandler packetHandler;
    private Packet packet;
    private String actorName;
    private String modelId;
    private String sceneName;

    public HsEventPlayerRequestMachinimaActorModel(PacketHandler packetHandler, Packet packet, String actorName, String modelId, String sceneName) {
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.actorName = actorName;
        this.modelId = modelId;
        this.sceneName = sceneName;
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

    public String getActorName() {
        return this.actorName;
    }

    public String getModelId() {
        return this.modelId;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}