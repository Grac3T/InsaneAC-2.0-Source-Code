package fr.whiizyyy.insaneac.check.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;

public class AutoClickerC
extends PacketCheck {
	private boolean sent;
    private double vl;

    public AutoClickerC() {
        super(Check.CheckType.AUTO_CLICKERC, "C", "AutoClicker", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker C.Alerts"));
        this.violations = -4.0;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
    	if (packet instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig) packet).c();

            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.sent = true;
            } else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                int vl = (int) this.vl;

                if (this.sent && !playerData.hasLag()) {
                    if (++vl >= 10) {
                    	vl = 0;
                    	AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(vl), 0.75, true);
                    }
                } else {
                	this.violations -= Math.min(this.violations + 4.0, 0.1);
                    vl = 0;
                }
                this.vl = vl;
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.sent = false;
        }
    }
}

