package me.Vark123.EpicClans;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;

@Getter
public class Main extends JavaPlugin {

	@Getter
	private static Main inst;

	private InventoryManager invManager;
	
	@Override
	public void onEnable() {
		inst = this;
		
		CommandExecutors.setExecutors();
		ListenerManager.registerListeners();
		FileManager.init();
		DatabaseManager.init();

		invManager = new InventoryManager(inst);
		invManager.invoke();
	}

	@Override
	public void onDisable() {
		
	}
	
}
