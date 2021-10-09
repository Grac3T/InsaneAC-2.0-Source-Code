package fr.whiizyyy.insaneac.check.fly;

import org.bukkit.GameMode;
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

public class FlyC extends PacketCheck {
    private Double lastY = null;
    private int threshold = 0;
    private int lastBypassTick = -10;

    public FlyC() {
        super(Check.CheckType.FLYC, "C", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly C.Alerts"));
        this.violations = -2.5;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            if (this.lastY != null) {
                double y;
                y = packetPlayInFlying.g() ? packetPlayInFlying.b() : this.lastY.doubleValue();
                if (!(this.lastY != y || player.getVehicle() != null || packetPlayInFlying.f() || player.isFlying() || player.getGameMode() == GameMode.CREATIVE || playerData.getTeleportTicks() <= playerData.getPingTicks() || playerData.getTotalTicks() - 10 <= this.lastBypassTick || playerData.getVelocityTicks() <= playerData.getPingTicks() * 2 || !playerData.isSpawnedIn() || playerData.hasLag() || playerData.hasFast())) {
                    World world = player.getWorld();
                    Cuboid cuboid = new Cuboid(playerData.getLocation()).add(new Cuboid(-0.5, 0.5, 0.0, 1.5, -0.5, 0.5));
                    int totalTicks = playerData.getTotalTicks();
                    this.run(() -> {
                        if (cuboid.checkBlocks(world, type -> !MaterialList.BAD_VELOCITY.contains((Object)type))) {
                        	if (this.threshold++ > 2) {
                                AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(y), this.threshold - 1);
                            }
                        } else {
                            this.threshold = 0;
                            this.violations -= Math.min(this.violations + 2.5, 0.01);
                            this.lastBypassTick = totalTicks;
                        }
                    });
                } else {
                    this.run(() -> {
                        this.threshold = 0;
                        this.violations -= Math.min(this.violations + 2.5, 0.01);
                    });
                }
            }
            if (packetPlayInFlying.g()) {
                this.lastY = packetPlayInFlying.b();
            }
        }
    }
}

