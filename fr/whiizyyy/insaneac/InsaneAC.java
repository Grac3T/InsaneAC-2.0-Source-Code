package fr.whiizyyy.insaneac;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import fr.whiizyyy.insaneac.cmd.AlertsCommand;
import fr.whiizyyy.insaneac.cmd.AntiCheatCommand;
import fr.whiizyyy.insaneac.cmd.CheckBanCommand;
import fr.whiizyyy.insaneac.cmd.LogsCommand;
import fr.whiizyyy.insaneac.listener.DataListener;
import fr.whiizyyy.insaneac.manager.CheckManager;
import fr.whiizyyy.insaneac.manager.ChecksFileManager;
import fr.whiizyyy.insaneac.manager.GuiManager;
import fr.whiizyyy.insaneac.manager.OptionsManager;
import fr.whiizyyy.insaneac.manager.PlayerManager;
import fr.whiizyyy.insaneac.manager.PunishmentManager;
import fr.whiizyyy.insaneac.type.LoaderCheck;
import fr.whiizyyy.insaneac.util.SafeReflection;

public class InsaneAC extends JavaPlugin implements Listener
{
    public static InsaneAC instance;
    private SimpleCommandMap commandMap;
    private DataListener dataListener;
    private LoaderCheck typeLoader;
    private List<Command> commands;
    private static InsaneAC plugin;
    public static HashMap<UUID, String> lastCheck;
    public FileConfiguration alerts;
    public File alertsf;
    
    public InsaneAC() {
        InsaneAC.lastCheck = new HashMap<UUID, String>();
    }
    
    public static InsaneAC getInstance() {
        return InsaneAC.instance;
    }
    
    public void onEnable() {
        InsaneAC.plugin = this;
        this.alerts = (FileConfiguration)new YamlConfiguration();
        this.typeLoader = new LoaderCheck();
        PlayerManager.enable(InsaneAC.instance = this);
        OptionsManager.getInstance().enable();
        ChecksFileManager.getInstance().enable();
        CheckManager.getInstance().enable();
        GuiManager.getInstance().enable();
        PunishmentManager.getInstance().enable();
        this.registerListeners();
        this.createFiles();
        this.getCommand("anticheat").setExecutor((CommandExecutor)new AntiCheatCommand((Plugin)this));
        this.getCommand("alerts").setExecutor((CommandExecutor)new AlertsCommand());
        this.getCommand("logs").setExecutor((CommandExecutor)new LogsCommand());
        this.getCommand("cb").setExecutor((CommandExecutor)new CheckBanCommand());
        final Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>)this.getDescription().getCommands();
        for (final Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            final PluginCommand command = this.getCommand((String)entry.getKey());
            command.setPermission(OptionsManager.getInstance().getModPermission());
            command.setPermissionMessage(OptionsManager.getInstance().NoPermissionMSG().replace("&", "§"));
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-----------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6InsaneAC Loading..."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6Author: &eWhiizyyy"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6Version: &e" + this.getVersion()));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &e[InsaneAC By Whiizyyy] Launching..."));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eStarting Injection..."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eLoading Checks..."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eLoading Data..."));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6InsaneAC Successfully Enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-----------------------------------------------------"));
    }
    
    public void shutdown() {
        InsaneAC.plugin = null;
        PlayerManager.disable();
        OptionsManager.getInstance().disable();
        CheckManager.getInstance().disable();
        PunishmentManager.getInstance().disable();
        GuiManager.getInstance().disable();
        this.unregisterCommands();
        this.unregisterListeners();
        this.typeLoader.setCheckClasses(null);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-----------------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6Disabling InsaneAC..."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6Author: &eWhiizyyy"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6Version: &e" + this.getVersion()));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &e[InsaneAC By Whiizyyy] ShutDown..."));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eSaving Checks..."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &eSaving Data..."));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', " &6InsaneAC Successfully Disabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-----------------------------------------------------"));
    }
    
    public void registerListeners() {
        this.dataListener = new DataListener();
        this.getServer().getPluginManager().registerEvents((Listener)this.dataListener, (Plugin)InsaneAC.plugin);
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)InsaneAC.plugin);
    }
    
    public void unregisterCommands() {
        final Map<String, Command> knownCommands = SafeReflection.getKnownCommands(this.commandMap);
        knownCommands.values().removeAll(this.commands);
        for (final Command command : this.commands) {
            command.unregister((CommandMap)this.commandMap);
        }
    }
    
    public void unregisterListeners() {
        HandlerList.unregisterAll((Listener)this.dataListener);
        HandlerList.unregisterAll((Plugin)getPlugin());
    }
    
    public static InsaneAC getPlugin() {
        return InsaneAC.plugin;
    }
    
    public SimpleCommandMap getCommandMap() {
        return this.commandMap;
    }
    
    public DataListener getDataListener() {
        return this.dataListener;
    }
    
    public LoaderCheck getTypeLoader() {
        return this.typeLoader;
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public FileConfiguration getAlerts() {
        return this.alerts;
    }
    
    public void SaveAlerts() {
        try {
            this.alerts.save(this.alertsf);
        }
        catch (IOException e) {
            e.printStackTrace();
            this.getLogger().warning("Failed to save Checks.yml!");
        }
    }
    
    private void createFiles() {
        this.saveRes(this.alertsf = new File(this.getDataFolder(), "Checks.yml"), "Checks.yml");
        this.alerts = (FileConfiguration)new YamlConfiguration();
        try {
            this.alerts.load(this.alertsf);
        }
        catch (IOException ex) {}
        catch (InvalidConfigurationException ex2) {}
    }
    
    public void saveRes(final File file, final String name) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            this.saveResource(name, false);
        }
    }
    
    public String getVersion() {
        return "2.1";
    }
}