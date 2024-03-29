package me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementManager;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementType;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.ClanRunController;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicParty.PlayerPartySystem.Party;
import me.Vark123.EpicRPGSkillsAndQuests.PlayerSystem.PlayerQuestImpl.PlayerDungeonQuest;

public class DungeonBossKillListener implements Listener {

	@EventHandler
	public void onDeath(MythicMobDeathEvent e) {
		Entity victim = e.getEntity();
		LivingEntity _killer = e.getKiller();
		if(victim instanceof Player)
			return;
		if(_killer == null)
			return;
		if(!(_killer instanceof Player))
			return;
		
		Player killer = (Player) _killer;
		PlayerManager.get().getByUID(killer.getUniqueId()).ifPresent(cPlayer -> {
			cPlayer.getClan().ifPresent(clan -> {
				me.Vark123.EpicRPGSkillsAndQuests.PlayerSystem.PlayerManager.get().getQuestPlayer(killer).ifPresent(qp -> {
					qp.getActiveQuests().values().stream()
					.filter(pQuest -> pQuest instanceof PlayerDungeonQuest)
					.map(pQuest -> (PlayerDungeonQuest) pQuest)
					.findAny()
					.ifPresent(dungeon -> {
						if(!dungeon.isSoloRun()) {
							if(dungeon.getParty().isPresent()) {
								Party party = dungeon.getParty().get();
								if(!ClanRunController.get().getClanParties().containsKey(party))
									return;
							} else {
								if(!ClanRunController.get().getSoloClanRuns().containsKey(killer))
									return;
							}
						}

						String display = victim.getName();
						AchievementManager.get().getAchievements().stream()
							.filter(achievement -> achievement.getType().equals(AchievementType.DUNGEON_KILL)
									&& achievement.getTarget().equals(display)
									&& !clan.getCompletedAchievements().contains(achievement.getId())
									&& victim.getWorld().getName().contains(achievement.getDifficulty()))
							.forEach(achievement -> AchievementManager.get().completeAchievement(clan, achievement));
					});
				});
			});
		});
	}
	
}
