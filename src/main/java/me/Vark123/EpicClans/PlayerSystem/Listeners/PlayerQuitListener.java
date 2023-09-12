package me.Vark123.EpicClans.PlayerSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		cleanPlayer(p);
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		Player p = e.getPlayer();
		cleanPlayer(p);
	}
	
	private void cleanPlayer(Player p) {
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresent(cPlayer -> {
			if(cPlayer.getClan().isEmpty())
				PlayerManager.get().unregisterPlayer(cPlayer);
		});
	}
	
}
