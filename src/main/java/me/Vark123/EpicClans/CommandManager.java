package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.ClanSystem.Commands.BaseClanCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.ClanCommandManager;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanAchievementCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanChatCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanColorCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanCreateCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanInfoCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanInviteCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanJoinCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanKickCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanLeaderChangeCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanLeaveCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanListCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanLogCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanPartyCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanPromoteCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanRemoveCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanRespCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanRolesCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanRotationCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanSignCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanTreasuryCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanUnsignCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanUpgradeCommand;
import me.Vark123.EpicClans.ClanSystem.Commands.Impl.ClanWarehouseCommand;

public final class CommandManager {

	private CommandManager() { }
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("epicclan").setExecutor(new BaseClanCommand());
	
		ClanCommandManager.get().registerSubcommand(new ClanCreateCommand());
		ClanCommandManager.get().registerSubcommand(new ClanInviteCommand());
		ClanCommandManager.get().registerSubcommand(new ClanJoinCommand());
		ClanCommandManager.get().registerSubcommand(new ClanChatCommand());
		ClanCommandManager.get().registerSubcommand(new ClanKickCommand());
		ClanCommandManager.get().registerSubcommand(new ClanLeaveCommand());
		ClanCommandManager.get().registerSubcommand(new ClanInfoCommand());
		ClanCommandManager.get().registerSubcommand(new ClanListCommand());
		ClanCommandManager.get().registerSubcommand(new ClanTreasuryCommand());
		ClanCommandManager.get().registerSubcommand(new ClanColorCommand());
		ClanCommandManager.get().registerSubcommand(new ClanPartyCommand());
		ClanCommandManager.get().registerSubcommand(new ClanRemoveCommand());
		ClanCommandManager.get().registerSubcommand(new ClanLogCommand());
		ClanCommandManager.get().registerSubcommand(new ClanRolesCommand());
		ClanCommandManager.get().registerSubcommand(new ClanPromoteCommand());
		ClanCommandManager.get().registerSubcommand(new ClanLeaderChangeCommand());
		ClanCommandManager.get().registerSubcommand(new ClanUpgradeCommand());
		ClanCommandManager.get().registerSubcommand(new ClanRotationCommand());
		ClanCommandManager.get().registerSubcommand(new ClanSignCommand());
		ClanCommandManager.get().registerSubcommand(new ClanUnsignCommand());
		ClanCommandManager.get().registerSubcommand(new ClanRespCommand());
		ClanCommandManager.get().registerSubcommand(new ClanAchievementCommand());
		ClanCommandManager.get().registerSubcommand(new ClanWarehouseCommand());
	}
	
}
