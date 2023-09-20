package me.Vark123.EpicClans.ClanSystem.EventSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.MobArena.MobArenaGame;
import net.md_5.bungee.api.ChatColor;

@Getter
public final class GameManager {

	private static final GameManager manager = new GameManager();
	
	@Setter
	private GameState state;
	@Setter
	private IGame game;
	
	private final List<IGame> games;
	private final List<Clan> savedClans;
	
	private final Map<Player, BukkitTask> respTasks;
	
	private GameManager() {
		state = GameState.OFF;
		games = new ArrayList<>();
		savedClans = new LinkedList<>();
		
		this.respTasks = new ConcurrentHashMap<>();
	}
	
	public static final GameManager get() {
		return manager;
	}
	
	public void registerGame(IGame game) {
		game.init();
		this.games.add(game);
	}
	
	public boolean registerClan(Clan clan) {
		if(savedClans.size() >= game.getMaxClansAmount())
			return false;
		if(savedClans.contains(clan))
			return false;
		savedClans.add(clan);
		return true;
	}
	
	public boolean unregisterClan(Clan clan) {
		if(!savedClans.contains(clan))
			return false;
		savedClans.remove(clan);
		return true;
	}
	
	public void startRecording() {
		if(!state.equals(GameState.OFF))
			return;
		if(games.size() <= 0)
			return;
		
		Random rand = new Random();
		IGame game = games.get(rand.nextInt(games.size()));
		
		if(game instanceof MobArenaGame) {
			Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§b§lZapraszamy klany do zapisywania sie na walke na §6§lPOTWORNEJ ARENIE!");
		}
		if(game instanceof BossFightGame) {
			Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§b§lZapraszamy klany do zapisywania sie na walke z §6§lPOTEZNYM PRZECIWNIKIEM!");
		}
		
		this.game = game;
		state = GameState.RECORDING;
	}
	
	public void startGame() {
		if(!state.equals(GameState.RECORDING))
			return;
		if(game == null)
			return;
		state = GameState.RUN;
		
		List<Clan> activeClans = GameManager.get().getSavedClans().stream()
				.filter(clan -> clan.getMembers().keySet()
							.stream()
							.map(cPlayer -> cPlayer.toBukkitPlayer())
							.filter(p -> p.isOnline())
							.count() > 0)
				.collect(Collectors.toList());
		
		int size = activeClans.size();
		if(size == 0) {
			stopGame();
			Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§b§lTurniej klanowy zostal odwolany gdyz nie ma zapisanych aktywnych klanow!");
			return;
		}
		if(size == 1) {
			stopGame();
			Clan clan = activeClans.get(0);
			ClanTreasury treasury = clan.getTreasury();
			treasury.addPr(1);
			
			clan.broadcastMessage("§bDo skarbca zostal dodany §6§o1 punkt rozwoju §bza zwyciestwo w turnieju klanowym!");
			Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] Clan "+clan.getId()+" has won tournament");
			clan.getLogger().logMessage("Do skarbca zostal dodany 1 punkt rozwoju za zwyciestwo w turnieju klanowym!");
			
			Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
					+ "§b§lTurniej klanowy wygrywa §r"+clan.getColor()+clan.getId()+"§b§l, poniewaz byl jedynym zapisanym aktywnym klanem!");
			return;
		}
		

		Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§b§lRozpoczal sie turniej klanowy! W turnieju wezma udzial nastepujace klany:");
		activeClans.forEach(clan -> Bukkit.broadcastMessage("  §4§l» §r"+clan.getColor()+clan.getId()));
		
		game.startGame(activeClans);
	}
	
	public void stopGame() {
		if(!state.equals(GameState.RUN))
			return;
		if(game == null)
			return;
		game.stopGame();
		savedClans.clear();
		game = null;
		state = GameState.OFF;
	}
	
	public void startRespTask(Player p) {
		if(respTasks.containsKey(p) && !respTasks.get(p).isCancelled())
			respTasks.get(p).cancel();
		
		p.sendMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§bPoczekaj §715 §bsekund na teleportacj!");
		p.sendMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§bPoczekaj §715 §bsekund na teleportacj!");
		p.sendMessage("§7["+Config.get().getPrefix()+"§7] "
				+ "§bPoczekaj §715 §bsekund na teleportacj!");
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!state.equals(GameState.RUN))
					return;
				if(game == null || !game.canResp(p))
					return;
				
				BossFightGame bossFight = (BossFightGame) game;
				Location resp = bossFight.getPlayerResp().toBukkitLocation();
				
				p.teleport(resp);
				p.playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, .8f);
				p.spawnParticle(Particle.REVERSE_PORTAL, p.getLocation().clone().add(0, 1.25, 0),
						30, 0.8, 0.8, 0.8, 0.1);
				
				p.sendMessage("§7["+Config.get().getPrefix()+"§7] "
						+ "§bPrzeteleportowales sie na arene klanowa!");
				
				respTasks.remove(p);
			}
		}.runTaskLater(Main.getInst(), 20*15);
		respTasks.put(p, task);
	}
}
