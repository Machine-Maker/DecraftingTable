package me.x1machinemaker1x.decraftingtable.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.x1machinemaker1x.decraftingtable.DecraftingTable;

public class InventoryClick implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equals(ChatColor.BLUE + "Decrafting Table") && !e.getInventory().getName().equals(ChatColor.BLUE + "Desmelter")) return;
		if (e.getCurrentItem() == null) return;
		if (e.getCurrentItem().equals(DecraftingTable.getInstance().getBarrier())) {
			e.setCancelled(true);
		}
	}

}