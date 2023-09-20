package me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import net.md_5.bungee.api.ChatColor;

public class BossDeathListener implements Listener {

	@EventHandler
	public void onDeath(MythicMobDeathEvent e) {
		if(!GameManager.get().getState().equals(GameState.RUN))
			return;
		
		IGame game = GameManager.get().getGame();
		if(game == null || !(game instanceof BossFightGame))
			return;
		
		BossFightGame bossFight = (BossFightGame) game;
		ActiveMob mob = e.getMob();
		if(!bossFight.getBoss().equals(mob))
			return;
		
		bossFight.getDamageCounter().entrySet().stream()
			.max((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
			.ifPresent(entry -> {
				Clan clan = entry.getKey();
				double damage = entry.getValue();
				
				Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
						+ "§b§lCoz za niesamowite emocje!");
				Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
						+ "§b§lTurniej walki z §6§lPOTEZNYM PRZECIWNIKIEM §b§lwygrywa klan §r"+clan.getColor()+clan.getId()+" §b§lzadajac §7§l"+((int) damage)+" §b§lobrazen!");
				
				ClanTreasury treasury = clan.getTreasury();
				treasury.addPr(1);
				
				clan.broadcastMessage("§bDo skarbca zostal dodany §6§o1 punkt rozwoju §bza zwyciestwo w turnieju klanowym!");
				Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] Clan "+clan.getId()+" has won tournament");
				clan.getLogger().logMessage("Do skarbca zostal dodany 1 punkt rozwoju za zwyciestwo w turnieju klanowym!");
			});
		GameManager.get().stopGame();
	}
	
}
