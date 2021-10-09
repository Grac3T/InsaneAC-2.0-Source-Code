package fr.whiizyyy.insaneac.cmd;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.manager.OptionsManager;
import fr.whiizyyy.insaneac.util.BukkitUtils;


public class LogsCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to perform this command.");
            return false;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /logs <player>");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Usage: /logs <player>");
            return true;
        }
        if (args.length == 1) {
            final String playerCheck = args[0].toLowerCase();
            final String latestLogs = BukkitUtils.getFromFile("plugins/InsaneAC/data/" + playerCheck + ".log");
            if (latestLogs.equals("-1")) {
                sender.sendMessage("§cNo log found for this player!");
            }
            else {
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin)InsaneAC.getInstance(), (Runnable)new Runnable() {
                    @Override
                    public void run() {
                    	sender.sendMessage(OptionsManager.getInstance().getLogsSepearted().replace('&', '§'));
                        sender.sendMessage(OptionsManager.getInstance().getPlayerLogs().replace('&', '§').replace("{player}", Bukkit.getOfflinePlayer(playerCheck).getName()));
                        final HashMap<String, Integer> flags = new HashMap<String, Integer>();
                        String[] split;
                        for (int length = (split = latestLogs.split("@")).length, i = 0; i < length; ++i) {
                            final String s = split[i];
                            final String hackName = s.split(",")[1];
                            if (flags.containsKey(hackName)) {
                                flags.replace(hackName, flags.get(hackName) + 1);
                            }
                            else {
                                flags.put(hackName, 1);
                            }
                        }
                        String displayed = "";
                        for (HashMap<String, Integer> highest = LogsCommand.this.getHighestEntry(flags, displayed); highest != null; highest = LogsCommand.this.getHighestEntry(flags, displayed)) {
                            for (final String s2 : highest.keySet()) {
                                sender.sendMessage(OptionsManager.getInstance().getPlayerLogsResult().replace('&', '§').replace("{check}", s2).replace("{vl}", highest.get(s2).toString()));
                                displayed = String.valueOf(String.valueOf(String.valueOf(displayed))) + s2;
                            }
                        }
                        sender.sendMessage(OptionsManager.getInstance().getLogsSepearted().replace('&', '§'));
                    }
                });
            }
        }
		return false;
    }
    
    private HashMap<String, Integer> getHighestEntry(final HashMap<String, Integer> entries, final String exclusions) {
        final HashMap<String, Integer> returnEntry = new HashMap<String, Integer>();
        int highestEntry = 0;
        String entry = null;
        for (final String s : entries.keySet()) {
            if (!exclusions.contains(s) && entries.get(s) > highestEntry) {
                highestEntry = entries.get(s);
                entry = s;
            }
        }
        if (entry == null || highestEntry == 0) {
            return null;
        }
        returnEntry.put(entry, highestEntry);
        return returnEntry;
    }
}