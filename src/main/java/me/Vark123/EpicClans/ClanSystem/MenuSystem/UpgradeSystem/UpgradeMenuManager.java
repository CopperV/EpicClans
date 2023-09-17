package me.Vark123.EpicClans.ClanSystem.MenuSystem.UpgradeSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGenerator;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.AClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;

@Getter
public class UpgradeMenuManager {

	private static final UpgradeMenuManager inst = new UpgradeMenuManager();
	
	private final ItemStack black;
	private final ItemStack back;

	private final ItemStack money;
	private final ItemStack stygia;
	private final ItemStack coins;
	private final ItemStack ruda;
	private final ItemStack pr;
	
	private final ItemStack red;
	private final ItemStack green;
	
	private UpgradeMenuManager() {
		black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = black.getItemMeta();
			im.setDisplayName(" ");
			black.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER, 1);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§c§lPOWROT");
			back.setItemMeta(im);
		}

		money = new ItemStack(Material.GOLD_NUGGET, 1);
		stygia = new ItemStack(Material.GHAST_TEAR, 1);
		coins = new ItemStack(Material.REDSTONE, 1);
		ruda = new ItemStack(Material.LAPIS_LAZULI, 1);
		pr = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
		
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
	}
	
	public static final UpgradeMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p, Clan clan) {
		RyseInventory.builder()
			.title("§6§lULEPSZENIA KLANU")
			.rows(3)
			.disableUpdateTask()
			.listener(UpgradeMenuEvents.getEvents().getClickInfoEvent())
			.provider(getInfoProvider(clan))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openConfirmationMenu(Player p, Clan clan, ItemStack upgradeItem) {
		RyseInventory.builder()
			.title("§6§lULEPSZYC §r"+upgradeItem.getItemMeta().getDisplayName()+"§6§l?")
			.rows(2)
			.disableUpdateTask()
			.listener(UpgradeMenuEvents.getEvents().getClickConfirmEvent())
			.provider(getConfirmProvider(clan, upgradeItem))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getInfoProvider(Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, black);
				
				ClanTreasury treasury = clan.getTreasury();
				ItemStack localMoney = money.clone();{
					ItemMeta im = localMoney.getItemMeta();
					im.setDisplayName("§e§o"+String.format("%.2f", treasury.getMoney())+"$");
					localMoney.setItemMeta(im);
				}
				ItemStack localStygia = stygia.clone();{
					ItemMeta im = localStygia.getItemMeta();
					im.setDisplayName("§3§o"+treasury.getStygia()+" stygii");
					localStygia.setItemMeta(im);
				}
				ItemStack localCoins = coins.clone();{
					ItemMeta im = localCoins.getItemMeta();
					im.setDisplayName("§c§o"+treasury.getCoins()+" smoczych monet");
					localCoins.setItemMeta(im);
				}
				ItemStack localRuda = ruda.clone();{
					ItemMeta im = localRuda.getItemMeta();
					im.setDisplayName("§9§o"+treasury.getRuda()+" brylek rudy");
					localRuda.setItemMeta(im);
				}
				ItemStack localPr = pr.clone();{
					ItemMeta im = localPr.getItemMeta();
					im.setDisplayName("§6§o"+treasury.getPr()+" punktow rozwoju");
					localPr.setItemMeta(im);
				}
				contents.set(11, localMoney);
				contents.set(12, localStygia);
				contents.set(13, localCoins);
				contents.set(14, localRuda);
				contents.set(15, localPr);
				
				List<ItemStack> upgrades = clan.getUpgrades().stream()
						.map(clanUpgrade -> {
							String id = clanUpgrade.getId();
							int level = clanUpgrade.getLevel();
							AClanUpgrade<?> upgrade = UpgradesManager.get()
									.getClanUpgradeById(id).get();
							
							ItemStack it = upgrade.toItem();
							
							ItemMeta im = it.getItemMeta();
							List<String> lore = new LinkedList<>();
							lore.add(" ");
							lore.add("§4§l» §8Poziom: §7"+level);
							switch(id.toLowerCase()) {
								case "exp":
								case "attack":
								case "defense":
								{
									double bonus = (double) upgrade.getUpgradeBonus(level);
									lore.add("§4§l» §8Bonus: §7"+String.format("%.1f", bonus*100)+"%");
									upgrade.getLevelRequirement(level+1).ifPresent(req -> {
										double newBonus = (double) upgrade.getUpgradeBonus(level+1);
										lore.add("§4§l» §8Nastepny poziom: §7"+String.format("%.1f", newBonus*100)+"%");
									});
								}
									break;
								case "players":
								{
									int size = (int) upgrade.getUpgradeBonus(level);
									lore.add("§4§l» §8Liczba graczy: §7"+size);
									upgrade.getLevelRequirement(level+1).ifPresent(req -> {
										int newSize = (int) upgrade.getUpgradeBonus(level+1);
										lore.add("§4§l» §8Nastepny poziom: §7"+newSize);
									});
								}
									break;
								case "generator":
								{
									ResourceGenerator generator = (ResourceGenerator) upgrade.getUpgradeBonus(level);
									lore.add(" ");
									lore.add("§7§nTygodniowa produkcja");
									if(generator.getMoney() > 0)
										lore.add(" §4§l» §e§o"+String.format("%.2f", generator.getMoney())+"$");
									if(generator.getStygia() > 0)
										lore.add(" §4§l» §3§o"+generator.getStygia()+" stygii");
									if(generator.getCoins() > 0)
										lore.add(" §4§l» §4§o"+generator.getCoins()+" smoczych monet");
									if(generator.getBrylki() > 0)
										lore.add(" §4§l» §9§o"+generator.getBrylki()+" brylek rudy");
									if(generator.getPr() > 0)
										lore.add(" §4§l» §6§o"+generator.getPr()+" punktow rozwoju");
									upgrade.getLevelRequirement(level+1).ifPresent(req -> {
										ResourceGenerator newGenerator = (ResourceGenerator) upgrade.getUpgradeBonus(level+1);
										lore.add(" ");
										lore.add("§7§nNastepny poziom");
										if(newGenerator.getMoney() > 0)
											lore.add(" §4§l» §e§o"+String.format("%.2f", newGenerator.getMoney())+"$");
										if(newGenerator.getStygia() > 0)
											lore.add(" §4§l» §3§o"+newGenerator.getStygia()+" stygii");
										if(newGenerator.getCoins() > 0)
											lore.add(" §4§l» §4§o"+newGenerator.getCoins()+" smoczych monet");
										if(newGenerator.getBrylki() > 0)
											lore.add(" §4§l» §9§o"+newGenerator.getBrylki()+" brylek rudy");
										if(newGenerator.getPr() > 0)
											lore.add(" §4§l» §6§o"+newGenerator.getPr()+" punktow rozwoju");
									});
								}
									break;
								case "rotations":
								{
									int size = (int) upgrade.getUpgradeBonus(level);
									lore.add("§4§l» §8Ilosc przedmiotow: §7"+size);
									upgrade.getLevelRequirement(level+1).ifPresent(req -> {
										int newSize = (int) upgrade.getUpgradeBonus(level+1);
										lore.add("§4§l» §8Nastepny poziom: §7"+newSize);
									});
								}
									break;
								case "warehouse":
								{
									int size = (int) upgrade.getUpgradeBonus(level) * (9*3);
									lore.add("§4§l» §8Wielkosc magazynu: §7"+size);
									upgrade.getLevelRequirement(level+1).ifPresent(req -> {
										int newSize = (int) upgrade.getUpgradeBonus(level+1) * (9*3);
										lore.add("§4§l» §8Nastepny poziom: §7"+newSize);
									});
								}
									break;
								default:
									break;
							}
							upgrade.getLevelRequirement(level+1).ifPresent(req -> {
								lore.add(" ");
								lore.add("§7§nWymagania do ulepszenia");
								if(req.getMoney() > 0)
									lore.add(" §4§l» §e§o"+String.format("%.2f", req.getMoney())+"$");
								if(req.getStygia() > 0)
									lore.add(" §4§l» §3§o"+req.getStygia()+" stygii");
								if(req.getCoins() > 0)
									lore.add(" §4§l» §4§o"+req.getCoins()+" smoczych monet");
								if(req.getRuda() > 0)
									lore.add(" §4§l» §9§o"+req.getRuda()+" brylek rudy");
								if(req.getPr() > 0)
									lore.add(" §4§l» §6§o"+req.getPr()+" punktow rozwoju");
							});
							im.setLore(lore);
							it.setItemMeta(im);
							
							return it;
						})
						.collect(Collectors.toList());
				int index = 19;
				for(ItemStack it : upgrades)
					contents.set(index++, it);
			}
		};
	}
	
	private InventoryProvider getConfirmProvider(Clan clan, ItemStack item) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < 9; ++i)
					contents.set(i, black);
				contents.set(4, item);
				contents.set(8, back);
				contents.set(11, green);
				contents.set(15, red);
			}
		};
	}
	
}
