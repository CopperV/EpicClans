package me.Vark123.EpicClans.ClanSystem.MenuSystem.ColorSystem;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class ColorMenuEvents {

	private static final ColorMenuEvents container = new ColorMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	
	private ColorMenuEvents() {
		this.clickEvent = clickEventCreator();
	}
	
	public static final ColorMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> clickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			Inventory inv = e.getClickedInventory();
			if(inv == null || !inv.equals(e.getView().getTopInventory()))
				return;
			
			int slot = e.getSlot();
			ItemStack it = inv.getItem(slot);
			if(it == null || it.getType().equals(Material.AIR))
				return;

			Player p = (Player) e.getWhoClicked();
			ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
			if(cPlayer.getClan().isEmpty()) {
				p.closeInventory();
				return;
			}
			
			Clan clan = cPlayer.getClan().get();
			int row = slot/9;
			if(row == 0)
				return;
			
			String color = clan.getColor();
			String hexColor = color.replace("§", "").replace("x", "").toUpperCase();
			int _red = Integer.parseInt(hexColor.subSequence(0, 2).toString(), 16);
			int _green = Integer.parseInt(hexColor.subSequence(2, 4).toString(), 16);
			int _blue = Integer.parseInt(hexColor.subSequence(4, 6).toString(), 16);
			int mod = 0;
			
			if(it.equals(ColorMenuManager.get().getMinus20()))
				mod = -20;
			else if(it.equals(ColorMenuManager.get().getMinus10()))
				mod = -10;
			else if(it.equals(ColorMenuManager.get().getMinus5()))
				mod = -5;
			else if(it.equals(ColorMenuManager.get().getMinus1()))
				mod = -1;
			else if(it.equals(ColorMenuManager.get().getPlus1()))
				mod = 1;
			else if(it.equals(ColorMenuManager.get().getPlus5()))
				mod = 5;
			else if(it.equals(ColorMenuManager.get().getPlus10()))
				mod = 10;
			else if(it.equals(ColorMenuManager.get().getPlus20()))
				mod = 20;
			else
				return;
			
			switch(row) {
				case 1:
					_red += mod;
					break;
				case 2:
					_green += mod;
					break;
				case 3:
					_blue += mod;
					break;
				default:
					return;
			}
			
			String strRed = Integer.toHexString(_red/16)+Integer.toHexString(_red%16);
			String strGreen = Integer.toHexString(_green/16)+Integer.toHexString(_green%16);
			String strBlue = Integer.toHexString(_blue/16)+Integer.toHexString(_blue%16);
			
			String newHex = strRed+strGreen+strBlue;
			newHex = newHex.toUpperCase();

			StringBuilder newColor = new StringBuilder("§x");
			for(int i = 0; i < newHex.length(); ++i)
				newColor.append("§"+newHex.charAt(i));
			clan.setColor(newColor.toString());
			clan.broadcastMessage("§bZostal zmieniony kolor klanu na "+clan.getColor()+"§o#"+newHex);
			ColorMenuManager.get().openMenu(p, clan);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
