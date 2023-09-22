package me.Vark123.EpicClans.PlayerSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import lombok.Getter;

@Getter
public final class PlayerManager {

	private static final PlayerManager inst = new PlayerManager();
	
	private final Collection<ClanPlayer> players;
	
	private PlayerManager() {
		players = new HashSet<>();
	}
	
	public static final PlayerManager get() {
		return inst;
	}
	
	public void registerPlayer(ClanPlayer cPlayer) {
		players.add(cPlayer);
	}

	public void unregisterPlayer(ClanPlayer cPlayer) {
		players.remove(cPlayer);
	}
	
	public Optional<ClanPlayer> getByUID(UUID uid) {
		return players
				.stream()
				.filter(cPlayer -> cPlayer.getUid().equals(uid))
				.findAny();
	}
	
}
