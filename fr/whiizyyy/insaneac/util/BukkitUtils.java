package fr.whiizyyy.insaneac.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtils {
	
	
	    public static String getFromFile(final String fileName) {
	        try {
	            final StringBuilder getHotKey = new StringBuilder();
	            final FileReader getFile = new FileReader(fileName);
	            final BufferedReader bufferReader = new BufferedReader(getFile);
	            String line;
	            while ((line = bufferReader.readLine()) != null) {
	                getHotKey.append(line);
	            }
	            bufferReader.close();
	            return getHotKey.toString();
	        }
	        catch (Exception e) {
	            return "-1";
	        }
	    }
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public static int getPotionLevel(Player player, PotionEffectType type) {
        PotionEffect potionEffect;
        ArrayList potionEffectList = new ArrayList(player.getActivePotionEffects());
        Iterator var3 = potionEffectList.iterator();
        do {
            if (var3.hasNext()) continue;
            return 0;
        } while ((potionEffect = (PotionEffect)var3.next()).getType().getId() != type.getId());
        return potionEffect.getAmplifier() + 1;
    }
    
    public static double getDirection(final Location from, final Location to) {
        if (from == null || to == null) {
            return 0.0;
        }
        final double difX = to.getX() - from.getX();
        final double difZ = to.getZ() - from.getZ();
        return wrapAngleTo180_float((float)(Math.atan2(difZ, difX) * 180.0 / 3.141592653589793) - 90.0f);
    }
    
    public static double getDistance(final double p1, final double p2, final double p3, final double p4) {
        final double delta1 = p3 - p1;
        final double delta2 = p4 - p2;
        return Math.sqrt(delta1 * delta1 + delta2 * delta2);
    }
    
    public static float wrapAngleTo180_float(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    public static void log(final Player player, final String log) {
        final String fileName = "plugins/InsaneAC/data/" + player.getName().toLowerCase() + ".log";
        final File file = new File(fileName.replace(fileName.split("/")[fileName.split("/").length - 1], ""));
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            Throwable t = null;
            try {
                final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("plugins/InsaneAC/data/" + player.getName().toLowerCase() + ".log", true)));
                try {
                    pw.println(log);
                }
                finally {
                    if (pw != null) {
                        pw.close();
                    }
                }
                if (pw != null) {
                    pw.close();
                }
            }
            finally {
                if (t == null) {

                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error #UBU001, Contact Whiizyyy#0681");
        }
    }

    public static float fixRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
}

