package fr.whiizyyy.insaneac.check;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import fr.whiizyyy.insaneac.data.PlayerData;

public abstract class EventCheck
extends Check {
    public EventCheck(Check.CheckType subType, String type, String friendlyName, Check.CheckVersion checkVersion) {
        super(subType, type, friendlyName, checkVersion);
    }

    public abstract void handle(Player var1, PlayerData var2, Event var3, long var4);
}

