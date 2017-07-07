package me.x1machinemaker1x.decraftingtable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

public class DecraftingTableRecipe {
	
	public static void createRecipe(Plugin pl) {
		ItemStack table;
		table = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta sm = (SkullMeta) table.getItemMeta();
		sm.setOwner("craftingtable");
		sm.setDisplayName(ChatColor.RESET + ChatColor.WHITE.toString() + "Decrafting Table");
		table.setItemMeta(sm);
		ShapedRecipe r = new ShapedRecipe(new NamespacedKey(pl, "DecraftingTable"), table);
		r.shape("!^!", "~¢~", "~~~");
		r.setIngredient('!', Material.IRON_INGOT);
		r.setIngredient('^', Material.DIAMOND);
		r.setIngredient('~', Material.WOOD);
		r.setIngredient('¢', Material.WORKBENCH);
		pl.getServer().addRecipe(r);
	}

}
