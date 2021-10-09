package fr.whiizyyy.insaneac;

import java.util.UUID;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.check.alert.Alert;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.PlayerManager;

public class InsaneAPI {
    
    public InsaneAPI() {
    }

	public static int ACGetPing(Player p) {
		
		PlayerData playerData = PlayerManager.getInstance().getPlayer(p);
		
		return playerData.getPing();
		
	}
	
	public static void ACGetTps() {
		
		return;
	}
	
	public static void ACGetCps() {
		
		return;
	}
	
	public static UUID ACGetUUID(Player p) {
		
		return p.getUniqueId();
	}
	
	public static void ACGetTime() {

		return;
	}
	
	public static int ACGetVl() {
		
		return Alert.instance.getVl();
	}
	
	public static String ACGetLogsMSG(Player player, String check, String vl) {
		
		return AlertsManager.getInstance().getFormattedAlert(player, check, vl);
	}
}