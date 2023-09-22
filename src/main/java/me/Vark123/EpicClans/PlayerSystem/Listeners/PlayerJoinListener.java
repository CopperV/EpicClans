package me.Vark123.EpicClans.PlayerSystem.Listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID uid = p.getUniqueId();
		PlayerManager.get().getByUID(uid).ifPresentOrElse(cPlayer -> { },
				() -> {
					ClanPlayer cPlayer = new ClanPlayer(uid);
					PlayerManager.get().registerPlayer(cPlayer);
				});
	}
	
}
