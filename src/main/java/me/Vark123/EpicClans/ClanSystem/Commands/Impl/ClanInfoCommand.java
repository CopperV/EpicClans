package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanInfoCommand extends AClanCommand {

	public ClanInfoCommand() {
		super("info", new String[]{ });
	}

	@Override
	public boolean canUse(Player sender) {
		return ClanManager.get().getClans().size() > 0;
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
		Clan clan;
		if(args == null || args.length < 1) {
			Optional<Clan> oClan = cPlayer.getClan();
			if(oClan.isEmpty())
				return false;
			clan = oClan.get();
		} else {
			Optional<Clan> oClan = ClanManager.get().getById(args[0]);
			if(oClan.isEmpty()) {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bKlan §7"+args[0]+" §bnie istnieje!");
				return false;
			}
			clan = oClan.get();
		}
		
		String color = clan.getColor().replace("§", "").replace("x", "").toUpperCase();
		Map<ClanRole, Collection<OfflinePlayer>> players = new ConcurrentHashMap<>();
		clan.getMembers().entrySet().stream().forEach(entry -> {
			Collection<OfflinePlayer> rolePlayers = players.getOrDefault(entry.getValue(), new LinkedList<>());
			rolePlayers.add(entry.getKey().toBukkitPlayer());
			players.put(entry.getValue(), rolePlayers);
		});
		
		sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bInformacje o klanie §r"+clan.getColor()+clan.getId()+"§b:");
		sender.sendMessage("§4§l» §7ID: §b"+clan.getId());
		sender.sendMessage("§4§l» §7Nazwa: §b"+clan.getName());
		
		sender.sendMessage("§4§l» §7Kolor: §b§o#"+color);
		sender.sendMessage(" ");
		sender.sendMessage("§b§nCzlonkowie:");
		players.entrySet().stream()
			.sorted((entry1, entry2) -> Integer.compare(entry1.getKey().getPriority(), entry2.getKey().getPriority()))
			.forEachOrdered(entry -> {
				ClanRole role = entry.getKey();
				StringBuilder builder = new StringBuilder("  §4§l» §r"+role.getDisplay()+"§7: ");
				entry.getValue().forEach(op -> builder.append(op.getName()+", "));
				builder.deleteCharAt(builder.length() - 1);
				builder.deleteCharAt(builder.length() - 1);
				sender.sendMessage(builder.toString());
			});
		return true;
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan info [klan] §7- Wypisz informacje o swoim klanie badz innym");
	}

}
