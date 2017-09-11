package me.x1machinemaker1x.decraftingtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.x1machinemaker1x.decraftingtable.DecraftingTable.Options;
import me.x1machinemaker1x.decraftingtable.utils.Decraft;

public class CheckDecrafts extends BukkitRunnable {
	
	private DecraftingTable main;
	private List<Decraft> toRemove;
	private List<Material> iWD;
	
	public CheckDecrafts(DecraftingTable main) {
		this.main = main;
		toRemove = new ArrayList<Decraft>();
		iWD = new ArrayList<Material>();
		iWD.add(Material.IRON_SPADE);
		iWD.add(Material.IRON_PICKAXE);
		iWD.add(Material.IRON_AXE);
		iWD.add(Material.FLINT_AND_STEEL);
		iWD.add(Material.BOW);
		iWD.add(Material.IRON_SWORD);
		iWD.add(Material.WOOD_SWORD);
		iWD.add(Material.WOOD_SPADE);
		iWD.add(Material.WOOD_PICKAXE);
		iWD.add(Material.WOOD_AXE);
		iWD.add(Material.STONE_SWORD);
		iWD.add(Material.STONE_SPADE);
		iWD.add(Material.STONE_PICKAXE);
		iWD.add(Material.STONE_AXE);
		iWD.add(Material.DIAMOND_SWORD);
		iWD.add(Material.DIAMOND_SPADE);
		iWD.add(Material.DIAMOND_PICKAXE);
		iWD.add(Material.DIAMOND_AXE);
		iWD.add(Material.GOLD_SWORD);
		iWD.add(Material.GOLD_SPADE);
		iWD.add(Material.GOLD_PICKAXE);
		iWD.add(Material.GOLD_AXE);
		iWD.add(Material.WOOD_HOE);
		iWD.add(Material.STONE_HOE);
		iWD.add(Material.IRON_HOE);
		iWD.add(Material.DIAMOND_HOE);
		iWD.add(Material.GOLD_HOE);
		iWD.add(Material.LEATHER_HELMET);
		iWD.add(Material.LEATHER_CHESTPLATE);
		iWD.add(Material.LEATHER_LEGGINGS);
		iWD.add(Material.LEATHER_BOOTS);
		iWD.add(Material.IRON_HELMET);
		iWD.add(Material.IRON_CHESTPLATE);
		iWD.add(Material.IRON_LEGGINGS);
		iWD.add(Material.IRON_BOOTS);
		iWD.add(Material.DIAMOND_HELMET);
		iWD.add(Material.DIAMOND_CHESTPLATE);
		iWD.add(Material.DIAMOND_LEGGINGS);
		iWD.add(Material.DIAMOND_BOOTS);
		iWD.add(Material.GOLD_HELMET);
		iWD.add(Material.GOLD_CHESTPLATE);
		iWD.add(Material.GOLD_LEGGINGS);
		iWD.add(Material.GOLD_BOOTS);
		iWD.add(Material.FISHING_ROD);
		iWD.add(Material.SHEARS);
	}
	
