package me.Vark123.EpicClans.ClanSystem.MenuSystem.RoleSystem;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.Builder;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.ResponseAction;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.ClanRole;

@Getter
public class RoleMenuManager {

	private static final RoleMenuManager inst = new RoleMenuManager();
	
	private final ItemStack empty;
	private final ItemStack back;
	
	private final ItemStack createRole;

	private final ItemStack defaultProgressIt;

	private final ItemStack changeDisplay;
	private final ItemStack changePriority;
	private final ItemStack removeRank;

	private final ItemStack green;
	private final ItemStack red;
	
	private RoleMenuManager() {
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

		createRole = new ItemStack(Material.GREEN_TERRACOTTA,1);{
			ItemMeta im = createRole.getItemMeta();
			im.setDisplayName("§2§lSTWORZ RANGE");
			createRole.setItemMeta(im);
		}

		defaultProgressIt = new ItemStack(Material.PAPER,1);{
			ItemMeta im = defaultProgressIt.getItemMeta();
			im.setDisplayName("§f<<<ID>>>");
			defaultProgressIt.setItemMeta(im);
		}

		changeDisplay = new ItemStack(Material.PAPER,1);{
			ItemMeta im = changeDisplay.getItemMeta();
			im.setDisplayName("§7Zmien nazwe rangi");
			changeDisplay.setItemMeta(im);
		}
		changePriority = new ItemStack(Material.OAK_SIGN,1);{
			ItemMeta im = changePriority.getItemMeta();
			im.setDisplayName("§7Zmien priorytet rangi");
			changePriority.setItemMeta(im);
		}
		removeRank = new ItemStack(Material.LAVA_BUCKET,1);{
			ItemMeta im = removeRank.getItemMeta();
			im.setDisplayName("§4Usun range §7[§e§lNIEODWRACALNE§7]");
			removeRank.setItemMeta(im);
		}

		green = new ItemStack(Material.GREEN_TERRACOTTA,1);{
			ItemMeta im = green.getItemMeta();
			im.setDisplayName("§a§lTAK");
			green.setItemMeta(im);
		}
		red = new ItemStack(Material.RED_TERRACOTTA,1);{
			ItemMeta im = red.getItemMeta();
			im.setDisplayName("§c§lNIE");
			red.setItemMeta(im);
		}
	}
	
	public static final RoleMenuManager get() {
		return inst;
	}
	
