package me.Vark123.EpicClans.PlayerSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;

public class PlayerRespTaskMoveListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.isCancelled())
			return;
		if(e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()))
			return;
		Player p = e.getPlayer();
		BukkitTask task = GameManager.get().getRespTasks().get(p);
		if(task == null)
			return;
		task.cancel();
		GameManager.get().getRespTasks().remove(p);
		p.sendTitle(" ", "§e§lTELEPORTACJA ANULOWANA", 5, 10, 15);
	}
	
}
