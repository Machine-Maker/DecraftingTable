package me.x1machinemaker1x.decraftingtable.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class DesmelterRecipe {
	
	public static void createRecipe(Plugin pl) {
		ItemStack desmelter = CustomSkulls.getDesmelter();
		ShapedRecipe r = new ShapedRecipe(new NamespacedKey(pl, "Desmelter"), desmelter);
		r.shape("!^!", "~+~", "~~~");
		r.setIngredient('!', Material.IRON_INGOT);
		r.setIngredient('^', Material.DIAMOND);
		r.setIngredient('+', Material.FURNACE);
		r.setIngredient('~', Material.STONE);
		pl.getServer().addRecipe(r);
	}
}