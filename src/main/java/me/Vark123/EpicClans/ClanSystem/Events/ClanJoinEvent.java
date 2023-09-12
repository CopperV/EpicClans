package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
public class ClanJoinEvent extends ClanEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private ClanPlayer newMember;

	public ClanJoinEvent(Clan clan, ClanPlayer newMember) {
		super(clan);
		this.newMember = newMember;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
