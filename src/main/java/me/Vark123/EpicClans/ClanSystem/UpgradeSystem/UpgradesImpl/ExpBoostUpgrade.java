package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class ExpBoostUpgrade extends AClanUpgrade<Double> {

	private ItemStack item;

	public ExpBoostUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("exp", levelRequirements);
		
		this.item = new ItemStack(Material.EXPERIENCE_BOTTLE);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§6§lDzielone doswiadczenie");
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
	public Double getUpgradeBonus(int level) {
		return 0.02 + 0.02*level;
	}

}
