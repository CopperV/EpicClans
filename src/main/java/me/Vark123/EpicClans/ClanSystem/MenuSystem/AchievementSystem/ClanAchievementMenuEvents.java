package me.Vark123.EpicClans.ClanSystem.MenuSystem.AchievementSystem;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class ClanAchievementMenuEvents {

	private static final ClanAchievementMenuEvents container = new ClanAchievementMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	
	private ClanAchievementMenuEvents() {
		this.clickEvent = clickEventCreator();
	}
	
	public static final ClanAchievementMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> clickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null 
					|| it.getType().equals(Material.AIR)
					|| it.equals(ClanAchievementMenuManager.get().getEmpty()))
				return;
			
			Player p = (Player) e.getWhoClicked();
			ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
			if(cPlayer.getClan().isEmpty()) {
				p.closeInventory();
				return;
			}
			Clan clan = cPlayer.getClan().get();
			
			ItemStack pageIt = inv.getItem(49);
			NBTItem nbtPage = new NBTItem(pageIt);
			if(!nbtPage.hasTag("log-page"))
				return;
			int page = nbtPage.getInteger("log-page");
			
			if(it.equals(ClanAchievementMenuManager.get().getPrevious())) {
				ClanAchievementMenuManager.get().openMenu(p, clan, page-1);
				return;
			}
			if(it.equals(ClanAchievementMenuManager.get().getNext())) {
				ClanAchievementMenuManager.get().openMenu(p, clan, page+1);
				return;
			}
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
