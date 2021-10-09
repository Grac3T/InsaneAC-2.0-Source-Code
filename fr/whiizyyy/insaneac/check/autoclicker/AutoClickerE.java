package fr.whiizyyy.insaneac.check.autoclicker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
import fr.whiizyyy.insaneac.util.MathUtil;


public class AutoClickerE
extends PacketCheck {
    private int ticks = 0;
    private Queue<Integer> tickList = new ConcurrentLinkedQueue<Integer>();

    public AutoClickerE() {
        super(Check.CheckType.AUTO_CLICKERE, "E", "AutoClicker", Check.CheckVersion.RELEASE);
        this.violations = -2.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker E.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(Player player, PlayerData playerData, Packet packet, long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (playerData.isDigging() && !playerData.isAbortedDigging()) {
                ++this.ticks;
            }
        } else if (packet instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.ticks = 0;
            } else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                BlockPosition blockPosition = packetPlayInBlockDig.a();
                EnumDirection enumDirection = packetPlayInBlockDig.b();
                if (blockPosition.equals((Object)playerData.getDiggingBlock()) && !enumDirection.equals((Object)playerData.getDiggingBlockFace())) {
                    this.tickList.add(this.ticks);
                    if (this.tickList.size() > 40) {
                        double deviation = MathUtil.deviation(this.tickList);
                        if (deviation < 0.325) {
                            AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(deviation), 1.325 - deviation, true);
                        } else {
                        	this.violations -= Math.min(this.violations + 2.0, 0.25);
                        }
                        this.tickList.clear();
                    }
                }
                this.ticks = 0;
            }
        }
    }
}

