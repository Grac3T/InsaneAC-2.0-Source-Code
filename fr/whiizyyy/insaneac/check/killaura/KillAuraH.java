package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;


public class KillAuraH
extends MovementCheck {
    public static double NON_SPRINT;
    public static double SPEED;
    static {
        KillAuraH.NON_SPRINT = 0.2325;
        KillAuraH.SPEED = 0.02;
    }

    public KillAuraH() {
        super(Check.CheckType.KILL_AURAH, "H", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -6.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura H.Alerts"));
    }

    @Override
    public void handle(final Player player, final PlayerData playerData, final PlayerLocation from, final PlayerLocation to, final long timestamp) {
    }
}

