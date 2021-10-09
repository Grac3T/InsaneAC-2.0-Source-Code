package fr.whiizyyy.insaneac.util;

import org.bukkit.inventory.*;

import org.bukkit.block.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class BlockUtil
{
    public static HashSet<Byte> blockPassSet;
    public static HashSet<Byte> blockAirFoliageSet;
    public static HashSet<Byte> fullSolid;
    public static HashSet<Byte> blockUseSet;
    
    static {
        BlockUtil.blockPassSet = new HashSet<Byte>();
        BlockUtil.blockAirFoliageSet = new HashSet<Byte>();
        BlockUtil.fullSolid = new HashSet<Byte>();
        BlockUtil.blockUseSet = new HashSet<Byte>();
    }
    
    @SuppressWarnings("rawtypes")
	public static Block getLowestBlockAt(final Location location) {
        Block block = location.getWorld().getBlockAt((int)location.getX(), 0, (int)location.getZ());
        if (block == null || ((Enum)block.getType()).equals(Material.AIR)) {
            block = location.getBlock();
            for (int i = (int)location.getY(); i > 0; --i) {
                final Block block2 = location.getWorld().getBlockAt((int)location.getX(), i, (int)location.getZ());
                final Block block3 = block2.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (block3 == null || ((Enum)block3.getType()).equals(Material.AIR)) {
                    block = block2;
                }
            }
        }
        return block;
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean containsBlock(final Location location, final Material material) {
        for (int i = 0; i < 256; ++i) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), i, (int)location.getZ());
            if (block != null && ((Enum)block.getType()).equals(material)) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean containsBlock(final Location location) {
        for (int i = 0; i < 256; ++i) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), i, (int)location.getZ());
            if (block != null && !((Enum)block.getType()).equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean containsBlockBelow(final Location location) {
        for (int i = 0; i < (int)location.getY(); ++i) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), i, (int)location.getZ());
            if (block != null && !((Enum)block.getType()).equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<Block> getBlocksAroundCenter(final Location location, final int n) {
        final ArrayList<Block> list = new ArrayList<Block>();
        for (int i = location.getBlockX() - n; i <= location.getBlockX() + n; ++i) {
            for (int j = location.getBlockY() - n; j <= location.getBlockY() + n; ++j) {
                for (int k = location.getBlockZ() - n; k <= location.getBlockZ() + n; ++k) {
                    final Location location2 = new Location(location.getWorld(), (double)i, (double)j, (double)k);
                    if (location2.distance(location) <= n) {
                        list.add(location2.getBlock());
                    }
                }
            }
        }
        return list;
    }
    
    public static Location stringToLocation(final String s) {
        final String[] array = s.split(",");
        return new Location(Bukkit.getWorld(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]), Double.parseDouble(array[3]), Float.parseFloat(array[4]), Float.parseFloat(array[5]));
    }
    
    public static String LocationToString(final Location location) {
        return String.valueOf(String.valueOf(location.getWorld().getName())) + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }
    
    public static boolean isStair(final Block block) {
        @SuppressWarnings("rawtypes")
		final String lowerCase = ((Enum)block.getType()).name().toLowerCase();
        return lowerCase.contains("stair") || lowerCase.contains("_step") || lowerCase.equals("step");
    }
    
    public static boolean isWeb(final Block block) {
        return block.getType() == Material.WEB;
    }
    
    public static boolean containsBlockType(final Material[] array, final Block block) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == block.getType()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLiquid(final Block block) {
        return block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean isSolid(final Block block) {
        return block != null && isSolid(block.getTypeId());
    }
    
    public static boolean isIce(final Block block) {
        return block != null && (block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE);
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean isAny(final Block block, final Material[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (((Enum)block.getType()).equals(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSolid(final int n) {
        return isSolid((byte)n);
    }
    
    public static boolean isSolid(final byte b) {
        if (BlockUtil.blockPassSet.isEmpty()) {
            BlockUtil.blockPassSet.add((byte) 0);
            BlockUtil.blockPassSet.add((byte)6);
            BlockUtil.blockPassSet.add((byte)8);
            BlockUtil.blockPassSet.add((byte)9);
            BlockUtil.blockPassSet.add((byte)10);
            BlockUtil.blockPassSet.add((byte)11);
            BlockUtil.blockPassSet.add((byte)27);
            BlockUtil.blockPassSet.add((byte)28);
            BlockUtil.blockPassSet.add((byte)30);
            BlockUtil.blockPassSet.add((byte)31);
            BlockUtil.blockPassSet.add((byte)32);
            BlockUtil.blockPassSet.add((byte)37);
            BlockUtil.blockPassSet.add((byte)38);
            BlockUtil.blockPassSet.add((byte)39);
            BlockUtil.blockPassSet.add((byte)40);
            BlockUtil.blockPassSet.add((byte)50);
            BlockUtil.blockPassSet.add((byte)51);
            BlockUtil.blockPassSet.add((byte)55);
            BlockUtil.blockPassSet.add((byte)59);
            BlockUtil.blockPassSet.add((byte)63);
            BlockUtil.blockPassSet.add((byte)66);
            BlockUtil.blockPassSet.add((byte)68);
            BlockUtil.blockPassSet.add((byte)69);
            BlockUtil.blockPassSet.add((byte)70);
            BlockUtil.blockPassSet.add((byte)72);
            BlockUtil.blockPassSet.add((byte)75);
            BlockUtil.blockPassSet.add((byte)76);
            BlockUtil.blockPassSet.add((byte)77);
            BlockUtil.blockPassSet.add((byte)78);
            BlockUtil.blockPassSet.add((byte)83);
            BlockUtil.blockPassSet.add((byte)90);
            BlockUtil.blockPassSet.add((byte)104);
            BlockUtil.blockPassSet.add((byte)105);
            BlockUtil.blockPassSet.add((byte)115);
            BlockUtil.blockPassSet.add((byte)119);
            BlockUtil.blockPassSet.add((byte)(-124));
            BlockUtil.blockPassSet.add((byte)(-113));
            BlockUtil.blockPassSet.add((byte)(-81));
            BlockUtil.blockPassSet.add((byte)(-85));
        }
        return !BlockUtil.blockPassSet.contains(b);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean airFoliage(final Block block) {
        return block != null && airFoliage(block.getTypeId());
    }
    
    public static boolean airFoliage(final int n) {
        return airFoliage((byte)n);
    }
    
    public static boolean airFoliage(final byte b) {
        if (BlockUtil.blockAirFoliageSet.isEmpty()) {
            BlockUtil.blockAirFoliageSet.add((byte)0);
            BlockUtil.blockAirFoliageSet.add((byte)6);
            BlockUtil.blockAirFoliageSet.add((byte)31);
            BlockUtil.blockAirFoliageSet.add((byte)32);
            BlockUtil.blockAirFoliageSet.add((byte)37);
            BlockUtil.blockAirFoliageSet.add((byte)38);
            BlockUtil.blockAirFoliageSet.add((byte)39);
            BlockUtil.blockAirFoliageSet.add((byte)40);
            BlockUtil.blockAirFoliageSet.add((byte)51);
            BlockUtil.blockAirFoliageSet.add((byte)59);
            BlockUtil.blockAirFoliageSet.add((byte)104);
            BlockUtil.blockAirFoliageSet.add((byte)105);
            BlockUtil.blockAirFoliageSet.add((byte)115);
            BlockUtil.blockAirFoliageSet.add((byte)(-115));
            BlockUtil.blockAirFoliageSet.add((byte)(-114));
        }
        return BlockUtil.blockAirFoliageSet.contains(b);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean fullSolid(final Block block) {
        return block != null && fullSolid(block.getTypeId());
    }
    
    public static boolean fullSolid(final int n) {
        return fullSolid((byte)n);
    }
    
    public static boolean fullSolid(final byte b) {
        if (BlockUtil.fullSolid.isEmpty()) {
            BlockUtil.fullSolid.add((byte)1);
            BlockUtil.fullSolid.add((byte)2);
            BlockUtil.fullSolid.add((byte)3);
            BlockUtil.fullSolid.add((byte)4);
            BlockUtil.fullSolid.add((byte)5);
            BlockUtil.fullSolid.add((byte)7);
            BlockUtil.fullSolid.add((byte)12);
            BlockUtil.fullSolid.add((byte)13);
            BlockUtil.fullSolid.add((byte)14);
            BlockUtil.fullSolid.add((byte)15);
            BlockUtil.fullSolid.add((byte)16);
            BlockUtil.fullSolid.add((byte)17);
            BlockUtil.fullSolid.add((byte)19);
            BlockUtil.fullSolid.add((byte)20);
            BlockUtil.fullSolid.add((byte)21);
            BlockUtil.fullSolid.add((byte)22);
            BlockUtil.fullSolid.add((byte)23);
            BlockUtil.fullSolid.add((byte)24);
            BlockUtil.fullSolid.add((byte)25);
            BlockUtil.fullSolid.add((byte)29);
            BlockUtil.fullSolid.add((byte)33);
            BlockUtil.fullSolid.add((byte)35);
            BlockUtil.fullSolid.add((byte)41);
            BlockUtil.fullSolid.add((byte)42);
            BlockUtil.fullSolid.add((byte)43);
            BlockUtil.fullSolid.add((byte)44);
            BlockUtil.fullSolid.add((byte)45);
            BlockUtil.fullSolid.add((byte)46);
            BlockUtil.fullSolid.add((byte)47);
            BlockUtil.fullSolid.add((byte)48);
            BlockUtil.fullSolid.add((byte)49);
            BlockUtil.fullSolid.add((byte)56);
            BlockUtil.fullSolid.add((byte)57);
            BlockUtil.fullSolid.add((byte)58);
            BlockUtil.fullSolid.add((byte)60);
            BlockUtil.fullSolid.add((byte)61);
            BlockUtil.fullSolid.add((byte)62);
            BlockUtil.fullSolid.add((byte)73);
            BlockUtil.fullSolid.add((byte)74);
            BlockUtil.fullSolid.add((byte)79);
            BlockUtil.fullSolid.add((byte)80);
            BlockUtil.fullSolid.add((byte)82);
            BlockUtil.fullSolid.add((byte)84);
            BlockUtil.fullSolid.add((byte)86);
            BlockUtil.fullSolid.add((byte)87);
            BlockUtil.fullSolid.add((byte)88);
            BlockUtil.fullSolid.add((byte)89);
            BlockUtil.fullSolid.add((byte)91);
            BlockUtil.fullSolid.add((byte)95);
            BlockUtil.fullSolid.add((byte)97);
            BlockUtil.fullSolid.add((byte)98);
            BlockUtil.fullSolid.add((byte)99);
            BlockUtil.fullSolid.add((byte)100);
            BlockUtil.fullSolid.add((byte)103);
            BlockUtil.fullSolid.add((byte)110);
            BlockUtil.fullSolid.add((byte)112);
            BlockUtil.fullSolid.add((byte)121);
            BlockUtil.fullSolid.add((byte)123);
            BlockUtil.fullSolid.add((byte)124);
            BlockUtil.fullSolid.add((byte)125);
            BlockUtil.fullSolid.add((byte)126);
            BlockUtil.fullSolid.add((byte)(-127));
            BlockUtil.fullSolid.add((byte)(-123));
            BlockUtil.fullSolid.add((byte)(-119));
            BlockUtil.fullSolid.add((byte)(-118));
            BlockUtil.fullSolid.add((byte)(-104));
            BlockUtil.fullSolid.add((byte)(-103));
            BlockUtil.fullSolid.add((byte)(-101));
            BlockUtil.fullSolid.add((byte)(-98));
        }
        return BlockUtil.fullSolid.contains(b);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean usable(final Block block) {
        return block != null && usable(block.getTypeId());
    }
    
    public static boolean usable(final int n) {
        return usable((byte)n);
    }
    
    public static boolean usable(final byte b) {
        if (BlockUtil.blockUseSet.isEmpty()) {
            BlockUtil.blockUseSet.add((byte)23);
            BlockUtil.blockUseSet.add((byte)26);
            BlockUtil.blockUseSet.add((byte)33);
            BlockUtil.blockUseSet.add((byte)47);
            BlockUtil.blockUseSet.add((byte)54);
            BlockUtil.blockUseSet.add((byte)58);
            BlockUtil.blockUseSet.add((byte)61);
            BlockUtil.blockUseSet.add((byte)62);
            BlockUtil.blockUseSet.add((byte)64);
            BlockUtil.blockUseSet.add((byte)69);
            BlockUtil.blockUseSet.add((byte)71);
            BlockUtil.blockUseSet.add((byte)77);
            BlockUtil.blockUseSet.add((byte)93);
            BlockUtil.blockUseSet.add((byte)94);
            BlockUtil.blockUseSet.add((byte)96);
            BlockUtil.blockUseSet.add((byte)107);
            BlockUtil.blockUseSet.add((byte)116);
            BlockUtil.blockUseSet.add((byte)117);
            BlockUtil.blockUseSet.add((byte)(-126));
            BlockUtil.blockUseSet.add((byte)(-111));
            BlockUtil.blockUseSet.add((byte)(-110));
            BlockUtil.blockUseSet.add((byte)(-102));
            BlockUtil.blockUseSet.add((byte)(-98));
        }
        return BlockUtil.blockUseSet.contains(b);
    }
    
    @SuppressWarnings("deprecation")
	public static boolean isBlock(final ItemStack itemStack) {
        return itemStack != null && itemStack.getTypeId() > 0 && itemStack.getTypeId() < 256;
    }
    
    public static Block getHighest(final Location location) {
        return getHighest(location, null);
    }
    
    public static Block getHighest(final Location location, final HashSet<Material> set) {
        location.setY(0.0);
        for (int i = 0; i < 256; ++i) {
            location.setY((double)(256 - i));
            if (isSolid(location.getBlock())) {
                break;
            }
        }
        return location.getBlock().getRelative(BlockFace.UP);
    }
    
    public static boolean isInAir(final Player player) {
        boolean b = false;
        final Iterator<Block> iterator = getSurrounding(player.getLocation().getBlock(), true).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getType() != Material.AIR) {
                b = true;
                break;
            }
        }
        return b;
    }
    
    public static ArrayList<Block> getSurrounding(final Block block, final boolean b) {
        final ArrayList<Block> list = new ArrayList<Block>();
        if (b) {
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        if (i != 0 || j != 0 || k != 0) {
                            list.add(block.getRelative(i, j, k));
                        }
                    }
                }
            }
        }
        else {
            list.add(block.getRelative(BlockFace.UP));
            list.add(block.getRelative(BlockFace.DOWN));
            list.add(block.getRelative(BlockFace.NORTH));
            list.add(block.getRelative(BlockFace.SOUTH));
            list.add(block.getRelative(BlockFace.EAST));
            list.add(block.getRelative(BlockFace.WEST));
        }
        return list;
    }
    
    public static ArrayList<Block> getSurroundingXZ(final Block block) {
        final ArrayList<Block> list = new ArrayList<Block>();
        list.add(block.getRelative(BlockFace.NORTH));
        list.add(block.getRelative(BlockFace.NORTH_EAST));
        list.add(block.getRelative(BlockFace.NORTH_WEST));
        list.add(block.getRelative(BlockFace.SOUTH));
        list.add(block.getRelative(BlockFace.SOUTH_EAST));
        list.add(block.getRelative(BlockFace.SOUTH_WEST));
        list.add(block.getRelative(BlockFace.EAST));
        list.add(block.getRelative(BlockFace.WEST));
        return list;
    }
    
    public static String serializeLocation(final Location location) {
        return new String(String.valueOf(String.valueOf(location.getWorld().getName())) + "," + (int)location.getX() + "," + (int)location.getY() + "," + (int)location.getZ() + "," + (int)location.getPitch() + "," + (int)location.getYaw());
    }
    
    public static Location deserializeLocation(final String s) {
        if (s == null) {
            return null;
        }
        final String[] array = s.split(",");
        final World world = Bukkit.getServer().getWorld(array[0]);
        final Double value = Double.parseDouble(array[1]);
        final Double value2 = Double.parseDouble(array[2]);
        final Double value3 = Double.parseDouble(array[3]);
        final Float value4 = Float.parseFloat(array[4]);
        final Float value5 = Float.parseFloat(array[5]);
        final Location location = new Location(world, (double)value, (double)value2, (double)value3);
        location.setPitch((float)value4);
        location.setYaw((float)value5);
        return location;
    }
    
    public static boolean isVisible(final Block block) {
        final Iterator<Block> iterator = getSurrounding(block, false).iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().getType().isOccluding()) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	public static ArrayList<Block> getSurroundingB(Block block) {
        @SuppressWarnings("rawtypes")
		ArrayList<Block> blocks = new ArrayList();
        for (double x = -0.5; x <= 0.5; x += 0.5) {
            for (double y = -0.5; y <= 0.5; y += 0.5) {
                for (double z = -0.5; z <= 0.5; z += 0.5) {
                    if ((x != 0) || (y != 0) || (z != 0)) {
                        blocks.add(block.getLocation().add(x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
}
