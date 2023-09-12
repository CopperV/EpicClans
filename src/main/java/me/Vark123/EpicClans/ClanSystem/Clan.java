package me.Vark123.EpicClans.ClanSystem;

import java.util.Collection;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
@Builder
public class Clan {

	private String id;
	private String name;
	private String color;
	
	private Collection<ClanRole> roles;
	private Map<ClanPlayer, ClanRole> members;
	
}
