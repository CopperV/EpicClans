package me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicMobDespawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import net.md_5.bungee.api.ChatColor;

public class BossDespawnListener implements Listener {

	@EventHandler
	public void onDespawn(MythicMobDespawnEvent e) {
		if(!GameManager.get().getState().equals(GameState.RUN))
			return;
		
		IGame game = GameManager.get().getGame();
		if(game == null || !(game instanceof BossFightGame))
			return;
		
		BossFightGame bossFight = (BossFightGame) game;
		ActiveMob mob = e.getMob();
		if(!bossFight.getBoss().equals(mob))
			return;
		
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] No clan has won tournament");
	
		Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§b§lCoz za niesamowite emocje!");
		Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§b§lPrzeciwnik okazal sie na tyle potezny, ze zaden klan go nie przezyl!");

		GameManager.get().stopGame();
	}
	
}
