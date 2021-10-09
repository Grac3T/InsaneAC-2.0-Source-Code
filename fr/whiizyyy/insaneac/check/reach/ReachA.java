package fr.whiizyyy.insaneac.check.reach;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.ReachCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.ReachData;
import fr.whiizyyy.insaneac.manager.AlertsManager;


public class ReachA
extends ReachCheck {
    private static final ThreadLocal<DecimalFormat> REACH_FORMAT = new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("#.###");
        }
    };

    public ReachA() {
        super(Check.CheckType.REACHA, "A", "Reach", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Reach A.Alerts"));
        this.violations = -1.5;
    }

    @Override
    public void handle(Player player, PlayerData playerData, ReachData reachData, long timestamp) {
    	if (!playerData.hasLag()) {
    		double reach = reachData.getReach();
        	double extra = reachData.getExtra();
        	double vertical = reachData.getVertical();
        	double movement = reachData.getMovement();
        	double horizontal = reachData.getHorizontal();
        	DecimalFormat format = REACH_FORMAT.get();
        	if (reach > 3.0) {
            	AlertsManager.getInstance().handleViolation(playerData, this, format.format(reach) + " " + format.format(horizontal) + " " + format.format(vertical) + " " + format.format(extra) + " " + format.format(movement), reach - (movement > 0.0 ? 2.5 : 2.25));
        	} else {
            	this.violations -= Math.min(this.violations + 3.0, 0.01);
        	}
    	}
    }
}

