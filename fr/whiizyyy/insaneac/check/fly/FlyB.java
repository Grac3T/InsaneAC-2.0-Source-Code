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
import fr.whiizyyy.insaneac.util.MathUtil;

public class FlyB extends MovementCheck {
    private int threshold = 0;
    private int lastBypassTick = -10;

    public FlyB() {
        super(Check.CheckType.FLYB, "B", "Fly", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Fly B.Alerts"));
        this.violations = -4.0;
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (from.getOnGround().booleanValue() && to.getOnGround().booleanValue() && from.getY() != to.getY() && !MathUtil.onGround(from.getY()) && !MathUtil.onGround(to.getY()) && playerData.isSpawnedIn() && playerData.getTotalTicks() - 10 > this.lastBypassTick) {
            World world = player.getWorld();
            Cuboid cuboid = new Cuboid(playerData.getLocation()).expand(0.5, 0.5, 0.5);
            double yDiff = to.getY() - from.getY();
            int totalTicks = playerData.getTotalTicks();
            this.run(() -> {
                if (cuboid.checkBlocks(world, type -> !MaterialList.INVALID_SHAPE.contains(type))) {
                    ++this.threshold;
                    AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(yDiff), this.threshold);
                } else {
                    this.threshold = 0;
                    this.violations -= Math.min(this.violations + 4.0, 0.05);
                    this.lastBypassTick = totalTicks;
                }
            });
        } else {
            this.threshold = 0;
            this.violations -= Math.min(this.violations + 4.0, 0.05);
        }
    }
}

