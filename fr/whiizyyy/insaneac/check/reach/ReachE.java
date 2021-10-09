package fr.whiizyyy.insaneac.check.reach;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.ReachCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.data.ReachData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;

public class ReachE extends ReachCheck {
    private static final ThreadLocal<DecimalFormat> REACH_FORMAT = new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("#.###");
        }
    };

    public ReachE() {
        super(Check.CheckType.REACHE, "E", "Reach", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Reach E.Alerts"));
        this.violations = -1.0;
    }

    @Override
    public void handle(Player player, PlayerData playerData, ReachData reachData, long timestamp) {
    	double reach = reachData.getReach();
        double extra = reachData.getExtra();
        double vertical = reachData.getVertical();
        double movement = reachData.getMovement();
        double horizontal = reachData.getHorizontal();
        DecimalFormat format = REACH_FORMAT.get();
        final PlayerLocation playerLocation = playerData.getLocation(playerData.getPingTicks() + 1);
        if (playerLocation == null) {
        	return;
        }
        double threshold = 3.0;
        if (playerData.getSprinting() == null || !playerData.getSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), playerLocation.getYaw()) < 90.0) {
           threshold += 0.5;
        }
        if (reach > 6.0) {
        	return;
        }
        if (reach > threshold) {
        	AlertsManager.getInstance().handleViolation(playerData, this, format.format(reach) + " " + format.format(horizontal) + " " + format.format(vertical) + " " + format.format(extra) + " " + format.format(movement), reach - (movement > 0.0 ? 2.5 : 2.25));
        } else {
        	this.violations -= Math.min(this.violations + 1.0, 0.01);
    	}
    }
}

