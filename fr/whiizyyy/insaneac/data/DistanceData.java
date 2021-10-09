package fr.whiizyyy.insaneac.data;

import fr.whiizyyy.insaneac.util.Cuboid;

public class DistanceData {
    private final Cuboid hitbox;
    private final double x;
    private final double z;
    private final double y;
    private final double dist;

    public DistanceData(Cuboid hitbox, double x, double z, double y, double dist) {
        this.hitbox = hitbox;
        this.x = x;
        this.z = z;
        this.y = y;
        this.dist = dist;
    }

    public Cuboid getHitbox() {
        return this.hitbox;
    }

    public double getX() {
        return this.x;
    }

    public double getZ() {
        return this.z;
    }

    public double getY() {
        return this.y;
    }

    public double getDist() {
        return this.dist;
    }
}



