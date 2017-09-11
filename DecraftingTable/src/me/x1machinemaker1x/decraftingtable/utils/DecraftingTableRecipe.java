package me.x1machinemaker1x.decraftingtable.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class DecraftingTableRecipe {
	
	public static void createRecipe(Plugin pl) {
		ItemStack table = CustomSkulls.getDecraftingTable();
		ShapedRecipe r = new ShapedRecipe(new NamespacedKey(pl, "DecraftingTable"), table);
		r.shape("!^!", "~¢~", "~~~");
		r.setIngredient('!', Material.IRON_INGOT);
		r.setIngredient('^', Material.DIAMOND);
		r.setIngredient('~', Material.WOOD);
		r.setIngredient('¢', Material.WORKBENCH);
		pl.getServer().addRecipe(r);
	}

}
