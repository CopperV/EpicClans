package me.Vark123.EpicClans.ClanSystem.MenuSystem.TreasurySystem;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.Builder;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.ResponseAction;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

@Getter
public final class TreasuryMenuManager {

	private static final TreasuryMenuManager inst = new TreasuryMenuManager();
	
	private final ItemStack black;
	private final ItemStack back;

	private final ItemStack money;
	private final ItemStack stygia;
	private final ItemStack coins;
	private final ItemStack ruda;
	private final ItemStack pr;

	private final ItemStack donateMoney;
	private final ItemStack donateStygia;
	private final ItemStack donateCoins;
	private final ItemStack donateRuda;

	private final ItemStack donate;
	
	@Getter(value = AccessLevel.NONE)
	private InventoryProvider donateProvider;
	
	private TreasuryMenuManager() {
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
		
		donateMoney = money.clone();{
			ItemMeta im = donateMoney.getItemMeta();
			im.setDisplayName("§e§oWplac walute");
			donateMoney.setItemMeta(im);
		}
		donateStygia = stygia.clone();{
			ItemMeta im = donateStygia.getItemMeta();
			im.setDisplayName("§3§oWplac stygie");
			donateStygia.setItemMeta(im);
		}
		donateCoins = coins.clone();{
			ItemMeta im = donateCoins.getItemMeta();
			im.setDisplayName("§c§oWplac smocze monety");
			donateCoins.setItemMeta(im);
		}
		donateRuda = ruda.clone();{
			ItemMeta im = donateRuda.getItemMeta();
			im.setDisplayName("§9§oWplac brylki rudy");
			donateRuda.setItemMeta(im);
		}
		
		donate = new ItemStack(Material.CHEST, 1);{
			ItemMeta im = donate.getItemMeta();
			im.setDisplayName("§a§lPRZEKAZ SUROWCE");
			donate.setItemMeta(im);
		}
		
		donateProvider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 1; i < 9; ++i)
					contents.set(i, black);
				contents.set(0, back);
				contents.set(10, donateMoney);
				contents.set(12, donateStygia);
				contents.set(14, donateCoins);
				contents.set(16, donateRuda);
			}
		};
	}
	
	public static final TreasuryMenuManager get() {
		return inst;
	}
	
	public void openTreasury(Player p, Clan clan) {
		RyseInventory.builder()
			.title("§6§lSKARBIEC KLANU")
			.rows(2)
			.disableUpdateTask()
			.listener(TreasuryMenuEvents.getEvents().getMenuClickEvent())
			.provider(getTreasuryProvider(clan.getTreasury()))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openDonate(Player p) {
		RyseInventory.builder()
			.title("§6§lSKARBIEC KLANU")
			.rows(2)
			.disableUpdateTask()
			.listener(TreasuryMenuEvents.getEvents().getDonateClickEvent())
			.provider(donateProvider)
			.build(Main.getInst())
			.open(p);
	}
	
	public void openDonateAnvil(Player p, ItemStack type) {
		RyseInventory.builder()
			.title("§6§lSKARBIEC KLANU")
			.type(InventoryOpenerType.ANVIL)
			.disableUpdateTask()
			.provider(getDonateAnvilProvider(type))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getTreasuryProvider(ClanTreasury treasury) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 1; i < 9; ++i)
					contents.set(i, black);
				contents.set(0, donate);
				
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
			}
		};
	}
	
	private InventoryProvider getDonateAnvilProvider(ItemStack type) {
		return new InventoryProvider() {
			@Override
			public void anvil(Player player, Builder anvil) {
				ItemStack it = type.clone();{
					ItemMeta im = it.getItemMeta();
					im.setDisplayName("0");
					it.setItemMeta(im);
				}
				
				anvil.itemLeft(it);
				anvil.onClick((slot, _anvil) -> {
					if(slot != 2)
						return Arrays.asList();
					ItemStack result = _anvil.getOutputItem();
					if(result == null || result.getType().equals(Material.AIR))
						return Arrays.asList();
					String text = _anvil.getText();
					if(!StringUtils.isNumeric(text))
						return Arrays.asList();
					
					ClanPlayer cPlayer = PlayerManager.get().getByUID(player.getUniqueId()).get();
					if(cPlayer.getClan().isEmpty())
						return Arrays.asList();
					Clan clan = cPlayer.getClan().get();
					ClanTreasury treasury = clan.getTreasury();
					
					int amount = Integer.parseInt(text);
					if(amount < 0)
						return Arrays.asList();
					
					RpgPlayer rpg = me.Vark123.EpicRPG.Players.PlayerManager.getInstance().getRpgPlayer(player);
					RpgVault vault = rpg.getVault();
					switch(result.getType()) {
						case GOLD_NUGGET:
							double toPay = Double.parseDouble(text);
							if(!vault.hasEnoughMoney(toPay)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie posiadasz wystarczajaco pieniedzy do wplaty "+String.format("%.2f", toPay)+"$");
								return Arrays.asList();
							}
							vault.removeMoney(toPay);
							treasury.addMoney(toPay);
							clan.broadcastMessage("§7§o"+player.getName()+" §bwplacil §e§o"+String.format("%.2f", toPay)+"$ §bdo skarbca");
							clan.getLogger().logMessage(player.getName()+" wplacil "+String.format("%.2f", toPay)+"$ do skarbca");
							break;
						case GHAST_TEAR:
							if(!vault.hasEnoughStygia(amount)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie posiadasz wystarczajaco stygii do wplaty "+amount);
								return Arrays.asList();
							}
							vault.removeStygia(amount);
							treasury.addStygia(amount);
							clan.broadcastMessage("§7§o"+player.getName()+" §bwplacil §3§o"+amount+" §bstygii do skarbca");
							clan.getLogger().logMessage(player.getName()+" wplacil "+amount+" stygii do skarbca");
							break;
						case REDSTONE:
							if(!vault.hasEnoughDragonCoins(amount)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie posiadasz wystarczajaco smoczych monet do wplaty "+amount);
								return Arrays.asList();
							}
							vault.removeDragonCoins(amount);
							treasury.addCoins(amount);
							clan.broadcastMessage("§7§o"+player.getName()+" §bwplacil §c§o"+amount+" §bsmoczych monet do skarbca");
							clan.getLogger().logMessage(player.getName()+" wplacil "+amount+" smoczych monet do skarbca");
							break;
						case LAPIS_LAZULI:
							if(!vault.hasEnoughBrylkiRudy(amount)) {
								player.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie posiadasz wystarczajaco brylek rudy do wplaty "+amount);
								return Arrays.asList();
							}
							vault.removeBrylkiRudy(amount);
							treasury.addRuda(amount);
							clan.broadcastMessage("§7§o"+player.getName()+" §bwplacil §9§o"+amount+" §bbrylek rudy do skarbca");
							clan.getLogger().logMessage(player.getName()+" wplacil "+amount+" brylek rudy do skarbca");
							break;
						default:
							return Arrays.asList();
					}
					
					return Arrays.asList(ResponseAction.close());
				});
			}
		};
	}
	
}
