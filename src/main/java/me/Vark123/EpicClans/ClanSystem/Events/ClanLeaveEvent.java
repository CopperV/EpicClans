package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
public class ClanLeaveEvent extends ClanEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private ClanPlayer oldMember;

	public ClanLeaveEvent(Clan clan, ClanPlayer oldMember) {
		super(clan);
		this.oldMember = oldMember;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
