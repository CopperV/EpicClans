package me.Vark123.EpicClans.ClanSystem.MenuSystem.LogMenuSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import me.Vark123.EpicClans.FileManager;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.Tools.Pair;

@Getter
public class ClanLogMenuManager {

	private static final ClanLogMenuManager inst = new ClanLogMenuManager();
	
	private final ItemStack previous;
	private final ItemStack next;
	
	private final ItemStack empty;
	private final ItemStack back;
	
	private final int ITEMS_PER_PAGE = 45;
	
	private ClanLogMenuManager() {
		previous = new ItemStack(Material.ARROW);{
			ItemMeta im = previous.getItemMeta();
			im.setDisplayName("§fPoprzednia");
			previous.setItemMeta(im);
		}
		next = new ItemStack(Material.ARROW);{
			ItemMeta im = next.getItemMeta();
			im.setDisplayName("§fNastepna");
			next.setItemMeta(im);
		}

		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPowrot");
			back.setItemMeta(im);
		}
	}
	
	public static final ClanLogMenuManager get() {
		return inst;
	}
	
	public void openLogListMenu(Player p, Clan clan, int page) {
		RyseInventory.builder()
			.title("§5§lDZIENNIK ZDARZEN")
			.rows(6)
			.disableUpdateTask()
			.listener(ClanLogMenuEvents.getEvents().getClickLogListEvent())
			.provider(getLogListProvider(clan, page))
			.build(Main.getInst())
			.open(p);
	}
	
	public void openLogMenu(Player p, Clan clan, String date, int page) {
		RyseInventory.builder()
			.title("§5§lDZIENNIK ZDARZEN §7[§3"+date+"§7]")
			.rows(6)
			.disableUpdateTask()
			.listener(ClanLogMenuEvents.getEvents().getClickLogEvent())
			.provider(getLogProvider(clan, date, page))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getLogListProvider(Clan clan, int page) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				File clanDir = new File(FileManager.getClanDir(), clan.getId());
				File logDir = new File(clanDir, "logs");
				List<ItemStack> logList = Arrays.asList(logDir.listFiles()).stream()
						.filter(file -> file.isFile())
						.map(file -> file.getName().replace(".log", ""))
						.sorted((date1, date2) -> date2.compareTo(date1))
						.map(date -> {
							ItemStack it = new ItemStack(Material.WRITTEN_BOOK);{
								ItemMeta im = it.getItemMeta();
								im.setDisplayName("§3§o"+date);
								it.setItemMeta(im);
							}
							
							NBTItem nbt = new NBTItem(it);
							nbt.setString("log-date", date);
							nbt.applyNBT(it);
							
							return it;
						})
						.collect(Collectors.toList());
				ItemStack pageInfo = new ItemStack(Material.PAPER);{
					ItemMeta im = pageInfo.getItemMeta();
					im.setDisplayName("§f§lStrona "+(page+1));
					pageInfo.setItemMeta(im);
					
					NBTItem nbt = new NBTItem(pageInfo);
					nbt.setInteger("log-page", page);
					nbt.applyNBT(pageInfo);
				}
				
				int start = page*ITEMS_PER_PAGE;
				int end = (page+1)*ITEMS_PER_PAGE;
				if(start < 0)
					start = 0;
				if(end > logList.size())
					end = logList.size();
				
				for(int i = 0; i < 9; ++i) {
					contents.set(ITEMS_PER_PAGE+i, empty);
				}
				contents.set(49, pageInfo);
				if(page > 0) 
					contents.set(47, previous);
				if(end < logList.size())
					contents.set(51, next);
				
				for(int i = 0; i < (end-start); ++i) {
					ItemStack it = logList.get(start + i);
					contents.set(i, it);
				}
			}
		};
	}
	
	private InventoryProvider getLogProvider(Clan clan, String date, int page) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				File clanDir = new File(FileManager.getClanDir(), clan.getId());
				File logDir = new File(clanDir, "logs");
				File log = new File(logDir, date+".log");
				if(!log.exists())
					return;
				
				try {
					List<ItemStack> logs = Files.lines(log.toPath())
							.parallel()
							.map(line -> {
								String date = line.substring(1, 20);
								String text = line.substring(22, line.length());
								return new Pair<>(date, text);
							})
							.sorted((pair1, pair2) -> pair2.getKey().compareTo(pair1.getKey()))
							.map(pair -> {
								ItemStack it = new ItemStack(Material.PAPER);{
									ItemMeta im = it.getItemMeta();
									im.setDisplayName("§7§o"+pair.getKey());
									im.setLore(Arrays.asList("§f"+pair.getValue()));
									it.setItemMeta(im);
								}
								return it;
							})
							.collect(Collectors.toList());
					ItemStack pageInfo = new ItemStack(Material.PAPER);{
						ItemMeta im = pageInfo.getItemMeta();
						im.setDisplayName("§f§lStrona "+(page+1));
						pageInfo.setItemMeta(im);
						
						NBTItem nbt = new NBTItem(pageInfo);
						nbt.setInteger("log-page", page);
						nbt.setString("log-date", date);
						nbt.applyNBT(pageInfo);
					}
					
					int start = page*ITEMS_PER_PAGE;
					int end = (page+1)*ITEMS_PER_PAGE;
					if(start < 0)
						start = 0;
					if(end > logs.size())
						end = logs.size();
					
					for(int i = 0; i < 9; ++i) {
						contents.set(ITEMS_PER_PAGE+i, empty);
					}
					contents.set(49, pageInfo);
					contents.set(53, back);
					if(page > 0) 
						contents.set(47, previous);
					if(end < logs.size())
						contents.set(51, next);
					
					for(int i = 0; i < (end-start); ++i) {
						ItemStack it = logs.get(start + i);
						contents.set(i, it);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
}
