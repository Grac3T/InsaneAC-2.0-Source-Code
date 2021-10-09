package fr.whiizyyy.insaneac.check.autoclicker;

import com.google.common.collect.Queues;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;

import java.util.Queue;

import org.bukkit.entity.*;

import net.minecraft.server.v1_8_R3.*;

public class AutoClickerJ extends PacketCheck
{
    private long abort;
    private boolean abort1;
    private Queue<Long> queue;
    
    public AutoClickerJ() {
        super(CheckType.AUTO_CLICKERJ, "J", "AutoClicker", CheckVersion.RELEASE);
        this.queue = Queues.newConcurrentLinkedQueue();
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker J.Alerts"));
        this.violations = -10.0;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                this.abort = timestamp;
                this.abort1 = true;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            if (this.abort1) {
                if (this.queue.size() == 20) {
                    final double devi = MathUtil.deviation(this.queue);
                    if (devi < 3.8) {
                        this.violations += 1.0 + devi;
                        AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(devi));
                    }
                    this.queue.poll();
                }
                this.queue.add(timestamp - this.abort);
            }
            this.abort1 = false;
        }
    }
}
