package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;

@Getter
public class ClanChatEvent extends ClanEvent {

	private static final HandlerList handlers = new HandlerList();

	private ClanPlayer sender;
	@Setter
	private String msg;

	public ClanChatEvent(Clan clan, ClanPlayer sender, String msg) {
		super(clan);
		this.sender = sender;
		this.msg = msg;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
