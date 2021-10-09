package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;

public class KillAuraE
extends MovementCheck  {
	double lastResult, previous;
    int vl;


    public KillAuraE() {
        super(Check.CheckType.KILL_AURAE, "E", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura E.Alerts"));
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
    	double yaw = playerData.getLocation().getYaw();
        double lastYaw = playerData.getLastLastLocation().getYaw();
        double pitch = playerData.getLocation().getPitch();
        double lastPitch = playerData.getLastLastLocation().getPitch();
        double result = Math.abs(Math.min(yaw, lastYaw) - Math.min(pitch, lastPitch));
        double pitchChange = Math.abs(from.getPitch() - to.getPitch());

        double gcd = MathUtil.gcd(0x4000, (pitchChange * Math.pow(2.0, 24.0)), (previous * Math.pow(2.0, 24.0)));

        if (result <= 175 && lastResult != result && gcd < 0x20000 && gcd > 0 && playerData.getLastAttackTicks() <= 3 && playerData.getLastAttacked() != null) {
            if (vl < 20) ++vl;
            if (vl > 17) AlertsManager.getInstance().handleViolation(playerData, this, "" + result + " < 175 VL: " + vl);

        }
        else if (vl > 1) {
            vl -= 5;
            violations -= Math.min(violations + 1.0, 0.05);
        }

        lastResult = result;
        previous = pitchChange;

    }
}

