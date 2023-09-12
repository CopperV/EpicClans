package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanChatSendListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerQuitListener;

public final class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerClanChatSendListener(), inst);
	}
	
}
