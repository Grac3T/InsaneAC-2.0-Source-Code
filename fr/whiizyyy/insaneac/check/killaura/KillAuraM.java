package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;


public class KillAuraM
extends PacketCheck {
	public KillAuraM() {
        super(Check.CheckType.KILL_AURAM, "M", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura M.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
    }
}

