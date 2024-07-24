package me.Vark123.EpicClans.ClanSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Events.ClanCreateEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgVault;

public class ClanCreateResourcesListener implements Listener{

	private static final int MONEY = 75_000;
	private static final int RUDA = 10_000;
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkResources(ClanCreateEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getCreator().toBukkitPlayer().getPlayer();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		if(vault.getMoney() < MONEY || vault.getBrylkiRudy() < RUDA) {
			e.setCancelMessage("Nie posiadasz wystarczajaco surowcow na stworzenie klanu!");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void spendResources(ClanCreateEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getCreator().toBukkitPlayer().getPlayer();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgVault vault = rpg.getVault();
		
		vault.removeMoney(MONEY);
		vault.removeBrylkiRudy(RUDA);
	}
	
}
