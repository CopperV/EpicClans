package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.ClanSystem.MenuSystem.RemoveSystem.ClanRemoveMenuManager;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanRemoveCommand extends AClanCommand {

	public ClanRemoveCommand() {
		super("remove", new String[]{"usun"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.LEADER));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		ClanRemoveMenuManager.get().openMenu(sender);
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan usun §7- Usun swoj klan");
	}

}
