package fr.whiizyyy.insaneac.check;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;

public abstract class AimCheck
extends Check {
    public AimCheck(Check.CheckType subType, String type, String friendlyName, Check.CheckVersion checkVersion) {
        super(subType, type, friendlyName, checkVersion);
    }

    public abstract void handle(Player var1, PlayerData var2, PlayerLocation var3, PlayerLocation var4, long var5);
}

