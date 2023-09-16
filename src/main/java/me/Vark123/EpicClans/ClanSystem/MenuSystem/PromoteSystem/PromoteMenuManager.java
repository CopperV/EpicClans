package me.Vark123.EpicClans.ClanSystem.MenuSystem.PromoteSystem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

@Getter
public class PromoteMenuManager {

	private static final PromoteMenuManager inst = new PromoteMenuManager();

	private final ItemStack empty;
	private final ItemStack back;
	
	private PromoteMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE,1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER,1);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§c§lPOWROT");
			back.setItemMeta(im);
		}
	}
	
	public static final PromoteMenuManager get() {
		return inst;
	}
	
	public void openPlayerListMenu(Player p, Clan clan) {
		RyseInventory.builder()
			.title("§3§lEDYTOR RANG GRACZY")
			.rows(2)
			.disableUpdateTask()
			.listener(PromoteMenuEvents.getEvents().getClickPlayerListEvent())
			.provider(getPlayerListProvider(p, clan))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openPlayerRankEditorMenu(Player moderator, OfflinePlayer target, Clan clan) {
		RyseInventory.builder()
			.title("§3§lEDYTOR RANG GRACZY")
			.rows(3)
			.disableUpdateTask()
			.listener(PromoteMenuEvents.getEvents().getClickPlayerRankEvent())
			.provider(getPlayerRanksProvider(target, clan))
			.build(Main.getInst())
			.open(moderator);
	}
	
	private InventoryProvider getPlayerListProvider(Player p, Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
				ClanRole role = clan.getMembers().get(cPlayer);
				int priority = role.getPriority();
				
				List<ItemStack> heads = clan.getMembers().entrySet().stream()
						.filter(entry -> entry.getValue().getPriority() > priority)
						.map(entry -> {
							ClanPlayer cTarget = entry.getKey();
							ClanRole targetRole = entry.getValue();
							OfflinePlayer target = cTarget.toBukkitPlayer();
							
							ItemStack it = new ItemStack(Material.PLAYER_HEAD);
							
							SkullMeta im = (SkullMeta) it.getItemMeta();
							im.setOwningPlayer(target);
							im.setDisplayName("§b"+target.getName());
							im.setLore(Arrays.asList(" ","  §4§l» §7Obecna ranga: §r"+targetRole.getDisplay()));
							it.setItemMeta(im);
							
							NBTItem nbt = new NBTItem(it);
							nbt.setString("clan-promote-player-uuid", target.getUniqueId().toString());
							nbt.applyNBT(it);
							
							return it;
						})
						.collect(Collectors.toList());
				
				int index = 0;
				for(ItemStack it : heads)
					contents.set(index++, it);
			}
		};
	}
	
	private InventoryProvider getPlayerRanksProvider(OfflinePlayer target, Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				ClanPlayer cTarget = PlayerManager.get().getByUID(target.getUniqueId()).get();
				ClanRole oldRole = clan.getMembers().get(cTarget);
				ClanPlayer cPlayer = PlayerManager.get().getByUID(player.getUniqueId()).get();
				ClanRole role = clan.getMembers().get(cPlayer);
				int priority = role.getPriority();
				
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);
				contents.set(8, back);
				
				ItemStack head = new ItemStack(Material.PLAYER_HEAD);{
					SkullMeta im = (SkullMeta) head.getItemMeta();
					im.setOwningPlayer(target);
					im.setDisplayName("§b"+target.getName());
					im.setLore(Arrays.asList(" ","  §4§l» §7Obecna ranga: §r"+oldRole.getDisplay()));
					head.setItemMeta(im);
					
					NBTItem nbt = new NBTItem(head);
					nbt.setString("clan-promote-player-uuid", target.getUniqueId().toString());
					nbt.applyNBT(head);
				}
				contents.set(0, head);
				
				List<ItemStack> roles = clan.getRoles().stream()
						.filter(_role -> _role.getPriority() > priority && !_role.equals(oldRole))
						.map(_role -> {
							String id = _role.getId();
							ItemStack it = new ItemStack(Material.BOOK);{
								ItemMeta im = it.getItemMeta();
								im.setDisplayName(_role.getDisplay());
								
								List<String> lore = new LinkedList<>();
								lore.add("§bPriorytet: §7"+_role.getPriority());
								lore.add(" ");
								lore.add("§b§nUPRAWNIENIA");
								lore.addAll(_role.getPermissions().stream()
										.map(perm -> "  §4§l» §7"+perm.getDisplay())
										.collect(Collectors.toList()));
								
								im.setLore(lore);
								it.setItemMeta(im);
							}
							
							NBTItem nbt = new NBTItem(it);
							nbt.setString("clan-promote-role-id", id);
							nbt.applyNBT(it);
							
							return it;
						})
						.collect(Collectors.toList());
				
				int index = 9;
				for(ItemStack it : roles)
					contents.set(index++, it);
			}
		};
	}
	
}
