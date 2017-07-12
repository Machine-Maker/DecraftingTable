package me.x1machinemaker1x.decraftingtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.x1machinemaker1x.decraftingtable.Updater.UpdateType;
import me.x1machinemaker1x.decraftingtable.events.InventoryClick;
import me.x1machinemaker1x.decraftingtable.events.PlayerInteract;
import mkremins.fanciful.FancyMessage;

public class DecraftingTable extends JavaPlugin implements 
Listener, CommandExecutor {
	
	public DecraftingTable() { }
	
	public static DecraftingTable instance;
	
	public static DecraftingTable getInstance() {
		return instance;
	}
	
	private boolean checkForUpdates;
	private boolean downloadUpdates;
	static boolean decraftEnchants;
	static boolean useLuckAttribute;
	static double enchantDecraftChance;
	static double percentEnchantsDecrafted;
	static List<String> bannedItems;
	
	Logger logger;
	
	ItemStack barrier;
	List<Integer> rs;
	List<Decraft> decrafts;
	List<Decraft> toRemove;
	List<Material> iWD;
	
	public void onEnable() {
		
		instance = this;
		
		logger = Bukkit.getLogger();
				
		getCommand("decraft").setExecutor(this);
		
		DecraftingTableRecipe.createRecipe(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		
		checkForUpdates = getConfig().getBoolean("CheckForUpdates");
		downloadUpdates = getConfig().getBoolean("DownloadUpdats");
		decraftEnchants = getConfig().getBoolean("DecraftEnchants");
		useLuckAttribute = getConfig().getBoolean("UseLuckPotions");
		enchantDecraftChance = getConfig().getDouble("EnchantmentDecraftChance");
		percentEnchantsDecrafted = getConfig().getDouble("PercentOfEnchantmentsDecrafted");
		bannedItems = getConfig().getStringList("BlacklistedItems");
		
		//UPDATE CHECKER
		UpdateType type = null;
		if (checkForUpdates) {
			if (downloadUpdates) {
				type = UpdateType.DEFAULT;
			}
			else {
				type = UpdateType.NO_DOWNLOAD;
			}
		}
		if (type != null) {
			Updater updater = new Updater(this, 271134, this.getFile(), type, true);
			switch (updater.getResult()) {
			case SUCCESS:
				logger.info("The newest version of DecraftingTable has been successfully downloaded!");
				break;
			case DISABLED:
				logger.info("Automatic updating for DecraftingTable has been disabled by a server administrator!");
				break;
			case FAIL_APIKEY:
				logger.severe("The API key for DecraftingTable has been improperly setup!");
				break;
			case FAIL_BADID:
				logger.severe("The project ID provided by the plugin is invalid and does not exist on dev.bukkit.org!");
				break;
			case FAIL_DBO:
				logger.severe("For some reason, the plugin was unable to contact dev.bukkit.org!");
				break;
			case FAIL_DOWNLOAD:
				logger.severe("A new version of DecraftingTable was found but the plugin was unable to download the new version!");
				break;
			case FAIL_NOVERSION:
				logger.severe("The file found of dev.bukkit.org did not contain a recognizable version!");
				break;
			case NO_UPDATE:
				logger.info("No update was found and nothing was downloaded!");
				break;
			case UPDATE_AVAILABLE:
				logger.info("An update was found but nothing was downloaded becuase updates are disabled in the config.yml file!");
				logger.info("You can download the new update here: " + updater.getLatestFileLink());
				break;
			default:
				logger.warning("This should never happen :)");
				break; 
			}
		}
		else {
			logger.info("Version checking and automatic downloads are disabled in the config!");
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
		
		barrier = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = barrier.getItemMeta();
		im.setDisplayName(" ");
		barrier.setItemMeta(im);
		
		rs = new ArrayList<Integer>();
		rs.add(11); rs.add(12); rs.add(13); rs.add(18); rs.add(20); rs.add(21); rs.add(22); rs.add(29); rs.add(30); rs.add(31);
		
		decrafts = new ArrayList<Decraft>();
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
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if (!decrafts.isEmpty()) {
					for (Decraft d : decrafts) {
						InventoryView invView = d.getPlayer().getOpenInventory();
						if (!invView.getTitle().equals(ChatColor.BLUE + "Decrafting Table")) { //Checks if player is still in decraft table.
							toRemove.add(d);
							continue;
						}
						ItemStack item = invView.getItem(25);
						List<Material> bItems = new ArrayList<Material>();
						for (String i : bannedItems) {
							bItems.add(Material.getMaterial(i));
						}
						ItemStack eBook = null;
						boolean allAir = true;
						for (int i = 0; i < rs.size(); i++) {
							if (!invView.getItem(DecraftingTable.getInstance().getRS().get(i)).getType().equals(Material.AIR)) {
								allAir = false;
								d.setDoneDecraft(true);
								break;
							}
							else {
								allAir = true;
							}
						}
						if (allAir) {
							d.setDoneDecraft(false);
						}
						if (!item.getType().equals(Material.AIR) && !bItems.contains(item.getType()) && !d.isDoneDecraft()) { //Checks if there is an item in the uncraft slot
							double chance = 1.0;
							if (iWD.contains(item.getType()) && (item.getDurability() != 0 || !item.getEnchantments().isEmpty())) {
								chance = new Double(item.getType().getMaxDurability() - item.getDurability())/new Double(item.getType().getMaxDurability());
								if (decraftEnchants && Math.random() < enchantDecraftChance) {
									eBook = new ItemStack(Material.ENCHANTED_BOOK);
									Map<Enchantment, Integer> enchants = item.getEnchantments();
									if (!enchants.isEmpty()) {
										EnchantmentStorageMeta meta = (EnchantmentStorageMeta) eBook.getItemMeta();
										for (Enchantment e : enchants.keySet()) {
											for (int i = 1 ; i <= enchants.get(e); i++) {
												if (Math.random() < percentEnchantsDecrafted) {
													meta.addStoredEnchant(e, i, true);
												}
											}
										}
										eBook.setItemMeta(meta);
									}
								}
								item.setDurability((short) 0);
							}
							if (useLuckAttribute) {
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
										else if (ingredient.getMaxStackSize() == 16) {
											amount = 16;
											break;
										}
									}
									for (int i = 0; i < items.length; i++) {
										if (items[i] == null) continue;
										if (Math.random() < chance) {
											ItemStack ingredient = (ItemStack) items[i];
											if (ingredient.getDurability() > 100) {
												invView.setItem(rs.get(i), new ItemStack(ingredient.getType(), amount));
											}
											else {
												invView.setItem(rs.get(i), new ItemStack(ingredient.getType(), amount, ingredient.getDurability()));
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
//						else if (d.isDoneDecraft() == false) {
//							for (int i = 0; i < DecraftingTable.getInstance().getRS().size(); i++) {
//								invView.setItem(DecraftingTable.getInstance().getRS().get(i), new ItemStack(Material.AIR));
//							}
//						}
						
					}
					decrafts.removeAll(toRemove);
				}
			}
		}, 20L, 20L);
	}
	
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length == 0 || !args[0].equals("reload")) {
			new FancyMessage("DecraftingTable V. ")
					.color(ChatColor.GOLD)
					.then(getDescription().getVersion())
					.color(ChatColor.DARK_RED)
					.then(" by ")
					.color(ChatColor.GOLD)
					.then(getDescription().getAuthors().toArray()[0] + "")
					.color(ChatColor.DARK_RED)
					.send(cs);
					
			new FancyMessage("Creates a recipe for a ")
					.color(ChatColor.GOLD)
					.then("Decrafting Table")
					.color(ChatColor.DARK_RED).style(ChatColor.UNDERLINE)
					.then(" that will decraft all craftable objects")
					.color(ChatColor.GOLD)
					.send(cs);
			
			new FancyMessage("Project Page: ")
					.color(ChatColor.GOLD)
					.then("https://dev.bukkit.org/projects/decraftingtable")
					.link("https://dev.bukkit.org/projects/decraftingtable")
					.color(ChatColor.BLUE).style(ChatColor.UNDERLINE)
					.tooltip("DecraftingTable Bukkit Project Page")
					.send(cs);;
			return true;
		}
		else {
			DecraftingTable.getInstance().reloadConfig();
			decraftEnchants = getConfig().getBoolean("DecraftEnchants");
			useLuckAttribute = getConfig().getBoolean("UseLuckPotions");
			enchantDecraftChance = getConfig().getDouble("EnchantmentDecraftChance");
			percentEnchantsDecrafted = getConfig().getDouble("PercentOfEnchantmentsDecrafted");
			bannedItems = getConfig().getStringList("BlacklistedItems");
			cs.sendMessage(ChatColor.GREEN + "The config has been reloaded!");
			return true;
		}
	}
	
	public ItemStack getBarrier() {
		return barrier;
	}
	
	public List<Integer> getRS() {
		return rs;
	}
	
	public List<Decraft> getDecrafts() {
		return decrafts;
	}
	
	public void addDecraft(Player p, Inventory inv) {
		decrafts.add(new Decraft(p, inv));
	}
	
	public Decraft getDecraft(Player p) {
		for (Decraft d : decrafts) {
			if (d.getPlayer().getUniqueId().equals(p.getUniqueId())) return d;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (!(e.getBlockAgainst().getState() instanceof Skull))	return;
		Skull skull = (Skull) e.getBlockAgainst().getState();
		if (!skull.getOwner().equals("craftingtable")) return;
		e.setCancelled(true);
	}

}
