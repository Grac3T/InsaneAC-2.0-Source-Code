package fr.whiizyyy.insaneac.manager;

import com.google.common.collect.Maps;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.injector.PacketDecoder;
import fr.whiizyyy.insaneac.injector.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManager {
    private static PlayerManager instance;
    private final InsaneAC plugin;
    private final Map<UUID, PlayerData> players = Maps.newConcurrentMap();

    public PlayerData getPlayer(Player player) {
        return this.players.get(player.getUniqueId());
    }

    public void inject(Player player) {
        PlayerData playerData = new PlayerData(player, this.plugin.getTypeLoader().loadChecks());
        Channel channel = playerData.getEntityPlayer().playerConnection.networkManager.channel;
        this.players.put(player.getUniqueId(), playerData);
        if (channel != null) {
            channel.pipeline().addBefore("packet_handler", "insaneac-encoder", (ChannelHandler)new PacketEncoder(playerData));
            channel.pipeline().addBefore("packet_handler", "insaneac-decoder", (ChannelHandler)new PacketDecoder(playerData));
        }
    }

    public void uninject(Player player) {
        PlayerData playerData = this.players.remove(player.getUniqueId());
        if (playerData != null) {
            playerData.setEnabled(false);
            PlayerConnection playerConnection = playerData.getEntityPlayer().playerConnection;
            if (playerConnection != null && !playerConnection.isDisconnected()) {
                Channel channel = playerConnection.networkManager.channel;
                try {
                    channel.pipeline().remove("insaneac-encoder");
                    channel.pipeline().remove("insaneac-decoder");
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
        }
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public static void enable(InsaneAC plugin) {
        instance = new PlayerManager(plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            instance.inject(player);
        }
    }

    public static void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            instance.uninject(player);
        }
        instance.getClass();
        instance = null;
    }

    public PlayerManager(InsaneAC plugin) {
        this.plugin = plugin;
    }

    public InsaneAC getPlugin() {
        return this.plugin;
    }

    public Map<UUID, PlayerData> getPlayers() {
        return this.players;
    }
}

