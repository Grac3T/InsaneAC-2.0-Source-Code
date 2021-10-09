package fr.whiizyyy.insaneac.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.potion.Potion;

public class MaterialList {
    public static final Set<Object> INVALID_SHAPE = ImmutableSet.of((Object)Material.ACACIA_STAIRS, (Object)Material.BIRCH_WOOD_STAIRS, (Object)Material.BRICK_STAIRS, (Object)Material.COBBLESTONE_STAIRS, (Object)Material.DARK_OAK_STAIRS, (Object)Material.JUNGLE_WOOD_STAIRS, (Object[])new Material[]{Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS, Material.SNOW, Material.STONE_SLAB2, Material.STEP, Material.WOOD_STEP, Material.CARPET, Material.CHEST, Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.ENDER_PORTAL_FRAME, Material.TRAP_DOOR, Material.SLIME_BLOCK, Material.WATER_LILY, Material.REDSTONE_COMPARATOR, Material.TRAP_DOOR, Material.CAULDRON, Material.STATIONARY_WATER, Material.FENCE, Material.HOPPER, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON});
    public static final Set<Object> FENCES = ImmutableSet.of((Object)Material.ACACIA_FENCE, (Object)Material.BIRCH_FENCE, (Object)Material.COBBLE_WALL, (Object)Material.DARK_OAK_FENCE, (Object)Material.FENCE, (Object)Material.IRON_FENCE, (Object[])new Material[]{Material.JUNGLE_FENCE, Material.NETHER_FENCE, Material.SPRUCE_FENCE, Material.STAINED_GLASS_PANE, Material.THIN_GLASS});
    public static final Set<Object> GATES = ImmutableSet.of((Object)Material.ACACIA_FENCE_GATE, (Object)Material.BIRCH_FENCE_GATE, (Object)Material.DARK_OAK_FENCE_GATE, (Object)Material.FENCE_GATE, (Object)Material.JUNGLE_FENCE_GATE, (Object)Material.SPRUCE_FENCE_GATE, (Object[])new Material[0]);
    public static final Set<Object>  BAD_VELOCITY = ImmutableSet.of((Object)Material.WATER, (Object)Material.STATIONARY_WATER, (Object)Material.LAVA, (Object)Material.STATIONARY_LAVA, (Object)Material.WEB, (Object)Material.SLIME_BLOCK, (Object[])new Material[]{Material.LADDER, Material.VINE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.SNOW, Material.FENCE, Material.STONE_SLAB2, Material.SOUL_SAND, Material.CHEST});
    public static final List<Object> ICE = ImmutableList.of((Object)Material.PACKED_ICE, (Object)Material.ICE);
    public static final List<Object> SLABS = ImmutableList.of((Object)Material.STONE_SLAB2, (Object)Material.STEP, (Object)Material.WOOD_STEP);
    public static final List<Object> STAIRS = ImmutableList.of((Object)Material.ACACIA_STAIRS, (Object)Material.BIRCH_WOOD_STAIRS, (Object)Material.BRICK_STAIRS, (Object)Material.COBBLESTONE_STAIRS, (Object)Material.DARK_OAK_STAIRS, (Object)Material.JUNGLE_WOOD_STAIRS, (Object)Material.NETHER_BRICK_STAIRS, (Object)Material.QUARTZ_STAIRS, (Object)Material.SANDSTONE_STAIRS, (Object)Material.RED_SANDSTONE_STAIRS, (Object)Material.SMOOTH_STAIRS, (Object)Material.SPRUCE_WOOD_STAIRS, (Object[])new Material[]{Material.WOOD_STAIRS});
    public static final List<Object> INPASSABLE = ImmutableList.of((Object)Material.STONE, (Object)Material.GRASS, (Object)Material.DIRT, (Object)Material.COBBLESTONE, (Object)Material.MOSSY_COBBLESTONE, (Object)Material.WOOD, (Object)Material.BEDROCK, (Object)Material.WOOL, (Object)Material.LOG, (Object)Material.LOG_2, (Object)Material.CLAY_BRICK, (Object)Material.NETHER_BRICK, (Object[])new Material[]{Material.SMOOTH_BRICK, Material.COAL_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.LAPIS_BLOCK, Material.GLASS, Material.STAINED_GLASS, Material.ENDER_STONE, Material.GLOWSTONE, Material.SANDSTONE, Material.RED_SANDSTONE, Material.BOOKSHELF, Material.NETHERRACK, Material.CLAY, Material.SNOW_BLOCK, Material.MELON_BLOCK, Material.EMERALD_BLOCK, Material.QUARTZ_BLOCK, Material.QUARTZ_ORE, Material.COAL_ORE, Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.GLOWING_REDSTONE_ORE, Material.GOLD_ORE, Material.IRON_ORE, Material.LAPIS_ORE});
    public static final List<Object> PLACEABLE = ImmutableList.of((Object)Material.DIAMOND_SWORD, (Object)Material.GOLD_SWORD, (Object)Material.IRON_SWORD, (Object)Material.STONE_SWORD, (Object)Material.WOOD_SWORD, (Object)Material.GOLDEN_APPLE);
    public static final List<Object> SpeedNoFalse = ImmutableList.of((Object)Material.WATER);

    public static boolean canPlace(ItemStack nmsStack) {
        org.bukkit.inventory.ItemStack itemStack = CraftItemStack.asBukkitCopy((ItemStack)nmsStack);
        Material type = itemStack.getType();
        if (type == Material.POTION) {
            Potion potion = Potion.fromItemStack((org.bukkit.inventory.ItemStack)itemStack);
            return !potion.isSplash();
        }
        return PLACEABLE.contains((Object)type);
    }
}

