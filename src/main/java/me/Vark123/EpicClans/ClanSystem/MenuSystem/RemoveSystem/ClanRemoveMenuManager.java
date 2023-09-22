package me.Vark123.EpicClans.ClanSystem.MenuSystem.RemoveSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Main;

@Getter
public class ClanRemoveMenuManager {

	private static final ClanRemoveMenuManager inst = new ClanRemoveMenuManager();
	
	private final ItemStack red;
	private final ItemStack green;
	
	private final ItemStack empty;
	
	private final InventoryProvider provider;
	
	private ClanRemoveMenuManager() {
		red = new ItemStack(Material.RED_TERRACOTTA);{
			ItemMeta im = red.getItemMeta();
			im.setDisplayName("§4§lNIE");
			red.setItemMeta(im);
		}
		green = new ItemStack(Material.GREEN_TERRACOTTA);{
			ItemMeta im = green.getItemMeta();
			im.setDisplayName("§2§lTAK");
			green.setItemMeta(im);
		}

		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		
		provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);
				contents.set(2, green);
				contents.set(6, red);
			}
		};
	}
	
	public static final ClanRemoveMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		RyseInventory.builder()
			.title("§b§lNA PEWNO USUNAC KLAN?")
			.rows(1)
			.disableUpdateTask()
			.listener(ClanRemoveMenuEvents.getEvents().getClickEvent())
			.provider(provider)
			.build(Main.getInst())
			.open(p);
	}
	
}
