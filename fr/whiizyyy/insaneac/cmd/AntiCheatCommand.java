package fr.whiizyyy.insaneac.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.manager.GuiManager;
import fr.whiizyyy.insaneac.manager.OptionsManager;

public class AntiCheatCommand implements CommandExecutor {
	
	public AntiCheatCommand(Plugin plugin){
	}
	
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        boolean passed = false;
        if (args.length == 1) {
            if (!(commandSender instanceof Player)) {
                this.sendInsaneACMessage(commandSender);
                passed = true;
            }
            Player player = (Player)commandSender;
            if (args[0].equalsIgnoreCase("info")) {
                this.sendHelpMessage(commandSender);
                passed = true;
            } else if (args[0].equalsIgnoreCase("gui")) {
            	if (player.hasPermission(OptionsManager.getInstance().getGuiPerm())) {
                GuiManager.getInstance().getMainGui().openGui(player);
            	}
                passed = true;
            }
        }
        if (!passed) {
            this.sendHelpMessage(commandSender);
        }
        return true;
    }

    private void sendHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage("§8§m---------------§8 §8[§6InsaneAC§8] §8§m---------------");
        commandSender.sendMessage("§6/anticheat §8- §eView this message.");
        commandSender.sendMessage("§6/anticheat gui §8- §eOpen the manager gui.");
        commandSender.sendMessage("§6/alerts §8- §eTo see/no longer see alerts.");
        commandSender.sendMessage("§6/logs <player> §8- §eSee all the logs of a player.");
        commandSender.sendMessage("§6/checkban <player> §8- §ePlayer information about a ban by the anticheat.");
        commandSender.sendMessage("§6/playerinfo <player> §8- §eSoon...");
        commandSender.sendMessage("§8§m----------------------------------------");
        commandSender.sendMessage("§7This server is running with §6InsaneAC (" + InsaneAC.getInstance().getVersion() + ") §7by §6Whiizyyy#0681§7");
        //this.sendHelpMessage(commandSender);
    }

    private void sendInsaneACMessage(CommandSender commandSender) {
        commandSender.sendMessage("Error#CACMD1 Contact Whizyyy#0681");
    }
}