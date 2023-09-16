package me.Vark123.EpicClans.ClanSystem.MenuSystem.RoleSystem;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public final class RoleMenuEvents {

	private static final RoleMenuEvents container = new RoleMenuEvents();
	
	private final EventCreator<InventoryClickEvent> roleListClickEvent;
	private final EventCreator<InventoryClickEvent> roleEditorClickEvent;
	private final EventCreator<InventoryClickEvent> roleDeleteClickEvent;
	
	private RoleMenuEvents() {
		this.roleListClickEvent = roleListClickEventCreator();
		this.roleEditorClickEvent = roleEditorClickEventCreator();
		this.roleDeleteClickEvent = roleDeleteClickEventCreator();
	}
	
	public static final RoleMenuEvents getEvents() {
		return container;
	}

	private EventCreator<InventoryClickEvent> roleListClickEventCreator() {
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
			
			if(it.equals(RoleMenuManager.get().getCreateRole())) {
				RoleMenuManager.get()
					.openRoleCreatorMenu(p, clan, slot, RoleMenuManager.get().getDefaultProgressIt().clone());
				return;
			}
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("clan-role-id"))
				return;
			
			
			String roleId = nbt.getString("clan-role-id");
			clan.getRoles().stream()
				.filter(role -> role.getId().equals(roleId))
				.findAny()
				.ifPresent(role -> {
					RoleMenuManager.get().openRoleEditorMenu(p, clan, role);
				});
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryClickEvent> roleEditorClickEventCreator() {
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
			
			if(it.equals(RoleMenuManager.get().getBack())) {
				RoleMenuManager.get().openRoleListMenu(p, clan);
				return;
			}
			
			if(it.equals(RoleMenuManager.get().getChangeDisplay())) {
				NBTItem nbt = new NBTItem(inv.getItem(9));
				String roleId = nbt.getString("clan-role-id");
				clan.getRoles().stream()
					.filter(role -> role.getId().equals(roleId))
					.findAny()
					.ifPresent(role -> {
						RoleMenuManager.get().openRoleEditorAnvilMenu(p, clan, role, "display");
					});
				return;
			}
			if(it.equals(RoleMenuManager.get().getChangePriority())) {
				NBTItem nbt = new NBTItem(inv.getItem(9));
				String roleId = nbt.getString("clan-role-id");
				clan.getRoles().stream()
					.filter(role -> role.getId().equals(roleId))
					.findAny()
					.ifPresent(role -> {
						RoleMenuManager.get().openRoleEditorAnvilMenu(p, clan, role, "priority");
					});
				return;
			}
			if(it.equals(RoleMenuManager.get().getRemoveRank())) {
				NBTItem nbt = new NBTItem(inv.getItem(9));
				String roleId = nbt.getString("clan-role-id");
				clan.getRoles().stream()
					.filter(role -> role.getId().equals(roleId))
					.findAny()
					.ifPresent(role -> {
						RoleMenuManager.get().openRoleDeleteMenu(p, role);
					});
				return;
			}
			
			NBTItem nbt = new NBTItem(it);
			if(!(nbt.hasTag("clan-role-id") && nbt.hasTag("clan-role-enum"))) 
				return;
			
			String roleId = nbt.getString("clan-role-id");
			ClanPermission perm = ClanPermission.valueOf(nbt.getString("clan-role-enum").toUpperCase());
			clan.getRoles().stream()
				.filter(role -> role.getId().equals(roleId))
				.findAny()
				.ifPresent(role -> {
					if(role.getPermissions().contains(perm)) {
						role.getPermissions().remove(perm);
						clan.broadcastMessage("§7§o"+p.getName()+" §busunal randze §r"+role.getDisplay()+" §buprawnienia do: §7"+perm.getDisplay());
						Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has removed permission "+perm.name()+" from rank "+role.getDisplay());
						clan.getLogger().logMessage(p.getName()+" usunal randze §r"+role.getDisplay()+" §ruprawnienia do: "+perm.getDisplay());
					} else {
						role.getPermissions().add(perm);
						clan.broadcastMessage("§7§o"+p.getName()+" §bdodal randze §r"+role.getDisplay()+" §buprawnienia do: §7"+perm.getDisplay());
						Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has added permission "+perm.name()+" to rank "+role.getDisplay());
						clan.getLogger().logMessage(p.getName()+" dodal randze §r"+role.getDisplay()+" §ruprawnienia do: "+perm.getDisplay());
					}
					RoleMenuManager.get().openRoleEditorMenu(p, clan, role);
				});
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}

	private EventCreator<InventoryClickEvent> roleDeleteClickEventCreator() {
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
			
			if(it.equals(RoleMenuManager.get().getRed())) {
				p.closeInventory();
				return;
			}
			
			if(!it.equals(RoleMenuManager.get().getGreen()))
				return;
			
			NBTItem nbt = new NBTItem(inv.getItem(0));
			if(!(nbt.hasTag("clan-role-id"))) 
				return;
			
			String roleId = nbt.getString("clan-role-id");
			ClanRole baseRole = ClanManager.get().getBaseRoles()
					.stream().filter(_role -> _role.getId().equals("member")).findAny().get();
			clan.getRoles().stream()
				.filter(role -> role.getId().equals(roleId))
				.findAny()
				.ifPresent(role -> {
					clan.getMembers().entrySet().stream()
						.filter(entry -> entry.getValue().equals(role))
						.map(entry -> entry.getKey())
						.collect(Collectors.toList())
						.stream()
						.forEach(cTarget -> clan.getMembers().put(cTarget, baseRole));
					clan.getRoles().remove(role);
					
					clan.broadcastMessage("§7§o"+p.getName()+" §busunal range: §r"+role.getDisplay());
					Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has removed clan rank - "+role.getDisplay());
					clan.getLogger().logMessage(p.getName()+" usunal range: "+role.getDisplay());
				});
			p.closeInventory();
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
