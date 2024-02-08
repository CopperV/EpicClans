package me.Vark123.EpicClans.ClanSystem.MenuSystem.RemoveSystem;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class ClanRemoveMenuEvents {

	private static final ClanRemoveMenuEvents container = new ClanRemoveMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	
	private ClanRemoveMenuEvents() {
		this.clickEvent = clickEventCreator();
	}
	
	public static final ClanRemoveMenuEvents getEvents() {
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
					|| it.equals(ClanRemoveMenuManager.get().getEmpty()))
				return;
			
			if(it.equals(ClanRemoveMenuManager.get().getRed())) {
				e.getWhoClicked().closeInventory();
				return;
			}

			Player p = (Player) e.getWhoClicked();
			ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
			if(cPlayer.getClan().isEmpty()) {
				p.closeInventory();
				return;
			}
			
			Clan clan = cPlayer.getClan().get();
			if(!it.equals(ClanRemoveMenuManager.get().getGreen())) {
				return;
			}
			
			p.closeInventory();
			ClanManager.get().removeClan(clan, cPlayer);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
