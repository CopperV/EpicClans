package me.Vark123.EpicClans.ClanSystem.EventSystem.MobArena;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.IGame;
import me.Vark123.EpicClans.Tools.EpicLocation;
import net.md_5.bungee.api.ChatColor;

@Getter
public class MobArenaGame implements IGame {

	private int maxClans;
	private String world;
	
	private List<MobArena> arenas;
	private List<MobWave> waves;
	
	private Map<Clan, MobArena> usedArenas;
	
	@Override
	public void init() {
		usedArenas = new ConcurrentHashMap<>();
		
		File file = new File(Main.getInst().getDataFolder(), "mobarena.yml");
		if(!file.exists())
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		
		this.maxClans = fYml.getInt("max_clans");
		this.world = fYml.getString("world");
		
		ConfigurationSection arenasSection = fYml.getConfigurationSection("arenas");
		arenas = arenasSection.getKeys(false).stream()
				.map(key -> {
					ConfigurationSection arenaSection = arenasSection.getConfigurationSection(key+".resp");
					int id = arenaSection.getInt("id");
					double x = arenaSection.getDouble("x");
					double y = arenaSection.getDouble("y");
					double z = arenaSection.getDouble("z");
					EpicLocation mainResp = new EpicLocation(world, x, y, z);
					
					ConfigurationSection respsSection = arenaSection.getConfigurationSection("mob_resps");
					List<EpicLocation> mobResps = respsSection.getKeys(false).stream()
							.map(key2 -> {
								ConfigurationSection respSection = respsSection.getConfigurationSection(key2);
								double x2 = respSection.getDouble("x");
								double y2 = respSection.getDouble("y");
								double z2 = respSection.getDouble("z");
								
								return new EpicLocation(world, x2, y2, z2);
							}).collect(Collectors.toList());

					
					return MobArena.builder()
							.id(id)
							.respLoc(mainResp)
							.mobRespLocs(mobResps)
							.build();
				}).collect(Collectors.toList());

		ConfigurationSection wavesSection = fYml.getConfigurationSection("waves");
		waves = wavesSection.getKeys(false).stream()
				.map(key -> {
					ConfigurationSection waveSection = wavesSection.getConfigurationSection(key);
					int wave = waveSection.getInt("wave");
					double level = waveSection.getDouble("level");
					int amount = waveSection.getInt("amount");
					List<String> mobs = waveSection.getStringList("mobs");
					
					return MobWave.builder()
							.wave(wave)
							.level(level)
							.amount(amount)
							.mobs(mobs)
							.build();
				}).collect(Collectors.toList());
	}

	@Override
	public void startGame(List<Clan> activeClans) {
		Random rand = new Random();
		List<MobArena> randomArenas = new LinkedList<>();
		do {
			MobArena arena = arenas.get(rand.nextInt(arenas.size()));
			if(randomArenas.contains(arena))
				continue;
			randomArenas.add(arena);
		} while(randomArenas.size() < activeClans.size());
		
		for(int i = 0; i < activeClans.size(); ++i) {
			Clan clan = activeClans.get(i);
			MobArena arena = randomArenas.get(i);
			usedArenas.put(clan, arena);
		}
		
		usedArenas.forEach((clan, arena) -> {
			Location resp = arena.getRespLoc().toBukkitLocation();
			clan.getMembers().keySet().stream()
				.map(cPlayer -> cPlayer.toBukkitPlayer())
				.filter(p -> p.isOnline())
				.forEach(p -> p.getPlayer().teleport(resp));
		});
		
		new BukkitRunnable() {
			@Override
			public void run() {
				startGameController();
			}
		}.runTaskLaterAsynchronously(Main.getInst(), 20*30);
	}

	@Override
	public void stopGame() {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm m kill Clan");
	}

	@Override
	public int getMaxClansAmount() {
		return maxClans;
	}
	
