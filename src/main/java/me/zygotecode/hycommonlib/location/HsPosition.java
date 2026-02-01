package me.zygotecode.hycommonlib.location;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;

public class HsPosition {
    private final double posX;
    private final double posY;
    private final double posZ;

    public HsPosition(Transform transform) {
        this.posX = transform.getPosition().getX();
        this.posY = transform.getPosition().getY();
        this.posZ = transform.getPosition().getZ();
    }

    public HsPosition(Vector3d vector3d) {
        this.posX = vector3d.getX();
        this.posY = vector3d.getY();
        this.posZ = vector3d.getZ();
    }

    public HsPosition(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public Transform toTransform() {
        return new Transform(this.posX, this.posY, this.posZ);
    }

    public Vector3d toVector() {
        return new Vector3d(this.posX, this.posY, this.posZ);
    }
}