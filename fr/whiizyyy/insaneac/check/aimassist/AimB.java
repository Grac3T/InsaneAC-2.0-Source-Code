package fr.whiizyyy.insaneac.check.aimassist;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.check.AimCheck;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;
import fr.whiizyyy.insaneac.util.MathUtil;

public class AimB extends AimCheck {
	 double last, result, previous, vl;
	
    public AimB() {
        super(Check.CheckType.AIM_ASSISTB, "B", "AimAssist", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(ChecksFileManager.getInstance().getBanAimB());
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
    	double pitchChange = Math.abs(from.getPitch() - to.getPitch());

        double gcd = MathUtil.gcd(0x4000, (pitchChange * Math.pow(2.0, 24.0)), (previous * Math.pow(2.0, 24.0)));

        if (Math.min(last, Math.atan(to.getPitch())) == result && gcd < 0x20000 && gcd > 0) {
            if (vl < 15) {
                vl++;
            }
            if (vl > 1) AlertsManager.getInstance().handleViolation(playerData, this, "");
        } else {
            if (vl > 0) {
                vl -= 0.5;
            }
        }

        previous = pitchChange;
        result = Math.min(last, Math.atan(to.getPitch()));
        last = Math.atan(to.getPitch());
    }
}

