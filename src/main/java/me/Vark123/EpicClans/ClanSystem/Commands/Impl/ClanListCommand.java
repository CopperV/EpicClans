package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;

public class ClanListCommand extends AClanCommand {

	public ClanListCommand() {
		super("list", new String[]{ });
	}

	@Override
	public boolean canUse(Player sender) {
		return ClanManager.get().getClans().size() > 0;
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bLista klanow na serwerze EpicRPG:");
		ClanManager.get().getClans().stream().forEachOrdered(clan -> {
			sender.sendMessage("  §4§l» §r§7[§r"+clan.getColor()+clan.getId()+"§r§7] - §b"+clan.getMembers().size()+" czlonkow");
		});
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan list §7- Wypisz na czat wszystkie klany, jakie obecnie istnieja");
	}

}
