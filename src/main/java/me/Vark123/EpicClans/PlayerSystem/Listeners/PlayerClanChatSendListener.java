package me.Vark123.EpicClans.PlayerSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class PlayerClanChatSendListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())
			return;
		
		String msg = e.getMessage();
		if(msg.contains("${")) {
			e.setCancelled(true);
			return;
		}
		
		if(msg.charAt(0) != Config.get().getClanChatPrefix())
			return;
		
		e.setCancelled(true);
		
		Player p = e.getPlayer();
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresentOrElse(cPlayer -> {
			cPlayer.getClan().ifPresentOrElse(clan -> {
				StringBuilder builder = new StringBuilder(msg);
				builder.deleteCharAt(0);
				if(builder.isEmpty()) {
					p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz wyslac pustej wiadomosci na czat druzynowy!");
					return;
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						clan.sendClanMessage(cPlayer, builder.toString());
					}
				}.runTask(Main.getInst());
			}, () -> {
				p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie jestes w zadnym klanie! Nie mozesz wysylac wiadomosci na czat klanowy!");
			});
		}, () -> {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bBLAD! §dNie jestes zapisany! Zglos blad administratorowi!");
		});
	}
	
}
