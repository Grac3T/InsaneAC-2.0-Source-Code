package fr.whiizyyy.insaneac.check.velocity;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class VelocityC extends PacketCheck {

    public VelocityC() {
        super(Check.CheckType.VELOCITYC, "C", "Velocity", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Velocity C.Alerts"));
        this.violations = -2.0;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            final PlayerLocation lastLastLocation = playerData.getLastLastLocation();
            final PlayerLocation location = playerData.getLocation();
            if (playerData.getVerticalVelocityTicks() > playerData.getMoveTicks() - 1 && playerData.getLastVelY() > 0.0 && playerData.getVerticalVelocityTicks() > playerData.getMaxPingTicks()) {
                if (location.getY() - lastLastLocation.getY() > 0.0) {
                	AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0);
                } else {
                	AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0);
                }
                playerData.setVelocityY(0.0);
            }
        }
    }
}

