package fr.whiizyyy.insaneac.gui.impl;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.gui.ClickData;
import fr.whiizyyy.insaneac.gui.Gui;
import fr.whiizyyy.insaneac.manager.GuiManager;
import fr.whiizyyy.insaneac.manager.OptionsManager;

public class MainGui extends Gui {
    private final ItemStack checkStack;
    private final ItemStack checkStack1;
    private final ItemStack checkStack2;
    private final ItemStack credits;
    private final ItemStack config;
    
    public MainGui() {
        super((Object)"§6§lInsaneAC (" + InsaneAC.getInstance().getVersion() + ") - Main", 9);
        this.checkStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta checkMeta = this.checkStack.getItemMeta();
        checkMeta.setDisplayName((Object)"§6" + "Combat Checks");
        checkMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Click to open"));
        this.checkStack.setItemMeta(checkMeta);
        this.inventory.setItem(2, this.checkStack);
        this.checkStack1 = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta checkMeta1 = this.checkStack1.getItemMeta();
        checkMeta1.setDisplayName((Object)"§6" + "Movement Checks");
        checkMeta1.setLore(Arrays.asList((Object)ChatColor.GRAY + "Click to open"));
        this.checkStack1.setItemMeta(checkMeta1);
        this.inventory.setItem(4, this.checkStack1);
        this.checkStack2 = new ItemStack(Material.REDSTONE_COMPARATOR);
        ItemMeta checkMeta2 = this.checkStack2.getItemMeta();
        checkMeta2.setDisplayName((Object)"§6" + "Other Checks");
        checkMeta2.setLore(Arrays.asList((Object)ChatColor.GRAY + "Click to open"));
        this.checkStack2.setItemMeta(checkMeta2);
        this.inventory.setItem(6, this.checkStack2);
        
        this.credits = new ItemStack(Material.PAPER);
        ItemMeta creditsMeta = this.credits.getItemMeta();
        creditsMeta.setDisplayName("§6§lCredits");
        creditsMeta.setLore(Arrays.asList("§8§m------------------------------", "§6§lInsaneAC", "§6Version §8§ §e" + InsaneAC.getInstance().getVersion(), "§6Author §8§ §eWhiizyyy / Whizyyy#0681", "§6License §8§ §e" + OptionsManager.getInstance().getLiscence(), "", "§c§lNOTE: §4InsaneAC is a fork of SpookyAC", "§8§m------------------------------"));
        this.credits.setItemMeta(creditsMeta);
        this.inventory.setItem(0, this.credits);
        
        this.config = new ItemStack(Material.WATCH);
        ItemMeta configMeta = this.config.getItemMeta();
        configMeta.setDisplayName("§6§lConfiguration");
        configMeta.setLore(Arrays.asList("§8§m------------------------------", "§e§lSoon...", "§8§m------------------------------"));
        this.config.setItemMeta(configMeta);
        this.inventory.setItem(8, this.config);
    }

    @Override
    public void onClick(ClickData clickData) {
        if (clickData.getSlot() == 2) {
            clickData.getPlayer().closeInventory();
            GuiManager.getInstance().getCheckGui().openGui(clickData.getPlayer());
        }
        if (clickData.getSlot() == 4) {
            clickData.getPlayer().closeInventory();
            GuiManager.getInstance().getMovGui().openGui(clickData.getPlayer());
        } 
        if (clickData.getSlot() == 6) {
            clickData.getPlayer().closeInventory();
            GuiManager.getInstance().getOtherGui().openGui(clickData.getPlayer());
        } 
        if (clickData.getSlot() == 0) {
        	return;
        }
        if (clickData.getSlot() == 8) {
        	return;
        }
    }
}

