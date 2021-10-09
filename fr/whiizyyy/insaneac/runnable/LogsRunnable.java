package fr.whiizyyy.insaneac.runnable;

import org.bukkit.scheduler.*;

import fr.whiizyyy.insaneac.check.alert.Alert;

import java.util.*;

public class LogsRunnable extends BukkitRunnable
{
    private final Queue<Alert> alerts;
    
    public void run() {
        while (this.alerts.size() >= 0) {
            if (this.alerts.size() == 0) {
                this.cancel();
            }
        }
    }
    
    public LogsRunnable(final Queue<Alert> alerts) {
        this.alerts = alerts;
    }
}
