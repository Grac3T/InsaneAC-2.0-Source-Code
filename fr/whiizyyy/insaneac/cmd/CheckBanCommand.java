package fr.whiizyyy.insaneac.cmd;

import org.bukkit.command.*;

import fr.whiizyyy.insaneac.manager.OptionsManager;
import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang3.StringEscapeUtils;

public class CheckBanCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (args.length == 1) {
        	if (!sender.hasPermission(OptionsManager.getInstance().getCheckBanPerm())) {
        		sender.sendMessage("§6InsaneAC §8§ " + OptionsManager.getInstance().NoPermissionMSG());
        		return false;
        	}
            if (OptionsManager.getInstance().getBanConfiguration().get(args[0]) == null) {
                sender.sendMessage("§cThis player has never been banned by InsaneAC?");
                return false;
            }
            final String enabled = ChatColor.GRAY + " " + StringEscapeUtils.unescapeHtml4("&#9658;") + " ";
            sender.sendMessage("§6§l" + args[0] + "'s last ban data:");
            sender.sendMessage("§8§l"  + enabled + "§6" + "UUID: " + "§e" + OptionsManager.getInstance().getBanConfiguration().get(args[0] + ".uuid"));
            sender.sendMessage("§8§l"  + enabled + "§6" + "Time: " + "§e" + OptionsManager.getInstance().getBanConfiguration().get(args[0] + ".time"));
            sender.sendMessage("§8§l"  + enabled + "§6" + "Check: " + "§e" + OptionsManager.getInstance().getBanConfiguration().get(args[0] + ".check"));
            sender.sendMessage("§8§l"  + enabled + "§6" + "Data: " + "§e" + OptionsManager.getInstance().getBanConfiguration().get(args[0] + ".data"));
        }
        else {
            sender.sendMessage("§cUsage: /cb <player>");
        }
        return true;
    }
}
