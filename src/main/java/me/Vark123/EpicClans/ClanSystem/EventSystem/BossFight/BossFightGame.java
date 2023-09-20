package me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.Tools.EpicLocation;
import net.md_5.bungee.api.ChatColor;

@Getter
public class BossFightGame implements IGame {

	private int maxClans;
	private String world;
	
	private EpicLocation bossResp;
	private EpicLocation playerResp;

	private List<String> bosses;
	
	private Map<Clan, Double> damageCounter;
	
	private List<Clan> clans;
	private BukkitTask controller;
	private ActiveMob boss;
	
	@Override
	public void init() {
		damageCounter = new ConcurrentHashMap<>();
		
		File file = new File(Main.getInst().getDataFolder(), "bossfight.yml");
		if(!file.exists())
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		
		this.maxClans = fYml.getInt("max_clans");
		this.world = fYml.getString("world");
		
		ConfigurationSection arenaSection = fYml.getConfigurationSection("arena");
		
		double x1 = arenaSection.getDouble("mob_spawn.x");
		double y1 = arenaSection.getDouble("mob_spawn.y");
		double z1 = arenaSection.getDouble("mob_spawn.z");
		bossResp = new EpicLocation(world, x1, y1, z1);
		
		double x2 = arenaSection.getDouble("resp.x");
		double y2 = arenaSection.getDouble("resp.y");
		double z2 = arenaSection.getDouble("resp.z");
		playerResp = new EpicLocation(world, x2, y2, z2);
		
		bosses = arenaSection.getStringList("bosses");
	}

	@Override
	public void startGame(List<Clan> activeClans) {
		clans = activeClans;
		
		int level = activeClans.size();
		Random rand = new Random();
		String bossId = bosses.get(rand.nextInt(bosses.size()));
		
		activeClans.forEach(clan -> damageCounter.put(clan, 0.));
		
		Location resp = playerResp.toBukkitLocation();
		activeClans.forEach(clan -> {
			clan.getMembers().keySet().stream()
				.map(cPlayer -> cPlayer.toBukkitPlayer())
				.filter(p -> p.isOnline())
				.map(p -> p.getPlayer())
				.forEach(p -> p.teleport(resp));
		});
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				startGameController();
				boss = MythicBukkit.inst().getMobManager().spawnMob(bossId, BukkitAdapter.adapt(bossResp.toBukkitLocation()), level);
			}
		}.runTaskLater(Main.getInst(), 20*30);
	}
	
	private void startGameController() {
		controller = new BukkitRunnable() {
			@Override
			public void run() {
				MutableInt count = new MutableInt();
				clans.stream()
					.forEach(clan -> {
						int counter = (int) clan.getMembers().keySet().stream()
								.map(cPlayer -> cPlayer.toBukkitPlayer())
								.filter(p -> p.isOnline())
								.map(p -> p.getPlayer())
								.filter(p -> p.getWorld().getName().equals(world))
								.count();
						count.add(counter);
					});
				if(count.getValue() <= 0) {
					Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] No clan has won tournament");
				
					Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
							+ "§b§lCoz za niesamowite emocje!");
					Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
							+ "§b§lPrzeciwnik okazal sie na tyle potezny, ze zaden klan go nie przezyl!");
					stopGame();
					this.cancel();
					return;
				}
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 20*15);
	}

	@Override
	public void stopGame() {
		if(controller != null && !controller.isCancelled())
			controller.cancel();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killall all "+world);
	}

	@Override
	public int getMaxClansAmount() {
		return maxClans;
	}

	@Override
	public boolean canResp(Player p) {
		return true;
	}

}
