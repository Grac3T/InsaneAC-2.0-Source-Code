package fr.whiizyyy.insaneac.check.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;


public class AutoClickerD
extends PacketCheck {
	
	    private int swings;
	    private int movements;
	    private long lastSwing;

    public AutoClickerD() {
        super(Check.CheckType.AUTO_CLICKERD, "D", "AutoClicker", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker D.Alerts"));
        this.violations = -1.0;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInArmAnimation && playerData.getTeleportTicks() > playerData.getMaxPingTicks() * 2 + 5 && !playerData.isDigging() && playerData.isBlocking()) {
            ++this.swings;
            this.lastSwing = System.currentTimeMillis();
        }
        else if (packet instanceof PacketPlayInFlying && this.swings > 0) {
            ++this.movements;
            if (this.movements == 20) {
                if (this.swings > 20) {
                	AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(this.swings), 0.0);
                }
                if (System.currentTimeMillis() - this.lastSwing <= 350L) {
                    playerData.setCps(this.swings);
                }
                this.movements = 0;
                this.swings = 0;
            }
        }
    }
}

