package me.Vark123.EpicClans.ClanSystem.AchievementSystem;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClanAchievement {

	private String id;
	private AchievementType type;
	private String target;
	
	private String display;
	private List<String> lore;
	private String difficulty;
	
	private AchievementReward reward;
	
	@Getter(value = AccessLevel.NONE)
	private ItemStack item;
	
	public ItemStack toItem() {
		if(item != null)
			return item.clone();
		item = new ItemStack(Material.RED_TERRACOTTA);{
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(display);
			
			List<String> lore = new LinkedList<>();
			lore.addAll(this.lore);
			lore.add(" ");
			lore.add("§6§l》§c Nagroda §6§l《");
			lore.add(" ");
			
			if(reward.getMoney() > 0)
				lore.add("  §4§l» §e§o"+String.format("%.2f", reward.getMoney())+"$");
			if(reward.getStygia() > 0)
				lore.add("  §4§l» §3§o"+reward.getStygia()+" stygii");
			if(reward.getCoins() > 0)
				lore.add("  §4§l» §4§o"+reward.getCoins()+" smoczych monet");
			if(reward.getBrylki() > 0)
				lore.add("  §4§l» §9§o"+reward.getBrylki()+" brylek rudy");
			if(reward.getPr() > 0)
				lore.add("  §4§l» §6§o"+reward.getPr()+" punktow rozwoju");
			
			im.setLore(lore);
			item.setItemMeta(im);
		}
		return item.clone();
	}
	
}
