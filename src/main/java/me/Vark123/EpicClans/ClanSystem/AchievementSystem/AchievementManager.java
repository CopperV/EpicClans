package me.Vark123.EpicClans.ClanSystem.AchievementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import net.md_5.bungee.api.ChatColor;

@Getter
public final class AchievementManager {

	private static final AchievementManager inst = new AchievementManager();
	
	private final List<ClanAchievement> achievements;
	
	private AchievementManager() {
		achievements = new ArrayList<>();
	}
	
	public static final AchievementManager get() {
		return inst;
	}
	
	public void registerAchievement(ClanAchievement achievement) {
		achievements.add(achievement);
	}
	
	public void completeAchievement(Clan clan, ClanAchievement achievement) {
		clan.getCompletedAchievements().add(achievement.getId());
		
		ClanTreasury treasury = clan.getTreasury();
		AchievementReward reward = achievement.getReward();
		treasury.addMoney(reward.getMoney());
		treasury.addStygia(reward.getStygia());
		treasury.addCoins(reward.getCoins());
		treasury.addRuda(reward.getBrylki());
		treasury.addPr(reward.getPr());

		clan.broadcastMessage("§6§lGRATULACJE! §bZdobyliscie osiagniecie klanowe: §r"+achievement.getDisplay());
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] Clan "+clan.getId()+" has completed achievement "+achievement.getId());
		clan.getLogger().logMessage("Zdobyto klanowe osiagniecie "+achievement.getDisplay());
	}
	
}
