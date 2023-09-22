package me.Vark123.EpicClans.ClanSystem.Listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Events.ClanKickEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanLeaveEvent;

public class ClanLeaveWarehouseProtectionListener implements Listener {

	@EventHandler
	public void onKick(ClanKickEvent e) {
		if(e.isCancelled())
			return;
		
		OfflinePlayer offPlayer = e.getKicked().toBukkitPlayer();
		if(!offPlayer.isOnline())
			return;
		
		Player p = offPlayer.getPlayer();
		Clan clan = e.getClan();
		clan.getWarehouses().values().stream()
			.filter(inv -> inv.getViewers().stream()
					.filter(human -> human.getUniqueId().equals(p.getUniqueId()))
					.findAny()
					.isPresent())
			.findAny()
			.ifPresent(inv -> p.closeInventory());
	}

	@EventHandler
	public void onLeave(ClanLeaveEvent e) {
		if(e.isCancelled())
			return;
		
		OfflinePlayer offPlayer = e.getOldMember().toBukkitPlayer();
		if(!offPlayer.isOnline())
			return;
		
		Player p = offPlayer.getPlayer();
		Clan clan = e.getClan();
		clan.getWarehouses().values().stream()
			.filter(inv -> inv.getViewers().stream()
					.filter(human -> human.getUniqueId().equals(p.getUniqueId()))
					.findAny()
					.isPresent())
			.findAny()
			.ifPresent(inv -> p.closeInventory());
	}
	
}
