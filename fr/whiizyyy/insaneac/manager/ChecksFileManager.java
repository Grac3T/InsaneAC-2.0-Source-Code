package fr.whiizyyy.insaneac.manager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;

public class ChecksFileManager {
    private static ChecksFileManager instance;
    private static File file;
    private static YamlConfiguration configuration;
    private YamlConfiguration banConfiguration;
    public static String HEADER = "InsaneAC - AntiCheat.\nChecks File";
    
    private int AlertsGui;
    
    private int BanAimA;
    private int BanAimB;
    private int BanAimC;
    private int BanAimD;
    private int BanAimE;
    private int BanAimF;
    
    

	public ChecksFileManager() {
        ChecksFileManager.configuration = new YamlConfiguration();
        this.banConfiguration = new YamlConfiguration();
    }
    
    public void enable() {
        this.setupConfig();
        this.readConfig();
    }
    
	public void disable() {
        ChecksFileManager.saveConfig();
    }
	
    @SuppressWarnings("static-access")
	private void readConfig() {
    	for (Check.CheckType type : Check.CheckType.values()) {
    		this.AlertsGui = this.configuration.getInt("CheckTypes." + type.getName() + ".Alerts");
    		
        	this.BanAimA = this.configuration.getInt("CheckTypes.AimAssist A.Alerts");
        	this.BanAimB = this.configuration.getInt("CheckTypes.AimAssist B.Alerts");
        	this.BanAimC = this.configuration.getInt("CheckTypes.AimAssist C.Alerts");
        	this.BanAimD = this.configuration.getInt("CheckTypes.AimAssist D.Alerts");
        	this.BanAimE = this.configuration.getInt("CheckTypes.AimAssist E.Alerts");
    		this.BanAimF = this.configuration.getInt("CheckTypes.AimAssist F.Alerts");
    		
    	}
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> readList(final Object o) {
        return (List<T>)((o instanceof List) ? ((List)o) : Arrays.asList(o));
    }

	private void setupConfig() {
    	InsaneAC plugin = InsaneAC.getPlugin();
        ChecksFileManager.file = new File(plugin.getDataFolder(), "Checks.yml");
        this.loadConfig();
    	ChecksFileManager.configuration.options().header(HEADER);
        ChecksFileManager.configuration.options().copyDefaults(true).copyHeader(true);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist A.Alerts", 6);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist B.Alerts", 6);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist C.Alerts", 6);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist D.Alerts", 6);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist E.Alerts", 6);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist F.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AimAssist F.Alerts", 6);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity A.Alerts", 5);
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity B.Alerts", 5);
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Velocity C.Alerts", 5);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly A.Alerts", 30);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly B.Alerts", 30);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly C.Alerts", 30);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly D.Alerts", 30);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly E.Alerts", 30);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly F.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Fly F.Alerts", 30);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.Timer A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Timer A.Alerts", 20);
        ChecksFileManager.configuration.addDefault("CheckTypes.Timer B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Timer B.Alerts", 20);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach A.Alerts", 10);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach B.Alerts", 10);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach C.Alerts", 10);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach D.Alerts", 10);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach E.Alerts", 10);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach F.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Reach F.Alerts", 10);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed A.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed B.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed C.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed D.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.Speed E.Alerts", 15);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker A.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker B.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker C.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker D.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker E.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker F.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker F.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker G.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker G.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker H.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker H.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker I.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker I.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker J.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.AutoClicker J.Alerts", 15);
        
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura A.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura A.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura B.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura B.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura C.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura C.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura D.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura D.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura E.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura E.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura F.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura F.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura G.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura G.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura H.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura H.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura I.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura I.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura J.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura J.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura K.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura K.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura L.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura L.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura M.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura M.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura N.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura N.Alerts", 15);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura O.enabled", true);
        ChecksFileManager.configuration.addDefault("CheckTypes.KillAura O.Alerts", 15);
        
        ChecksFileManager.saveConfig();
    }

    private void loadConfig() {
        try {
            ChecksFileManager.configuration.load(ChecksFileManager.file);
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
            ChecksFileManager.configuration.save(ChecksFileManager.file);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static ChecksFileManager getInstance() {
        ChecksFileManager optionsManager = instance == null ? (instance = new ChecksFileManager()) : instance;
        return optionsManager;
    }

	public File getFile() {
        return ChecksFileManager.file;
    }

	public YamlConfiguration getConfiguration() {
        return ChecksFileManager.configuration;
    }
	
    public int getBanAimA() {
        return this.BanAimA;
    }
    
    public int getBanAimB() {
    	return this.BanAimB;
    }
    
    public int getBanAimC() {
    	return this.BanAimC;
    }
    
    public int getBanAimD() {
    	return this.BanAimD;
    }
    
    public int getBanAimE() {
    	return this.BanAimE;
    }
    
    public int getBanAimF() {
    	return this.BanAimF;
    }
    
    public int getAlertsGui() {
    	return this.AlertsGui;
    	
    }
   
}