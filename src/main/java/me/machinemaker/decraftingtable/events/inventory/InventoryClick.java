package me.machinemaker.decraftingtable.events.inventory;

import com.google.inject.Inject;
import me.machinemaker.decraftingtable.DecraftingTable;
import me.machinemaker.decraftingtable.misc.Inventories;
import me.machinemaker.decraftingtable.misc.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class InventoryClick implements Listener {

    @Inject
    DecraftingTable plugin;

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

        if (Objects.equals(event.getCurrentItem(), Items.SPACER.getItem()) || (event.getSlot() != type.getInputSlot() && !(isNull(event.getCursor()) || event.getCursor().getType() == Material.AIR))) {
            event.setCancelled(true);
            return;
        }

        switch (type) {
            case DECRAFTING:
                if (event.getRawSlot() == type.getInputSlot() && nonNull(event.getCursor())) {
                    List<Recipe> recipes = Bukkit.getRecipesFor(event.getCursor()).stream().filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe).collect(Collectors.toList());
                    if (recipes.isEmpty()) {
                        event.getWhoClicked().sendMessage("No recipe for that"); // TODO: 6/19/2020 Lang
                        return;
                    }
                    Recipe main = recipes.get(0);
                    if (event.getCursor().getAmount() < main.getResult().getAmount()) {
                        event.getWhoClicked().sendMessage(String.format("Requires more input: %d", main.getResult().getAmount())); // TODO: 6/19/2020 Lang
                        return;
                    }
                    int decraftAmount = (int) Math.floor((double) event.getCursor().getAmount() / (double) main.getResult().getAmount());
                    if (main instanceof ShapedRecipe) {
                        ShapedRecipe recipe = (ShapedRecipe) main;

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
                    } else if (main instanceof ShapelessRecipe) {
                        ShapelessRecipe recipe = (ShapelessRecipe) main;

                        Iterator<ItemStack> iterator = recipe.getIngredientList().iterator();
                        for (int x = 0; x < 3; x ++) {
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
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ((Player) event.getWhoClicked()).updateInventory();
                            int newInputAmount = inputStack.getAmount() - (main.getResult().getAmount() * decraftAmount);
                            inputStack.setAmount(newInputAmount);
                            top.setItem(type.getInputSlot(), inputStack);
                            event.getWhoClicked().sendMessage("Decraft completed"); // TODO: 6/19/2020 Lang
                        }
                    }.runTask(this.plugin);
                }
        }
    }
}
