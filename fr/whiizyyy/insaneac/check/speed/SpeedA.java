package fr.whiizyyy.insaneac.check.speed;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import com.google.common.util.concurrent.AtomicDouble;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.BukkitUtils;
import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.MaterialList;
import fr.whiizyyy.insaneac.util.MathUtil;

public class SpeedA extends MovementCheck {
    private int lastGroundTick = 0;
    private int lastFastAirTick = 0;
    private int lastAirTick = 0;
    private int lastBlockAboveTick = -20;
    private int lastBypassTick = -40;
    private int bypassFallbackTicks = 0;
    private int sprintTicks = 0;

    public SpeedA() {
        super(Check.CheckType.SPEEDA, "A", "Speed", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Speed A.Alerts"));
        this.violations = -10.0;
    }

	@Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (!(to.getX() == from.getX() && to.getZ() == from.getZ() || playerData.getTeleportTicks() <= playerData.getMaxPingTicks() || playerData.getHorizontalSpeedTicks() <= (playerData.getPingTicks() + 10) * 2 || player.getAllowFlight() || playerData.hasLag(timestamp - 50L))) {
            double distance = MathUtil.hypot(to.getX() - from.getX(), to.getZ() - from.getZ());
            AtomicDouble limit = new AtomicDouble(0.0);
            if (to.getOnGround().booleanValue()) {
                if (this.bypassFallbackTicks > 0) {
                    this.bypassFallbackTicks -= 10;
                }
                this.lastGroundTick = playerData.getTotalTicks();
                this.sprintTicks = playerData.getSprinting() != null && !playerData.getSprinting().booleanValue() ? ++this.sprintTicks : 0;
                double angle = Math.toDegrees(- Math.atan2(to.getX() - from.getX(), to.getZ() - from.getZ()));
                double angleDiff = MathUtil.getDistanceBetweenAngles360(angle, to.getYaw());
                boolean straightSprint = angleDiff < 5.0 && angleDiff < 90.0;
                limit.addAndGet((double)BukkitUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.0573);
                limit.addAndGet(this.sprintTicks <= 1 ? (straightSprint ? 0.281 : 0.2865) : (straightSprint ? 0.217 : 0.2325));
                if ((double)player.getWalkSpeed() > 0.2) {
                    limit.set(limit.get() * (double)player.getWalkSpeed() / 0.2);
                }
                if (this.lastAirTick >= this.lastGroundTick - 10) {
                    limit.addAndGet((double)(this.lastGroundTick - this.lastAirTick) * 0.125);
                }
            } else {
                if (this.bypassFallbackTicks > 0) {
                    limit.addAndGet(0.1);
                    --this.bypassFallbackTicks;
                }
                this.lastAirTick = playerData.getTotalTicks();
                boolean fastAir = false;
                if (distance > 0.36 && this.lastFastAirTick < this.lastGroundTick) {
                    this.lastFastAirTick = playerData.getTotalTicks();
                    limit.addAndGet(0.6125);
                    fastAir = true;
                } else {
                    limit.addAndGet(0.36);
                }
                limit.addAndGet((double)BukkitUtils.getPotionLevel(player, PotionEffectType.SPEED) * (fastAir ? 0.0375 : 0.0175));
                if ((double)player.getWalkSpeed() > 0.2) {
                    limit.addAndGet(limit.get() * ((double)player.getWalkSpeed() - 0.2) * 2.0);
                }
            }
            if (distance > limit.get()) {
                World world = player.getWorld();
                PlayerLocation playerLocation = playerData.getLocation();
                Cuboid cuboidAbove = new Cuboid(playerLocation).move(0.0, 2.0, 0.0).expand(0.5, 0.5, 0.5);
                Cuboid cuboid = new Cuboid(playerLocation).move(0.0, -0.5, 0.0).expand(0.5, 1.0, 0.5);
                int totalTicks = playerData.getTotalTicks();
                int sprintTicks = this.sprintTicks;
                playerData.getLastAttackTicks();
                this.run(() -> {
                    boolean bypass;
                    boolean blockAbove;
                    blockAbove = totalTicks - 20 < this.lastBlockAboveTick;
                    if (!blockAbove) {
                        if (!cuboidAbove.checkBlocks(world, type -> type == Material.AIR)) {
                            blockAbove = true;
                            this.lastBlockAboveTick = totalTicks;
                        }
                    }
                    bypass = totalTicks - 40 < this.lastBypassTick;
                    if (!bypass) {
                        if (!cuboid.checkBlocks(world, type -> !MaterialList.ICE.contains((Object)type) && !MaterialList.SLABS.contains((Object)type) && !MaterialList.STAIRS.contains((Object)type) && type != Material.SLIME_BLOCK)) {
                            bypass = true;
                            this.lastBypassTick = totalTicks;
                        }
                    }
                    if (blockAbove) {
                        limit.addAndGet(0.2);
                    }
                    if (bypass) {
                        limit.addAndGet(to.getOnGround() != false ? 0.2 : 0.3);
                        this.bypassFallbackTicks = 60;
                    }
                    if (distance > limit.get()) {
                        AlertsManager.getInstance().handleViolation(playerData, this, "" + (distance - limit.get()) + " " + (to.getOnGround() != false ? "ground" : "air") + " " + "sprint ticks " + sprintTicks, distance - limit.get() + 0.3);
                    } else {
                        this.violations -= Math.min(this.violations + 10.0, 0.025);
                    }
                });
            } else {
                this.violations -= Math.min(this.violations + 10.0, 0.025);
            }
        }
    }
}

