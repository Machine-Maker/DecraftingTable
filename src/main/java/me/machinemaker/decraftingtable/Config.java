package me.machinemaker.decraftingtable;

import me.machinemaker.configmanager.BaseConfig;
import me.machinemaker.configmanager.annotations.NewConfig;
import me.machinemaker.configmanager.annotations.Path;
import me.machinemaker.configmanager.configs.ConfigFormat;

@NewConfig(name = "config", fileName = "config.yml", format = ConfigFormat.YAML)
public class Config extends BaseConfig {

    @Path("decrafting-table.enabled")
    public Boolean decraftEnabled = true;

    @Path("decrafting-table.exp-cost")
    public Integer decraftExpCost = 0;

    @Path("disenchanter.enabled")
    public Boolean disenchanterEnabled = true;

    @Path("disenchanter.exp-cost")
    public Integer disenchanterExpCost = 0;

    @Path("stone-combiner.enabled")
    public Boolean combinerEnabled = true;

    @Path("stone-combiner.exp-cost")
    public Integer combinerExpCost = 0;
}
