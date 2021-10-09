package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.ReachCheck;
import fr.whiizyyy.insaneac.data.DistanceData;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.ReachData;
import fr.whiizyyy.insaneac.manager.AlertsManager;


public class KillAuraD
extends ReachCheck {
    private int threshhold = 0;

    public KillAuraD() {
        super(Check.CheckType.KILL_AURAD, "D", "KillAura", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura D.Alerts"));
        this.violations = -3.0;
    }

    @Override
    public void handle(Player player, PlayerData playerData, ReachData reachData, long timestamp) {
        DistanceData distanceData = reachData.getDistanceData();
        double hitboxX = distanceData.getX() / distanceData.getDist() * reachData.getExtra();
        double hitboxZ = distanceData.getZ() / distanceData.getDist() * reachData.getExtra();
        if (!playerData.hasLag() && Math.max(Math.abs(hitboxX), Math.abs(hitboxZ)) > 0.55) {
            if (this.threshhold++ > 2 + playerData.getPingTicks() / 3) {
                AlertsManager.getInstance().handleViolation(playerData, this, "" + hitboxX + " " + hitboxZ + " " + reachData.getExtra());
            }
        } else {
        	this.violations -= Math.min(this.violations + 3.0, 0.1);
            this.threshhold = 0;
        }
    }
}

