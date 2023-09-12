package me.Vark123.EpicClans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public final class FileManager {

	private static File clanDir = new File(Main.getInst().getDataFolder(), "clans");
	
	private FileManager() { }
	
	public static void init() {
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config.yml", false);
		Config.get().init();
		
		if(!clanDir.exists())
			clanDir.mkdir();
		loadClans();
	}
	
	private static void loadClans() {
		Arrays.asList(clanDir.listFiles()).parallelStream()
			.filter(file -> file.getName().endsWith(".yml"))
			.map(YamlConfiguration::loadConfiguration)
			.forEach(fYml -> {
				String id = fYml.getString("id");
				String display = fYml.getString("display");
				String color = fYml.getString("color");
				
				ConfigurationSection roleSection = fYml.getConfigurationSection("roles");
				Collection<ClanRole> roles = roleSection != null ? 
						roleSection.getKeys(false).stream()
							.map(key -> {
								String roleId = roleSection.getString(key+".id");
								String roleDisplay = roleSection.getString(key+".display");
								int rolePriority = roleSection.getInt(key+".priority");
								List<ClanPermission> permissions = roleSection.getStringList(key+".perm").stream()
										.map(line -> line.toUpperCase())
										.filter(line -> EnumUtils.isValidEnum(ClanPermission.class, line))
										.map(ClanPermission::valueOf)
										.collect(Collectors.toList());
								return new ClanRole(roleId, roleDisplay, rolePriority, true, permissions);
							})
							.collect(Collectors.toList()) : new ArrayList<>();
				roles.addAll(ClanManager.get().getBaseRoles());
				
				Map<ClanPlayer, ClanRole> members = new ConcurrentHashMap<>();
				ConfigurationSection memberSection = fYml.getConfigurationSection("members");
				if(memberSection != null)
					memberSection.getKeys(false).forEach(key -> {
						UUID uid = UUID.fromString(memberSection.getString(key+".uid"));
						ClanPlayer cPlayer = new ClanPlayer(uid);
						
						String roleId = memberSection.getString(key+".role");
						roles.stream()
							.filter(role -> role.getId().equals(roleId))
							.findAny()
							.ifPresentOrElse(role -> {
								members.put(cPlayer, role);
							}, () -> {
								ClanRole role = roles.stream().filter(_role -> _role.getId().equals("member")).findAny().get();
								members.put(cPlayer, role);
							});
					});
				
				Clan clan = Clan.builder()
						.id(id)
						.name(display)
						.color(color)
						.roles(roles)
						.members(members)
						.build();
				ClanManager.get().registerClan(clan);
				
				members.keySet().forEach(cPlayer -> {
					cPlayer.setClan(clan);
					PlayerManager.get().registerPlayer(cPlayer);
				});
			});
	}
	
	public static void saveClan(Clan clan) {
		File file = new File(clanDir, clan.getId()+".yml");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		
		fYml.set("id", clan.getId());
		fYml.set("display", clan.getName());
		fYml.set("color", clan.getColor());
		
		fYml.set("roles", null);
		fYml.set("members", null);
		
		clan.getRoles().stream()
			.filter(role -> !ClanManager.get().getBaseRoles().contains(role))
			.forEach(role -> {
				String roleId = role.getId();
				fYml.set("roles."+roleId+".id", roleId);
				fYml.set("roles."+roleId+".display", role.getDisplay());
				fYml.set("roles."+roleId+".priority", role.getPriority());
				fYml.set("roles."+roleId+".perm", role.getPermissions().stream()
						.map(perm -> perm.name())
						.collect(Collectors.toList()));
			});
		clan.getMembers().entrySet().stream().forEach(entry -> {
			ClanPlayer cPlayer = entry.getKey();
			ClanRole role = entry.getValue();
			String uid = cPlayer.getUid().toString();
			
			fYml.set("members."+uid+".uid", uid);
			fYml.set("members."+uid+".role", role.getId());
		});
		
		try {
			fYml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveClans() {
		ClanManager.get().getClans().forEach(FileManager::saveClan);
	}
	
}
