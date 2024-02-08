package me.Vark123.EpicClans.ClanSystem.MenuSystem.PromoteSystem;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class PromoteMenuEvents {

	private static final PromoteMenuEvents container = new PromoteMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickPlayerListEvent;
	private final EventCreator<InventoryClickEvent> clickPlayerRankEvent;
	
	private PromoteMenuEvents() {
		this.clickPlayerListEvent = clickPlayerListEventCreator();
		this.clickPlayerRankEvent = clickPlayerRankEventCreator();
	}
	
	public static final PromoteMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> clickPlayerListEventCreator() {
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
			if(!clan.hasPermission(cPlayer, ClanPermission.PROMOTE)){
				p.closeInventory();
				return;
			}
			
			NBTItem nbt = new NBTItem(it);
			if(!(nbt.hasTag("clan-promote-player-uuid")))
				return;
			
			UUID uid = UUID.fromString(nbt.getString("clan-promote-player-uuid"));
			PlayerManager.get().getByUID(uid).ifPresentOrElse(cTarget -> {
				Optional<Clan> oTargetClan = cTarget.getClan();
				if(oTargetClan.isEmpty() || !oTargetClan.get().equals(clan)) {
					p.closeInventory();
					return;
				}
				ClanRole targetRole = clan.getMembers().get(cTarget);
				if(targetRole == null) {
					p.closeInventory();
					return;
				}
				PromoteMenuManager.get().openPlayerRankEditorMenu(p, cTarget.toBukkitPlayer(), clan);
			}, () -> {
				p.closeInventory();
			});
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryClickEvent> clickPlayerRankEventCreator() {
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
			if(!clan.hasPermission(cPlayer, ClanPermission.PROMOTE)){
				p.closeInventory();
				return;
			}
			
			if(it.equals(PromoteMenuManager.get().getBack())) {
				PromoteMenuManager.get().openPlayerListMenu(p, clan);
				return;
			}
			
			ClanRole moderatorRole = clan.getMembers().get(cPlayer);
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("clan-promote-role-id"))
				return;
			
			String roleId = nbt.getString("clan-promote-role-id");
			NBTItem nbtTarget = new NBTItem(inv.getItem(0));
			if(!nbtTarget.hasTag("clan-promote-player-uuid"))
				return;
			
			UUID uid = UUID.fromString(nbtTarget.getString("clan-promote-player-uuid"));
			PlayerManager.get().getByUID(uid).ifPresentOrElse(cTarget -> {
				Optional<Clan> oTargetClan = cTarget.getClan();
				if(oTargetClan.isEmpty() || !oTargetClan.get().equals(clan)) {
					p.closeInventory();
					return;
				}
				
				ClanRole targetRole = clan.getMembers().get(cTarget);
				if(targetRole == null
						|| targetRole.getPriority() <= moderatorRole.getPriority()) {
					p.closeInventory();
					return;
				}
				
				clan.getRoles().stream()
					.filter(role -> role.getId().equals(roleId))
					.findAny()
					.ifPresentOrElse(role -> {
						if(targetRole.equals(role)) {
							p.closeInventory();
							return;
						}
						
						clan.getMembers().put(cTarget, role);
						
						OfflinePlayer offline = cTarget.toBukkitPlayer();
						clan.broadcastMessage("§7§o"+p.getName()+" §bzmienil range §7§o"+offline.getName()+" §bna §r"+role.getDisplay());
						Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has change player rank "+offline.getName()+" from "+targetRole.getDisplay()+" into "+role.getDisplay());
						clan.getLogger().logMessage(p.getName()+" zmienil range "+offline.getName()+" na §r"+role.getDisplay());
						
						p.closeInventory();
				}, () -> p.closeInventory());
			}, () -> p.closeInventory());
			
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
