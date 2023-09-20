package me.Vark123.EpicClans.ClanSystem.EventSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.nikl.calendarevents.CalendarEvent;

public class GameRecordingStartListener implements Listener {

	@EventHandler
	public void onDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("clan_event_prepare"))
			return;
		GameManager.get().startRecording();
	}
	
}
