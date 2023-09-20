package me.Vark123.EpicClans;

import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;

import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossDeathListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossDespawnListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossFightModifier;
import me.Vark123.EpicClans.ClanSystem.EventSystem.Listeners.GameRecordingStartListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.Listeners.GameStartListener;
import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGeneratorListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanChatSendListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerQuitListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerRespTaskMoveListener;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifierManager;
import me.nikl.calendarevents.CalendarEventsApi;

public final class ListenerManager {

	private ListenerManager() { }
	
	public static void registerListeners() {
		Main inst = Main.getInst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerClanChatSendListener(), inst);

		Bukkit.getPluginManager().registerEvents(new ResourceGeneratorListener(), inst);

		Bukkit.getPluginManager().registerEvents(new GameRecordingStartListener(), inst);
		Bukkit.getPluginManager().registerEvents(new GameStartListener(), inst);
		Bukkit.getPluginManager().registerEvents(new BossDeathListener(), inst);
		Bukkit.getPluginManager().registerEvents(new BossDespawnListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerRespTaskMoveListener(), inst);
		
//		GameManager.get().registerGame(new MobArenaGame());
		GameManager.get().registerGame(new BossFightGame());

		DamageModifierManager.getInstance().registerModifier(new BossFightModifier(), EventPriority.MONITOR);
		
		CalendarEventsApi calendar = Main.getInst().getCalendar();
		if(calendar.isRegisteredEvent("clan_resource_generator"))
			calendar.removeEvent("clan_resource_generator");
		calendar.addEvent("clan_resource_generator", "monday", "00:05");
		if(calendar.isRegisteredEvent("clan_event_prepare"))
			calendar.removeEvent("clan_event_prepare");
		calendar.addEvent("clan_event_prepare", "every day", "18:07");
		if(calendar.isRegisteredEvent("clan_event"))
			calendar.removeEvent("clan_event");
		calendar.addEvent("clan_event", "every day", "18:10");
	}
	
}
