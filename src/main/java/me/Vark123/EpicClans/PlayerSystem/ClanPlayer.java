package me.Vark123.EpicClans.PlayerSystem;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.ClanSystem.Clan;

@AllArgsConstructor
@Getter
public class ClanPlayer {

	private UUID uid;
	@Setter
	private Clan clan;

	public ClanPlayer(UUID uid) {
		this(uid, null);
	}
	
	public OfflinePlayer toBukkitPlayer() {
		return Bukkit.getOfflinePlayer(uid);
	}
	
	public Optional<Clan> getClan() {
		return Optional.ofNullable(clan);
	}
	
}
