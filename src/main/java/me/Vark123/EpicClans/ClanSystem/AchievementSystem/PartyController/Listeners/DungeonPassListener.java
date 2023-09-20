package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementManager;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementType;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicParty.PlayerPartySystem.PartyPlayer;
import me.Vark123.EpicRPGSkillsAndQuests.PlayerSystem.PlayerQuestImpl.PlayerDungeonQuest;
import me.Vark123.EpicRPGSkillsAndQuests.QuestSystem.DungeonSystem.Events.DungeonEndEvent;

public class DungeonPassListener implements Listener {

	@EventHandler
	public void onEnd(DungeonEndEvent e) {
		PlayerDungeonQuest dungeon = e.getDungeon();
		PartyPlayer owner = dungeon.getPartyPlayer();
		Player p = owner.getPlayer();
		
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresent(cPlayer -> {
			cPlayer.getClan().ifPresent(clan -> {
				if(!dungeon.isSoloRun()) {
					if(dungeon.getParty().isPresent()) {
						Party party = dungeon.getParty().get();
						if(!ClanRunController.get().getClanParties().containsKey(party))
							return;
					} else {
						if(!ClanRunController.get().getSoloClanRuns().containsKey(p))
							return;
					}
				}
				
				String display = dungeon.getQuest().getDisplay();
				AchievementManager.get().getAchievements().stream()
					.filter(achievement -> achievement.getType().equals(AchievementType.DUNGEON_PASS)
							&& achievement.getTarget().equals(display)
							&& !clan.getCompletedAchievements().contains(achievement.getId())
							&& dungeon.getWorld().contains(achievement.getDifficulty()))
					.forEach(achievement -> AchievementManager.get().completeAchievement(clan, achievement));
			});
		});
	}
	
}
