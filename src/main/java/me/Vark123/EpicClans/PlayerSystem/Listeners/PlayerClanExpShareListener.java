package me.Vark123.EpicClans.PlayerSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.Events.ExpModifyEvent;
import me.Vark123.EpicRPG.Options.Serializables.ResourcesInfoSerializable;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class PlayerClanExpShareListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onExp(ExpModifyEvent e) {
		if(e.isCancelled())
			return;
		if(e.getReason().equalsIgnoreCase("quest"))
			return;
		
		Player p = e.getRpg().getPlayer();
		PlayerManager.get().getByUID(p.getUniqueId()).ifPresent(cp -> {
			cp.getClan().ifPresent(clan -> {
				int amount = (int) (e.getAmount()*e.getModifier());
				if(amount <= 0)
					return;
				clan.getUpgrades().stream()
					.filter(upgrade -> upgrade.getId().equals("exp"))
					.findAny()
					.ifPresent(clanUpgrade -> {
						UpgradesManager.get().getClanUpgradeById("exp")
							.ifPresent(upgrade -> {
								double mod = (double) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
								int clanExp = (int) (amount * mod);
								if(clanExp <= 0)
									return;
								
								clan.getMembers().keySet().stream()
									.filter(_cp -> !_cp.equals(cp))
									.filter(_cp -> Bukkit.getOfflinePlayer(_cp.getUid()).isOnline())
									.forEach(_cp -> {
										Player _p = Bukkit.getPlayer(_cp.getUid());
										RpgPlayer _rpg = me.Vark123.EpicRPG.Players.PlayerManager.getInstance().getRpgPlayer(_p);
										if(_rpg == null)
											return;
										
										ExpSystem.getInstance().addRawExp(_rpg, clanExp);
										OptionsAPI.get().getPlayerManager().getPlayerOptions(_p)
											.ifPresent(op -> {
												op.getPlayerOptionByID("epicrpg_resources")
													.ifPresent(pOption -> {
														ResourcesInfoSerializable option = (ResourcesInfoSerializable) pOption.getValue();
														if(!option.isClanExpInfo())
															return;
														RpgPlayerInfo info = _rpg.getInfo();
														_p.sendMessage("§7[§r"+clan.getColor()+clan.getId()+"§7]§a+"+ clanExp +" xp §7[§a"+info.getExp()+" xp§7/§a"+info.getNextLevel()+" xp§7]");
													});
											});
									});
							});
					});
			});
		});
	}
	
}
