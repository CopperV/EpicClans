package me.Vark123.EpicClans.ClanSystem.Commands;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

@Getter
public final class ClanCommandManager {

	private static final ClanCommandManager inst = new ClanCommandManager();
	
	private final Map<String, AClanCommand> clanSubcommands;
	
	private ClanCommandManager() {
		clanSubcommands = new LinkedHashMap<>();
	}
	
	public static final ClanCommandManager get() {
		return inst;
	}
	
	public void registerSubcommand(AClanCommand subcmd) {
		clanSubcommands.put(subcmd.getCmd().toLowerCase(), subcmd);
		for(String alias : subcmd.getAliases())
			clanSubcommands.put(alias, subcmd);
	}
	
	public Optional<AClanCommand> getClanSubcommand(String subcmd) {
		return Optional.ofNullable(clanSubcommands.get(subcmd.toLowerCase()));
	}
	
}
