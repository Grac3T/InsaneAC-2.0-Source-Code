package fr.whiizyyy.insaneac.check.aimassist;

import java.util.Deque;
import java.util.LinkedList;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.check.AimCheck;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;
import fr.whiizyyy.insaneac.util.MathUtil;

public class AimF extends AimCheck {
	
	 double last, result, previous, vl;
	 
	    private final Deque<Double> notOverSpeeds;
	    private final Deque<Double> overSpeeds;
	
    public AimF() {
        super(Check.CheckType.AIM_ASSISTF, "F", "AimAssist", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.setMaxViolation(ChecksFileManager.getInstance().getBanAimF());
        this.notOverSpeeds = new LinkedList<Double>();
        this.overSpeeds = new LinkedList<Double>();
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (from.getYaw() != to.getYaw()) {
            final double speed = Math.hypot(from.getYaw(), to.getYaw());
            if (MathUtil.isMouseOverEntity(player)) {
                this.overSpeeds.addLast(speed);
            }
            else {
                this.notOverSpeeds.addLast(speed);
            }
            final int total = this.overSpeeds.size() + this.notOverSpeeds.size();
            if (total == 1000) {
                double averageNotOver = 0.0;
                for (final double d : this.notOverSpeeds) {
                    averageNotOver += d;
                }
                averageNotOver /= this.notOverSpeeds.size();
                double averageOver = 0.0;
                for (final double d2 : this.overSpeeds) {
                    averageOver += d2;
                }
                averageOver /= this.overSpeeds.size();
                if (averageNotOver > averageOver) {
                    vl += 1.25;
                    if (vl > 5.0) {
                    	AlertsManager.getInstance().handleViolation(playerData, this, "");
                    }
                }
                else {
                    vl -= 0.4;
                }
                this.notOverSpeeds.clear();
                this.overSpeeds.clear();
            }
        }
    }
}