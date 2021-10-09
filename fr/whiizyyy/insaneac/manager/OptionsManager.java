package fr.whiizyyy.insaneac.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.whiizyyy.insaneac.InsaneAC;

public class OptionsManager {
    private static OptionsManager instance;
    private static File file;
    private static YamlConfiguration configuration;
    private YamlConfiguration banConfiguration;
    public static String HEADER = "InsaneAC - AntiCheat.\nCreated By Whiizyyy#0681.\nSupport Discord Server: https://discord.gg/d5FVCXc.\n";
    private File banFile;
    private String anticheatName;
    private String logsPlayer;
    private String logsResult;
    private boolean alertNoSpam;
    private String alertCertainty;
    private String logsSeparated;
    private String alertExperimental;
    private String alertMessage;
    private List<String> banCommands;
    private String banMessage;
    private String banSeparated;
    private boolean autoBan;
    private boolean banAnnouncement;
    private boolean bypassEnabled;
    private String bypassPermission;
    private String modPermission;
    private String liscence;
    private String NoPermission;
    private String alertsenable;
    private String alertsdisable;
    private String guiperm;
    private String checkban;

    @SuppressWarnings("static-access")
	public OptionsManager() {
        this.configuration = new YamlConfiguration();
        this.banConfiguration = new YamlConfiguration();
    }
    
    public void enable() {
        this.setupConfig();
        this.readConfig();
    }
    
    @SuppressWarnings("static-access")
	public void disable() {
        this.saveConfig();
    }
    
