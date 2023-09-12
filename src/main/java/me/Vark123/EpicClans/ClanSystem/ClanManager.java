package me.Vark123.EpicClans.ClanSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import lombok.Getter;

@Getter
public final class ClanManager {

	private static final ClanManager inst = new ClanManager();
	
	private final Collection<Clan> clans;
	
	private final Collection<ClanRole> baseRoles;
	
	private ClanManager() {
		clans = new HashSet<>();
		
		baseRoles = new HashSet<>();
		baseRoles.add(new ClanRole("leader",
				"§e♚ Lider",
				0,
				false,
				Arrays.asList(ClanPermission.values())));
		baseRoles.add(new ClanRole("member",
				"§7Czlonek",
				Integer.MAX_VALUE,
				false,
				new ArrayList<>()));
	}
	
	public static final ClanManager get() {
		return inst;
	}
	
	public void registerClan(Clan clan) {
		this.clans.add(clan);
	}
	
	public void unregisterClan(Clan clan) {
		this.clans.remove(clan);
	}
	
}
