package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class KillAuraB
extends PacketCheck {
    private Float lastYaw = null;
    private float yawSpeed = 360.0f;

    public KillAuraB() {
        super(Check.CheckType.KILL_AURAB, "B", "KillAura", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura VB.Alerts"));
        this.violations = -2.0;
    }

    @SuppressWarnings({ "unused", "rawtypes" })
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
            PacketPlayInFlying.PacketPlayInLook packetPlayInLook = (PacketPlayInFlying.PacketPlayInLook)packet;
            if (this.lastYaw != null && timestamp - playerData.getLastPosition() > 3500L) {
                float changeYaw = Math.abs(this.lastYaw.floatValue() - packetPlayInLook.d());
                if (this.yawSpeed < 45.0f && playerData.getTeleportTicks() > 5) {
                    float nearValue;
                    if (changeYaw > 345.0f && changeYaw < 375.0f) {
                        float nearValue2 = Math.abs(360.0f - (changeYaw + Math.abs(180.0f - Math.abs(packetPlayInLook.d() % 180.0f - this.lastYaw.floatValue() % 180.0f))));
                        if ((double)Math.abs(nearValue2) == 0.0) {
                            AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(changeYaw), 1.0, true);
                        }
                    } else if ((double)changeYaw > 172.5 && (double)changeYaw < 187.5 && (double)Math.abs(nearValue = Math.abs(180.0f - (changeYaw + Math.abs(90.0f - Math.abs(packetPlayInLook.d() % 90.0f - this.lastYaw.floatValue() % 90.0f))))) == 0.0) {
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(changeYaw), 0.75, true);
                    }
                    this.violations -= Math.min(this.violations + 2.0, 0.1);
                }
                this.yawSpeed *= 3.0f;
                this.yawSpeed += changeYaw;
                this.yawSpeed /= 4.0f;
            }
            this.lastYaw = Float.valueOf(packetPlayInLook.d());
        }
    }
}

