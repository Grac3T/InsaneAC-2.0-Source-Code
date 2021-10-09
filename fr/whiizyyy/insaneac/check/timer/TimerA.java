package fr.whiizyyy.insaneac.check.timer;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;

public class TimerA extends PacketCheck {
    private static final long CHECK_TIME_LINEAR = TimerA.toNanos(45L);
    private Long lastPacket = null;
    private long offset = -100L;

    public TimerA() {
        super(Check.CheckType.TIMERA, "A", "Timer", Check.CheckVersion.RELEASE);
        this.violations = -5.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Timer A.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            final PlayerLocation playerLocation = playerData.getLocation(playerData.getPingTicks() + 1);
            if (playerLocation == null) {
            	return;
            }
            long now = System.nanoTime();
            if (this.lastPacket != null) {
                long diff = now - this.lastPacket;
                this.offset += TimerA.toNanos(50L) - diff;
                if (this.offset <= CHECK_TIME_LINEAR) {
                    this.violations -= Math.min(this.violations + 5.0, 0.005);
                } else {
                	if (playerData.getTotalTicks() > 100 && playerData.getTeleportTicks() > playerData.getMaxPingTicks() * 2 && playerData.isSpawnedIn() && (playerData.getSteerTicks() == 0 || playerData.getSteerTicks() > playerData.getPingTicks())) {
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(TimerA.fromNanos(this.offset)));
                    }
                    this.offset = 0L;
                }
            }
            this.lastPacket = now;
        } else if (packet instanceof PacketPlayOutPosition) {
        	this.offset = -100L;
        }
    }

    public static long toNanos(long number) {
        return TimeUnit.MILLISECONDS.toNanos(number);
    }

    public static long fromNanos(long number) {
        return TimeUnit.NANOSECONDS.toMillis(number);
    }
}

