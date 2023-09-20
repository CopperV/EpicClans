package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.ClanSystem.Events.ClanLeaveEvent;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.PlayerManager;

public class ClanLeaveListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(ClanLeaveEvent e) {
		if(e.isCancelled())
			return;
		ClanPlayer cPlayer = e.getOldMember();
		OfflinePlayer offPlayer = cPlayer.toBukkitPlayer();
		if(!offPlayer.isOnline())
			return;
		
		Player p = offPlayer.getPlayer();
		ClanRunController.get().getSoloClanRuns().remove(p);
		PlayerManager.get().getPartyPlayer(p).ifPresent(pp -> {
			pp.getParty().ifPresent(party -> {
				ClanRunController.get().getClanParties().remove(party);
			});
		});
	}
	
}
