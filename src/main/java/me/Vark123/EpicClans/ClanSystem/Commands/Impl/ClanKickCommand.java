package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanKickCommand extends AClanCommand {

	public ClanKickCommand() {
		super("kick", new String[]{"wyrzuc"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.KICK));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bMusisz podac gracza, ktorego chcesz wyrzucic");
			return false;
		}
		
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if(!target.hasPlayedBefore()) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bGracz §7§o"+args[0]+" §bnigdy nie pojawil sie na serwerze!");
			return false;
		}
		if(target.getUniqueId().equals(sender.getUniqueId())) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz wyrzucic samego siebie z klanu!");
			return false;
		}
		
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(target.getUniqueId()).ifPresentOrElse(cTarget -> {
			cTarget.getClan().ifPresentOrElse(targetClan -> {
				if(!targetClan.equals(clan)) {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §bnie znajduje sie w Twoim klanie!");
					result.setFalse();
					return;
				}
				ClanRole role = clan.getMembers().get(cPlayer);
				ClanRole targetRole = clan.getMembers().get(cTarget);
				if(targetRole.getPriority() <= role.getPriority()) {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §bposiada zbyt wysoka range! Nie mozesz go wyrzucic!");
					result.setFalse();
					return;
				}
				ClanManager.get().kickFromClan(clan, cPlayer, cTarget);
			}, () -> {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §bnie znajduje sie w Twoim klanie!");
				result.setFalse();
			});
		}, () -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §7§o"+args[0]+" §bnie znajduje sie w Twoim klanie!");
			result.setFalse();
		});

		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan wyrzuc <gracz> §7- Wyrzuc gracza z Twojego klanu");
	}

}
