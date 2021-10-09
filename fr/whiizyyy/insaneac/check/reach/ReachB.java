package fr.whiizyyy.insaneac.check.reach;

import org.bukkit.GameMode;

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
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class ReachB
        extends PacketCheck {
    boolean sameTick;

    public ReachB() {
        super(Check.CheckType.REACHB, "B", "Reach", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Reach B.Alerts"));
        this.violations = -3.0;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInUseEntity && !player.getGameMode().equals((Object) GameMode.CREATIVE) && !playerData.hasLag() && !playerData.hasFast() && playerData.getLastAttacked() != null && !this.sameTick) {
            final PlayerLocation playerLocation = playerData.getLocation();
            final PlayerData targetData = playerData.getLastAttacked();
            if (targetData == null) {
                return;
            }
            final PlayerLocation targetLocation = targetData.getLocation(targetData.getPingTicks() + 1);
            if (targetLocation == null) {
                return;
            }
            final double range = Math.hypot(playerLocation.getX() - targetLocation.getX(), playerLocation.getZ() - targetLocation.getZ());
            if (range > 6.0) {
                return;
            }
            double threshold = 3.4;
            if (targetData.getSprinting() == null || !targetData.getSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), targetLocation.getYaw()) < 90.0) {
                threshold += 0.5;
            }
            if (playerData.getPing() > 100 || player.getMaximumNoDamageTicks() <= 13) {
            	return;
            }
            if (range > threshold) {
            	AlertsManager.getInstance().handleViolation(playerData, this, "range: " + range);
            } else {
            	this.violations -= Math.min(this.violations + 10.0, 0.1);
            }
            this.sameTick = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sameTick = false;
        }
    }
}