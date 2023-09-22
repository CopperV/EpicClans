package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.ClanSystem.MenuSystem.PromoteSystem.PromoteMenuManager;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanPromoteCommand extends AClanCommand {

	public ClanPromoteCommand() {
		super("promote", new String[]{"promuj", "awansuj", "degraduj"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.PROMOTE));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		PromoteMenuManager.get().openPlayerListMenu(sender, clan);
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan promuj §7- Zmien graczom rangi klanowe");
	}

}
