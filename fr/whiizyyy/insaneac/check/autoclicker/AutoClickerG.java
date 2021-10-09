package fr.whiizyyy.insaneac.check.autoclicker;

import java.util.*;

import java.util.concurrent.*;
import org.bukkit.entity.*;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;

public class AutoClickerG extends PacketCheck
{
    private boolean swing;
    private int ticksSinceBreak;
    private int ticksSinceArm;
    private Queue<Integer> swings;
    
    public AutoClickerG() {
        super(CheckType.AUTO_CLICKERG, "G", "AutoClicker", CheckVersion.RELEASE);
        this.swing = false;
        this.ticksSinceBreak = 100;
        this.ticksSinceArm = 10;
        this.swings = new ConcurrentLinkedQueue<Integer>();
        this.violations = -15.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker G.Alerts"));
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying && !playerData.hasLag()) {
            if (this.swing && !playerData.isSwingDigging()) {
                this.swings.add(this.ticksSinceArm);
                if (this.swings.size() > 9) {
                    this.swings.poll();
                }
                this.ticksSinceArm = 0;
            }
            this.swing = false;
            ++this.ticksSinceBreak;
            ++this.ticksSinceArm;
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            this.swing = true;
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (!playerData.hasLag() && packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK && player.getGameMode() == GameMode.SURVIVAL) {
                final double cps = this.getCPS();
                if (this.ticksSinceBreak > 4 && this.ticksSinceArm <= 2 && cps > 7.0) {
                    if (this.swing) {
                        this.violations -= Math.min(this.violations + 15.0, 0.1);
                    }
                    else {
                        AlertsManager.getInstance().handleViolation(playerData, this, this.ticksSinceBreak + " " + this.ticksSinceArm + " " + cps, 1.0, true);
                    }
                }
                this.ticksSinceBreak = 0;
            }
            else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                this.ticksSinceBreak = 0;
            }
        }
    }
    
    public double getCPS() {
        return 20.0 / MathUtil.average(this.swings);
    }
}
