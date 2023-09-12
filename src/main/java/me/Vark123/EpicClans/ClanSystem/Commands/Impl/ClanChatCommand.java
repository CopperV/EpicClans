package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanChatCommand extends AClanCommand {

	public ClanChatCommand() {
		super("chat", new String[]{"czat"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				if(cPlayer.getClan().isEmpty())
					result.setFalse();
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		if(args == null || args.length < 1) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz wyslac pustej wiadomosci!");
			return false;
		}
		
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		StringBuilder msg = new StringBuilder();
		for(String arg : args) {
			msg.append(arg);
			msg.append(" ");
		}
		msg.deleteCharAt(msg.length() - 1);
		
		clan.sendClanMessage(cPlayer, msg.toString());

		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan czat <wiadomosc> §7- Wysyla wiadomosc na specjalny czat klanowy");
	}

}
