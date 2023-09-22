package me.Vark123.EpicClans.ClanSystem.Commands;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicClans.Config;

public class BaseClanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epicclan"))
			return false;
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		if(args.length == 0) {
			showCorrectUsage(p);
			return false;
		}
		
		MutableBoolean returnValue = new MutableBoolean(true);
		ClanCommandManager.get().getClanSubcommand(args[0])
			.ifPresentOrElse(subcmd -> {
				if(!subcmd.canUse(p)) {
					showCorrectUsage(p);
					returnValue.setFalse();
					return;
				}
				if(args.length > 1) {
					String[] newArgs = new String[args.length - 1];
					for(int i = 0; i < newArgs.length; ++i)
						newArgs[i] = args[i+1];
					boolean res = subcmd.useCommand(p, newArgs);
					if(!res)
						subcmd.showCorrectUsage(p);
					returnValue.setValue(res);
				} else {
					boolean res = subcmd.useCommand(p);
					if(!res)
						subcmd.showCorrectUsage(p);
					returnValue.setValue(res);
				}
			}, () -> {
				showCorrectUsage(p);
				returnValue.setFalse();
			});
		return returnValue.booleanValue();
	}

	private void showCorrectUsage(Player sender) {
		sender.sendMessage("§7["+Config.get().getPrefix()+"§7] §3Poprawne uzycie komendy §7§o/klan");
		ClanCommandManager.get().getClanSubcommands().keySet().stream()
			.filter(key -> {
				AClanCommand cmd = ClanCommandManager.get().getClanSubcommand(key).get();
				return cmd.getCmd().equals(key)
						&& cmd.canUse(sender);
			}).forEach(key -> {
				ClanCommandManager.get().getClanSubcommand(key).get().showCorrectUsage(sender);
			});
	}

}
