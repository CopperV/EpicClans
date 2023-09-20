package me.Vark123.EpicClans;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.Placeholders.ClanPlaceholders;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;

	private InventoryManager invManager;
	private PlaceholderExpansion playerPlaceholders;
	
	private CalendarEventsApi calendar;
	
	@Override
	public void onEnable() {
		inst = this;

		invManager = new InventoryManager(inst);
		invManager.invoke();
		
		playerPlaceholders = new ClanPlaceholders();
		playerPlaceholders.register();
		
		CalendarEvents calend = (CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents");
		calendar = calend.getApi();
		
		CommandManager.setExecutors();
		ListenerManager.registerListeners();
		FileManager.init();
		DatabaseManager.init();
		
		EpicClansApi.get();
	}

	@Override
	public void onDisable() {
		playerPlaceholders.unregister();
		
		FileManager.saveClans();
		ClanManager.get().getClans().clear();
	}
	
}
