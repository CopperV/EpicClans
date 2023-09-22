package me.Vark123.EpicClans.ClanSystem.MenuSystem.WarehouseSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public class WarehouseMenuManager {

	private static final WarehouseMenuManager inst = new WarehouseMenuManager();

	private final ItemStack black;
	
	private WarehouseMenuManager() {
		black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = black.getItemMeta();
			im.setDisplayName(" ");
			black.setItemMeta(im);
		}
	}
	
	public static final WarehouseMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p, Clan clan) {
		RyseInventory.builder()
			.title("§2§lMAGAZYN KLANOWY")
			.rows(1)
			.disableUpdateTask()
			.provider(getProvider(clan))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getProvider(Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, black);
				
				clan.getUpgrades().stream()
					.filter(upgrade -> upgrade.getId().equals("warehouse"))
					.findAny()
					.ifPresent(clanUpgrade -> {
						AClanUpgrade<?> upgrade = UpgradesManager.get().getClanUpgradeById(clanUpgrade.getId()).get();
						int level = (int) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
						for(int i = 0; i < level; ++i) {
							Inventory inv = clan.getWarehouses().get(i+1);
							ItemStack it = new ItemStack(Material.CHEST);{
								ItemMeta im = it.getItemMeta();
								im.setDisplayName("§eMagazyn nr "+(i+1));
								it.setItemMeta(im);
							}
							
							contents.set(i, IntelligentItem.of(it, click -> {
								ClanPlayer cPlayer = PlayerManager.get().getByUID(click.getWhoClicked().getUniqueId()).get();
								if(cPlayer.getClan().isEmpty() || !cPlayer.getClan().get().equals(clan)) {
									click.getWhoClicked().closeInventory();
									return;
								}
								click.getWhoClicked().openInventory(inv);
							}));
						}
					});
			}
		};
	}
	
}
