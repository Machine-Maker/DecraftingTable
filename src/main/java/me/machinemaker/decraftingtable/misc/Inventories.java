package me.machinemaker.decraftingtable.misc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;
import java.util.List;

public enum Inventories implements IInventory {

    DECRAFTING(ChatColor.BLUE + "Decrafting Table", 25) {
        @Override
        public Inventory create() {
            Inventory inv = Bukkit.createInventory(null, 45, this.getInvName());
            List<Integer> rs = Arrays.asList(11, 12, 13, 20, 21, 22, 29, 30, 31, 18);
            for (int i = 0; i < 45; i++) {
                if (!rs.contains(i) && i != 25) inv.setItem(i, Items.SPACER.getItem());
            }
            this.setInvName(this.getInvName());
            return inv;
        }
    };

    @Setter @Getter
    private String invName;

    @Setter @Getter
    private int inputSlot;

    Inventories(String name, int inputSlot) {
        this.setInvName(name);
        this.setInputSlot(inputSlot);
    }

    @Override
    public boolean isEqual(InventoryView inventory) {
        return inventory.getTitle().equals(this.getInvName());
    }

    public static boolean isCustom(InventoryView inventoryView) {
        return Arrays.stream(Inventories.values()).anyMatch(inv -> inv.getInvName().equals(inventoryView.getTitle()));
    }

    public static Inventories which(InventoryView inventoryView) {
        return Arrays.stream(Inventories.values()).filter(type -> type.getInvName().equals(inventoryView.getTitle())).findFirst().orElse(null);
    }
}
