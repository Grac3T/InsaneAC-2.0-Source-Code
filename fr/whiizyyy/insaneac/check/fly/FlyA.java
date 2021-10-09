package fr.whiizyyy.insaneac.check.fly;

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

public class FlyA extends MovementCheck {
    private int threshold = 0;
    private int lastBypassTick = -10;

    public FlyA() {
        super(Check.CheckType.FLYA, "A", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly A.Alerts"));
        this.violations = -4.0;
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (!player.isFlying() && playerData.getTeleportTicks() > 1 && !playerData.hasLag()) {
            if (to.getY() % 0.5 == 0.0 && !to.getOnGround().booleanValue() && from.getY() < to.getY() && playerData.getTotalTicks() - 10 > this.lastBypassTick) {
                World world = player.getWorld();
                Cuboid cuboid = new Cuboid(playerData.getLocation()).expand(0.5, 0.5, 0.5);
                int totalTicks = playerData.getTotalTicks();
                this.run(() -> {
                    if (cuboid.checkBlocks(world, type -> !MaterialList.INVALID_SHAPE.contains(type))) {
                        ++this.threshold;
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(to.getY() % 1.0), this.threshold);
                    } else {
                        this.threshold = 0;
                        this.violations -= Math.min(this.violations + 4.0, 0.05);
                        this.lastBypassTick = totalTicks;
                    }
                });
            } else {
                this.violations -= Math.min(this.violations + 4.0, 0.025);
                this.threshold = 0;
            }
        }
    }
}

