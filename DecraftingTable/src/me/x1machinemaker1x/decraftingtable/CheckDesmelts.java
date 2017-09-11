package me.x1machinemaker1x.decraftingtable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitRunnable;

import me.x1machinemaker1x.decraftingtable.DecraftingTable.Options;
import me.x1machinemaker1x.decraftingtable.utils.Desmelt;
import me.x1machinemaker1x.decraftingtable.utils.DesmeltTimer;

public class CheckDesmelts extends BukkitRunnable {
	
	private DecraftingTable main;
	
	private List<Desmelt> toRemove;
	
	public CheckDesmelts(DecraftingTable main) {
		this.main = main;
		toRemove = new ArrayList<Desmelt>();
	}
	
	@Override
	public void run() {
		if (!main.getDesmelts().isEmpty()) {
			List<Material> bItems = new ArrayList<Material>();
			for (String i : Options.BANNED_ITEMS_DESMELTING.bItems) {
				bItems.add(Material.matchMaterial(i));
			}
			for (Desmelt d : main.getDesmelts()) {
				System.out.println(main.getDesmelts().size());
				InventoryView invView = d.getPlayer().getOpenInventory();
				if (!invView.getTitle().equals(ChatColor.BLUE + "Desmelter")) { //Checks if player is still in desmelt table.
					toRemove.add(d);
					continue;
				}
				ItemStack item = invView.getItem(16);
				boolean allAir = true;;
				if (invView.getItem(10).getType() != Material.AIR) {
					allAir = false;
					d.setDoneDesmelt(true);
				}
				else {
					allAir = true;
				}
				if (allAir) {
					d.setDoneDesmelt(false);
				}
				if ((item.getType() != Material.AIR) && !bItems.contains(item.getType()) && !d.isDoneDesmelt()) {
					List<Recipe> recipes = Bukkit.getServer().getRecipesFor(item);
					if (!recipes.isEmpty()) {
						FurnaceRecipe fRecipe = null;
						for (Recipe r :  recipes) {
							if (r instanceof FurnaceRecipe) {
								fRecipe = (FurnaceRecipe) r;
								break;
							}
						}
						if (fRecipe != null) {
							invView.setItem(16, new ItemStack(Material.AIR));
							ItemStack input = fRecipe.getInput();
							ItemStack newInput;
							if (input.getDurability() > 100) {
								newInput = new ItemStack(input.getType(), item.getAmount(), (short) 0);
							}
							else {
								newInput = new ItemStack(input.getType(), item.getAmount(), input.getDurability());
							}
							if (Options.DESMELT_TIMER.bValue) {
								new DesmeltTimer(invView, newInput, main.getBarrier()).runTaskTimer(main, 0L, 5L);
							}
							else {
								invView.setItem(10, newInput);
							}
							d.setDoneDesmelt(true);
						}
					}
				}
			}
			main.removeDesmelts(toRemove);
		}
	}
}