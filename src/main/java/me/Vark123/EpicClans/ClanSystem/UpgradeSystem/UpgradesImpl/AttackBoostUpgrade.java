package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class AttackBoostUpgrade extends AClanUpgrade<Double> {

	private ItemStack item;

	public AttackBoostUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("attack", levelRequirements);
		
		this.item = new ItemStack(Material.IRON_SWORD);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§c§lBonus do obrazen");
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
		return 0.02*level;
	}

}
