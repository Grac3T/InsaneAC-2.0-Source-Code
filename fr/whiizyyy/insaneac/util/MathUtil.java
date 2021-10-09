package fr.whiizyyy.insaneac.util;

import com.google.common.base.Objects;

import fr.whiizyyy.insaneac.data.PlayerLocation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.MathHelper;

public class MathUtil {
    public static double lowest(Iterable<? extends Number> numbers) {
        Double lowest = null;
        Iterator<? extends Number> var2 = numbers.iterator();
        while (var2.hasNext()) {
            Number number = var2.next();
            if (lowest != null && number.doubleValue() >= lowest) continue;
            lowest = number.doubleValue();
        }
        return (Double)Objects.firstNonNull((Object)lowest, (Object)0.0);
    }

    public static int getPing(Player p) {
            return ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) p).getHandle().ping;
    }
    
    public static boolean isMouseOverEntity(final Player player) {
        return rayTrace(player, 6.0) != null;
    }
    
    public static boolean blocksNear(final Player player) {
        return blocksNear(player.getLocation());
    }

    public static ArrayList<Block> getSurrounding(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static boolean blocksNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block block2 : getSurrounding(loc.getBlock(), true)) {
            if (block2.getType() == Material.AIR) {
                continue;
            }
            nearBlocks = true;
            break;
        }
        for (final Block block2 : getSurrounding(loc.getBlock(), false)) {
            if (block2.getType() == Material.AIR) {
                continue;
            }
            nearBlocks = true;
            break;
        }
        loc.setY(loc.getY() - 0.5);
        if (loc.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }
    
    static boolean isBlock(final Block block, final Material[] materials) {
        final Material type = block.getType();
        for (final Material m : materials) {
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }
    
    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }
    
    public static /* varargs */ double highest(Number ... numbers) {
        return MathUtil.highest(Arrays.asList(numbers));
    }

    public static int pingFormula(final long ping) {
        return (int)Math.ceil(ping / 50.0);
    }
    
    public static long nowlong() {
        return System.currentTimeMillis();
    }
    
    public static boolean isInWater(final Player player) {
        final Material m = player.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER;
    }
    
    public static boolean isInWeb(final Player player) {
        final Material m = player.getLocation().getBlock().getType();
        return m == Material.WEB;
    }
    
    public static CustomLocation getLocationInFrontOfPlayer(final Player player, final double distance) {
        return new CustomLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    }
    
    public static CustomLocation getLocationInFrontOfLocation(final double x, final double y, final double z, final float yaw, final float pitch, final double distance) {
        return new CustomLocation(0.0, 0.0, 0.0, 0.0f, 0.0f);
    }
    
    public static Entity rayTrace(final Player player, final double distance) {
        final CustomLocation playerLocation = CustomLocation.fromBukkitLocation(player.getLocation());
        Entity currentTarget = null;
        float lowestFov = Float.MAX_VALUE;
        for (final org.bukkit.entity.Entity entity : player.getNearbyEntities(distance, distance, distance)) {
            final CustomLocation entityLocation = CustomLocation.fromBukkitLocation(entity.getLocation());
            final float fov = getRotationFromPosition1(playerLocation, entityLocation)[0] - playerLocation.getYaw();
            final double groundDistance = playerLocation.getGroundDistanceTo(entityLocation);
            if (lowestFov < fov && fov < groundDistance + 2.0) {
                currentTarget = (Entity) entity;
                lowestFov = fov;
            }
        }
        return currentTarget;
    }
    
    public static float[] getRotationFromPosition1(final CustomLocation playerLocation, final CustomLocation targetLocation) {
        final double xDiff = targetLocation.getX() - playerLocation.getX();
        final double zDiff = targetLocation.getZ() - playerLocation.getZ();
        final double yDiff = targetLocation.getY() - (playerLocation.getY() + 0.4);
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static double highest(Iterable<? extends Number> numbers) {
        Double highest = null;
        Iterator<? extends Number> var2 = numbers.iterator();
        while (var2.hasNext()) {
            Number number = var2.next();
            if (highest != null && number.doubleValue() <= highest) continue;
            highest = number.doubleValue();
        }
        return (Double)Objects.firstNonNull((Object)highest, (Object)0.0);
    }

    public static double total(Iterable<? extends Number> numbers) {
        double total = 0.0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    public static double totalAbs(Iterable<? extends Number> numbers) {
        double total = 0.0;
        for (Number number : numbers) {
            total += Math.abs(number.doubleValue());
        }
        return total;
    }

    public static double average(Iterable<? extends Number> numbers) {
        double total = 0.0;
        int i = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
            ++i;
        }
        return total / (double)i;
    }

    public static double deviationSquared(Iterable<? extends Number> numbers) {
        double total = 0.0;
        int i = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
            ++i;
        }
        double average = total / (double)i;
        double deviation = 0.0;
        for (Number number : numbers) {
            deviation += Math.pow(number.doubleValue() - average, 2.0);
        }
        return deviation / (double)(i - 1);
    }

    public static double deviation(Iterable<? extends Number> numbers) {
        return Math.sqrt(MathUtil.deviationSquared(numbers));
    }

    public static double varianceSquared(Number value, Iterable<? extends Number> numbers) {
        double variance = 0.0;
        int i = 0;
        for (Number number : numbers) {
            variance += Math.pow(number.doubleValue() - value.doubleValue(), 2.0);
            ++i;
        }
        return variance / (double)(i - 1);
    }

    public static double variance(Number value, Iterable<? extends Number> numbers) {
        return Math.sqrt(MathUtil.varianceSquared(value, numbers));
    }

    public static /* varargs */ double hypot(double ... values) {
        return Math.sqrt(MathUtil.hypotSquared(values));
    }

    public static /* varargs */ double hypotSquared(double ... values) {
        double total = 0.0;
        double[] var3 = values;
        int var4 = values.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            double value = var3[var5];
            total += Math.pow(value, 2.0);
        }
        return total;
    }

    public static /* varargs */ Double getMinimumAngle(PlayerLocation playerLocation, PlayerLocation ... targetLocations) {
        Double angle = null;
        PlayerLocation[] var3 = targetLocations;
        int var4 = targetLocations.length;
        for (int var5 = 0; var5 < var4; ++var5) {
            PlayerLocation targetLocation = var3[var5];
            double xDiff = targetLocation.getX() - playerLocation.getX();
            double zDiff = targetLocation.getZ() - playerLocation.getZ();
            float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
            double yawDiff = MathUtil.getDistanceBetweenAngles360(playerLocation.getYaw(), yaw);
            if (angle != null && angle <= yawDiff) continue;
            angle = yawDiff;
        }
        return angle;
    }

    public static float[] getRotationFromPosition(PlayerLocation playerLocation, PlayerLocation targetLocation) {
        double xDiff = targetLocation.getX() - playerLocation.getX();
        double zDiff = targetLocation.getZ() - playerLocation.getZ();
        double yDiff = targetLocation.getY() + 0.81 - playerLocation.getY() - 1.2;
        float dist = (float)Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static double relEntityRoundPos(double d) {
        return (double)MathHelper.floor((double)(d * 32.0)) / 32.0;
    }

    public static float relEntityRoundLook(float d) {
        return MathHelper.d((float)(d * 256.0f / 360.0f));
    }

    public static double getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    public static double getDistanceBetweenAngles360(double angle1, double angle2) {
        double distance = Math.abs(angle1 % 360.0 - angle2 % 360.0);
        distance = Math.min(360.0 - distance, distance);
        return Math.abs(distance);
    }

    public static int toInt(float number) {
        return (int)((double)new BigDecimal(number).setScale(5, RoundingMode.UP).floatValue() * 10000.0);
    }

    public static int gcd(long limit, int a, int b) {
        return (long)b <= limit ? a : MathUtil.gcd(limit, b, a % b);
    }

    public static int gcd(int number1, int number2) {
        return BigInteger.valueOf(number1).gcd(BigInteger.valueOf(number2)).intValue();
    }

    public static boolean onGround(double y) {
        return y % 0.015625 == 0.0;
    }

    public static double getFraction(final double n) {
        return n % 1.0;
    
	}
    
    public static double gcd(double limit, double a, double b) {
        return b <= limit ? a : MathUtil.gcd(limit, b, a % b);
    }
}