	public void openRoleListMenu(Player p, Clan clan) {
		int size = clan.getRoles().stream()
				.filter(role -> !ClanManager.get().getBaseRoles().contains(role))
				.collect(Collectors.toList())
				.size()+2;
		size = (size-1) / 9 + 2;
		if(size < 2) size = 2;
		if(size > 6) size = 6;
		RyseInventory.builder()
			.title("§6§lRANGI KLANOWE")
			.rows(size)
			.disableUpdateTask()
			.listener(RoleMenuEvents.getEvents().getRoleListClickEvent())
			.provider(getRoleListProvider(clan))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openRoleCreatorMenu(Player p, Clan clan, int stage, ItemStack progressIt) {
		RyseInventory.builder()
			.title("§e§lKREATOR RANG")
			.type(InventoryOpenerType.ANVIL)
			.disableUpdateTask()
			.provider(getRoleCreatorProvider(clan, stage, progressIt))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openRoleEditorMenu(Player p, Clan clan, ClanRole role) {
		int size = Arrays.asList(ClanPermission.values()).stream()
				.filter(perm -> !perm.equals(ClanPermission.LEADER))
				.collect(Collectors.toList())
				.size()/9+2;
		RyseInventory.builder()
			.title("§2§lEDYTOR RANGI §7["+role.getDisplay()+"§7]")
			.rows(size)
			.disableUpdateTask()
			.listener(RoleMenuEvents.getEvents().getRoleEditorClickEvent())
			.provider(getRoleEditorProvider(clan, role))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openRoleEditorAnvilMenu(Player p, Clan clan, ClanRole role, String editor) {
		RyseInventory.builder()
			.title("§2§lEDYTOR RANGI §7["+role.getDisplay()+"§7]")
			.type(InventoryOpenerType.ANVIL)
			.disableUpdateTask()
			.provider(getRoleEditorAnvilProvider(clan, role, editor))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openRoleDeleteMenu(Player p, ClanRole role) {
		RyseInventory.builder()
			.title("§c§lNA PEWNO USUNAC RANGE §r"+role.getDisplay()+"§c§l?")
			.rows(1)
			.disableUpdateTask()
			.listener(RoleMenuEvents.getEvents().getRoleDeleteClickEvent())
			.provider(getRoleDeleteProvider(role))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getRoleListProvider(Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);
				
				if(clan.getRoles().size() < Config.get().getMaxRoles()+ClanManager.get().getBaseRoles().size())
					contents.set(0, createRole);
				
				List<ItemStack> roles = clan.getRoles().stream()
						.filter(role -> !ClanManager.get().getBaseRoles().contains(role))
						.map(role -> {
							String id = role.getId();
							ItemStack it = new ItemStack(Material.BOOK);{
								ItemMeta im = it.getItemMeta();
								im.setDisplayName(role.getDisplay());
								
								List<String> lore = new LinkedList<>();
								lore.add("§bPriorytet: §7"+role.getPriority());
								lore.add(" ");
								lore.add("§b§nUPRAWNIENIA");
								lore.addAll(role.getPermissions().stream()
										.map(perm -> "  §4§l» §7"+perm.getDisplay())
										.collect(Collectors.toList()));
								
								im.setLore(lore);
								it.setItemMeta(im);
							}
							
							NBTItem nbt = new NBTItem(it);
							nbt.setString("clan-role-id", id);
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
	
	private InventoryProvider getRoleCreatorProvider(Clan clan, int stage, ItemStack progressIt) {
		return new InventoryProvider() {
			@Override
			public void anvil(Player player, Builder anvil) {
				anvil.itemLeft(progressIt);
				anvil.onClick((slot, _anvil) -> {
					if(slot != 2)
						return Arrays.asList();
					ItemStack result = _anvil.getOutputItem();
					if(result == null || result.getType().equals(Material.AIR))
						return Arrays.asList();
					String text = _anvil.getText();
					switch(stage) {
						case 0:
						{
							boolean isUnique = clan.getRoles()
									.stream()
									.filter(role -> role.getId().equalsIgnoreCase(text))
									.findAny()
									.isEmpty();
							if(!isUnique) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bID rangi musi byc unikatowe!");
								return Arrays.asList();
							}
							
							NBTItem nbt = new NBTItem(progressIt);
							nbt.setString("clan-creator-id", text);
							nbt.applyNBT(progressIt);
							
							ItemMeta im = progressIt.getItemMeta();
							im.setDisplayName("§f<<<NAZWA>>>");
							progressIt.setItemMeta(im);
							openRoleCreatorMenu(player, clan, stage+1, progressIt);
							return Arrays.asList();
						}
						case 1:
						{
							NBTItem nbt = new NBTItem(progressIt);
							nbt.setString("clan-creator-display", ChatColor.translateAlternateColorCodes('&', text));
							nbt.applyNBT(progressIt);
							
							ItemMeta im = progressIt.getItemMeta();
							im.setDisplayName("§f<<<PRIORYTET>>>");
							progressIt.setItemMeta(im);
							openRoleCreatorMenu(player, clan, stage+1, progressIt);
							return Arrays.asList();
						}
						case 2:
						{
							if(!StringUtils.isNumeric(text)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bPriorytet musi byc liczba wieksza od 0!");
								return Arrays.asList();
							}
							if(!ValueRange.of(1, Integer.MAX_VALUE)
									.isValidValue(Long.parseLong(text))) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bPriorytet musi byc liczba z zakresu od 1 do "+Integer.MAX_VALUE+"!");
								return Arrays.asList();
							}
							
							NBTItem nbt = new NBTItem(progressIt);
							String id = nbt.getString("clan-creator-id");
							String display = nbt.getString("clan-creator-display");
							int priority = Integer.parseInt(text);
							
							ClanRole role = new ClanRole(id, display, priority, false, new ArrayList<>());
							clan.getRoles().add(role);
							
							clan.broadcastMessage("§7§o"+player.getName()+" §bstworzyl nowa range: §r"+role.getDisplay());
							
							openRoleListMenu(player, clan);

							Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+player.getName()+" has created new clan rank - "+role.getDisplay());
							clan.getLogger().logMessage(player.getName()+" stworzyl nowa range: "+role.getDisplay());
							
							return Arrays.asList();
						}
						default:
							return Arrays.asList(ResponseAction.close());
					}
				});
			}
		};
	}
	
	private InventoryProvider getRoleEditorProvider(Clan clan, ClanRole role) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);

				contents.set(0, changeDisplay);
				contents.set(1, changePriority);
				contents.set(2, removeRank);
				contents.set(8, back);
				
				String roleId = role.getId();
				List<ItemStack> permItems = Arrays.asList(ClanPermission.values()).stream()
						.filter(perm -> !perm.equals(ClanPermission.LEADER))
						.map(perm -> {
							Material material = role.getPermissions().contains(perm) ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA;
							String color = material.equals(Material.GREEN_TERRACOTTA) ? "§a§l" : "§c§l";
							
							ItemStack it = new ItemStack(material);
							
							ItemMeta im = it.getItemMeta();
							im.setDisplayName(color+perm.getDisplay());
							it.setItemMeta(im);
							
							NBTItem nbt = new NBTItem(it);
							nbt.setString("clan-role-id", roleId);
							nbt.setString("clan-role-enum", perm.name());
							nbt.applyNBT(it);
							return it;
						})
						.collect(Collectors.toList());
				
				int index = 9;
				for(ItemStack it : permItems)
					contents.set(index++, it);
			}
		};
	}
	
