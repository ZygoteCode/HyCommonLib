package me.zygotecode.hycommonlib.location;

public class HsLocation {
    private final HsPosition position;
    private HsRotation rotation;

    public HsLocation(HsPosition position, HsRotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public HsLocation(double posX, double posY, double posZ) {
        this.position = new HsPosition(posX, posY, posZ);
    }

    public HsLocation(double posX, double posY, double posZ, float pitch, float yaw, float roll) {
        this.position = new HsPosition(posX, posY, posZ);
        this.rotation = new HsRotation(pitch, yaw, roll);
    }

    public HsPosition getPosition() {
        return this.position;
    }

    public HsRotation getRotation() {
        return this.rotation;
    }

    public double getPosX() {
        return this.position.getPosX();
    }

    public double getPosY() {
        return this.position.getPosY();
    }

    public double getPosZ() {
        return this.position.getPosZ();
    }

    public float getPitch() {
        return this.rotation.getPitch();
    }

    public float getYaw() {
        return this.rotation.getYaw();
    }

    public float getRoll() {
        return this.rotation.getRoll();
    }
}