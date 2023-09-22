package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
public class ClanKickEvent extends ClanEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private ClanPlayer kicker;
	private ClanPlayer kicked;

	public ClanKickEvent(Clan clan, ClanPlayer kicker, ClanPlayer kicked) {
		super(clan);
		this.kicker = kicker;
		this.kicked = kicked;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
