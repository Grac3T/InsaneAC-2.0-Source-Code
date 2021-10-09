package fr.whiizyyy.insaneac.manager;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.alert.Alert;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.util.BukkitUtils;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class AlertsManager {
	
    public static AlertsManager instance;
    private static final String NAME;
    private static final String CERTAINTY;
    private static final String CERTAINTYEXP;
    private static final String ALERT;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final SimpleDateFormat simpleDateFormat;
    public static ArrayList<Player> cooldown;
    
    public AlertsManager() {
        this.simpleDateFormat = new SimpleDateFormat("k:m z");
        AlertsManager.cooldown = new ArrayList<Player>();
    }
    static {
        NAME = OptionsManager.getInstance().getAnticheatName();
        CERTAINTY = OptionsManager.getInstance().getAlertCertainty();
        CERTAINTYEXP = OptionsManager.getInstance().getAlertExp();
        ALERT = OptionsManager.getInstance().getAlertMessage();
    }

    public String getFormattedAlert(Player player, String check, String vl) {

    	DecimalFormat decimalFormat = new DecimalFormat("##.##");
    	PlayerData playerData = PlayerManager.getInstance().getPlayer(player);
    	
    	for(double tps : MinecraftServer.getServer().recentTps) {
    	
    		return ChatColor.translateAlternateColorCodes((char)'&', (String)ALERT.replace("{prefix}", NAME).replace("{player}", player.getName()).replace("{certainty}", CERTAINTY).replace("{cheat}", check).replace("{vl}", vl).replace("{maxvl}", InsaneAC.getInstance().getAlerts().get("CheckTypes." + check + ".Alerts") + "").replace("{ping}", playerData.getPing() + "").replace("{cps}", playerData.getCps() + "").replace("{tps}", decimalFormat.format(tps) + ""));
    	}
		return vl;
    }
    	
    	
    
    public String getFormattedExpir(Player player, String check, String vl) {
    	
    	DecimalFormat decimalFormat = new DecimalFormat("##.##");
    	PlayerData playerData = PlayerManager.getInstance().getPlayer(player);
    	
    	for(double tps : MinecraftServer.getServer().recentTps) {
    	
    		return ChatColor.translateAlternateColorCodes((char)'&', (String)ALERT.replace("{prefix}", NAME).replace("{player}", player.getName()).replace("{certainty}", CERTAINTYEXP).replace("{cheat}", check).replace("{vl}", vl).replace("{maxvl}", InsaneAC.getInstance().getAlerts().get("CheckTypes." + check + ".Alerts") + "").replace("{ping}", playerData.getPing() + "").replace("{cps}", playerData.getCps() + "").replace("{tps}", decimalFormat.format(tps) + ""));
    	}
		return vl;
    }

	public void handleViolation(PlayerData playerData, Check check, String data) {
        this.handleViolation(playerData, check, data, 1.0);
    }

    public void handleViolation(PlayerData playerData, Check check, String data, double vl) {
        this.handleViolation(playerData, check, data, vl, false);
    }

    @SuppressWarnings("static-access")
	public void handleViolation(PlayerData playerData, Check check, String data, double vl, boolean disguise) {
    	InsaneAC.getInstance().lastCheck.put(playerData.getPlayer().getUniqueId(), check.getFriendlyName() + " " + check.getSubType()); 
        this.executorService.submit(() -> this._handleViolation(playerData, check, data, vl, disguise));
    }

    private void _handleViolation(PlayerData playerData, Check check, String data, double vl, boolean disguise) {
        if (playerData.isEnabled()) {
            check.setViolations(check.getViolations() + vl);
            int currentViolation = (int)Math.floor(check.getViolations());
            
            if (currentViolation > 0) {
                if (currentViolation > check.getLastViolation()) {
                    Alert alert = new Alert(check, data, currentViolation);
                    if (disguise) {
                        if (playerData.getSpoofedAlerts().size() > 5) {
                            playerData.getSpoofedAlerts().poll();
                        }
                        playerData.getSpoofedAlerts().add(alert);
                        playerData.setCheckSpoofing(true);
                    } else {
                        this.handleAlert(playerData, check, currentViolation);
                    }
                    if (check.getViolations() >= (double)check.getMaxViolation()) {
                        this.handleBan(playerData, check, disguise);
                        final String name = playerData.getPlayer().getName();
                        OptionsManager.getInstance().getBanConfiguration().set(name + ".uuid", (Object)String.valueOf(playerData.getPlayer().getUniqueId()));
                        OptionsManager.getInstance().getBanConfiguration().set(name + ".time", (Object)String.valueOf(this.simpleDateFormat.format(new Date(alert.getTimestamp()))));
                        OptionsManager.getInstance().getBanConfiguration().set(name + ".check", (Object)String.valueOf(check.getType().getName()));
                        OptionsManager.getInstance().getBanConfiguration().set(name + ".data", (Object)String.valueOf( alert.getVl() + "VL " + ((PlayerData)playerData).getPing() + "MS"));
                    }
                    this.handleExport(playerData, alert);
                }
                check.setLastViolation(currentViolation);
            }
        }
    }

    public void handleExport(PlayerData playerData, Alert alert) {
        if (playerData.getQueuedAlerts().size() > 20) {
            playerData.getQueuedAlerts().clear();
        }
        playerData.getQueuedAlerts().add(alert);
    }

    public void handleAlert(PlayerData playerData, Check check, int vl) {
    	if (OptionsManager.getInstance().isNoSpam()) {
            if (CheckManager.getInstance().enabled(check.getType())) {
        		BukkitUtils.log(playerData.getPlayer(), "[" + new SimpleDateFormat("dd/MM/yy hh:mm:ss").format(new Date()) + "], " + check.getFriendlyName() + " " + check.getSubType() + "@");
                if (AlertsManager.cooldown.contains(playerData.getPlayer()) && !check.getFriendlyName().contains("Reach") || AlertsManager.cooldown.contains(playerData.getPlayer()) && !check.getFriendlyName().contains("KillAura") || AlertsManager.cooldown.contains(playerData.getPlayer()) && !check.getFriendlyName().contains("Auto Clicker")|| AlertsManager.cooldown.contains(playerData.getPlayer()) && !check.getFriendlyName().contains("Velocity")|| AlertsManager.cooldown.contains(playerData.getPlayer()) && !check.getFriendlyName().contains("AimAssist")) {
                	return;
                }
            	if (check.getCheckVersion() == Check.CheckVersion.RELEASE) {
                    PlayerManager.getInstance().getPlayers().values().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer).forEach(player -> ((CommandSender)player).sendMessage(this.getFormattedAlert(playerData.getPlayer(), String.valueOf(check.getFriendlyName()) + " " + check.getSubType(), String.valueOf(vl))));
                    AlertsManager.cooldown.add(playerData.getPlayer());
                    new BukkitRunnable() {
                        public void run() {
                            AlertsManager.cooldown.remove(playerData.getPlayer());
                        }
                    }.runTaskLaterAsynchronously((Plugin)InsaneAC.getInstance(), 15L);
            	}else {
                    PlayerManager.getInstance().getPlayers().values().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer).forEach(player -> ((CommandSender)player).sendMessage(this.getFormattedExpir(playerData.getPlayer(), String.valueOf(check.getFriendlyName()) + " " + check.getSubType(), String.valueOf(vl))));
                    AlertsManager.cooldown.add(playerData.getPlayer());
                    new BukkitRunnable() {
                        public void run() {
                            AlertsManager.cooldown.remove(playerData.getPlayer());
                        }
                    }.runTaskLaterAsynchronously((Plugin)InsaneAC.getInstance(), 15L);
            	}
            }
    	} else {
            if (CheckManager.getInstance().enabled(check.getType())) {
        		BukkitUtils.log(playerData.getPlayer(), "[" + new SimpleDateFormat("dd/MM/yy hh:mm:ss").format(new Date()) + "], " + check.getFriendlyName() + " " + check.getSubType() + "@");
            	if (check.getCheckVersion() == Check.CheckVersion.RELEASE) {
                	PlayerManager.getInstance().getPlayers().values().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer).forEach(player -> ((CommandSender)player).sendMessage(this.getFormattedAlert(playerData.getPlayer(), String.valueOf(check.getFriendlyName()) + " " + check.getSubType(), String.valueOf(vl))));
        		}else {
                	PlayerManager.getInstance().getPlayers().values().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer).forEach(player -> ((CommandSender)player).sendMessage(this.getFormattedExpir(playerData.getPlayer(), String.valueOf(check.getFriendlyName()) + " " + check.getSubType(), String.valueOf(vl))));
        		}
            }
    	}
    }

    public void handleBan(PlayerData playerData, Check check, boolean disguise) {
        if (!(!CheckManager.getInstance().enabled(check.getType()) || !OptionsManager.getInstance().isAutoBan() || check.getCheckVersion() != Check.CheckVersion.RELEASE || playerData.isBanned() || OptionsManager.getInstance().isBypassEnabled() && playerData.getPlayer().hasPermission(OptionsManager.getInstance().getBypassPermission()))) {
            if (disguise) {
                playerData.setSpoofBan(true);
                playerData.setSpoofBanCheck(check);
            }else {
                this._handleBan(playerData, check);
            }
        }
    }

    public void _handleBan(PlayerData playerData, Check check) {
        PunishmentManager.getInstance().insertBan(playerData, check);
    }

    public static AlertsManager getInstance() {
        AlertsManager alertsManager = instance == null ? (instance = new AlertsManager()) : instance;
        return alertsManager;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }
}