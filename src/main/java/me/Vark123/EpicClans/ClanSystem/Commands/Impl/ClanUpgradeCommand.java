package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.ClanSystem.MenuSystem.UpgradeSystem.UpgradeMenuManager;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanUpgradeCommand extends AClanCommand {

	public ClanUpgradeCommand() {
		super("upgrade", new String[]{"ulepszenia", "ulepsz"});
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
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		UpgradeMenuManager.get().openMenu(sender, clan);
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan ulepszenia §7- Otwiera spis ulepszen klanowych");
	}

}
