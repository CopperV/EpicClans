package me.Vark123.EpicClans.PlayerSystem;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.Events.ClanInviteEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;

@Getter
public class ClanPlayer {

	private UUID uid;
	@Setter
	private Clan clan;
	
	private Map<BukkitTask, Clan> clanInvitations = new ConcurrentHashMap<>();

	public ClanPlayer(UUID uid) {
		this(uid, null);
	}

	public ClanPlayer(UUID uid, Clan clan) {
		super();
		this.uid = uid;
		this.clan = clan;
	}
	
	public OfflinePlayer toBukkitPlayer() {
		return Bukkit.getOfflinePlayer(uid);
	}
	
	public Optional<Clan> getClan() {
		return Optional.ofNullable(clan);
	}
	
	public void sendInvitation(ClanPlayer sender, Clan clan) {
		Player p = sender.toBukkitPlayer().getPlayer();
		Player p2 = toBukkitPlayer().getPlayer();
		
		ClanInviteEvent event = new ClanInviteEvent(clan, sender, this);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozna zaprosic §7§o"+p2.getName()+" §bdo klanu");
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				p.sendMessage("§dPowod: §7§o"+event.getCancelMessage());
			return;
		}

		MutableObject<BukkitTask> task = new MutableObject<>();
		task.setValue(new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				clanInvitations.remove(task.getValue());
			}
		}.runTaskLaterAsynchronously(Main.getInst(), 20*Config.get().getClanInvitationDuration()));
		clanInvitations.put(task.getValue(), clan);
		
		TextComponent comp = new TextComponent("§7["+Config.get().getPrefix()+"§7] §bOtrzymales zaproszenie do klanu §r"+clan.getColor()+clan.getId()+"§r§b od §7§o"+p.getName()+"§r§b."
				+ " Dolacz uzywajac komendy ");
		TextComponent click = new TextComponent("§7§o/klan dolacz "+clan.getId());
		click.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/epicclan join "+clan.getId()));
		comp.addExtra(click);
		p2.spigot().sendMessage(ChatMessageType.CHAT, comp);
		p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bZaprosiles §7§o"+p2.getName()+" §bdo klanu!");
		
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" send clan invitation ["+clan.getId()+"] to "+p2.getName());
		clan.getLogger().logMessage(p.getName()+" zaprosil "+p2.getName()+" do klanu");
	}
	
	public void cancelInvitation(BukkitTask task) {
		if(!clanInvitations.containsKey(task))
			return;
		task.cancel();
		clanInvitations.remove(task);
	}
	
}
