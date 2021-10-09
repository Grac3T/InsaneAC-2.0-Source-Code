package fr.whiizyyy.insaneac.check;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import fr.whiizyyy.insaneac.InsaneAC;

public class Check {
	
    public static Check instance;
    private final CheckType type;
    private final String subType;
    private final String friendlyName;
    private final CheckVersion checkVersion;
    protected double violations = 0.0;
    private int lastViolation = 0;
    private int maxViolation = Integer.MAX_VALUE;
    private long lastCheck = 0L;

    public void run(final Runnable runnable) {
        new BukkitRunnable(){

            public void run() {
                runnable.run();
            }
        }.runTask((Plugin)InsaneAC.getPlugin());
    }

    public Check(CheckType type, String subType, String friendlyName, CheckVersion checkVersion) {
        this.type = type;
        this.subType = subType;
        this.friendlyName = friendlyName;
        this.checkVersion = checkVersion;
    }

    public CheckType getType() {
        return this.type;
    }

    public String getSubType() {
        return this.subType;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public CheckVersion getCheckVersion() {
        return this.checkVersion;
    }

    public double getViolations() {
        return this.violations;
    }

    public int getLastViolation() {
        return this.lastViolation;
    }

    public int getMaxViolation() {
        return this.maxViolation;
    }

    public long getLastCheck() {
        return this.lastCheck;
    }

    public void setViolations(double violations) {
        this.violations = violations;
    }

    public void setLastViolation(int lastViolation) {
        this.lastViolation = lastViolation;
    }

    public void setMaxViolation(int maxViolation) {
        this.maxViolation = maxViolation;
    }

    public void setLastCheck(long lastCheck) {
        this.lastCheck = lastCheck;
    }

    public static enum CheckVersion {
    	
        RELEASE,
        EXPERIMENTAL;
        

        private CheckVersion() {
        }
    }

    public static enum CheckType {
    	
    	
        AIM_ASSISTA("AimAssist A", "Combat"),
        AIM_ASSISTB("AimAssist B", "Combat"),
        AIM_ASSISTC("AimAssist C", "Combat"),
        AIM_ASSISTD("AimAssist D", "Combat"),
        AIM_ASSISTE("AimAssist E", "Combat"),
        AIM_ASSISTF("AimAssist F", "Combat"),
        
        VELOCITYA("Velocity A", "Combat"),
        VELOCITYB("Velocity B", "Combat"),
        VELOCITYC("Velocity C", "Combat"),
        
        REACHA("Reach A", "Combat"),
        REACHB("Reach B", "Combat"),
        REACHC("Reach C", "Combat"),
        REACHD("Reach D", "Combat"),
        REACHE("Reach E", "Combat"),
        REACHF("Reach F", "Combat"),
        
        AUTO_CLICKERA("AutoClicker A", "Combat"),
        AUTO_CLICKERB("AutoClicker B", "Combat"),
        AUTO_CLICKERC("AutoClicker C", "Combat"),
        AUTO_CLICKERD("AutoClicker D", "Combat"),
        AUTO_CLICKERE("AutoClicker E", "Combat"),
        AUTO_CLICKERF("AutoClicker F", "Combat"),
        AUTO_CLICKERG("AutoClicker G", "Combat"),
        AUTO_CLICKERH("AutoClicker H", "Combat"),
        AUTO_CLICKERI("AutoClicker I", "Combat"),
        AUTO_CLICKERJ("AutoClicker J", "Combat"),
        
        KILL_AURAA("KillAura A", "Combat"),
        KILL_AURAB("KillAura B", "Combat"),
        KILL_AURAC("KillAura C", "Combat"),
        KILL_AURAD("KillAura D", "Combat"),
        KILL_AURAE("KillAura E", "Combat"),
        KILL_AURAF("KillAura F", "Combat"),
        KILL_AURAG("KillAura G", "Combat"),
        KILL_AURAH("KillAura H", "Combat"),
        KILL_AURAI("KillAura I", "Combat"),
        KILL_AURAJ("KillAura J", "Combat"),
        KILL_AURAK("KillAura K", "Combat"),
        KILL_AURAL("KillAura L", "Combat"),
        KILL_AURAM("KillAura M", "Combat"),
        KILL_AURAN("KillAura N", "Combat"),
        KILL_AURAO("KillAura O", "Combat"),
        
    	
    	FLYA("Fly A", "Movement"),
    	FLYB("Fly B", "Movement"),
    	FLYC("Fly C", "Movement"),
    	FLYD("Fly D", "Movement"),
    	FLYE("Fly E", "Movement"),
    	FLYF("Fly F", "Movement"),
    	
    	SPEEDA("Speed A", "Movement"),
    	SPEEDB("Speed B", "Movement"),
    	SPEEDC("Speed C", "Movement"),
    	SPEEDD("Speed D", "Movement"),
    	SPEEDE("Speed E", "Movement"),
    	
    	TIMERA("Timer A", "Other"),
    	TIMERB("Timer B", "Other");
    	
        
        private final String name;
        private final String suffix;

        private CheckType(String name, String suffix) {
            this.name = name;
            this.suffix = suffix;
        }

        public String getName() {
            return this.name;
        }

        public String getSuffix() {
            return this.suffix;
        }
    }

}

