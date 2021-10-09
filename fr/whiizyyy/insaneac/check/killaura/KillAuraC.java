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

public class KillAuraC
extends PacketCheck {
    private int ticks = 0;
    private Integer lastTicks = null;
    private int invalidTicks = 0;
    private int totalTicks = 0;

    public KillAuraC() {
        super(Check.CheckType.KILL_AURAC, "C", "KillAura", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura C.Alerts"));
        this.violations = -5.0;
    }

    @SuppressWarnings({ "rawtypes"})
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            ++this.ticks;
        } else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            if (this.ticks <= 8) {
                if (this.lastTicks != null) {
                    if (this.lastTicks == this.ticks) {
                        ++this.invalidTicks;
                    }
                    ++this.totalTicks;
                    if (this.totalTicks >= 25) {
                        if (this.invalidTicks > 22) {
                            AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(this.invalidTicks), 1.0 + (double)(this.invalidTicks - 22) / 6.0);
                        } else {
                            this.violations -= Math.min(this.violations + 5.0, 1.0);
                        }
                        this.invalidTicks = 0;
                        this.totalTicks = 0;
                    }
                }
                this.lastTicks = this.ticks;
            }
            this.ticks = 0;
        }
    }
}

