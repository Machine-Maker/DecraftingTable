package me.x1machinemaker1x.decraftingtable.utils;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DesmeltTimer extends BukkitRunnable {
	
	private InventoryView invView;
	private int count;
	private ItemStack result;
	private ItemStack barrier;
	
	public DesmeltTimer(InventoryView invView, ItemStack result, ItemStack barrier) {
		this.invView = invView;
		this.result = result;
		this.barrier = barrier;
		count = 9;
	}
	
	@Override
	public void run() {
		if (count == 0) {
			invView.setItem(10, result);
			for (int i = 0; i < 27; i++) {
				if (i != 10 && i != 16) {
					invView.setItem(i, barrier);
				}
			}
			this.cancel();
		}
		else {
			invView.getItem(count - 1).setDurability((short) 5);
			invView.getItem(count + 8).setDurability((short) 5);
			invView.getItem(count + 17).setDurability((short) 5);
			count--;
		}
	}
}