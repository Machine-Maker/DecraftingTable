package me.x1machinemaker1x.decraftingtable.utils;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntitySkull;

public class CustomSkulls {
	
	private static String CRAFTING_SKULL_URL = "http://textures.minecraft.net/texture/4c683c87ec1aba12dbdebc99747e4c18432e95ba27f975a9ab73d9ddd59a";
	private static String FURNACE_SKULL_URL = "http://textures.minecraft.net/texture/2347278ca72dc319b2249c951882983393dde0204afc6434bd326f3ab56b2781";
	
	/**
     * Return a skull from a url, and a displayName
     * 
     * @param url skin url
     * @param name displayName
     */
    public static ItemStack getCustomSkull(String url, String name) {
    	Base64 base64 = new Base64();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        byte[] encodedData = base64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        headMeta.setDisplayName(ChatColor.RESET + name);
        head.setItemMeta(headMeta);
        return head;
    }
    
    /**
     * Gets the decrafting table itemstack
     * 
     * @return Decrafting table itemstack
     */
    public static ItemStack getDecraftingTable() {
    	return getCustomSkull(CRAFTING_SKULL_URL, "Decrafting Table");
    }
    
    /**
     * Gets the desmelter itemstack
     * @return Desmelter itemstack
     */
    public static ItemStack getDesmelter() {
    	return getCustomSkull(FURNACE_SKULL_URL, "Desmelter");
    }
    
    /**
     * Checks if the block is a decrafting table
     * 
     * @param b Block to check
     * @return true if the block is a decrafting table
     */
    public static boolean isDecraftingTable(Block b) {
    	if (b.getState() instanceof Skull) {
    		Skull skull = (Skull) b.getState();
    		if (skull.getSkullType() == SkullType.PLAYER) {
    			TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
    			String skinURL = Base64Coder.decodeString(skullTile.d().getCompound("Owner").getCompound("Properties").get("textures").toString().split("\"")[1]).split("\"")[1];
    			if (skinURL.equals(CRAFTING_SKULL_URL)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * Checks if the block is a desmelter
     * 
     * @param b Block to check
     * @return true if the block is a desmelter
     */
    public static boolean isDesmelter(Block b) {
    	if (b.getState() instanceof Skull) {
    		Skull skull = (Skull) b.getState();
    		if (skull.getSkullType() == SkullType.PLAYER) {
    			TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)skull.getWorld()).getHandle().getTileEntity(new BlockPosition(skull.getX(), skull.getY(), skull.getZ()));
    			String skinURL = Base64Coder.decodeString(skullTile.d().getCompound("Owner").getCompound("Properties").get("textures").toString().split("\"")[1]).split("\"")[1];
    			if (skinURL.equals(FURNACE_SKULL_URL)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
}