	private void startGameController() {
		new BukkitRunnable() {
			MutableInt waveNumber = new MutableInt(1);
			@Override
			public void run() {
				eliminateClans();
				
				if(usedArenas.size() <= 0) {
					Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] No clan has won tournament");
					
					Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
							+ "§b§lCoz za niesamowite emocje!");
					Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
							+ "§b§lPotwory na arenie okazaly sie byc tak duzym wyzwaniem, ze zaden klan nie zdolal przetrwac ich naporu!");
					GameManager.get().stopGame();
					cancel();
					return;
				}
				if(usedArenas.size() == 1) {
					usedArenas.keySet().forEach(clan -> {
						Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
								+ "§b§lCoz za niesamowite emocje!");
						Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
								+ "§b§lTurniej na §6§lPOTWORNEJ ARENIE §b§lwygrywa klan §r"+clan.getColor()+clan.getId()+"§b§l!");
						
						ClanTreasury treasury = clan.getTreasury();
						treasury.addPr(1);
						
						clan.broadcastMessage("§bDo skarbca zostal dodany §6§o1 punkt rozwoju §bza zwyciestwo w turnieju klanowym!");
						Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] Clan "+clan.getId()+" has won tournament");
						clan.getLogger().logMessage("Do skarbca zostal dodany 1 punkt rozwoju za zwyciestwo w turnieju klanowym!");
					});
					GameManager.get().stopGame();
					cancel();
					return;
				}
				
				waves.stream()
					.filter(wave -> wave.getWave() == waveNumber.getValue())
					.findAny()
					.ifPresentOrElse(wave -> {
						double level = wave.getLevel();
						
						Random rand = new Random();
						List<String> mobs = new LinkedList<>();
						for(int i = 0; i < wave.getAmount(); ++i) {
							mobs.add(wave.getMobs().get(rand.nextInt(wave.getMobs().size())));
						}
						
						usedArenas.entrySet().stream().forEach(entry -> {
							Clan clan = entry.getKey();
							MobArena arena = entry.getValue();
							
							mobs.forEach(mmId -> {
								Location loc = arena.getMobRespLocs().get(rand.nextInt(arena.getMobRespLocs().size()))
										.toBukkitLocation();
								MythicBukkit.inst().getMobManager().spawnMob(mmId, BukkitAdapter.adapt(loc), level);
							});
							
							clan.broadcastMessage("§b§lROZPOCZELA SIE §e§l"+waveNumber.getValue()+" §b§lFALA!");
						});
						
						waveNumber.increment();
					}, () -> {
						Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
								+ "§b§lCoz za niesamowite emocje!");
						Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
								+ "§b§lTurniej na §6§lPOTWORNEJ ARENIE §b§lwygrywa klany:");
						usedArenas.keySet().forEach(clan -> Bukkit.broadcastMessage("  §4§l» §r"+clan.getColor()+clan.getId()));
						
						usedArenas.keySet().forEach(clan -> {
							ClanTreasury treasury = clan.getTreasury();
							treasury.addPr(1);
							
							clan.broadcastMessage("§bDo skarbca zostal dodany §6§o1 punkt rozwoju §bza zwyciestwo w turnieju klanowym!");
							Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] Clan "+clan.getId()+" has won tournament");
							clan.getLogger().logMessage("Do skarbca zostal dodany 1 punkt rozwoju za zwyciestwo w turnieju klanowym!");
						});
						
						GameManager.get().stopGame();
						cancel();
						return;
					});
			}
		}.runTaskTimer(Main.getInst(), 0, 20*30);
	}
	
	private void eliminateClans() {
		usedArenas.keySet().stream()
			.filter(clan -> clan.getMembers().keySet().stream()
					.map(cPlayer -> cPlayer.toBukkitPlayer())
					.filter(p -> p.isOnline())
					.map(p -> p.getPlayer())
					.filter(p -> p.getWorld().getName().equals(world))
					.count() <= 0)
			.collect(Collectors.toList())
			.stream()
			.forEach(clan -> {
				clan.broadcastMessage("§bKlan odpadl z turnieju!");
				usedArenas.remove(clan);
			});
	}

	@Override
	public boolean canResp(Player p) {
		return false;
	}

}
