package fr.whiizyyy.insaneac.check.velocity;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class VelocityA extends PacketCheck {
    private int ticks = 0;

    public VelocityA() {
        super(Check.CheckType.VELOCITYA, "A", "Velocity", Check.CheckVersion.RELEASE);
        this.ticks = 0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Velocity A.Alerts"));
        this.violations = -2.0;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            final PlayerLocation from = playerData.getLastLastLocation();
            final PlayerLocation to = playerData.getLocation();
            if (playerData.getVerticalVelocityTicks() > playerData.getMoveTicks() && playerData.getLastVelY() > 0.0) {
                double y = to.getY() - from.getY();
                if (y > 0.0) {
                    final double percentage;
                    if ((y = Math.ceil(y * 8000.0) / 8000.0) < 0.41999998688697815 && playerData.getLastLastLocation().getOnGround() && from.getOnGround() && !to.getOnGround() && MathUtil.onGround(from.getY()) && !MathUtil.onGround(to.getY()) && (percentage = y / playerData.getLastVelY()) < 0.995) {
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(percentage), 1.0);
                    }
                    playerData.setLastVelY(0.0);
                    this.ticks = 0;
                }
                else if (!playerData.hasLag(timestamp - 100L) && !playerData.hasFast(timestamp - 100L) && this.ticks++ > playerData.getMaxPingTicks() * 2 + 1) {
                    playerData.setLastVelY(0.0);
                    this.ticks = 0;
                    AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(100), 1.0);
                }
                else {
                    this.violations -= Math.min(this.violations + 2.0, 0.1);
                }
            }
        }
    }
}

