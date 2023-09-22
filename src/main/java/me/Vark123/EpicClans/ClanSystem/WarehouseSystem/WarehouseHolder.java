package me.Vark123.EpicClans.ClanSystem.WarehouseSystem;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WarehouseHolder implements InventoryHolder {

	private int warehouseNumber;
	
	@Override
	public Inventory getInventory() {
		return null;
	}

}
