package fr.whiizyyy.insaneac.check.timer;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class TimerB extends PacketCheck {

    private long lastPacket = 0L;
    private int count = 0;

    public TimerB() {
        super(Check.CheckType.TIMERB, "B", "Timer", Check.CheckVersion.RELEASE);
        this.violations = -10.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Timer B.Alerts"));
    }


	@Override
    public void handle(Player player, PlayerData playerData, @SuppressWarnings("rawtypes") Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (playerData.getTeleportTicks() > playerData.getPingTicks() && !playerData.hasLag()) {
                long delay = timestamp - this.lastPacket;
                if (delay > 100L) {
                    if (this.count++ > 6) {
                        this.count = 0;
                        AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0);
                    }
                } else {
                    this.count = 0;
                    this.violations -= Math.min(this.violations + 10.0, 0.001);
                }
                this.lastPacket = timestamp;
            }
        }
    }
}

