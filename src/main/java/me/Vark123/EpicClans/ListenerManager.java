package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGeneratorListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanChatSendListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerQuitListener;
import me.Vark123.EpicRPGSkillsAndQuests.Main;
import me.nikl.calendarevents.CalendarEventsApi;

public final class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerClanChatSendListener(), inst);

		Bukkit.getPluginManager().registerEvents(new ResourceGeneratorListener(), inst);
		
		CalendarEventsApi calendar = Main.getInst().getCalendar();
		if(calendar.isRegisteredEvent("clan_resource_generator"))
			calendar.removeEvent("clan_resource_generator");
		calendar.addEvent("clan_resource_generator", "every day", "00:05");
		if(calendar.isRegisteredEvent("clan_event_prepare"))
			calendar.removeEvent("clan_event_prepare");
		calendar.addEvent("clan_event_prepare", "every day", "17:30");
		if(calendar.isRegisteredEvent("clan_event"))
			calendar.removeEvent("clan_event");
		calendar.addEvent("clan_event", "every day", "18:00");
	}
	
}
