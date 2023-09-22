package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class ClanWarehouseUpgrade extends AClanUpgrade<Integer> {

	private ItemStack item;

	public ClanWarehouseUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("warehouse", levelRequirements);
		
		this.item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§2§lMagazyn klanowy");
		item.setItemMeta(im);
		
		NBTItem nbt = new NBTItem(item);
		nbt.setString("clan-upgrade-id", id);
		nbt.applyNBT(item);
	}
	
	@Override
	public ItemStack toItem() {
		return item.clone();
	}

	@Override
	public Integer getUpgradeBonus(int level) {
		return level;
	}

}
