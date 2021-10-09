package fr.whiizyyy.insaneac.data;

public class VelocityPacket {
    private final int entityId;
    private final int x;
    private final int y;
    private final int z;

    public VelocityPacket(int entityId, int x, int y, int z) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}

