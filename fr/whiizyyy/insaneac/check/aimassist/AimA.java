package fr.whiizyyy.insaneac.check.aimassist;

import org.bukkit.entity.Player;


import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;


public class AimA extends PacketCheck  {

    public AimA() {
        super(Check.CheckType.AIM_ASSISTA, "A", "AimAssist", Check.CheckVersion.RELEASE);
        this.violations = -6.0;
        this.setMaxViolation(ChecksFileManager.getInstance().getBanAimA());
    }

    boolean lastYaw, lastPitch;
    double lastPitchAbs, lastYawAbs, vl;
    
	@SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
		if (packet instanceof PacketPlayInFlying) {
            if (playerData.getLocation().getYaw() == playerData.getLastLastLocation().getYaw() && lastYaw == false && lastYawAbs > 1 || playerData.getLocation().getPitch() == playerData.getLastLastLocation().getPitch() && lastPitch == false && lastPitchAbs > 1) {
                if (vl < 30) vl++;
                if (vl > 5) AlertsManager.getInstance().handleViolation(playerData, this, "");
            } else {
                violations -= Math.min(violations + 6.0, 0.05);
                if (vl > 0) {
                    vl -= 0.25;
                }
            }
            lastYaw = (playerData.getLocation().getYaw() == playerData.getLastLastLocation().getYaw());
            lastPitch = (playerData.getLocation().getPitch() == playerData.getLastLastLocation().getPitch());
            lastPitchAbs = Math.abs(playerData.getLocation().getPitch() - playerData.getLastLastLocation().getPitch());
            lastYawAbs = Math.abs(playerData.getLocation().getYaw() - playerData.getLastLastLocation().getYaw());
        }
    }
}

