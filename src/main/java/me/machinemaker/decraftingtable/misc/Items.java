package me.machinemaker.decraftingtable.misc;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static java.util.Objects.nonNull;

public enum Items {
    SPACER(Material.BLACK_STAINED_GLASS_PANE, " ");

    @Getter
    private final ItemStack item;

    Items(Material mat, int amount, String name) {
        item = new ItemStack(mat, amount);
        ItemMeta im = item.getItemMeta();
        if (nonNull(im)) {
            im.setDisplayName(name);
            item.setItemMeta(im);
        }
    }

    Items(Material mat, String name) {
        this(mat, 1, name);
    }
}
