package me.machinemaker.decraftingtable;

import com.google.inject.Injector;
import me.machinemaker.decraftingtable.events.inventory.InventoryClick;
import me.machinemaker.decraftingtable.events.inventory.InventoryClose;
import me.machinemaker.decraftingtable.events.inventory.InventoryDrag;
import me.machinemaker.decraftingtable.events.player.PlayerInteract;
import me.machinemaker.decraftingtable.misc.DependencyInjection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DecraftingTable extends JavaPlugin {

    @Override
    public void onEnable() {
        Injector injector = new DependencyInjection(this).createInjector();

        registerEvents(injector);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void registerEvents(Injector injector) {
        PluginManager pm = this.getServer().getPluginManager();

        PlayerInteract playerInteract = new PlayerInteract();
        InventoryClick inventoryClick = new InventoryClick();
        InventoryDrag inventoryDrag = new InventoryDrag();
        InventoryClose inventoryClose = new InventoryClose();

        pm.registerEvents(playerInteract, this);
        pm.registerEvents(inventoryClick, this);
        pm.registerEvents(inventoryDrag, this);
        pm.registerEvents(inventoryClose, this);

        injector.injectMembers(inventoryClick);
    }
}
