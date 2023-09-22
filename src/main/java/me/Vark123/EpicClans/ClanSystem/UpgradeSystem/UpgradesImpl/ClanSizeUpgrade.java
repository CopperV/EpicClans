package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class ClanSizeUpgrade extends AClanUpgrade<Integer> {

	private ItemStack item;

	public ClanSizeUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("players", levelRequirements);
		
		this.item = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§b§lMaksymalna liczba graczy");
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
		return 4 + level;
	}

}
