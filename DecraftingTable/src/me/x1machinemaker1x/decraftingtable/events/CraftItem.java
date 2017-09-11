package me.x1machinemaker1x.decraftingtable.events;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.x1machinemaker1x.decraftingtable.utils.CustomSkulls;

public class CraftItem implements Listener {
	
	@EventHandler
	public void onItemCraft(CraftItemEvent e) {
		if (e.getRecipe().getResult().equals(CustomSkulls.getDecraftingTable())) {
			if (!e.getWhoClicked().hasPermission("decraftingtable.use.decraft")) {
				e.setResult(Result.DENY);
			}
		}
		else if (e.getRecipe().getResult().equals(CustomSkulls.getDesmelter())) {
			if (!e.getWhoClicked().hasPermission("decraftingtable.use.desmelt")) {
				e.setResult(Result.DENY);
			}
		}
	}
}