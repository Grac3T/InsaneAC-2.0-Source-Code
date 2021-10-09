package fr.whiizyyy.insaneac.check;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.ReachData;


public abstract class ReachCheck
extends Check {
    public ReachCheck(Check.CheckType subType, String type, String friendlyName, Check.CheckVersion checkVersion) {
        super(subType, type, friendlyName, checkVersion);
    }

    public abstract void handle(Player var1, PlayerData var2, ReachData var3, long var4);
}

