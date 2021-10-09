package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;


public class KillAuraJ
extends PacketCheck {
    private boolean sent;
    private boolean failed;
    private int movements;
	private double vl;

    public KillAuraJ() {
        super(Check.CheckType.KILL_AURAJ, "J", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura J.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
    	 if (playerData.isDigging()) {
    		 int vl = (int) this.vl;
    		 if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
    			 this.movements = 0;
    			 vl = 0;
    		 }
    		 else if (packet instanceof PacketPlayInArmAnimation && this.movements >= 2) {
    			 if (this.sent) {
    				 if (!this.failed) {
    					 if (++vl >= 15) {
    						 AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0, true);
    					 }
    					 this.failed = true;
    				 }
    			 }
    			 else {
    				 this.sent = true;
    			 }
    		 }
    		 else if (packet instanceof PacketPlayInFlying) {
    			 this.failed = false;
    			 this.sent = false;
    			 ++this.movements;
    		 }
    		 this.vl = vl;
    	 }
    }
}

