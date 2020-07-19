package me.machinemaker.decraftingtable;

import com.google.common.collect.Sets;
import com.google.inject.Injector;
import me.machinemaker.decraftingtable.events.inventory.InventoryClick;
import me.machinemaker.decraftingtable.events.inventory.InventoryClose;
import me.machinemaker.decraftingtable.events.inventory.InventoryDrag;
import me.machinemaker.decraftingtable.events.player.PlayerInteract;
import me.machinemaker.decraftingtable.misc.DependencyInjection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public final class DecraftingTable extends JavaPlugin {

    private final Config config = new Config();
    private final Collection<Listener> events = Sets.newHashSet(
            new PlayerInteract(),
            new InventoryClick(),
            new InventoryDrag(),
            new InventoryClose()
    );

    @Override
    public void onEnable() {
        config.init(this);
        Injector injector = new DependencyInjection(this, config).createInjector();

        registerEvents(injector);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void registerEvents(Injector injector) {
        PluginManager pm = this.getServer().getPluginManager();
        events.forEach(event -> {
            pm.registerEvents(event, this);
            injector.injectMembers(event);
        });
    }
}
