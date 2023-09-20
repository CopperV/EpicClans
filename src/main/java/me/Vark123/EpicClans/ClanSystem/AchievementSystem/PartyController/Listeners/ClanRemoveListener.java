package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.ClanSystem.Events.ClanLeaveEvent;

public class ClanRemoveListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(ClanLeaveEvent e) {
		if(e.isCancelled())
			return;
		
		Clan clan = e.getClan();
		ClanRunController.get().getClanParties().entrySet()
			.stream()
			.filter(entry -> entry.getValue().equals(clan))
			.map(entry -> entry.getKey())
			.collect(Collectors.toList())
			.stream()
			.forEach(party -> ClanRunController.get().getClanParties().remove(party));
		ClanRunController.get().getSoloClanRuns().entrySet()
			.stream()
			.filter(entry -> entry.getValue().equals(clan))
			.map(entry -> entry.getKey())
			.collect(Collectors.toList())
			.stream()
			.forEach(p -> ClanRunController.get().getSoloClanRuns().remove(p));
	}

}
