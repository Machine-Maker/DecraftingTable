package me.machinemaker.decraftingtable.misc;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import lombok.Getter;
import me.machinemaker.decraftingtable.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
public enum Inventories {

    DECRAFTING(ChatColor.BLUE + "Decrafting Table", 25, 45, Sets.newHashSet(11, 12, 13, 20, 21, 22, 29, 30, 31), recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe, config -> config.decraftExpCost, config -> config.decraftEnabled, material -> material == Material.CRAFTING_TABLE),
    DISENCHANTER(ChatColor.BLUE + "Dis-Enchanter", 15, 27, Sets.newHashSet(10, 12), recipe -> true, config -> config.disenchanterExpCost, config -> config.disenchanterEnabled, Tag.ANVIL::isTagged),
    STONE_COMBINER(ChatColor.BLUE + "Stone Combiner", 15, 27, Sets.newHashSet(11), recipe -> recipe instanceof StonecuttingRecipe, config -> config.combinerExpCost, config -> config.combinerEnabled, material -> material == Material.STONECUTTER)
    ;

    @Inject
    private static Config staticConfig;

    private final String invName;
    private final int inputSlot;
    private final int size;
    private final Collection<Integer> reservedSlots;
    private final Predicate<Recipe> validRecipeType;
    private final Function<Config, Integer> levelRequirement;
    private final Predicate<Config> enabledCheck;
    private final Predicate<Material> materialCheck;

    Inventories(String name, int inputSlot, int size, Collection<Integer> reservedSlots, Predicate<Recipe> validRecipeType, Function<Config, Integer> levelRequirement, Predicate<Config> enabledCheck, Predicate<Material> materialCheck) {
        this.invName = name;
        this.inputSlot = inputSlot;
        this.size = size;
        this.reservedSlots = reservedSlots;
        this.validRecipeType = validRecipeType;
        this.levelRequirement = levelRequirement;
        this.enabledCheck = enabledCheck;
        this.materialCheck = materialCheck;
    }

    public Inventory create() {
        return create("");
    }

    public Inventory create(String titleExtra) {
        Inventory inv = Bukkit.createInventory(null, this.size, this.invName + titleExtra);
        for (int i = 0; i < this.size; i++) {
            if (!this.reservedSlots.contains(i) && i != this.inputSlot) inv.setItem(i, Items.SPACER.getItem());
        }
        return inv;
    }

    public boolean isValidRecipe(Recipe recipe) {
        return this.validRecipeType.test(recipe);
    }

    public int getLevelRequirement() {
        return this.levelRequirement.apply(Inventories.staticConfig);
    }

    public boolean isEnabled() {
        return this.enabledCheck.test(Inventories.staticConfig);
    }

    public static boolean isCustom(InventoryView inventoryView) {
        return Arrays.stream(Inventories.values()).anyMatch(inv -> inventoryView.getTitle().startsWith(inv.invName));
    }

    public static Inventories which(InventoryView inventoryView) {
        return Arrays.stream(Inventories.values()).filter(inv -> inventoryView.getTitle().startsWith(inv.invName)).findFirst().orElse(null);
    }

    public static Inventories which(Material material) {
        return Arrays.stream(Inventories.values()).filter(inv -> inv.materialCheck.test(material)).findFirst().orElse(null);
    }
}
