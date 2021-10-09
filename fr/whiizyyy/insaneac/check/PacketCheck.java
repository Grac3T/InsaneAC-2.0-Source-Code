package fr.whiizyyy.insaneac.check;

import org.bukkit.entity.Player;
import fr.whiizyyy.insaneac.data.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;

public abstract class PacketCheck
extends Check {
    public PacketCheck(Check.CheckType type, String subType, String friendlyName, Check.CheckVersion checkVersion) {
        super(type, subType, friendlyName, checkVersion);
    }

    public abstract void handle(Player var1, PlayerData var2, @SuppressWarnings("rawtypes") Packet var3, long var4);
}

