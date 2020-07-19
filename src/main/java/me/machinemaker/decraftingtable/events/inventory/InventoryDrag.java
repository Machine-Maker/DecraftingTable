package me.machinemaker.decraftingtable.events.inventory;

import me.machinemaker.decraftingtable.misc.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDrag implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventories inv = Inventories.which(event.getView());
        if (inv != null) {
            if (event.getRawSlots().size() == 1 && event.getRawSlots().contains(inv.getInputSlot())) return;
            event.setCancelled(event.getRawSlots().stream().anyMatch(slot -> slot < event.getView().getTopInventory().getSize()));
        }
    }
}
