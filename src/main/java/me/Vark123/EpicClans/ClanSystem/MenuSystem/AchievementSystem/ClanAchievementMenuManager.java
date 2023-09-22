package me.Vark123.EpicClans.ClanSystem.MenuSystem.AchievementSystem;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementManager;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.ClanAchievement;

@Getter
public class ClanAchievementMenuManager {

	private static final ClanAchievementMenuManager inst = new ClanAchievementMenuManager();
	
	private final ItemStack previous;
	private final ItemStack next;
	
	private final ItemStack empty;
	private final ItemStack back;
	
	private final int ITEMS_PER_PAGE = 45;
	
	private ClanAchievementMenuManager() {
		previous = new ItemStack(Material.ARROW);{
			ItemMeta im = previous.getItemMeta();
			im.setDisplayName("§fPoprzednia");
			previous.setItemMeta(im);
		}
		next = new ItemStack(Material.ARROW);{
			ItemMeta im = next.getItemMeta();
			im.setDisplayName("§fNastepna");
			next.setItemMeta(im);
		}

		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPowrot");
			back.setItemMeta(im);
		}
	}
	
	public static final ClanAchievementMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p, Clan clan, int page) {
		RyseInventory.builder()
			.title("§b§lOSIAGNIECIA KLANOWE")
			.rows(6)
			.disableUpdateTask()
			.listener(ClanAchievementMenuEvents.getEvents().getClickEvent())
			.provider(getProvider(clan, page))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getProvider(Clan clan, int page) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				List<ClanAchievement> achievements = AchievementManager.get().getAchievements();
				int start = page*ITEMS_PER_PAGE;
				int end = (page+1)*ITEMS_PER_PAGE;
				if(start < 0)
					start = 0;
				if(end > achievements.size())
					end = achievements.size();
				
				List<ItemStack> items = new LinkedList<>();
				for(int i = start; i < end; ++i) {
					ClanAchievement achievement = achievements.get(i);
					ItemStack it = achievement.toItem();
					if(clan.getCompletedAchievements().contains(achievement.getId()))
						it.setType(Material.GREEN_TERRACOTTA);
					items.add(it);
				}
				
				ItemStack pageInfo = new ItemStack(Material.PAPER);{
					ItemMeta im = pageInfo.getItemMeta();
					im.setDisplayName("§f§lStrona "+(page+1));
					pageInfo.setItemMeta(im);
					
					NBTItem nbt = new NBTItem(pageInfo);
					nbt.setInteger("log-page", page);
					nbt.applyNBT(pageInfo);
				}
				
				for(int i = 0; i < 9; ++i) {
					contents.set(ITEMS_PER_PAGE+i, empty);
				}
				contents.set(49, pageInfo);
				if(page > 0) 
					contents.set(47, previous);
				if(end < achievements.size())
					contents.set(51, next);
				
				for(int i = 0; i < (end-start); ++i) {
					ItemStack it = items.get(i);
					contents.set(i, it);
				}
			}
		};
	}
	
}
