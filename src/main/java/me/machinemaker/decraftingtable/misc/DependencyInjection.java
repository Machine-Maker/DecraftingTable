package me.machinemaker.decraftingtable.misc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.machinemaker.decraftingtable.Config;
import me.machinemaker.decraftingtable.DecraftingTable;

public class DependencyInjection extends AbstractModule {

    private final DecraftingTable plugin;
    private final Config config;

    public DependencyInjection(DecraftingTable plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        requestStaticInjection(Inventories.class);
        this.bind(DecraftingTable.class).toInstance(this.plugin);
        this.bind(Config.class).toInstance(this.config);
    }
}
