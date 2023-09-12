package me.Vark123.EpicClans.ClanSystem.Commands.Impl;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.Commands.AClanCommand;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public class ClanCreateCommand extends AClanCommand {

	public ClanCreateCommand() {
		super("create", new String[]{"stworz"});
	}

	@Override
	public boolean canUse(Player sender) {
		MutableBoolean result = new MutableBoolean(true);
		PlayerManager.get().getByUID(sender.getUniqueId())
			.ifPresentOrElse(cPlayer -> {
				cPlayer.getClan().ifPresent(clan -> result.setFalse());
			}, () -> result.setFalse());
		return result.booleanValue();
	}

	@Override
	public boolean useCommand(Player sender, String... args) {
		if(args == null || args.length < 2) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bMusisz podac id klanu oraz jego nazwe");
			return false;
		}
		
		String id = args[0];
		int length = id.length();
		if(length < Config.get().getClanIdMinLength()) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§bId §o"+id+" §r§bjest za krotkie."
					+ " Powinno ono sie skladac z conajmniej "+Config.get().getClanIdMinLength()+" znakow");
			return false;
		}
		if(length > Config.get().getClanIdMaxLength()) {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§bId §o"+id+" §r§bjest za dlugiej."
					+ " Powinno ono sie skladac z nie wiecej niz "+Config.get().getClanIdMaxLength()+" znakow");
			return false;
		}

		MutableBoolean result = new MutableBoolean(true);
		ClanManager.get().getById(id).ifPresentOrElse(clan -> {
			sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz stworzyc klan o id §o"+id+"§r§b. Juz istnieje taki klan!");
			result.setFalse();
		}, () -> {
			StringBuilder builder = new StringBuilder();
			for(int i = 1; i < args.length; ++i)
				builder.append(args[i]+" ");
			builder.deleteCharAt(builder.length()-1);
			
			String name = builder.toString();
			int nameLen = name.length();
			if(nameLen < Config.get().getClanNameMinLength()) {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] "
						+ "§bNazwa klanu §o"+name+" §r§bjest za krotka."
						+ " Powinno ona sie skladac z conajmniej "+Config.get().getClanNameMinLength()+" znakow");
				result.setFalse();
				return;
			}
			if(nameLen > Config.get().getClanNameMaxLength()) {
				sender.sendMessage("§7["+Config.get().getPrefix()+"§7] "
						+ "§bNazwa klanu §o"+name+" §r§bjest za dluga."
						+ " Powinna ona sie skladac z nie wiecej niz "+Config.get().getClanNameMaxLength()+" znakow");
				result.setFalse();
				return;
			}
			
			ClanPlayer cPlayer = PlayerManager.get().getByUID(sender.getUniqueId()).get();
			ClanManager.get().createClan(cPlayer, id, name);
		});

		return result.booleanValue();
	}

	@Override
	public void showCorrectUsage(Player sender) {
		sender.sendMessage("  §b/klan stworz <id> <nazwa> §7- Stworz swoj wlasny klan! Stworzenie klanu wymaga §e§o75 000 $ §7oraz §9§o10 000 Brylek Rudy");
	}

}
