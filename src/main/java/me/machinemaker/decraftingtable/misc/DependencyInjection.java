package me.machinemaker.decraftingtable.misc;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.machinemaker.decraftingtable.DecraftingTable;

public class DependencyInjection extends AbstractModule {

    private final DecraftingTable plugin;

    public DependencyInjection(DecraftingTable plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(DecraftingTable.class).toInstance(this.plugin);
    }
}
