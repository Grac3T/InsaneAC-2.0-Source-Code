package fr.whiizyyy.insaneac.check.fly;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.MaterialList;

public class FlyE extends MovementCheck {
    private int threshold = 0;
    private int lastBypassTick = -10;

    public FlyE() {
        super(Check.CheckType.FLYE, "E", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly E.Alerts"));
        this.violations = -10.0;
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (from.getOnGround().booleanValue() && to.getY() > from.getY() && playerData.getVelocityTicks() > (playerData.getPingTicks() + 1) * 4 && playerData.getTeleportTicks() > playerData.getMaxPingTicks() * 2 && playerData.isSpawnedIn() && !player.isFlying() && playerData.getTotalTicks() - 10 > this.lastBypassTick) {
            if (to.getY() - from.getY() < 0.41999998688697815 && (to.getY() - 0.41999998688697815) % 1.0 > 1.0E-15) {
                if (player.getMaximumNoDamageTicks() <= 13) {
                	return;
                }
                World world = player.getWorld();
                Cuboid cuboid = new Cuboid(playerData.getLocation()).move(0.0, 1.8, 0.0).expand(0.5, 0.5, 0.5);
                Cuboid cuboidUnder = new Cuboid(playerData.getLocation()).move(0.0, -0.25, 0.0).expand(0.5, 0.75, 0.5);
                double yDiff = to.getY() - from.getY();
                int totalTicks = playerData.getTotalTicks();
                this.run(() -> {
                    if (cuboid.checkBlocks(world, type -> type == Material.AIR)) {
                        if (cuboidUnder.checkBlocks(world, type -> !MaterialList.INVALID_SHAPE.contains(type) && !MaterialList.BAD_VELOCITY.contains(type))) {
                            ++this.threshold;
                            AlertsManager.getInstance().handleViolation(playerData, this, "" + yDiff + " " + to.getY(), this.threshold);
                            return;
                        }
                    }
                    this.threshold = 0;
                    this.violations -= Math.min(this.violations + 10.0, 0.025);
                    this.lastBypassTick = totalTicks;
                });
            } else {
                this.threshold = 0;
                this.violations -= Math.min(this.violations + 10, 0.025);
            }
        }
    }
}

