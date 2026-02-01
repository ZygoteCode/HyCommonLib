package me.zygotecode.hycommonlib.location;

import com.hypixel.hytale.math.vector.Vector3f;

public class HsRotation {
    private final float pitch;
    private final float yaw;
    private final float roll;

    public HsRotation(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getRoll() {
        return this.roll;
    }

    public Vector3f toVector() {
        return new Vector3f(this.getPitch(), this.getYaw(), this.getRoll());
    }
}