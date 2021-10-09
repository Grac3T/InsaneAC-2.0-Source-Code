package fr.whiizyyy.insaneac.check.aimassist;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.check.AimCheck;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;


public class AimC extends AimCheck {

	private float suspiciousYaw;
	
    public AimC() {
        super(Check.CheckType.AIM_ASSISTC, "C", "AimAssist", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(ChecksFileManager.getInstance().getBanAimC());
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (System.currentTimeMillis() - playerData.getLastAttackPacket() >= 10000L) {
            return;
        }
        final float diff = Math.abs(to.getYaw() - from.getYaw()) % 360.0f;
        if (diff > 1.0f && Math.round(diff * 10.0f) * 0.1f == diff && Math.round(diff) != diff) {
            if (diff == this.suspiciousYaw) {
            	AlertsManager.getInstance().handleViolation(playerData, this, "");
            }
            this.suspiciousYaw = Math.round(diff * 10.0f) * 0.1f;
        }
        else {
            this.suspiciousYaw = 0.0f;
        }
    }
}

