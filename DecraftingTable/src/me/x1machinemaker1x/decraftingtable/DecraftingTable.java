package me.x1machinemaker1x.decraftingtable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.x1machinemaker1x.decraftingtable.Updater.UpdateType;
import me.x1machinemaker1x.decraftingtable.events.BlockBreak_Place;
import me.x1machinemaker1x.decraftingtable.events.CraftItem;
import me.x1machinemaker1x.decraftingtable.events.InventoryClick;
import me.x1machinemaker1x.decraftingtable.events.PlayerInteract;
import me.x1machinemaker1x.decraftingtable.utils.Decraft;
import me.x1machinemaker1x.decraftingtable.utils.DecraftingTableRecipe;
import me.x1machinemaker1x.decraftingtable.utils.Desmelt;
import me.x1machinemaker1x.decraftingtable.utils.DesmelterRecipe;
import me.x1machinemaker1x.decraftingtable.utils.Metrics;
import mkremins.fanciful.FancyMessage;

public class DecraftingTable extends JavaPlugin implements CommandExecutor {
	
	public DecraftingTable() { }
	
	public static DecraftingTable instance;
	
	public static DecraftingTable getInstance() {
		return instance;
	}
	
	Logger logger;
	
	ItemStack barrier;
	List<Integer> rs;
	List<Decraft> decrafts;
	List<Desmelt> desmelts;
	
	@SuppressWarnings("unused")
	public void onEnable() {
		
		instance = this;
		
		logger = this.getLogger();
				
		getCommand("decraft").setExecutor(this);
		
		if (!DecraftingTableRecipe.createRecipe(this)) {
			return;
		}
		DesmelterRecipe.createRecipe(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		Options.updateValues();
		
		if (Options.USE_METRICS.bValue) {
			Metrics metrics =  new Metrics(this);
			this.getLogger().info("Plugin metrics have been enabled!");
		}
		else {
			this.getLogger().info("Plugin metrics have been disabled");
		}
		
		//UPDATE CHECKER
		UpdateType type = null;
		if (Options.CHECK_FOR_UPDATES.bValue) {
			if (Options.DOWNLOAD_UPDATES.bValue) {
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
		
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
		getServer().getPluginManager().registerEvents(new BlockBreak_Place(), this);
		getServer().getPluginManager().registerEvents(new CraftItem(), this);
		
		barrier = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = barrier.getItemMeta();
		im.setDisplayName(" ");
		barrier.setItemMeta(im);
		
		rs = new ArrayList<Integer>();
		rs.add(11); rs.add(12); rs.add(13); rs.add(20); rs.add(21); rs.add(22); rs.add(29); rs.add(30); rs.add(31); rs.add(18); 
		
		decrafts = new ArrayList<Decraft>();
		desmelts = new ArrayList<Desmelt>();
		
		new CheckDecrafts(this).runTaskTimer(this, 0L, 20L); //Decrafting checker
		new CheckDesmelts(this).runTaskTimer(this, 0L, 20L);
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
			Options.updateValues();
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
	
	public List<Desmelt> getDesmelts() {
		return desmelts;
	}
	
	public void addDecraft(Player p, Inventory inv) {
		decrafts.add(new Decraft(p, inv));
	}
	
	public void addDesmelt(Player p, Inventory inv) {
		desmelts.add(new Desmelt(inv, p));
	}
	
	public Decraft getDecraft(Player p) {
		for (Decraft d : decrafts) {
			if (d.getPlayer().getUniqueId().equals(p.getUniqueId())) return d;
		}
		return null;
	}
	
	public Desmelt getDesmelt(Player p) {
		for (Desmelt d : desmelts) {
			if (d.getPlayer().getUniqueId().equals(p.getUniqueId())) return d;
		}
		return null;
	}
	
	public void removeDecrafts(List<Decraft> toRemove) {
		decrafts.removeAll(toRemove);
	}
	
	public void removeDesmelts(List<Desmelt> toRemove) {
		desmelts.removeAll(toRemove);
	}
	
	public enum Options {
		USE_METRICS("UseMetrics", "bool"),
		CHECK_FOR_UPDATES("CheckForUpdates", "bool"),
		DOWNLOAD_UPDATES("DownloadUpdates", "bool"),
		DECRAFT_ENCHANTS("DecraftEnchants", "bool"),
		USE_LUCK_ATTRIBUTE("UseLuckPotions", "bool"),
		ENCHANT_DECRAFT_CHANCE("EnchantmentDecraftChance", "double"),
		PERCENT_ENCHANTMENTS_DECRAFTED("PercentOfEnchantmentsDecrafted", "double"),
		DESMELT_TIMER("DesmeltTimer", "bool"),
		BANNED_ITEMS_DECRAFTING("BlacklistedItems.Decrafting", "string_list"),
		BANNED_ITEMS_DESMELTING("BlacklistedItems.Desmelting", "string_list");
		
		private String path;
		private String type;
		boolean bValue;
		float fValue;
		List<String> bItems;
		
		private Options(String path, String type) {
			this.type = type;
			this.path = path;
		}
		
		public static void updateValues() {
			for (Options o : Options.values()) {
				switch (o.type) {
				case "bool":
					o.bValue = instance.getConfig().getBoolean(o.path);
					break;
				case "double":
					o.fValue = (float) instance.getConfig().getDouble(o.path);
					break;
				case "string_list":
					o.bItems = instance.getConfig().getStringList(o.path);
					break;
				}
			}
		}
	}
}