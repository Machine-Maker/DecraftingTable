package me.x1machinemaker1x.decraftingtable.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.x1machinemaker1x.decraftingtable.DecraftingTable;
import me.x1machinemaker1x.decraftingtable.utils.CustomSkulls;

public class PlayerInteract implements Listener {
	
	private DecraftingTable main;
	
	public PlayerInteract(DecraftingTable main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		if (CustomSkulls.isDecraftingTable(e.getClickedBlock())) {
			if (!e.getPlayer().hasPermission("decraftingtable.use.decraft")) return;
			Inventory inv = Bukkit.getServer().createInventory(null, 45, ChatColor.BLUE + "Decrafting Table");
			for (int i = 0; i < 45; i++) {
				if (!main.getRS().contains(i) && i != 25) {
					inv.setItem(i, DecraftingTable.getInstance().getBarrier());
				}
			}
			e.getPlayer().openInventory(inv);
			if (main.getDecraft(e.getPlayer()) == null) {
				main.addDecraft(e.getPlayer(), inv);
			}
			return;
		}
		if (CustomSkulls.isDesmelter(e.getClickedBlock())) {
			if (!e.getPlayer().hasPermission("decraftingtable.use.desmelt")) return;
			Inventory inv = Bukkit.getServer().createInventory(null, 27, ChatColor.BLUE + "Desmelter");
			for (int i = 0; i < 27; i++) {
				if (i != 10 && i != 16) {
					inv.setItem(i, main.getBarrier());
				}
			}
			e.getPlayer().openInventory(inv);
			if (main.getDesmelt(e.getPlayer()) == null) {
				main.addDesmelt(e.getPlayer(), inv);
			}
			return;
		}
	}
}