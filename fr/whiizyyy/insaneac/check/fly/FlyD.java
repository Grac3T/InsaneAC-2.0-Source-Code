package fr.whiizyyy.insaneac.check.fly;

import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.MaterialList;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class FlyD extends PacketCheck {
    private int jumped = 0;
    private boolean jumping = false;
    private Double lastY = null;
    private int lastBypassTick = -10;

    public FlyD() {
        super(Check.CheckType.FLYD, "D", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly D.Alerts"));
        this.violations = -10.0;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (!player.isFlying() && !playerData.hasLag() && !packetPlayInFlying.f() && playerData.getVelocityTicks() > playerData.getPingTicks() * 2 && playerData.getTeleportTicks() > playerData.getPingTicks() && !player.isFlying() && playerData.getTotalTicks() - 10 > this.lastBypassTick) {
                if (this.lastY != null) {
                    if (this.jumping && packetPlayInFlying.b() < this.lastY) {
                        if (this.jumped++ > 1) {
                            World world = player.getWorld();
                            Cuboid cuboid = new Cuboid(playerData.getLocation()).add(new Cuboid(-0.5, 0.5, -0.5, 1.5, -0.5, 0.5));
                            int totalTicks = playerData.getTotalTicks();
                            this.run(() -> {
                                if (cuboid.checkBlocks(world, type -> !MaterialList.BAD_VELOCITY.contains((Object)type))) {
                                    AlertsManager.getInstance().handleViolation(playerData, this, "", this.jumped - 1);
                                } else {
                                    this.jumped = 0;
                                    this.violations -= Math.min(this.violations + 10.0, 0.001);
                                    this.lastBypassTick = totalTicks;
                                }
                            });
                        }
                        this.jumping = false;
                    } else if (packetPlayInFlying.b() > this.lastY) {
                        this.jumping = true;
                    }
                }
            } else if (playerData.getLocation().getY() % 0.5 == 0.0 || (playerData.getLocation().getY() - 0.41999998688697815) % 1.0 > 1.0E-15) {
                this.jumped = 0;
                this.jumping = false;
            }
            this.violations -= Math.min(this.violations + 5.0, 0.025);
            if (packetPlayInFlying.h()) {
                this.lastY = packetPlayInFlying.b();
            }
        }
    }
}

