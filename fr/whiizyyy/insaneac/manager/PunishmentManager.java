package fr.whiizyyy.insaneac.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.data.PlayerData;

public class PunishmentManager {
    private static PunishmentManager instance;

    public void enable() {
    }

    public void disable() {
    }

    public void insertBan(PlayerData playerData, Check check) {
    
        new BukkitRunnable() {
            public void run() {
            	
                playerData.setBanned(true);
                String name = playerData.getPlayer().getName();
                for (String banCommand : OptionsManager.getInstance().getBanCommands()) 

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String)String.format(banCommand, name).replace("{check}", check.getFriendlyName()));
                
                if (OptionsManager.getInstance().isBanAnnouncement()) {
                	Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)String.format(OptionsManager.getInstance().getSm())));
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)String.format(OptionsManager.getInstance().getBanMessage().replace("{check}", check.getFriendlyName()), playerData.getPlayer().getName())));
                	Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)String.format(OptionsManager.getInstance().getSm())));
                }
            }
        }.runTask(InsaneAC.instance);
    }
    
    public static PunishmentManager getInstance() {
        PunishmentManager punishmentManager = instance == null ? (instance = new PunishmentManager()) : instance;
        return punishmentManager;
    }
}

