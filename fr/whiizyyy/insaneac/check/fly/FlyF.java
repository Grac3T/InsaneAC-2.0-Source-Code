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

public class FlyF extends MovementCheck {
    private Double lastYDiff = null;
    private int lastBypassTick = 0;

    public FlyF() {
        super(Check.CheckType.FLYF, "F", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly F.Alerts"));
        this.violations = -5.0;
    }

	@Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (!player.isFlying() && !from.getOnGround().booleanValue() && !to.getOnGround().booleanValue() && to.getY() != from.getY() && playerData.getTeleportTicks() > playerData.getMaxPingTicks() * 2 + 10 && player.getAllowFlight() == false) {
            boolean XZ;
            double yDiff = to.getY() - from.getY();
            XZ = from.getX() != to.getX() && from.getZ() != to.getZ();
            if (this.lastYDiff != null && Math.abs(yDiff / 0.9800000190734863 + 0.08) > 1.0E-12 && Math.abs(yDiff + 0.9800000190734863) > 1.0E-12 && Math.abs(yDiff - 0.019999999105930755) > 1.0E-12) {
                double predictedYDiff = (this.lastYDiff - 0.08) * 0.9800000190734863;
                double diff = Math.abs(predictedYDiff - yDiff);
                if (((PlayerData)playerData).getVelocityTicks() > 20 + playerData.getMaxPingTicks() && ((PlayerData)playerData).getTeleportTicks() > ((PlayerData)playerData).getPingTicks() && !player.getAllowFlight() && ((PlayerData)playerData).getTotalTicks() - 10 > this.lastBypassTick) {
                	if (diff > 1.0E-2) {
                        World world = player.getWorld();
                        Cuboid cuboid = new Cuboid(playerData.getLocation()).add(new Cuboid(-0.5, 0.5, -0.5, 1.5, -0.5, 0.5));
                        Cuboid cuboidAbove = new Cuboid(playerData.getLocation()).move(0.0, 2.0, 0.0).add(new Cuboid(-0.5, 0.5, -0.5, 0.5, -0.5, 0.5));
                        int totalTicks = ((PlayerData)playerData).getTotalTicks();
                        this.run(() -> {
                            if (cuboid.checkBlocks(world, type -> !MaterialList.BAD_VELOCITY.contains(type) && !MaterialList.INVALID_SHAPE.contains(type)) && cuboidAbove.checkBlocks(world, type -> type == Material.AIR)) {
                                AlertsManager.getInstance().handleViolation(playerData, this, ((PlayerData)playerData).getVelocityTicks() + " " + diff + " " + yDiff + " " + to.getY() % 1.0 + " " + XZ, XZ ? 1.0 : 0.5);
                            } else {
                                this.violations -= Math.min(this.violations + 5.0, 0.025);
                                this.lastBypassTick = totalTicks;
                            }
                        });
                    } else {
                        this.violations -= Math.min(this.violations + 5.0, 0.025);
                    }
                }
            }
            this.lastYDiff = yDiff;
        } else {
            this.lastYDiff = null;
        }
    }
}

