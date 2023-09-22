package me.Vark123.EpicClans.ClanSystem.MenuSystem.LogMenuSystem;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import net.md_5.bungee.api.ChatColor;

@Getter
public final class ClanLogMenuEvents {

	private static final ClanLogMenuEvents container = new ClanLogMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickLogListEvent;
	private final EventCreator<InventoryClickEvent> clickLogEvent;
	
	private ClanLogMenuEvents() {
		this.clickLogListEvent = clickLogListEventCreator();
		this.clickLogEvent = clickLogEventCreator();
	}
	
	public static final ClanLogMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> clickLogListEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null 
					|| it.getType().equals(Material.AIR)
					|| it.equals(ClanLogMenuManager.get().getEmpty()))
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
			
			if(it.equals(ClanLogMenuManager.get().getPrevious())) {
				ClanLogMenuManager.get().openLogListMenu(p, clan, page-1);
				return;
			}
			if(it.equals(ClanLogMenuManager.get().getNext())) {
				ClanLogMenuManager.get().openLogListMenu(p, clan, page+1);
				return;
			}
			
			if(!it.getType().equals(Material.WRITTEN_BOOK))
				return;
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("log-date"))
				return;
			
			String date = nbt.getString("log-date");
			ClanLogMenuManager.get().openLogMenu(p, clan, date, 0);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryClickEvent> clickLogEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null 
					|| it.getType().equals(Material.AIR)
					|| it.equals(ClanLogMenuManager.get().getEmpty()))
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
			if(!nbtPage.hasTag("log-page") || !nbtPage.hasTag("log-date"))
				return;
			int page = nbtPage.getInteger("log-page");
			String date = nbtPage.getString("log-date");
			
			if(it.equals(ClanLogMenuManager.get().getBack())) {
				ClanLogMenuManager.get().openLogListMenu(p, clan, 0);
				return;
			}
			if(it.equals(ClanLogMenuManager.get().getPrevious())) {
				ClanLogMenuManager.get().openLogMenu(p, clan, date, page-1);
				return;
			}
			if(it.equals(ClanLogMenuManager.get().getNext())) {
				ClanLogMenuManager.get().openLogMenu(p, clan, date, page+1);
				return;
			}
			
			if(!it.getType().equals(Material.PAPER))
				return;
			
			if(slot >= 45)
				return;
			
			ItemMeta im = it.getItemMeta();
			String time = ChatColor.stripColor(im.getDisplayName());
			String log = ChatColor.stripColor(im.getLore().get(0));
			
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §7[§7"+time+"§7] §b"+log);
			
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
