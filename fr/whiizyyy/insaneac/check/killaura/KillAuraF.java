package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class KillAuraF
extends PacketCheck {
    private boolean place;

    public KillAuraF() {
        super(Check.CheckType.KILL_AURAF, "F", "KillAura", Check.CheckVersion.EXPERIMENTAL);
        this.violations = -1.0;
        this.place = false;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura F.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            this.place = false;
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            this.place = true;
        }
        else if (packet instanceof PacketPlayInUseEntity && this.place && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0, true);
        }
    }
}

