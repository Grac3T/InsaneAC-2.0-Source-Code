package fr.whiizyyy.insaneac.check.aimassist;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.check.AimCheck;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;
import fr.whiizyyy.insaneac.util.MathUtil;

public class AimE extends AimCheck {
	 double last, result, previous, vl;
	
    public AimE() {
        super(Check.CheckType.AIM_ASSISTE, "E", "AimAssist", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(ChecksFileManager.getInstance().getBanAimE());
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (System.currentTimeMillis() - playerData.getLastAttackPacket() >= 10000L) {
            return;
        }
        final double diff = MathUtil.getDistanceBetweenAngles(to.getYaw(), from.getYaw());
        if (from.getPitch() == to.getPitch() && diff >= 3.0 && from.getPitch() != 90.0f && to.getPitch() != 90.0f) {
            if ((vl += 0.9) >= 5.0) {
            	AlertsManager.getInstance().handleViolation(playerData, this, "");
            }
        }
        else {
            vl -= 1.6;
        }
    }
}

