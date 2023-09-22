package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
public class ClanInviteEvent extends ClanEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private ClanPlayer sender;
	private ClanPlayer receiver;

	public ClanInviteEvent(Clan clan, ClanPlayer sender, ClanPlayer receiver) {
		super(clan);
		this.sender = sender;
		this.receiver = receiver;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
