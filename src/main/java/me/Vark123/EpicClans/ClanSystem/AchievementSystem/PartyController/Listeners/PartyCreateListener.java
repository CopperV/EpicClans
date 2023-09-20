package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyCreateEvent;

public class PartyCreateListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreate(PartyCreateEvent e) {
		if(e.isCancelled())
			return;
		
		Party party = e.getParty();
		PartyPlayer leader = e.getLeader();
		PartyPlayer member = e.getMember();
		
		Player p1 = leader.getPlayer();
		Player p2 = member.getPlayer();
		ClanRunController.get().getSoloClanRuns().remove(p1);
		ClanRunController.get().getSoloClanRuns().remove(p2);
		
		ClanPlayer cPlayer1 = PlayerManager.get()
				.getByUID(p1.getUniqueId()).get();
		ClanPlayer cPlayer2 = PlayerManager.get()
				.getByUID(p2.getUniqueId()).get();
	
		if(cPlayer1.getClan().isEmpty() || cPlayer2.getClan().isEmpty()
				|| !cPlayer1.getClan().get().equals(cPlayer2.getClan().get()))
			return;
		
		Clan clan = cPlayer1.getClan().get();
		ClanRunController.get().getClanParties().put(party, clan);
	}
	
}
