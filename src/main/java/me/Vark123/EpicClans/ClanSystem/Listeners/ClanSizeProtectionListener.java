package me.Vark123.EpicClans.ClanSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Events.ClanInviteEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanJoinEvent;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;

public class ClanSizeProtectionListener implements Listener {

	@EventHandler
	public void onInvite(ClanInviteEvent e) {
		if(e.isCancelled())
			return;
		
		Clan clan = e.getClan();
		clan.getUpgrades().stream()
			.filter(upgrade -> upgrade.getId().equals("players"))
			.findAny()
			.ifPresent(clanUpgrade -> {
				UpgradesManager.get().getClanUpgradeById("players")
					.ifPresent(upgrade -> {
						int clanSize = clan.getMembers().size();
						int size = (int) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
						if(clanSize < size)
							return;
						e.setCancelled(true);
						e.setCancelMessage("Klan posiada maksymalna ilosc graczy!");
					});
			});
	}

	@EventHandler
	public void onJoin(ClanJoinEvent e) {
		if(e.isCancelled())
			return;
		
		Clan clan = e.getClan();
		clan.getUpgrades().stream()
			.filter(upgrade -> upgrade.getId().equals("players"))
			.findAny()
			.ifPresent(clanUpgrade -> {
				UpgradesManager.get().getClanUpgradeById("players")
					.ifPresent(upgrade -> {
						int clanSize = clan.getMembers().size();
						int size = (int) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
						if(clanSize < size)
							return;
						e.setCancelled(true);
						e.setCancelMessage("Klan posiada maksymalna ilosc graczy!");
					});
			});
	}
	
}
