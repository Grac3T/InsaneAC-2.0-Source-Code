package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class KillAuraO
extends PacketCheck {
    private Long lastFlying = null;

    public KillAuraO() {
        super(Check.CheckType.KILL_AURAO, "O", "KillAura", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura O.Alerts"));
        this.violations = -2.0;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.lastFlying != null) {
                if (timestamp - this.lastFlying > 40L && timestamp - this.lastFlying < 800L) {
                    AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf((timestamp - this.lastFlying) * 2L), 0.25);
                } else {
                    this.violations -= Math.min(this.violations + 2.0, 0.1);
                }
                this.lastFlying = null;
            }
        } else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            long lastFlying = playerData.getLastFlying();
            if (timestamp - lastFlying < 10L) {
                this.lastFlying = lastFlying;
            } else {
                this.violations -= Math.min(this.violations + 2.0, 0.1);
            }
        }
    }
}

