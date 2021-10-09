package fr.whiizyyy.insaneac.data;

import fr.whiizyyy.insaneac.util.SafeReflection;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;

public class GolemEntity {
    private final PlayerData playerData;
    private int entityId = -1;

    @SuppressWarnings({ "rawtypes", "unused" })
	public void handle(PlayerData otherData, Packet packet) {
        int entityId;
        PacketPlayOutEntityTeleport packetPlayOutEntityTeleport;
        if (packet instanceof PacketPlayOutEntity) {
            PacketPlayOutEntity packetPlayOutEntity = (PacketPlayOutEntity)packet;
            int entityId2 = SafeReflection.getEntityId(packetPlayOutEntity);
            if (entityId2 == this.playerData.getEntityPlayer().getId()) {
                otherData.getEntityPlayer().playerConnection.sendPacket((Packet)SafeReflection.copyWithNewId(this.entityId, packetPlayOutEntity));
            }
        } else if (packet instanceof PacketPlayOutEntityTeleport && (entityId = SafeReflection.getEntityId(packetPlayOutEntityTeleport = (PacketPlayOutEntityTeleport)packet)) == this.playerData.getEntityPlayer().getId()) {
            otherData.getEntityPlayer().playerConnection.sendPacket((Packet)SafeReflection.copyWithNewId(this.entityId, packetPlayOutEntityTeleport));
        }
    }

    public PacketPlayOutSpawnEntityLiving createPacket() {
        WorldServer worldServer = (WorldServer)this.playerData.getEntityPlayer().world;
        EntityIronGolem entity = new EntityIronGolem((World)worldServer);
        this.entityId = entity.getId();
        entity.setHealth(Float.NaN);
        entity.locX = this.playerData.getLocation().getX();
        entity.locY = this.playerData.getLocation().getY();
        entity.locZ = this.playerData.getLocation().getZ();
        entity.motX = this.playerData.getEntityPlayer().motX;
        entity.motY = this.playerData.getEntityPlayer().motY;
        entity.motZ = this.playerData.getEntityPlayer().motZ;
        entity.pitch = this.playerData.getLocation().getPitch();
        entity.yaw = this.playerData.getLocation().getYaw();
        entity.aK = this.playerData.getEntityPlayer().aK;
        entity.setInvisible(true);
        entity.k(true);
        return new PacketPlayOutSpawnEntityLiving((EntityLiving)entity);
    }

    public GolemEntity(PlayerData playerData) {
        this.playerData = playerData;
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public int getEntityId() {
        return this.entityId;
    }
}