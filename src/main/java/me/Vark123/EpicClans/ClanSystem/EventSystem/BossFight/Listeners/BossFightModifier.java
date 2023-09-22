package me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class BossFightModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		if(!(damager instanceof Player))
			return damage;
		
		if(!GameManager.get().getState().equals(GameState.RUN))
			return damage;
		
		IGame game = GameManager.get().getGame();
		if(game == null || !(game instanceof BossFightGame))
			return damage;
		
		Optional<ClanPlayer> oClanPlayer = PlayerManager.get().getByUID(damager.getUniqueId());
		if(oClanPlayer.isEmpty())
			return damage;
		
		ClanPlayer cPlayer = oClanPlayer.get();
		if(cPlayer.getClan().isEmpty())
			return damage;
		
		BossFightGame bossFight = (BossFightGame) game;
		Clan clan = cPlayer.getClan().get();
		if(!bossFight.getDamageCounter().containsKey(clan))
			return damage;
		
		if(!damager.getWorld().getName().equals(bossFight.getWorld()))
			return damage;
		
		double countedDmg = bossFight.getDamageCounter().get(clan);
		bossFight.getDamageCounter().put(clan, countedDmg+damage);
		return damage;
	}

}
