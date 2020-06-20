package me.machinemaker.decraftingtable.events.inventory;

import me.machinemaker.decraftingtable.misc.Inventories;
import me.machinemaker.decraftingtable.misc.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!Inventories.isCustom(event.getView())) return;
        List<ItemStack> toBeDropped = Arrays.stream(event.getInventory().getStorageContents()).filter(itemStack -> nonNull(itemStack) && !itemStack.equals(Items.SPACER.getItem())).collect(Collectors.toList());
        toBeDropped.forEach(item -> event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item));
    }
}
