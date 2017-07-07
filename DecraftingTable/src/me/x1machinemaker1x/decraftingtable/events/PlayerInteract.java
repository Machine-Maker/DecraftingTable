package me.x1machinemaker1x.decraftingtable.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.x1machinemaker1x.decraftingtable.DecraftingTable;

public class PlayerInteract implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		BlockState bs = e.getClickedBlock().getState();
		if (!(bs instanceof Skull)) return;
		Skull skull = (Skull) bs;
		if (!skull.getSkullType().equals(SkullType.PLAYER)) return;
		if (!skull.getOwningPlayer().equals(Bukkit.getOfflinePlayer("craftingtable"))) return;
		Inventory inv = Bukkit.getServer().createInventory(null, 45, ChatColor.BLUE + "Decrafting Table");
		for (int i = 0; i < 45; i++) {
			if (!DecraftingTable.getInstance().getRS().contains(i) && i != 25) {
				inv.setItem(i, DecraftingTable.getInstance().getBarrier());
			}
		}
		e.getPlayer().openInventory(inv);
		if (DecraftingTable.getInstance().getDecraft(e.getPlayer()) == null) {
			DecraftingTable.getInstance().addDecraft(e.getPlayer(), inv);
		}		
	}
}
