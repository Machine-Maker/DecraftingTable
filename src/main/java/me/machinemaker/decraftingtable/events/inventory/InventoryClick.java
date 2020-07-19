package me.machinemaker.decraftingtable.events.inventory;

import com.google.inject.Inject;
import me.machinemaker.decraftingtable.Config;
import me.machinemaker.decraftingtable.DecraftingTable;
import me.machinemaker.decraftingtable.misc.Inventories;
import me.machinemaker.decraftingtable.misc.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class InventoryClick implements Listener {

    @Inject
    DecraftingTable plugin;

    @Inject
    Config config;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Inventories.isCustom(event.getView())) return;

        Inventories type = Inventories.which(event.getView());
        Inventory top = event.getView().getTopInventory();
        if (event.getRawSlot() >= top.getSize()) {
            if (event.isShiftClick())
                event.setCancelled(true);
            return;
        }

        if (Items.SPACER.getItem().isSimilar(event.getCurrentItem()) || (event.getSlot() != type.getInputSlot() && !(isNull(event.getCursor()) || event.getCursor().getType() == Material.AIR))) {
            event.setCancelled(true);
            return;
        }

        if (event.getRawSlot() != type.getInputSlot() || isNull(event.getCursor()) || event.getCursor().getType() == Material.AIR) return;

        if (type == Inventories.DISENCHANTER) {
            ItemStack tool = event.getCursor().clone();
            if (tool.getEnchantments().isEmpty()) return;
            ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
            if (meta == null) throw new IllegalStateException("This won't happen");
            tool.getEnchantments().forEach((enchantment, level) -> {
                meta.addStoredEnchant(enchantment, level, true);
                tool.removeEnchantment(enchantment);
            });
            enchantedBook.setItemMeta(meta);
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                top.setItem(15, null);
                top.setItem(12, tool);
                top.setItem(10, enchantedBook);
                // TODO: 7/17/2020 Lang
            });
            return;
        }

        List<Recipe> recipes = Bukkit.getRecipesFor(event.getCursor()).stream().filter(type::isValidRecipe).collect(Collectors.toList());
        if (recipes.isEmpty()) {
            event.getWhoClicked().sendMessage("No recipe for that"); // TODO: 6/19/2020 Lang
            return;
        }
        if (((Player) event.getWhoClicked()).getLevel() < type.getLevelRequirement()) {
            event.getWhoClicked().sendMessage("Not enough levels"); // TODO: 7/17/2020 Lang
            return;
        }
        Recipe usedRecipe = recipes.get(0);
        if (event.getCursor().getAmount() < usedRecipe.getResult().getAmount()) {
            event.getWhoClicked().sendMessage(String.format("Requires more input: %d", usedRecipe.getResult().getAmount())); // TODO: 6/19/2020 Lang
            return;
        }
        int decraftAmount = 0;
        int newInputAmount = 0;
        switch (event.getAction()) {
            case PLACE_ALL:
                decraftAmount = (int) Math.floor((double) event.getCursor().getAmount() / (double) usedRecipe.getResult().getAmount());
                newInputAmount = event.getCursor().getAmount() - (usedRecipe.getResult().getAmount() * decraftAmount);
                break;
            case PLACE_ONE:
                decraftAmount = 1;
                newInputAmount = 0;
                break;
            default:
                plugin.getLogger().severe(String.format("Unexpected inventory action!!! %s", event.getAction()));
                return;
        }

        switch (type) {
            case DECRAFTING: {
                if (usedRecipe instanceof ShapedRecipe) {
                    ShapedRecipe recipe = (ShapedRecipe) usedRecipe;

                    ItemStack[][] result = new ItemStack[3][3];
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (recipe.getShape().length <= y || recipe.getShape()[y].length() <= x)
                                continue;
                            result[x][y] = recipe.getIngredientMap().get(recipe.getShape()[y].charAt(x));
                            if (nonNull(result[x][y]))
                                result[x][y].setAmount(result[x][y].getAmount() * decraftAmount);
                            top.setItem((9 * y) + 9 + 2 + x, result[x][y]);
                        }
                    }
                } else if (usedRecipe instanceof ShapelessRecipe) {
                    ShapelessRecipe recipe = (ShapelessRecipe) usedRecipe;

                    Iterator<ItemStack> iterator = recipe.getIngredientList().iterator();
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            ItemStack itemStack;
                            if (iterator.hasNext())
                                itemStack = iterator.next();
                            else itemStack = null;
                            if (nonNull(itemStack))
                                itemStack.setAmount(itemStack.getAmount() * decraftAmount);
                            top.setItem((9 * y) + 9 + 2 + x, itemStack);
                        }
                    }
                }

                ItemStack inputStack = event.getCursor().clone();
                final int finalNewInputAmount = newInputAmount;
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    ((Player) event.getWhoClicked()).updateInventory();
                    inputStack.setAmount(finalNewInputAmount);
                    top.setItem(type.getInputSlot(), inputStack);
                    event.getWhoClicked().sendMessage("Decraft completed"); // TODO: 6/19/2020 Lang
                });
                break;
            }
            case STONE_COMBINER:
                StonecuttingRecipe recipe = (StonecuttingRecipe) usedRecipe;
                ItemStack inputStack = event.getCursor().clone();
                ItemStack outputStack = recipe.getInput().clone();
                outputStack.setAmount(recipe.getInput().getAmount() * decraftAmount);
                inputStack.setAmount(newInputAmount);
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    top.setItem(11, outputStack);
                    top.setItem(15, inputStack);
//                    ((Player) event.getWhoClicked()).updateInventory();
                    // TODO: 7/17/2020 Lang
                });

        }
    }
}
