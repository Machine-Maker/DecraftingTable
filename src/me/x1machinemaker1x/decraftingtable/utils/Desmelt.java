package me.x1machinemaker1x.decraftingtable.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Desmelt {
	
	private boolean doneDesmelt;
	private Inventory inv;
	private Player p;
	
	public Desmelt(Inventory inv, Player p) {
		this.doneDesmelt = true;
		this.inv = inv;
		this.p = p;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void setDoneDesmelt(boolean doneDesmelt) {
		this.doneDesmelt = doneDesmelt;
	}
	
	public boolean isDoneDesmelt() {
		return doneDesmelt;
	}
}
