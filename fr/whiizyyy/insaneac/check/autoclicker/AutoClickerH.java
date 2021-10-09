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

public class AutoClickerH extends PacketCheck
{
    private Integer releaseTime;
    private Queue<Integer> clickQueue;
    private int start;
    
    public AutoClickerH() {
        super(CheckType.AUTO_CLICKERI, "H", "AutoHClicker", CheckVersion.RELEASE);
        this.releaseTime = null;
        this.clickQueue = new ConcurrentLinkedQueue<Integer>();
        this.start = 0;
        this.violations = -7.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker H.Alerts"));
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (playerData.isDigging()) {
                ++this.start;
            }
            if (this.releaseTime != null) {
                ++this.releaseTime;
            }
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                this.releaseTime = 0;
            }
            else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK && this.releaseTime != null && this.releaseTime < 4 && this.releaseTime > 0) {
                this.clickQueue.add(this.releaseTime);
                if (this.clickQueue.size() > 10) {
                    final double value = MathUtil.variance(1, this.clickQueue) / this.start;
                    if (value > 0.2) {
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(value), 1.0, true);
                    }
                    else {
                        this.violations -= Math.min(this.violations + 7.0, 0.1);
                    }
                    this.clickQueue.clear();
                    this.start = 0;
                }
                this.releaseTime = null;
            }
        }
    }
}
