package me.Vark123.EpicClans.ClanSystem.ColorSystem;

import java.util.Arrays;

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

@Getter
public class ColorMenuManager {

	private static final ColorMenuManager inst = new ColorMenuManager();
	
	private final ItemStack red;
	private final ItemStack green;
	private final ItemStack blue;

	private final ItemStack minus20;
	private final ItemStack minus10;
	private final ItemStack minus5;
	private final ItemStack minus1;
	private final ItemStack plus1;
	private final ItemStack plus5;
	private final ItemStack plus10;
	private final ItemStack plus20;
	
	private ColorMenuManager() {
		red = new ItemStack(Material.RED_TERRACOTTA);
		green = new ItemStack(Material.GREEN_TERRACOTTA);
		blue = new ItemStack(Material.BLUE_TERRACOTTA);

		minus20 = new ItemStack(Material.PAPER,20);{
			ItemMeta im = minus20.getItemMeta();
			im.setDisplayName("§o-20");
			minus20.setItemMeta(im);
		}
		minus10 = new ItemStack(Material.PAPER,10);{
			ItemMeta im = minus10.getItemMeta();
			im.setDisplayName("§o-10");
			minus10.setItemMeta(im);
		}
		minus5 = new ItemStack(Material.PAPER,5);{
			ItemMeta im = minus5.getItemMeta();
			im.setDisplayName("§o-5");
			minus5.setItemMeta(im);
		}
		minus1 = new ItemStack(Material.PAPER,1);{
			ItemMeta im = minus1.getItemMeta();
			im.setDisplayName("§o-1");
			minus1.setItemMeta(im);
		}
		plus1 = new ItemStack(Material.PAPER,1);{
			ItemMeta im = plus1.getItemMeta();
			im.setDisplayName("§o+1");
			plus1.setItemMeta(im);
		}
		plus5 = new ItemStack(Material.PAPER,5);{
			ItemMeta im = plus5.getItemMeta();
			im.setDisplayName("§o+5");
			plus5.setItemMeta(im);
		}
		plus10 = new ItemStack(Material.PAPER,10);{
			ItemMeta im = plus10.getItemMeta();
			im.setDisplayName("§o+10");
			plus10.setItemMeta(im);
		}
		plus20 = new ItemStack(Material.PAPER,20);{
			ItemMeta im = plus20.getItemMeta();
			im.setDisplayName("§o+20");
			plus20.setItemMeta(im);
		}
	}
	
	public static final ColorMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p, Clan clan) {
		RyseInventory.builder()
			.title("§6§lKOLOR KLANU")
			.rows(4)
			.disableUpdateTask()
			.listener(ColorMenuEvents.getEvents().getClickEvent())
			.provider(getProvider(clan))
			.build(Main.getInst())
			.open(p);
	}
	
	private InventoryProvider getProvider(Clan clan) {
		return new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				String color = clan.getColor();
				String hexColor = color.replace("§", "").replace("x", "").toUpperCase();
				int _red = Integer.parseInt(hexColor.subSequence(0, 2).toString(), 16);
				int _green = Integer.parseInt(hexColor.subSequence(2, 4).toString(), 16);
				int _blue = Integer.parseInt(hexColor.subSequence(4, 6).toString(), 16);
				
				ItemStack colorIt = new ItemStack(Material.WHITE_DYE);{
					ItemMeta im = colorIt.getItemMeta();
					im.setDisplayName("§7Obecny kolor: §r"+color+"PROBKA");
					im.setLore(Arrays.asList(
							"§7ID: §r"+color+clan.getId(),
							"§7Nazwa: §r"+color+clan.getName(),
							"§7Kod koloru: §b§o#"+hexColor,
							" ",
							"§cCzerwony: §7"+_red,
							"§aZielony: §7"+_green,
							"§bNiebieski: §7"+_blue));
					colorIt.setItemMeta(im);
					
					contents.set(4, colorIt);
					contents.set(13, red);
					contents.set(22, green);
					contents.set(31, blue);
					
					if(_red - 20 >= 0)
						contents.set(9, minus20);
					if(_red - 10 >= 0)
						contents.set(10, minus10);
					if(_red - 5 >= 0)
						contents.set(11, minus5);
					if(_red - 1 >= 0)
						contents.set(12, minus1);
					if(_red + 1 < 256)
						contents.set(14, plus1);
					if(_red + 5 < 256)
						contents.set(15, plus5);
					if(_red + 10 < 256)
						contents.set(16, plus10);
					if(_red + 20 < 256)
						contents.set(17, plus20);
					
					if(_green - 20 >= 0)
						contents.set(18, minus20);
					if(_green - 10 >= 0)
						contents.set(19, minus10);
					if(_green - 5 >= 0)
						contents.set(20, minus5);
					if(_green - 1 >= 0)
						contents.set(21, minus1);
					if(_green + 1 < 256)
						contents.set(23, plus1);
					if(_green + 5 < 256)
						contents.set(24, plus5);
					if(_green + 10 < 256)
						contents.set(25, plus10);
					if(_green + 20 < 256)
						contents.set(26, plus20);
					
					if(_blue - 20 >= 0)
						contents.set(27, minus20);
					if(_blue - 10 >= 0)
						contents.set(28, minus10);
					if(_blue - 5 >= 0)
						contents.set(29, minus5);
					if(_blue - 1 >= 0)
						contents.set(30, minus1);
					if(_blue + 1 < 256)
						contents.set(32, plus1);
					if(_blue + 5 < 256)
						contents.set(33, plus5);
					if(_blue + 10 < 256)
						contents.set(34, plus10);
					if(_blue + 20 < 256)
						contents.set(35, plus20);
				}
			}
		};
	}
	
}
