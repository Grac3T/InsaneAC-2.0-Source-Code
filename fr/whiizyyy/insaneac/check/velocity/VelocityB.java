package fr.whiizyyy.insaneac.check.velocity;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.MathUtil;

public class VelocityB extends MovementCheck {
    private int lastBypassTicks = 0;
    private double total = 0.0;

    public VelocityB() {
        super(Check.CheckType.VELOCITYB, "B", "Velocity", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Velocity B.Alerts"));
        this.violations = -2.0;
    }

	@Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
		if (playerData.getPing() > 120) {
			return;
		}
        if (!playerData.hasLag() && playerData.getVelX() != 0.0 && playerData.getVelZ() != 0.0 && playerData.getHorizontalVelocityTicks() > playerData.getMoveTicks() && (to.getY() - from.getY()) > 0.0) {
            double velocity = MathUtil.hypot(playerData.getVelX(), playerData.getVelZ());
            if (playerData.getLastLastLocation().getOnGround().booleanValue() && from.getOnGround().booleanValue() && !to.getOnGround().booleanValue() && MathUtil.onGround(from.getY()) && !MathUtil.onGround(to.getY()) && velocity > 0.0 && playerData.getTotalTicks() - this.lastBypassTicks > 10) {
                Vector vector = new Vector(playerData.getLastLastLocation().getX(), playerData.getLastLastLocation().getY(), playerData.getLastLastLocation().getZ());
                vector.subtract(new Vector(from.getX(), from.getY(), from.getZ()));
                PlayerLocation newTo = to.add(vector.getX(), vector.getY(), vector.getZ());
                double distance = MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
                double properDistance = MathUtil.hypot(newTo.getX() - from.getX(), newTo.getZ() - from.getZ());
                double percentage = Math.max(distance, properDistance) / velocity;
                this.violations -= Math.min(this.violations, 0.01);
                if (percentage < 1.0) {
                    World world = player.getWorld();
                    Cuboid cuboid = new Cuboid(to).add(new Cuboid(-1.0, 1.0, 0.0, 2.05, -1.0, 1.0));
                    int totalTicks = playerData.getTotalTicks();
                    this.run(() -> {
                        if (cuboid.checkBlocks(world, type -> type == Material.AIR)) {
                            this.total += 1.0 - percentage;
                            if (this.total > 2.0) {
                                this.total = 0.0;
                                AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0);
                            }
                        } else {
                            this.total -= Math.min(this.total, 0.03);
                            this.lastBypassTicks = totalTicks;
                            this.violations -= Math.min(this.violations + 2.0, 0.01);
                        }
                    });
                } else {
                    this.total -= Math.min(this.total, 0.03);
                    this.violations -= Math.min(this.violations + 2.0, 0.01);
                }
            }
            playerData.setVelX(0.0);
            playerData.setVelZ(0.0);
        }
    }
}

