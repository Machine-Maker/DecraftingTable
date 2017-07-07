package me.x1machinemaker1x.decraftingtable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Decraft {
	
	private boolean doneDecraft = true;
	private Inventory inv;
	private Player p;
	
	public Decraft(Player p, Inventory inv) {
		this.p = p;
		this.inv = inv;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void setDoneDecraft(boolean doneDecraft) {
		this.doneDecraft = doneDecraft;
	}
	
	public boolean isDoneDecraft() {
		return doneDecraft;
	}
}
