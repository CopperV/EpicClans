package me.Vark123.EpicClans;

import org.bukkit.Bukkit;

import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.ClanKickListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.ClanLeaveListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.ClanRemoveListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.DungeonBossKillListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.DungeonPassListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.PartyCreateListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.PartyJoinListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.PartyRemoveListener;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.PartyController.Listeners.PlayerDisconnectListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.GameManager;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.BossFightGame;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossDeathListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossDespawnListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.BossFight.Listeners.BossFightListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.Listeners.GameRecordingStartListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.Listeners.GameStartListener;
import me.Vark123.EpicClans.ClanSystem.EventSystem.MobArena.MobArenaGame;
import me.Vark123.EpicClans.ClanSystem.Listeners.ClanLeaveWarehouseProtectionListener;
import me.Vark123.EpicClans.ClanSystem.Listeners.ClanSizeProtectionListener;
import me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem.ResourceGeneratorListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanChatSendListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanExpShareListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerClanFightListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerQuitListener;
import me.Vark123.EpicClans.PlayerSystem.Listeners.PlayerRespTaskMoveListener;
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

		Bukkit.getPluginManager().registerEvents(new ClanKickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ClanLeaveListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ClanRemoveListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PartyCreateListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PartyJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PartyRemoveListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerDisconnectListener(), inst);

		Bukkit.getPluginManager().registerEvents(new DungeonBossKillListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DungeonPassListener(), inst);

		Bukkit.getPluginManager().registerEvents(new ClanSizeProtectionListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ClanLeaveWarehouseProtectionListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new BossFightListener(), inst);

		Bukkit.getPluginManager().registerEvents(new PlayerClanExpShareListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerClanFightListener(), inst);
		
		GameManager.get().registerGame(new MobArenaGame());
		GameManager.get().registerGame(new BossFightGame());
		
		CalendarEventsApi calendar = Main.getInst().getCalendar();
		if(calendar.isRegisteredEvent("clan_resource_generator"))
			calendar.removeEvent("clan_resource_generator");
		calendar.addEvent("clan_resource_generator", "monday", "00:05");
		if(calendar.isRegisteredEvent("clan_event_prepare"))
			calendar.removeEvent("clan_event_prepare");
		calendar.addEvent("clan_event_prepare", "every day", "17:30");
		if(calendar.isRegisteredEvent("clan_event"))
			calendar.removeEvent("clan_event");
		calendar.addEvent("clan_event", "every day", "18:00");
	}
	
}
