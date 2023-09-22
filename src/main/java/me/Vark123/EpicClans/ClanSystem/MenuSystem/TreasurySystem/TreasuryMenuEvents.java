package me.Vark123.EpicClans.ClanSystem.MenuSystem.TreasurySystem;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public class TreasuryMenuEvents {

	private static final TreasuryMenuEvents container = new TreasuryMenuEvents();
	
	private final EventCreator<InventoryClickEvent> menuClickEvent;
	private final EventCreator<InventoryClickEvent> donateClickEvent;
	
	private TreasuryMenuEvents() {
		this.menuClickEvent = menuClickEventCreator();
		this.donateClickEvent = donateClickEventCreator();
	}
	
	public static final TreasuryMenuEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> menuClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;
			
			if(!it.equals(TreasuryMenuManager.get().getDonate()))
				return;
			
			Player p = (Player) e.getWhoClicked();
			TreasuryMenuManager.get().openDonate(p);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	private EventCreator<InventoryClickEvent> donateClickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;

			Player p = (Player) e.getWhoClicked();
			ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
			if(it.equals(TreasuryMenuManager.get().getBack())) {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					TreasuryMenuManager.get().openTreasury(p, clan);
				}, () -> {
					p.closeInventory();
				});
				return;
			}
			
			if(it.equals(TreasuryMenuManager.get().getBlack()))
				return;
			
			TreasuryMenuManager.get().openDonateAnvil(p, it);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