    @SuppressWarnings("static-access")
	private void readConfig() {
        this.liscence = this.configuration.getString("License", "ENTER YOUR LICENSE HERE");
        this.anticheatName = this.configuration.getString("Prefix", "&6AC &8//");
        this.NoPermission = this.banConfiguration.getString("NoPermission", "&cNo Permission.");
        this.logsPlayer = this.configuration.getString("Logs.Playerlogs", "&6&l{player}&8's logs:");
        this.logsResult = this.configuration.getString("Logs.Result", "&6 {check} &8(x{vl})");
        this.logsSeparated = this.configuration.getString("Logs.Separated", "&8&m----------------------------------");
        this.alertNoSpam = this.configuration.getBoolean("Alerts.No-Spam", false);
        this.alertsenable = this.configuration.getString("Alerts.Enable", "{prefix} &eYou are now viewing alerts!");
        this.alertsdisable = this.configuration.getString("Alerts.Disable", "{prefix} &eYou are no longer viewing alerts!");
        this.alertCertainty = this.configuration.getString("Alerts.Certainty", "&8is using");
        this.alertExperimental = this.configuration.getString("Alerts.Experimental", "&8might be using");
        this.alertMessage = this.configuration.getString("Alerts.Message", "{prefix} &6{player} {certainty} &6{cheat} &8(&6{vl}&8/&6{maxvl}&8) &c&lPING/TPS: &4{ping}&8/&4{tps}");
        this.modPermission = this.configuration.getString("Alerts.Permission", "insaneac.notify");
        this.banCommands = this.readList(this.configuration.get("Bans.Commands", "banip %s 30d InsaneAC // Cheating -s"));
        this.banMessage = this.configuration.getString("Bans.Message", "&6InsaneAC &ehas detected &6%s &efor cheating and removed them from the network.");
        this.banSeparated = this.configuration.getString("Bans.Separated", "");
        this.autoBan = this.configuration.getBoolean("Bans.Enabled", true);
        this.banAnnouncement = this.configuration.getBoolean("Bans.Announce.Enabled", true);
        this.bypassEnabled = this.configuration.getBoolean("Permissions.Bypass.Enabled", true);
        this.bypassPermission = this.configuration.getString("Permissions.Bypass.Permission", "insaneac.bypass");
        this.guiperm = this.configuration.getString("Permissions.InsaneGui.Permission", "insaneac.gui");
        this.checkban = this.configuration.getString("Permissions.CheckBan.Permission", "insaneac.checkban");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> readList(final Object o) {
        return (List<T>)((o instanceof List) ? ((List)o) : Arrays.asList(o));
    }

    @SuppressWarnings("static-access")
	private void setupConfig() {
    	InsaneAC plugin = InsaneAC.getPlugin();
        this.file = new File(plugin.getDataFolder(), "InsaneAC.yml");
        this.loadConfig();
    	this.configuration.options().header(HEADER);
        this.configuration.options().copyDefaults(true).copyHeader(true);
        this.configuration.addDefault("License", "ENTER YOUR LICENSE HERE");
        this.configuration.addDefault("Prefix", "&6AC &8//");
        this.configuration.addDefault("NoPermission", "&cNo Permission.");
        this.configuration.addDefault("Logs.Playerlogs", "&6&l{player}&8's logs:");
        this.configuration.addDefault("Logs.Result", "&6 {check} &8(x{vl})");
        this.configuration.addDefault("Logs.Separated", "&8&m----------------------------------");
        this.configuration.addDefault("Alerts.No-Spam", false);
        this.configuration.addDefault("Alerts.Enable", "{prefix} &eYou are now viewing alerts!");
        this.configuration.addDefault("Alerts.Disable", "{prefix} &eYou are no longer viewing alerts!");
        this.configuration.addDefault("Alerts.Certainty", "&8is using");
        this.configuration.addDefault("Alerts.Experimental", "&8might be using");
        this.configuration.addDefault("Alerts.Message", "{prefix} &6{player} {certainty} &6{cheat} &8(&6{vl}&8/&6{maxvl}&8) &c&lPING/TPS: &4{ping}&8/&4{tps}");
        this.configuration.addDefault("Alerts.Permission", "insaneac.notify");
        this.configuration.addDefault("Bans.Commands", "banip %s 30d InsaneAC // Cheating -s");
        this.configuration.addDefault("Bans.Message", "&6InsaneAC &ehas detected &6%s &efor cheating and removed them from the network.");
        this.configuration.addDefault("Bans.Separated", "");
        this.configuration.addDefault("Bans.Enabled", true);
        this.configuration.addDefault("Bans.Announce.Enabled", true);
        this.configuration.addDefault("Permissions.Bypass.Enabled", true);
        this.configuration.addDefault("Permissions.Bypass.Permission", "insaneac.bypass");
        this.configuration.addDefault("Permissions.InsaneGui.Permission", "insaneac.gui");
        this.configuration.addDefault("Permissions.CheckBan.Permission", "insaneac.checkban");
        this.saveConfig();
    }

    @SuppressWarnings("static-access")
	private void loadConfig() {
        try {
            this.configuration.load(this.file);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public YamlConfiguration getBanConfiguration() {
        return this.banConfiguration;
    }
    
    public static void saveConfig() {
        try {
            OptionsManager.configuration.save(OptionsManager.file);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static OptionsManager getInstance() {
        OptionsManager optionsManager = instance == null ? (instance = new OptionsManager()) : instance;
        return optionsManager;
    }

    @SuppressWarnings("static-access")
	public File getFile() {
        return this.file;
    }

    @SuppressWarnings("static-access")
	public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public File getBanFile() {
        return this.banFile;
    }

    public String getAnticheatName() {
        return this.anticheatName;
    }
    
    public String getLiscence() {
        return this.liscence;
    }
    
    public String getSm() {
        return this.banSeparated;
    }

    public String getAlertCertainty() {
        return this.alertCertainty;
    }
    
    public String NoPermissionMSG() {
    	return this.NoPermission;
    }
    
    public boolean isNoSpam() {
        return this.alertNoSpam;
    }
    
    public String getPlayerLogs() {
        return this.logsPlayer;
    }
    
    public String getLogsSepearted() {
        return this.logsSeparated;
    }
    
    public String getPlayerLogsResult() {
        return this.logsResult;
    }
    
    public String getAlertExp() {
        return this.alertExperimental;
    }

    public String getAlertMessage() {
        return this.alertMessage;
    }

    public List<String> getBanCommands() {
        return this.banCommands;
    }

    public String getBanMessage() {
        return this.banMessage;
    }

    public boolean isAutoBan() {
        return this.autoBan;
    }

    public boolean isBanAnnouncement() {
        return this.banAnnouncement;
    }

    public boolean isBypassEnabled() {
        return this.bypassEnabled;
    }

    public String getBypassPermission() {
        return this.bypassPermission;
    }
    
    public String getModPermission() {
        return this.modPermission;
    }
    
    public String getAlertsEnableMessage() {
    	return this.alertsenable;
    	
    }
    
    public String getAlertsDisableMessage() {
    	return this.alertsdisable;
    	
    }
    
    public String getGuiPerm() {
    	return this.guiperm;
    }
    
    public String getCheckBanPerm() {
    	return this.checkban;
    }
   
}