package me.Vark123.EpicClans.ClanSystem;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.ClanSystem.Events.ClanChatEvent;
import me.Vark123.EpicClans.ClanSystem.LogSystem.ClanLogger;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.ClanUpgrade;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import net.md_5.bungee.api.ChatColor;

@Getter
@AllArgsConstructor
@Builder
public class Clan {

	private String id;
	private String name;
	@Setter
	private String color;
	private ClanTreasury treasury;
	private ClanLogger logger;
	
	private Collection<String> completedAchievements;
	private Collection<ClanUpgrade> upgrades;
	
	private Collection<ClanRole> roles;
	private Map<ClanPlayer, ClanRole> members;
	
	public boolean hasPermission(ClanPlayer cPlayer, ClanPermission perm) {
		ClanRole role = members.get(cPlayer);
		if(role == null)
			return false;
		return role.getPermissions().contains(perm);
	}
	
	public void sendClanMessage(ClanPlayer sender, String msg) {
		Player player = sender.toBukkitPlayer().getPlayer();
		
		ClanChatEvent event = new ClanChatEvent(this, sender, msg);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			if(event.getCancelMessage() != null && !event.getCancelMessage().isEmpty())
				player.sendMessage(event.getCancelMessage());
			return;
		}

		ClanRole role = members.get(sender);
		if(role == null)
			return;
		members.keySet().stream()
			.map(cPlayer -> cPlayer.toBukkitPlayer())
			.filter(p -> p.isOnline())
			.map(p -> p.getPlayer())
			.forEach(p -> {
				p.sendMessage("§8[§b§lCC§8] §r§8["+role.getDisplay()+"§r§8] §e"+player.getName()
					+"§7: §7§o"+event.getMsg());
			});
		
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] [ClanChat - "+id+"] "+player.getName()+": "+msg);
	}
	
	public void broadcastMessage(String msg) {
		members.keySet().stream()
			.map(cPlayer -> cPlayer.toBukkitPlayer())
			.filter(p -> p.isOnline())
			.map(p -> p.getPlayer())
			.forEach(p -> p.sendMessage("§8[§d"+Config.get().getPrefix()+"§8] §b"+msg));
		Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] [ClanChat - "+id+"] ClanBroadcast: "+msg);
	}
	
}
