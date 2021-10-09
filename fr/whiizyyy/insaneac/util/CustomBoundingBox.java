package fr.whiizyyy.insaneac.util;

public class CustomBoundingBox
{
    private final double minX;
    private final double centerX;
    private final double maxX;
    private final double minZ;
    private final double centerZ;
    private final double maxZ;
    
    public CustomBoundingBox(final double x, final double z) {
        this.minX = x - 0.3;
        this.centerX = x;
        this.maxX = x + 0.3;
        this.minZ = z - 0.3;
        this.centerZ = z;
        this.maxZ = z + 0.3;
    }
    
    public double getDistanceSquared(final CustomBoundingBox hitbox) {
        final double dx = Math.pow(Math.min(Math.abs(hitbox.centerX - this.minX), Math.abs(hitbox.centerX - this.maxX)), 2.0);
        final double dz = Math.pow(Math.min(Math.abs(hitbox.centerZ - this.minZ), Math.abs(hitbox.centerZ - this.maxZ)), 2.0);
        return dx + dz;
    }
}
