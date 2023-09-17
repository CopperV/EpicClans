package me.Vark123.EpicClans.ClanSystem.MenuSystem.UpgradeSystem;

import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class UpgradeMenuEvents {

	private static final UpgradeMenuEvents container = new UpgradeMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickInfoEvent;
	private final EventCreator<InventoryClickEvent> clickConfirmEvent;
	
	private UpgradeMenuEvents() {
		this.clickInfoEvent = clickInfoEventCreator();
		this.clickConfirmEvent = clickConfirmEventCreator();
	}
	
	public static final UpgradeMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> clickInfoEventCreator() {
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
			if(cPlayer.getClan().isEmpty()) {
				p.closeInventory();
				return;
			}
			
			Clan clan = cPlayer.getClan().get();
			if(!clan.hasPermission(cPlayer, ClanPermission.UPGRADE))
				return;
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("clan-upgrade-id"))
				return;
			
			String upgradeId = nbt.getString("clan-upgrade-id");
			clan.getUpgrades().stream()
			.filter(upgrade -> upgrade.getId().equals(upgradeId))
			.findAny()
			.ifPresent(clanUpgrade -> {
				AClanUpgrade<?> upgrade = UpgradesManager.get().getClanUpgradeById(upgradeId).get();
				upgrade.getLevelRequirement(clanUpgrade.getLevel()+1)
					.ifPresent(req -> {
						UpgradeMenuManager.get().openConfirmationMenu(p, clan, it);
					});
			});
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryClickEvent> clickConfirmEventCreator() {
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
			if(cPlayer.getClan().isEmpty()) {
				p.closeInventory();
				return;
			}

			Clan clan = cPlayer.getClan().get();
			
			if(it.equals(UpgradeMenuManager.get().getBack())) {
				UpgradeMenuManager.get().openMenu(p, clan);
				return;
			}
			if(it.equals(UpgradeMenuManager.get().getRed())) {
				UpgradeMenuManager.get().openMenu(p, clan);
				return;
			}
			
			if(!it.equals(UpgradeMenuManager.get().getGreen())) {
				return;
			}
			
			if(!clan.hasPermission(cPlayer, ClanPermission.UPGRADE))
				return;
			
			ItemStack upgradeItem = inv.getItem(4);
			NBTItem nbt = new NBTItem(upgradeItem);
			if(!nbt.hasTag("clan-upgrade-id"))
				return;
			
			String upgradeId = nbt.getString("clan-upgrade-id");
			clan.getUpgrades().stream()
				.filter(upgrade -> upgrade.getId().equals(upgradeId))
				.findAny()
				.ifPresentOrElse(clanUpgrade -> {
					AClanUpgrade<?> upgrade = UpgradesManager.get().getClanUpgradeById(upgradeId).get();
					upgrade.getLevelRequirement(clanUpgrade.getLevel()+1)
						.ifPresentOrElse(req -> {
							String display = upgradeItem.getItemMeta().getDisplayName();
							if(req.checkClanRequirements(clan)) {
								clanUpgrade.setLevel(clanUpgrade.getLevel()+1);
								
								clan.broadcastMessage("§7§o"+p.getName()+" §bulepszyl §r"+display+" §bna poziom §7"+clanUpgrade.getLevel());
								Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has upgraded "+display+" in clan ["+clan.getId()+"] on"+clanUpgrade.getLevel()+" level");
								clan.getLogger().logMessage(p.getName()+" ulepszyl §r"+display+" na poziom "+clanUpgrade.getLevel());

								p.closeInventory();
							} else {
								p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie posiadasz wystarczajaco duzo surowcow na ulepszenie §r"
										+display);
								p.closeInventory();
							}
						}, () -> p.closeInventory());
				}, () -> p.closeInventory());
			
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
