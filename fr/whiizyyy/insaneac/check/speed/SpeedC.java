package fr.whiizyyy.insaneac.check.speed;

import org.bukkit.entity.*;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;

public class SpeedC extends MovementCheck
{
	private double vl;
    
    public SpeedC() {
        super(CheckType.SPEEDC, "C", "Speed", CheckVersion.EXPERIMENTAL);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Speed C.Alerts"));
        this.violations = 3.0;
    }
    
    @Override
    public void handle(final Player player, final PlayerData playerData, final PlayerLocation from, final PlayerLocation to, final long timestamp) {
    	int vl = (int) this.vl;
        if (!player.getAllowFlight() && !player.isInsideVehicle() && !playerData.isOnGround()) {
            final double offsetH = Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
            final double offsetY = to.getY() - from.getY();
            if (offsetH > 0.0 && offsetY == 0.0) {
                if (++vl >= 30) {
                    AlertsManager.getInstance().handleViolation(playerData, this, "", offsetH, true);
                }
            }
            else {
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.vl = vl;
    }
}