	private InventoryProvider getRoleEditorAnvilProvider(Clan clan, ClanRole role, String editor) {
		return new InventoryProvider() {
			@Override
			public void anvil(Player player, Builder anvil) {
				ItemStack editIt = new ItemStack(Material.PAPER);{
					ItemMeta im = editIt.getItemMeta();
					switch(editor.toLowerCase()) {
						case "display":
							im.setDisplayName(role.getDisplay().replace("§", "&"));
							break;
						case "priority":
							im.setDisplayName(""+role.getPriority());
							break;
					}
					editIt.setItemMeta(im);
				}
				anvil.itemLeft(editIt);
				
				anvil.onClick((slot, _anvil) -> {
					if(slot != 2)
						return Arrays.asList();
					ItemStack result = _anvil.getOutputItem();
					if(result == null || result.getType().equals(Material.AIR))
						return Arrays.asList();
					String text = _anvil.getText();
					switch(editor.toLowerCase()) {
						case "display":
						{
							String oldDisplay = role.getDisplay();
							role.setDisplay(ChatColor.translateAlternateColorCodes('&', text));
							
							clan.broadcastMessage("§7§o"+player.getName()+" §bzmienil nazwe rangi §r"+oldDisplay+" §bna §r"+role.getDisplay());
							Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+player.getName()+" has change rank name "+oldDisplay+" into "+role.getDisplay());
							clan.getLogger().logMessage(player.getName()+" zmienil nazwe rangi "+oldDisplay+" §rna "+role.getDisplay());
							
							openRoleEditorMenu(player, clan, role);
							return Arrays.asList();
						}
						case "priority":
						{
							if(!StringUtils.isNumeric(text)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bPriorytet musi byc liczba wieksza od 0!");
								return Arrays.asList();
							}
							if(!ValueRange.of(1, Integer.MAX_VALUE)
									.isValidValue(Long.parseLong(text))) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bPriorytet musi byc liczba z zakresu od 1 do "+Integer.MAX_VALUE+"!");
								return Arrays.asList();
							}
							int priority = Integer.parseInt(text);
							
							int oldPriority = role.getPriority();
							role.setPriority(priority);
							
							clan.broadcastMessage("§7§o"+player.getName()+" §bzmienil priorytet rangi §r"+role.getDisplay()+" §bz §7"+oldPriority+" §bna §7"+role.getPriority());
							Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+player.getName()+" has change prority rank "+role.getDisplay()+" from "+oldPriority+" into "+role.getPriority());
							clan.getLogger().logMessage(player.getName()+" zmienil priorytet rangi "+role.getDisplay()+" §rz "+oldPriority+" na "+role.getPriority());
							
							openRoleEditorMenu(player, clan, role);
							return Arrays.asList();
						}
						default:
							return Arrays.asList(ResponseAction.close());
					}
				});
			}
		};
	}
	
	private InventoryProvider getRoleDeleteProvider(ClanRole role) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, empty);
				contents.set(2, green);
				contents.set(6, red);
				
				ItemStack roleInfo = empty.clone();
				NBTItem nbt = new NBTItem(roleInfo);
				nbt.setString("clan-role-id", role.getId());
				nbt.applyNBT(roleInfo);
				
				contents.set(0, roleInfo);
			}
		};
	}
	
}