	@Override
	public void run() {
		if (!main.getDecrafts().isEmpty()) {
			for (Decraft d : main.getDecrafts()) {
				InventoryView invView = d.getPlayer().getOpenInventory();
				if (!invView.getTitle().equals(ChatColor.BLUE + "Decrafting Table")) { //Checks if player is still in decraft table.
					toRemove.add(d);
					continue;
				}
				ItemStack item = invView.getItem(25);
				if (item.getType() == Material.SKULL_ITEM) {
					if (item.getDurability() == (short) 3) {
						continue;
					}
				}
				List<Material> bItems = new ArrayList<Material>();
				for (String i : Options.BANNED_ITEMS_DECRAFTING.bItems) {
					bItems.add(Material.matchMaterial(i));
				}
				ItemStack eBook = null;
				boolean allAir = true;
				for (int i = 0; i < main.getRS().size(); i++) {
					if (!invView.getItem(DecraftingTable.getInstance().getRS().get(i)).getType().equals(Material.AIR)) {
						allAir = false;
						d.setDoneDecraft(true);
						break;
					}
				}
				if (allAir) {
					d.setDoneDecraft(false);
				}
				if (!item.getType().equals(Material.AIR) && !bItems.contains(item.getType()) && !d.isDoneDecraft()) { //Checks if there is an item in the uncraft slot
					double chance = 1.0;
					if (iWD.contains(item.getType()) && (item.getDurability() != 0 || !item.getEnchantments().isEmpty())) {
						chance = new Double(item.getType().getMaxDurability() - item.getDurability())/new Double(item.getType().getMaxDurability());
						if (Options.DECRAFT_ENCHANTS.bValue && Math.random() < Options.ENCHANT_DECRAFT_CHANCE.fValue) {
							eBook = new ItemStack(Material.ENCHANTED_BOOK);
							Map<Enchantment, Integer> enchants = item.getEnchantments();
							if (!enchants.isEmpty()) {
								EnchantmentStorageMeta meta = (EnchantmentStorageMeta) eBook.getItemMeta();
								for (Enchantment e : enchants.keySet()) {
									for (int i = 1 ; i <= enchants.get(e); i++) {
										if (Math.random() < Options.PERCENT_ENCHANTMENTS_DECRAFTED.fValue) {
											meta.addStoredEnchant(e, i, true);
										}
									}
								}
								eBook.setItemMeta(meta);
							}
						}
						item.setDurability((short) 0);
					}
					if (Options.USE_LUCK_ATTRIBUTE.bValue) {
						AttributeInstance a = d.getPlayer().getAttribute(Attribute.GENERIC_LUCK);
						double luckChance = a.getValue()/1024.0;
						if (luckChance < 0) {
							if (chance - luckChance < 0) {
								chance = 0.0;
							}
							else {
								chance -= luckChance;
							}
						}
						else {
							if (chance + luckChance > 1) {
								chance = 1.0;
							}
							else {
								chance += luckChance;
							}
						}
					}
					List<Recipe> recipes = Bukkit.getServer().getRecipesFor(item);
					if (!recipes.isEmpty()) { //Checks if there are recipes for the item
						ShapedRecipe withShape = null;
						ShapelessRecipe withoutShape = null;
						if (item.getType().equals(Material.LEATHER_HELMET)) {
							withShape = (ShapedRecipe) recipes.get(1);
						}
						else {
							for (Recipe r : recipes) {
								if (r instanceof ShapedRecipe) {
									withShape = (ShapedRecipe) r;
									break;
								}
								else if (r instanceof ShapelessRecipe) {
									withoutShape = (ShapelessRecipe) r;
									break;
								}
							}
						}
						if (withShape != null || withoutShape != null) {
							Object[] items = new Object[] { };
							int recipeAmount = 0;
							int amount = 0;
							if (withoutShape == null) {
								if (withShape.getResult().getAmount() <= item.getAmount()) {
									recipeAmount = withShape.getResult().getAmount();
									amount = item.getAmount() / recipeAmount;
									Map<Character, ItemStack> i = withShape.getIngredientMap();
									items = i.values().toArray();
								}
							}
							else {
								if (withoutShape.getResult().getAmount() <= item.getAmount()) {
									recipeAmount = withoutShape.getResult().getAmount();
									amount = item.getAmount() / recipeAmount;
									items = withoutShape.getIngredientList().toArray();
								}
							}
							for (int i = 0; i < items.length; i++) {
								if (items[i] == null) continue;
								ItemStack ingredient = (ItemStack) items[i];
								if (ingredient.getMaxStackSize() == 1) {
									amount = 1;
									break;
								}
								else if (ingredient.getMaxStackSize() == 16 && item.getAmount() >= 16) {
									amount = 16;
									break;
								}
							}
							for (int i = 0; i < items.length; i++) {
								if (items[i] == null) continue;
								if (Math.random() < chance) {
									ItemStack ingredient = (ItemStack) items[i];
									if (ingredient.getDurability() > 100) {
										invView.setItem(main.getRS().get(i), new ItemStack(ingredient.getType(), amount));
									}
									else {
										invView.setItem(main.getRS().get(i), new ItemStack(ingredient.getType(), amount, ingredient.getDurability()));
									}
								}
							}
							if (eBook != null) {
								EnchantmentStorageMeta meta = (EnchantmentStorageMeta) eBook.getItemMeta();
								if (!meta.getStoredEnchants().isEmpty()) {
									invView.setItem(18, eBook);
								}
							}
							eBook = null;
							if (amount != 0) { //If amount = 0, that means there is not enough of the item to uncraft
								invView.setItem(25, new ItemStack(item.getType(), item.getAmount()-(amount * recipeAmount), (short) item.getDurability()));
								d.setDoneDecraft(true);	
							}						
						}
					}
				}
			}
			main.removeDecrafts(toRemove);;
		}
	}
}