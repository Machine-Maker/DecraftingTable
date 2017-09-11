package me.x1machinemaker1x.decraftingtable.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.x1machinemaker1x.decraftingtable.utils.CustomSkulls;

public class BlockBreak_Place implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (CustomSkulls.isDecraftingTable(e.getBlock())) {
			e.setDropItems(false);
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), CustomSkulls.getDecraftingTable());
			return;
		}
		if (CustomSkulls.isDesmelter(e.getBlock())) {
			e.setDropItems(false);
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), CustomSkulls.getDesmelter());
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (CustomSkulls.isDecraftingTable(e.getBlockAgainst()) || CustomSkulls.isDesmelter(e.getBlockAgainst())) {
			e.setCancelled(true);
		}
	}
}