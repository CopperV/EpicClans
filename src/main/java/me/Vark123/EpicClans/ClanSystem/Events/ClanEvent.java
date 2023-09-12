package me.Vark123.EpicClans.ClanSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicClans.ClanSystem.Clan;

@Getter
public abstract class ClanEvent extends Event implements Cancellable {

	@Setter
	private boolean cancelled;
	
	private Clan clan;
	@Setter
	protected String cancelMessage;

	public ClanEvent(Clan clan) {
		this.clan = clan;
	}
	
}
