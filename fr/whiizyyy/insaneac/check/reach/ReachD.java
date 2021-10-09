package fr.whiizyyy.insaneac.check.reach;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.ReachCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.ReachData;
import fr.whiizyyy.insaneac.manager.AlertsManager;


public class ReachD
extends ReachCheck {
    private static final ThreadLocal<DecimalFormat> REACH_FORMAT = new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("#.###");
        }
    };

    public ReachD() {
        super(Check.CheckType.REACHD, "D", "Reach", Check.CheckVersion.EXPERIMENTAL);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Reach D.Alerts"));
        this.violations = -1.5;
    }

    @Override
    public void handle(Player player, PlayerData playerData, ReachData reachData, long timestamp) {
    	if (!playerData.hasLag()) {
    		double reach = reachData.getReach();
    		double reach2 = reachData.getReach() + 0.0001;
        	DecimalFormat format = REACH_FORMAT.get();
        	double WhiizyyyAllHehe = 2.955;
        	double WhiizyyyMDRALL = 2.9556;
        	if (reach > 2.9 && reach > WhiizyyyAllHehe && reach2 > WhiizyyyMDRALL && playerData.isOnGround()) {
            	AlertsManager.getInstance().handleViolation(playerData, this, format.format(reach));
        	} else {
            	this.violations -= Math.min(this.violations + 1.5, 0.01);
        	}
    	}
    }
}