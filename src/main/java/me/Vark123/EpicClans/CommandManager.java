package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.ClanSystem.Commands.BaseClanCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.ClanCommandManager;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanChatCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanCreateCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanInviteCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanJoinCommand;

public final class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicclan").setExecutor(new BaseClanCommand());
	
		ClanCommandManager.get().registerSubcommand(new ClanCreateCommand());
		ClanCommandManager.get().registerSubcommand(new ClanInviteCommand());
		ClanCommandManager.get().registerSubcommand(new ClanJoinCommand());
		ClanCommandManager.get().registerSubcommand(new ClanChatCommand());
	}
	
}
