package fr.whiizyyy.insaneac.check.autoclicker;

import net.minecraft.server.v1_8_R3.BlockPosition;

import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;


public class AutoClickerF
extends PacketCheck {
    private int ticks = 0;
    private int abortTicks = 0;
    private int lastticks = -1;
    private int combo = 0;
    private double value = 0.0;

    public AutoClickerF() {
        super(Check.CheckType.AUTO_CLICKERF, "F", "AutoClicker", Check.CheckVersion.RELEASE);
        this.violations = -15.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker F.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
    	if (playerData.hasLag()) {
    		return;
    	}
        if (packet instanceof PacketPlayInFlying) {
            if (playerData.isDigging() && !playerData.isAbortedDigging()) {
                ++this.ticks;
            }
            ++this.abortTicks;
        } else if (packet instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.ticks = 0;
            } else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                BlockPosition blockPosition = packetPlayInBlockDig.a();
                EnumDirection enumDirection = packetPlayInBlockDig.b();
                if (blockPosition.equals((Object)playerData.getDiggingBlock()) && !enumDirection.equals((Object)playerData.getDiggingBlockFace())) {
                    if (this.ticks == this.lastticks && this.abortTicks < 4) {
                        this.value += 0.3 + (double)this.combo * 0.2;
                        if (this.value > 30.0) {
                            AlertsManager.getInstance().handleViolation(playerData, this, "");
                            this.value = 0.0;
                        } else {
                        	this.violations -= Math.min(this.violations + 15.0, 0.1);
                        }
                        ++this.combo;
                    } else {
                        this.value -= Math.min(this.value, 1.0);
                        this.combo = 0;
                    }
                    this.lastticks = this.ticks;
                }
                this.abortTicks = 0;
                this.ticks = 0;
            }
        }
    }
}

