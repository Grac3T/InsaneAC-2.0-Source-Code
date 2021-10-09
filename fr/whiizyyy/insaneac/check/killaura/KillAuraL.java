package fr.whiizyyy.insaneac.check.killaura;

import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.MathUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;


public class KillAuraL
extends PacketCheck {
    private Queue<Integer> intervals = new ConcurrentLinkedQueue<Integer>();
    private int ticks = 0;
    private boolean swung = false;
    private boolean place = false;

	 
    public KillAuraL() {
        super(Check.CheckType.KILL_AURAL, "L", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura L.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.swung && !playerData.isSwingDigging() && !this.place && !playerData.hasLag()) {
                if (this.ticks < 8) {
                    this.intervals.add(this.ticks);
                    if (this.intervals.size() >= 40) {
                        double deviation = MathUtil.deviation(this.intervals);
                        this.violations += (0.325 - deviation) * 2.0 + 0.675;
                        if (this.violations < -7.0) {
                            this.violations = -7.0;
                        }
                        if (deviation < 0.325) {
                            AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(deviation), 0.0);
                        }
                        this.intervals.clear();
                    }
                }
                this.ticks = 0;
            }
            this.place = false;
            this.swung = false;
            ++this.ticks;
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.swung = true;
        } else if (packet instanceof PacketPlayInBlockPlace) {
            this.place = true;
        }
    }
}

