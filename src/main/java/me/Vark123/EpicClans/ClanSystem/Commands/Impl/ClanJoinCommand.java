package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanJoinCommand extends AClanCommand {

	public ClanJoinCommand() {
		super("join", new String[]{"dolacz"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> result.setFalse(), () -> {
					cPlayer.getClanInvitations().entrySet().stream()
						.filter(entry -> !entry.getKey().isCancelled()
								&& ClanManager.get().getClans().contains(entry.getValue()))
						.findAny()
						.ifPresentOrElse(entry -> { }, () -> result.setFalse());
				});
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bMusisz podac klan, do ktorego chcesz dolaczyc");
			return false;
		}
		
		MutableBoolean result = new MutableBoolean(true);
		
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		cPlayer.getClanInvitations().entrySet().stream()
			.filter(entry -> !entry.getKey().isCancelled()
					&& entry.getValue().getId().equals(args[0])
					&& ClanManager.get().getClans().contains(entry.getValue()))
			.findFirst()
			.ifPresentOrElse(entry -> {
				cPlayer.cancelInvitation(entry.getKey());
				Clan clan = entry.getValue();
				result.setValue(ClanManager.get().joinToClan(clan, cPlayer));
			}, () -> {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §dNie masz zadnego zaproszenia do klanu §7§o"+args[0]);
				result.setFalse();
			});
		
		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan dolacz <klan> §7- Dolacz do swojego wymarzonego klanu");
	}

}
