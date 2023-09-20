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
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyJoinEvent;

public class PartyJoinListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PartyJoinEvent e) {
		if(e.isCancelled())
			return;
		
		Party party = e.getParty();
		if(!ClanRunController.get().getClanParties().containsKey(party))
			return;
		
		PartyPlayer member = e.getNewMember();
		Player p = member.getPlayer();
		ClanRunController.get().getSoloClanRuns().remove(p);
		
		ClanPlayer cPlayer = PlayerManager.get()
				.getByUID(p.getUniqueId()).get();
		Clan clan = ClanRunController.get().getClanParties().get(party);
		if(cPlayer.getClan().isPresent() && cPlayer.getClan().get().equals(clan))
			return;
		
		ClanRunController.get().getClanParties().remove(party);
	}

}
