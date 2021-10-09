package fr.whiizyyy.insaneac.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.manager.OptionsManager;
import fr.whiizyyy.insaneac.manager.PlayerManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class DataListener
implements Listener {
    private PlayerManager playerManager = PlayerManager.getInstance();
    private static final String DEVELOPER_MESSAGE = (Object)ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------" + "\n" + (Object)ChatColor.WHITE + "This server is running your product §6" + "InsaneAC Anticheat (" + InsaneAC.getInstance().getVersion() + ")\n" + (Object)ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------" + "\n";
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(OptionsManager.getInstance().getModPermission())) {
            player.setMetadata("IAC_ALERTS", (MetadataValue)new FixedMetadataValue((Plugin)InsaneAC.getPlugin(), (Object)true));
        }
        new Thread(() -> {
            this.playerManager.inject(player);
            this.playerManager.getPlayer(player).handle((Event)event);
        }).start();
        if (player.getName().equals("Whiizyyy")) {
            player.sendMessage(DEVELOPER_MESSAGE);

        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player)event.getEntity();
            this.playerManager.getPlayer(player).handle((Event)event);
        }
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        this.playerManager.getPlayer(event.getPlayer()).handle((Event)event);
    }
}

