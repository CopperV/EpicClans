package me.Vark123.EpicClans.PlayerSystem.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;

public class PlayerClanFightListener implements Listener {
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresent(cp -> {
			cp.getClan().ifPresent(clan -> {
				clan.getUpgrades().stream()
					.filter(upgrade -> upgrade.getId().equals("attack"))
					.findAny()
					.ifPresent(clanUpgrade -> {
						UpgradesManager.get().getClanUpgradeById("attack")
							.ifPresent(upgrade -> {
								double bonus = (double) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
								e.increaseModifier(bonus);
							});
					});
			});
		});
	}
	
	@EventHandler
	public void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getDamager();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresent(cp -> {
			cp.getClan().ifPresent(clan -> {
				clan.getUpgrades().stream()
					.filter(upgrade -> upgrade.getId().equals("defense"))
					.findAny()
					.ifPresent(clanUpgrade -> {
						UpgradesManager.get().getClanUpgradeById("defense")
							.ifPresent(upgrade -> {
								double bonus = (double) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
								e.decreaseModifier(bonus);
							});
					});
			});
		});
	}
	
}
