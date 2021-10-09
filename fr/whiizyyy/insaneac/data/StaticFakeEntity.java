package fr.whiizyyy.insaneac.data;

import com.mojang.authlib.GameProfile;

import fr.whiizyyy.insaneac.util.SafeReflection;

import java.util.Collections;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;

public class StaticFakeEntity {
    private int entityId = -1;
    private final int oldId;
    private final PlayerLocation location;
    private final GameProfile gameProfile;
    private Entity entity;

    public PacketPlayOutNamedEntitySpawn create(WorldServer worldServer) {
        this.entity = new Entity((World)worldServer){

            protected void h() {
            }

            protected void a(NBTTagCompound nbtTagCompound) {
            }

            protected void b(NBTTagCompound nbtTagCompound) {
            }
        };
        this.entityId = this.entity.getId();
        UUID uuid = this.gameProfile.getId();
        int c = (int)Math.floor(this.location.getX() * 32.0);
        int d = (int)Math.floor(this.location.getY() * 32.0);
        int e = (int)Math.floor(this.location.getZ() * 32.0);
        byte f = (byte)(this.location.getYaw() * 256.0f / 360.0f);
        byte g = (byte)(this.location.getPitch() * 256.0f / 360.0f);
        DataWatcher i = this.entity.getDataWatcher();
        i.watch(0, (Object)32);
        return SafeReflection.spawn(this.entityId, uuid, c, d, e, f, g, 0, i, Collections.emptyList());
    }

    public PacketPlayOutEntityTeleport teleport(double x, double y, double z) {
        this.entity.locX = x;
        this.entity.locY = y;
        this.entity.locZ = z;
        this.entity.onGround = true;
        return new PacketPlayOutEntityTeleport(this.entity);
    }

    public PacketPlayOutEntityDestroy destroy() {
        return new PacketPlayOutEntityDestroy(new int[]{this.entityId});
    }

    public StaticFakeEntity(int oldId, PlayerLocation location, GameProfile gameProfile) {
        this.oldId = oldId;
        this.location = location;
        this.gameProfile = gameProfile;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getOldId() {
        return this.oldId;
    }

    public PlayerLocation getLocation() {
        return this.location;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Entity getEntity() {
        return this.entity;
    }

}

