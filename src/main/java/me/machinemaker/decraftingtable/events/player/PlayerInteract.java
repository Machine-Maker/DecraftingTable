package me.machinemaker.decraftingtable.events.player;

import me.machinemaker.decraftingtable.misc.Inventories;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static java.util.Objects.isNull;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK || isNull(event.getClickedBlock())) return;
        Block b = event.getClickedBlock();
        if (b.getType() == Material.CRAFTING_TABLE && event.getPlayer().isSneaking()) {
            event.setCancelled(true);
            event.getPlayer().openInventory(Inventories.DECRAFTING.create());
        }
    }
}
