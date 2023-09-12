package me.Vark123.EpicClans;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.Placeholders.ClanPlaceholders;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;

	private InventoryManager invManager;
	private PlaceholderExpansion playerPlaceholders;
	
	@Override
	public void onEnable() {
		inst = this;
		
		CommandManager.setExecutors();
		ListenerManager.registerListeners();
		FileManager.init();
		DatabaseManager.init();

		invManager = new InventoryManager(inst);
		invManager.invoke();
		
		playerPlaceholders = new ClanPlaceholders();
		playerPlaceholders.register();
		
		EpicClansApi.get();
	}

	@Override
	public void onDisable() {
		playerPlaceholders.unregister();
		
		FileManager.saveClans();
		ClanManager.get().getClans().clear();
	}
	
}
