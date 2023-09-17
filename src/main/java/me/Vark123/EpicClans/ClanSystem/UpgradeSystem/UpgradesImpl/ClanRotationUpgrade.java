package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class ClanRotationUpgrade extends AClanUpgrade<Integer> {

	private ItemStack item;

	public ClanRotationUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("rotations", levelRequirements);
		
		this.item = new ItemStack(Material.ENDER_PEARL);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§6§lRotacja klanowa");
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
