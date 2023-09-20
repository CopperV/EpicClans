package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanRotationCommand extends AClanCommand {

	public ClanRotationCommand() {
		super("rotation", new String[]{"rotacja"});
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
		clan.getUpgrades().stream()
			.filter(upgrade -> upgrade.getId().equals("rotations"))
			.findAny()
			.ifPresent(clanUpgrade -> {
				int level = clanUpgrade.getLevel();
				String rotation = "klan"+level;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chestcommands open "+rotation+" "+sender.getName());
			});
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan rotacja §7- Otwiera cotygodniowa rotacje klanowa");
	}

}
