package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanInviteCommand extends AClanCommand {

	public ClanInviteCommand() {
		super("invite", new String[]{"zapros"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.INVITE));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bMusisz podac gracza, ktorego chcesz zaprosic");
			return false;
		}
		
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		
		Player target = Bukkit.getPlayerExact(args[0]);
		if(target == null) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bGracz §7§o"+args[0]+" §bjest offline!");
			return false;
		}
		if(target.equals(sender)) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz zaprosic samego siebie do klanu!");
			return false;
		}
		
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(target.getUniqueId()).ifPresentOrElse(cTarget -> {
			if(cTarget.getClan().isPresent()) {
				if(cTarget.getClan().get().equals(clan))
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §b"+args[0]+" juz jest w Twoim klanie!");
				else
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §b"+args[0]+" jest czlonkiem innego klanu. Nie mozesz go zaprosic!");
				result.setFalse();
				return;
			}
			cTarget.getClanInvitations().entrySet().stream()
				.filter(entry -> !entry.getKey().isCancelled()
						&& entry.getValue().equals(clan))
				.findAny()
				.ifPresent(entry -> {
					sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §b"+args[0]+" juz otrzymal zaproszenie do klanu!");
					result.setFalse();
				});
			if(result.isTrue())
				cTarget.sendInvitation(cPlayer, clan);
		}, () -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bBLAD! §7§o"+args[0]+" §dnie jest zapisany! Zglos blad administratorowi!");
			result.setFalse();
		});

		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan zapros <gracz> §7- Zapros gracza do swojego klanu");
	}

}
