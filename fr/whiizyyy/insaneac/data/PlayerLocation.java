package fr.whiizyyy.insaneac.data;

import org.bukkit.Location;

import org.bukkit.World;
import org.bukkit.util.Vector;

import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.MathHelper;
import fr.whiizyyy.insaneac.util.MathUtil;


public class PlayerLocation {
    private long timestamp;
    private int tickTime;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Boolean onGround;

    public PlayerLocation spectator() {
        return new PlayerLocation(this.timestamp, this.tickTime, MathUtil.relEntityRoundPos(this.x), MathUtil.relEntityRoundPos(this.y), MathUtil.relEntityRoundPos(this.z), MathUtil.relEntityRoundLook(this.yaw), MathUtil.relEntityRoundLook(this.pitch), this.onGround);
    }

    public boolean sameBlock(PlayerLocation other) {
        return (int)Math.floor(this.x) == (int)Math.floor(other.x) && (int)Math.floor(this.y) == (int)Math.floor(other.y) && (int)Math.floor(this.z) == (int)Math.floor(other.z);
    }

    public PlayerLocation add(double x, double y, double z) {
        return new PlayerLocation(this.timestamp, this.tickTime, this.x + x, this.y + y, this.z + z, this.yaw, this.pitch, this.onGround);
    }

    public PlayerLocation clone() {
        return new PlayerLocation(this.timestamp, this.tickTime, this.x, this.y, this.z, this.yaw, this.pitch, this.onGround);
    }

    public double distanceXZSquared(PlayerLocation location) {
        return Math.pow(this.x - location.x, 2.0) + Math.pow(this.z - location.z, 2.0);
    }

    public double distanceXZ(PlayerLocation location) {
        return MathHelper.sqrt_double(this.distanceXZSquared(location));
    }

    public Location toLocation(World world) {
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public Cuboid hitbox() {
        return new Cuboid(this.x, this.y, this.z).add(new Cuboid(-0.3, 0.3, 0.0, 1.8, -0.3, 0.3)).expand(0.1, 0.1, 0.1);
    }

    public Cuboid to(PlayerLocation playerLocation) {
        return new Cuboid(Math.min(this.x, playerLocation.x), Math.max(this.x, playerLocation.x), Math.min(this.y, playerLocation.y), Math.max(this.y, playerLocation.y), Math.min(this.z, playerLocation.z), Math.max(this.z, playerLocation.z));
    }

    public double distanceSquared(PlayerLocation other) {
        return MathUtil.hypotSquared(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public boolean sameLocation(PlayerLocation playerLocation) {
        return this.x == playerLocation.x && this.y == playerLocation.y && this.z == playerLocation.z;
    }

    public boolean sameDirection(PlayerLocation playerLocation) {
        return this.yaw == playerLocation.yaw && this.pitch == playerLocation.pitch;
    }

    public boolean sameLocationAndDirection(PlayerLocation playerLocation) {
        return this.x == playerLocation.x && this.y == playerLocation.y && this.z == playerLocation.z && this.yaw == playerLocation.yaw && this.pitch == playerLocation.pitch;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            PlayerLocation location = (PlayerLocation)o;
            if (this.timestamp != location.timestamp) {
                return false;
            }
            if (this.tickTime != location.tickTime) {
                return false;
            }
            if (Double.compare(location.x, this.x) != 0) {
                return false;
            }
            if (Double.compare(location.y, this.y) != 0) {
                return false;
            }
            if (Double.compare(location.z, this.z) != 0) {
                return false;
            }
            if (Float.compare(location.yaw, this.yaw) != 0) {
                return false;
            }
            return Float.compare(location.pitch, this.pitch) == 0;
        }
        return false;
    }

    public int hashCode() {
        int result = (int)(this.timestamp ^ this.timestamp >>> 32);
        result = 31 * result + this.tickTime;
        long temp = Double.doubleToLongBits(this.x);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + (this.yaw != 0.0f ? Float.floatToIntBits(this.yaw) : 0);
        result = 31 * result + (this.pitch != 0.0f ? Float.floatToIntBits(this.pitch) : 0);
        return result;
    }

    public PlayerLocation(long timestamp, int tickTime, double x, double y, double z, float yaw, float pitch, Boolean onGround) {
        this.timestamp = timestamp;
        this.tickTime = tickTime;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public int getTickTime() {
        return this.tickTime;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Boolean getOnGround() {
        return this.onGround;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setOnGround(Boolean onGround) {
        this.onGround = onGround;
    }

	public Vector toVector() {
		// TODO Auto-generated method stub
		return null;
	}
}

