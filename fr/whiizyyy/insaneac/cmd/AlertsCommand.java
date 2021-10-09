package fr.whiizyyy.insaneac.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.OptionsManager;
import fr.whiizyyy.insaneac.manager.PlayerManager;


public class AlertsCommand implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to perform this command.");
            return false;
        }
        Player player = (Player)sender;
        PlayerData playerData = PlayerManager.getInstance().getPlayer(player);
        if (playerData != null) {
            playerData.setAlerts(!playerData.isAlerts());
            if (playerData.isAlerts()) {
                sender.sendMessage(OptionsManager.getInstance().getAlertsEnableMessage().replace("&", "§").replace("{prefix}", OptionsManager.getInstance().getAnticheatName().replace("&", "§")));
                player.setMetadata("IAC_ALERTS", (MetadataValue)new FixedMetadataValue((Plugin)InsaneAC.getPlugin(), (Object)true));
            } else {
                sender.sendMessage(OptionsManager.getInstance().getAlertsDisableMessage().replace("&", "§").replace("{prefix}", OptionsManager.getInstance().getAnticheatName().replace("&", "§")));
                player.removeMetadata("IAC_ALERTS", (Plugin)InsaneAC.getPlugin());
            }
        }
        return true;
    }
}

