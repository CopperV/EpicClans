package me.Vark123.EpicClans;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;

@Getter
public class Config {

	private static final Config conf = new Config();
	
	private String prefix;
	
	private int maxRoles;
	private int clanIdMinLength;
	private int clanIdMaxLength;
	private int clanNameMinLength;
	private int clanNameMaxLength;
	private String clanDefaultColor;
	private long clanInvitationDuration;
	private char clanChatPrefix;
	
	private Config() { }
	
	public static final Config get() {
		return conf;
	}
	
	public void init() {
		File config = new File(Main.getInst().getDataFolder(), "config.yml");
		if(!config.exists())
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(config);
		
		this.prefix = ChatColor.translateAlternateColorCodes('&', fYml.getString("prefix"));
		
		this.maxRoles = fYml.getInt("clan.max-roles");
		this.clanIdMinLength = fYml.getInt("clan.min-id-len");
		this.clanIdMaxLength = fYml.getInt("clan.max-id-len");
		this.clanNameMinLength = fYml.getInt("clan.min-name-len");
		this.clanNameMaxLength = fYml.getInt("clan.max-name-len");
		this.clanDefaultColor = ChatColor.translateAlternateColorCodes('&', fYml.getString("clan.default-color"));

		this.clanInvitationDuration = fYml.getLong("clan.invitation-time");
		this.clanChatPrefix = fYml.getString("clan.chat-prefix","!").charAt(0);
	}

}
