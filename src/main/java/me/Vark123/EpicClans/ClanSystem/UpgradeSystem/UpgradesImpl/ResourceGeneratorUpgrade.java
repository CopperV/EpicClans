package me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGenerator;
import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGeneratorManager;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;


public class ResourceGeneratorUpgrade extends AClanUpgrade<ResourceGenerator> {

	private ItemStack item;

	public ResourceGeneratorUpgrade(Collection<UpgradeRequirements> levelRequirements) {
		super("generator", levelRequirements);
		
		this.item = new ItemStack(Material.GOLD_INGOT);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§a§lGenerator surowcow");
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
	public ResourceGenerator getUpgradeBonus(int level) {
		return ResourceGeneratorManager.get().getGenerators().get(level);
	}

}
