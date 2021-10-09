package fr.whiizyyy.insaneac.manager;

import com.google.common.collect.Maps;

import fr.whiizyyy.insaneac.check.Check;

import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckManager {
    private static CheckManager instance;
    private Map<Check.CheckType, Boolean> checks;

    public void enable() {
        this.checks = Maps.newConcurrentMap();
        this.loadChecks();
    }

    public void disable() {
        this.saveChecks();
        this.checks = null;
        instance = null;
    }

    public boolean enabled(Check.CheckType type) {
        return this.checks.get((Object)type);
    }

    public void enableType(Check.CheckType type, Player executor) {
        if (this.validate(type)) {
            this.checks.put(type, true);
            if (executor != null) {
                executor.sendMessage((Object)ChatColor.GREEN + type.getName() + " check has been enabled.");
                this.saveChecks();
            }
        }
    }

    public void disableType(Check.CheckType type, Player executor) {
        if (this.validate(type)) {
            this.checks.put(type, false);
            if (executor != null) {
                executor.sendMessage((Object)ChatColor.RED + type.getName() + " check has been disabled.");
                this.saveChecks();
            }
        }
    }

    private void loadChecks() {
        for (Check.CheckType type : Check.CheckType.values()) {
            this.checks.put(type, ChecksFileManager.getInstance().getConfiguration().getBoolean("CheckTypes." + type.getName() + ".enabled"));
        }
    }

    private void saveChecks() {
        for (Check.CheckType type : Check.CheckType.values()) {
        	ChecksFileManager.getInstance().getConfiguration().set("CheckTypes." + type.getName() + ".enabled", (Object)this.enabled(type));
        }
        ChecksFileManager.saveConfig();
    }

    private boolean validate(Check.CheckType type) {
        return this.checks.containsKey((Object)type);
    }

    public static CheckManager getInstance() {
        CheckManager checkManager = instance == null ? (instance = new CheckManager()) : instance;
        return checkManager;
    }

    public Map<Check.CheckType, Boolean> getChecks() {
        return this.checks;
    }
}

