package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.ClanSystem.Commands.BaseClanCommand;

public final class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicclan").setExecutor(new BaseClanCommand());
	}
	
}
