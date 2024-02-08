package me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;

public class BossFightListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		
		if(!(damager instanceof Player))
			return;
		
		if(!GameManager.get().getState().equals(GameState.RUN))
			return;
		
		IGame game = GameManager.get().getGame();
		if(game == null || !(game instanceof BossFightGame))
			return;
		
		Optional<ClanPlayer> oClanPlayer = PlayerManager.get().getByUID(damager.getUniqueId());
		if(oClanPlayer.isEmpty())
			return;
		
		ClanPlayer cPlayer = oClanPlayer.get();
		if(cPlayer.getClan().isEmpty())
			return;
		
		BossFightGame bossFight = (BossFightGame) game;
		Clan clan = cPlayer.getClan().get();
		if(!bossFight.getDamageCounter().containsKey(clan))
			return;
		
		if(!damager.getWorld().getName().equals(bossFight.getWorld()))
			return;
		
		double damage = e.getFinalDamage();
		double countedDmg = bossFight.getDamageCounter().get(clan);
		bossFight.getDamageCounter().put(clan, countedDmg+damage);
	}

}
