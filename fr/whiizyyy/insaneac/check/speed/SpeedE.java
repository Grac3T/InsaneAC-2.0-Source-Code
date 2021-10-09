package fr.whiizyyy.insaneac.check.speed;

import org.bukkit.entity.*;
import org.bukkit.potion.*;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.BukkitUtils;
import fr.whiizyyy.insaneac.util.MathUtil;

public class SpeedE extends MovementCheck
{
    private int threshold;
    
    public SpeedE() {
        super(CheckType.SPEEDE, "E", "Speed", CheckVersion.RELEASE);
        this.violations = -6.0;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Speed E.Alerts"));
        this.threshold = 0;
    }
    
    @Override
    public void handle(final Player player, final PlayerData playerData, final PlayerLocation from, final PlayerLocation to, final long timestamp) {
        if (to.getOnGround() && from.getOnGround()) {
            if (((PlayerData)playerData).getSneaking() != null && ((PlayerData)playerData).getSneaking() && !playerData.hasLag()) {
                final double angle = Math.toDegrees(-Math.atan2(to.getX() - from.getX(), to.getZ() - from.getZ()));
                final double angleDiff = MathUtil.getDistanceBetweenAngles360((float)angle, to.getYaw());
                final double distance = MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
                double limit = (angleDiff > 15.0) ? 0.09165 : 0.0651;
                limit += BukkitUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.02;
                if (player.getWalkSpeed() > 0.2) {
                    limit *= player.getWalkSpeed() / 0.2;
                }
                if (distance > limit) {
                    if (this.threshold++ > 19) {
                        AlertsManager.getInstance().handleViolation(playerData, this, distance - limit + " " + angleDiff);
                        this.threshold = 0;
                    }
                }
                else {
                	this.violations -= Math.min(this.violations + 6.0, 0.05);
                    this.threshold = 0;
                }
            }
            else {
                this.threshold = 0;
            }
        }
    }
}
