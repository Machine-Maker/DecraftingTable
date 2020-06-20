package me.machinemaker.decraftingtable.events.inventory;

import me.machinemaker.decraftingtable.misc.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDrag implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (Inventories.isCustom(event.getView())) {
            event.setCancelled(event.getRawSlots().stream().anyMatch(slot -> slot < event.getView().getTopInventory().getSize()));
        }
    }
}
