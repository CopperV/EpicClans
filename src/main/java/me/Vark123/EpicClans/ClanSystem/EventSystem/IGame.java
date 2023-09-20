package me.Vark123.EpicClans.ClanSystem.EventSystem;

import java.util.List;

import org.bukkit.entity.Player;

import me.Vark123.EpicClans.ClanSystem.Clan;

public interface IGame {

	public void init();
	public void startGame(List<Clan> activeClans);
	public void stopGame();
	public int getMaxClansAmount();
	public boolean canResp(Player p);
	
}
