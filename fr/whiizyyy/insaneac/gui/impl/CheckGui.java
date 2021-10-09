package fr.whiizyyy.insaneac.gui.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Maps;

import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.gui.ClickData;
import fr.whiizyyy.insaneac.gui.Gui;
import fr.whiizyyy.insaneac.manager.CheckManager;
import fr.whiizyyy.insaneac.manager.GuiManager;

public class CheckGui extends Gui {
    private final Map<Integer, Check.CheckType> checksById = Maps.newConcurrentMap();
    private final ItemStack back;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public CheckGui() {
        super((Object)"§6" + "Combat Checks", 54);
        int total = 0;
        this.back = new ItemStack(Material.ARROW);
        ItemMeta checkMeta1 = this.back.getItemMeta();
        checkMeta1.setDisplayName((Object)"§6§l" + "Back");
        checkMeta1.setLore(Arrays.asList((Object)ChatColor.GRAY + "Click to back."));
        this.back.setItemMeta(checkMeta1);
        this.inventory.setItem(53, this.back);
        for (Check.CheckType type : Check.CheckType.values()) {
        	if (type.getSuffix().equals("Combat")) {
        		ItemStack checkStack = new ItemStack(CheckManager.getInstance().enabled(type) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
            	ItemMeta checkMeta = checkStack.getItemMeta();
            	checkMeta.setDisplayName("§6§l" + type.getName());
            	checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§e§lSoon...", "§8§m--------------------"));
            	/*if (type.getName().equalsIgnoreCase("AimAssist A")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimA(), "§8§m--------------------"));
            	} 
            	else if (type.getName().equalsIgnoreCase("AimAssist B")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimB(), "§8§m--------------------"));
            	}
            	else if (type.getName().equalsIgnoreCase("AimAssist C")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimC(), "§8§m--------------------"));
            	} 
            	else if (type.getName().equalsIgnoreCase("AimAssist D")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimD(), "§8§m--------------------"));
            	} 
            	else if (type.getName().equalsIgnoreCase("AimAssist E")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimE(), "§8§m--------------------"));
            	} 
            	else if (type.getName().equalsIgnoreCase("AimAssist F")) {
            		checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §e" + ChecksFileManager.getInstance().getBanAimF(), "§8§m--------------------"));
            	}
            	
            	else checkMeta.setLore((List)Arrays.asList("§8§m--------------------", "§6Alerts to ban §8§ §eNONE", "§8§m--------------------"));*/
            	checkStack.setItemMeta(checkMeta);
            	this.inventory.setItem(total, checkStack);
            	this.checksById.put(total, type);
        	}
            ++total;
        }
    }

    @Override
    public void onClick(ClickData clickData) {
        Integer slot = clickData.getSlot();
        if (slot <= this.getInventory().getSize() && slot >= 0) {
            ItemStack checkStack = clickData.getItemStack();
            Check.CheckType type = this.checksById.get(slot);
            if (checkStack.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lBack")) {
            	clickData.getPlayer().closeInventory();
            	GuiManager.getInstance().getMainGui().openGui(clickData.getPlayer());
            }
            else if (checkStack.getItemMeta().getDisplayName().equalsIgnoreCase("§6§l" + type.getName())) {
                boolean enabled = CheckManager.getInstance().enabled(type);
                checkStack.setType(enabled ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK);
                if (enabled) {
                    CheckManager.getInstance().disableType(type, clickData.getPlayer());
                } else {
                    CheckManager.getInstance().enableType(type, clickData.getPlayer());
                }
                this.inventory.setItem(slot.intValue(), checkStack);
            }
        }
    }
}



