package me.machinemaker.decraftingtable.misc;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public interface IInventory {
    Inventory create();
    boolean isEqual(InventoryView inventory);
}
