package me.Vark123.EpicClans;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;

@Getter
public class Config {

	private static final Config conf = new Config();
	
	private String prefix;
	
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
	}

}
