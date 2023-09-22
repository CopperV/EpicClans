package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;

public class PlayerDisconnectListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ClanRunController.get().getSoloClanRuns().remove(p);
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		ClanRunController.get().getSoloClanRuns().remove(p);
	}
	
}
