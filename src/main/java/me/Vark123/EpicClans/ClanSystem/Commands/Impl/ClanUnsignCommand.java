package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import java.util.logging.Level;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameState;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class ClanUnsignCommand extends AClanCommand {

	public ClanUnsignCommand() {
		super("unsign", new String[]{"wypisz"});
	}

	@Override
	public boolean canUse(Player sender) {
		if(!GameManager.get().getState().equals(GameState.RECORDING))
			return false;
		
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresentOrElse(clan -> {
					if(!GameManager.get().getSavedClans().contains(clan)) {
						result.setFalse();
						return;
					}
					result.setValue(clan.hasPermission(cPlayer, ClanPermission.TOURNAMENT));
				}, () -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan = cPlayer.getClan().get();
		
		if(!GameManager.get().unregisterClan(clan))
			return false;

		clan.broadcastMessage("§7§o"+sender.getName()+" §bwypisal klan z turnieju klanowego!");
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+sender.getName()+" has unsigned clan from tournament");
		clan.getLogger().logMessage(sender.getName()+" wypisal klan na turnieju klanowego!");
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan wypisz §7- Wypisz swoj klan z turnieju klanowego");
	}

}
