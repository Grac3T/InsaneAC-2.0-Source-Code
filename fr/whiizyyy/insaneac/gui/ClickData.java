package fr.whiizyyy.insaneac.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickData {
    private final Player player;
    private final Inventory inventory;
    private final ItemStack itemStack;
    private final ClickType clickType;
    private final Integer slot;

    public ClickData(Player player, Inventory inventory, ItemStack itemStack, ClickType clickType, Integer slot) {
        this.player = player;
        this.inventory = inventory;
        this.itemStack = itemStack;
        this.clickType = clickType;
        this.slot = slot;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public Integer getSlot() {
        return this.slot;
    }
}

