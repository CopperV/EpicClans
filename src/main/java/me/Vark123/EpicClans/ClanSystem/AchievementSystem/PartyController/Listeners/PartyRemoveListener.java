package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicParty.PlayerPartySystem.Events.PartyRemoveEvent;
import me.Vark123.EpicRPGSkillsAndQuests.PlayerSystem.PlayerQuestImpl.PlayerDungeonQuest;

public class PartyRemoveListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreate(PartyRemoveEvent e) {
		if(e.isCancelled())
			return;
		
		Party party = e.getParty();
		PartyPlayer leader = party.getLeader();
		
		ClanRunController.get().getClanParties().remove(party);
		
		me.Vark123.EpicRPGSkillsAndQuests.PlayerSystem.PlayerManager.get().getQuestPlayer(leader.getPlayer()).ifPresent(qp -> {
			qp.getActiveQuests().values().stream()
				.filter(pQuest -> pQuest instanceof PlayerDungeonQuest)
				.map(pQuest -> (PlayerDungeonQuest) pQuest)
				.findAny()
				.ifPresent(dungeon -> {
					dungeon.setParty(null);
					PartyPlayer newOnwer = dungeon.getPartyPlayer();
					Player p = newOnwer.getPlayer();
					ClanPlayer cPlayer = PlayerManager.get().getByUID(p.getUniqueId()).get();
					ClanRunController.get().getSoloClanRuns().put(p, cPlayer.getClan().get());
				});
		});
	}

}
