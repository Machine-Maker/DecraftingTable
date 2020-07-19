package me.machinemaker.decraftingtable.events.player;

import com.google.inject.Inject;
import me.machinemaker.decraftingtable.Config;
import me.machinemaker.decraftingtable.misc.Inventories;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static java.util.Objects.isNull;

public class PlayerInteract implements Listener {

    @Inject
    Config config;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK || isNull(event.getClickedBlock())) return;
        Block b = event.getClickedBlock();
        Inventories inv = Inventories.which(b.getType());
        if (inv != null && event.getPlayer().isSneaking() && inv.isEnabled()) {
            event.setCancelled(true);
            ChatColor levelColor = event.getPlayer().getLevel() < inv.getLevelRequirement() ? ChatColor.RED : ChatColor.GREEN;
            event.getPlayer().openInventory(inv.create(ChatColor.BLACK + " -- " + levelColor + inv.getLevelRequirement() + " Levels"));
        }
    }
}
