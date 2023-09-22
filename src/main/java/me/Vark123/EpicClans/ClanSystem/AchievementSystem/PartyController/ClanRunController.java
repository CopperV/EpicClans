package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicParty.PlayerPartySystem.Party;

@Getter
public final class ClanRunController {

	private static final ClanRunController inst = new ClanRunController();
	
	private final Map<Party, Clan> clanParties;
	private final Map<Player, Clan> soloClanRuns;
	
	private ClanRunController() {
		clanParties = new ConcurrentHashMap<>();
		soloClanRuns = new ConcurrentHashMap<>();
	}
	
	public static final ClanRunController get() {
		return inst;
	}
	
}
