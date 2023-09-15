package me.Vark123.EpicClans.ClanSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.FileManager;
import me.Vark123.EpicClans.ClanSystem.Events.ClanCreateEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanJoinEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanKickEvent;
import me.Vark123.EpicClans.ClanSystem.Events.ClanLeaveEvent;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;
import net.md_5.bungee.api.ChatColor;

@Getter
public final class ClanManager {

	private static final ClanManager inst = new ClanManager();
	
	private final Collection<Clan> clans;
	
	private final Collection<ClanRole> baseRoles;
	
	private ClanManager() {
		clans = new HashSet<>();
		
		baseRoles = new HashSet<>();
		baseRoles.add(new ClanRole("leader",
				"§e♚ Lider",
				0,
				false,
				Arrays.asList(ClanPermission.values())));
		baseRoles.add(new ClanRole("member",
				"§7Czlonek",
				Integer.MAX_VALUE,
				false,
				new ArrayList<>()));
	}
	
	public static final ClanManager get() {
		return inst;
	}
	
	public void registerClan(Clan clan) {
		this.clans.add(clan);
	}
	
	public void unregisterClan(Clan clan) {
		this.clans.remove(clan);
	}
	
	public Optional<Clan> getById(String id) {
		return clans.stream()
				.filter(clan -> clan.getId().equals(id))
				.findAny();
	}
	
	public Clan createClan(ClanPlayer cPlayer, String id, String name) {
		Map<ClanPlayer, ClanRole> members = new ConcurrentHashMap<>();
		members.put(
				cPlayer,
				baseRoles.stream().filter(_role -> _role.getId().equals("leader")).findAny().get());
		Clan clan = Clan.builder()
				.id(id)
				.name(name)
				.color(Config.get().getClanDefaultColor())
				.treasury(ClanTreasury.builder().build())
				.roles(baseRoles)
				.members(members)
				.build();
		
		Player p = cPlayer.toBukkitPlayer().getPlayer();
		
		ClanEvent event = new ClanCreateEvent(clan, cPlayer);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§bNie mozna utworzyc klanu §o"+id);
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				p.sendMessage("§bPowod: §7§o"+event.getCancelMessage());
			return null;
		}

		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+cPlayer.toBukkitPlayer().getName()+" created Clan with ID ["+id+"] and NAME ["+name+"]");
		
		cPlayer.setClan(clan);
		registerClan(clan);
		FileManager.saveClan(clan);
		return clan;
	}
	
	public boolean joinToClan(Clan clan, ClanPlayer newMember) {
		Player p = newMember.toBukkitPlayer().getPlayer();
		
		ClanEvent event = new ClanJoinEvent(clan, newMember);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			clan.broadcastMessage("§7"+p.getName()+" §bnie moze dolaczyc do klanu");
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz dolaczyc do klanu §r"+clan.getColor()+clan.getId());
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty()) {
				clan.broadcastMessage("§bPowod: §r"+event.getCancelMessage());
				p.sendMessage("§bPowod: §r"+event.getCancelMessage());
			}
			return false;
		}

		clan.broadcastMessage("§7"+p.getName()+" §bdolaczyl do klanu!");
		p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bDolaczyles do klanu §r"+clan.getColor()+clan.getId());
		
		ClanRole role = ClanManager.get().getBaseRoles().stream()
				.filter(_role -> _role.getId().equals("member"))
				.findAny()
				.get();
		
		clan.getMembers().put(newMember, role);
		newMember.setClan(clan);

		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has joined to clan "+clan.getId());
		return true;
	}
	
	public boolean kickFromClan(Clan clan, ClanPlayer kicker, ClanPlayer kicked) {
		ClanKickEvent event = new ClanKickEvent(clan, kicker, kicked);
		Bukkit.getPluginManager().callEvent(event);
		
		Player p = kicker.toBukkitPlayer().getPlayer();
		if(event.isCancelled()) {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz wyrzucic z klanu §7"+kicked.toBukkitPlayer().getName());
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty()) {
				p.sendMessage("§bPowod: §r"+event.getCancelMessage());
			}
			return false;
		}
		
		clan.getMembers().remove(kicked);
		kicked.setClan(null);
		OfflinePlayer kickedPlayer = kicked.toBukkitPlayer();
		if(kickedPlayer.isOnline()) {
			kickedPlayer.getPlayer().sendMessage("§7["+Config.get().getPrefix()+"§7] §bZostales wyrzucony z klanu §r"
					+clan.getColor()+clan.getId()+" §r§bprzez §7§o"+p.getName());
		} else {
			PlayerManager.get().unregisterPlayer(kicked);
		}
		
		clan.broadcastMessage("§7§o"+p.getName()+" §bwyrzucil §7§o"+kickedPlayer.getName()+" §bz klanu");

		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+kickedPlayer.getName()+" has been kicked from clan "+clan.getId()+" by "+p.getName());
		return true;
	}
	
	public boolean leaveClan(Clan clan, ClanPlayer oldMember) {
		ClanEvent event = new ClanLeaveEvent(clan, oldMember);
		Bukkit.getPluginManager().callEvent(event);
		
		Player p = oldMember.toBukkitPlayer().getPlayer();
		if(event.isCancelled()) {
			p.sendMessage("§7["+Config.get().getPrefix()+"§7] §bNie mozesz opuscic klanu §r"+clan.getColor()+clan.getId());
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty()) {
				p.sendMessage("§bPowod: §r"+event.getCancelMessage());
			}
			return false;
		}
		
		oldMember.setClan(null);
		clan.getMembers().remove(oldMember);
		p.getPlayer().sendMessage("§7["+Config.get().getPrefix()+"§7] §bOpusciles klan §r"+clan.getColor()+clan.getId());
		clan.broadcastMessage("§7§o"+p.getName()+" §bopuscil klan");

		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "+p.getName()+" has lef clan "+clan.getId());
		return true;
	}
	
}
