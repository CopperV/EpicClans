package me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicClans.Config;
import me.Vark123.EpicClans.Main;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.nikl.calendarevents.CalendarEvent;

public class ResourceGeneratorListener implements Listener {

	@EventHandler
	public void onDate(CalendarEvent e) {
		if(e.isCancelled())
			return;
		if(!e.getLabels().contains("clan_resource_generator"))
			return;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				ClanManager.get().getClans().parallelStream()
					.forEach(clan -> {
						clan.getUpgrades().stream()
							.filter(upgrade -> upgrade.getId().equals("generator"))
							.findAny()
							.ifPresent(clanUpgrade -> {
								UpgradesManager.get().getClanUpgradeById("generator")
									.ifPresent(upgrade -> {
										ClanTreasury treasury = clan.getTreasury();
										ResourceGenerator generator = (ResourceGenerator) upgrade.getUpgradeBonus(clanUpgrade.getLevel());
										
										if(generator.getMoney() > 0) {
											treasury.addMoney(generator.getMoney());
											clan.broadcastMessage("§bGenerator surowcow dodal §e§o"+String.format("%.2f", generator.getMoney())+"$ §bdo skarbca klanu");
											Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "
													+ "Resource generator has added "+String.format("%.2f", generator.getMoney())+"$ to clan "+clan.getId()+" treasury");
											clan.getLogger().logMessage("Generator surowcow dodal "+String.format("%.2f", generator.getMoney())+"$ do skarbca klanu");
										}
										if(generator.getStygia() > 0) {
											treasury.addStygia(generator.getStygia());
											clan.broadcastMessage("§bGenerator surowcow dodal §3§o"+generator.getStygia()+" stygii §bdo skarbca klanu");
											Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "
													+ "Resource generator has added "+generator.getStygia()+" stygia to clan "+clan.getId()+" treasury");
											clan.getLogger().logMessage("Generator surowcow dodal "+generator.getStygia()+" stygii do skarbca klanu");
										}
										if(generator.getCoins() > 0) {
											treasury.addCoins(generator.getCoins());
											clan.broadcastMessage("§bGenerator surowcow dodal §4§o"+generator.getCoins()+" smoczych monet §bdo skarbca klanu");
											Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "
													+ "Resource generator has added "+generator.getCoins()+" dragon coins to clan "+clan.getId()+" treasury");
											clan.getLogger().logMessage("Generator surowcow dodal "+generator.getCoins()+" smoczych monet do skarbca klanu");
										}
										if(generator.getBrylki() > 0) {
											treasury.addRuda(generator.getBrylki());
											clan.broadcastMessage("§bGenerator surowcow dodal §9§o"+generator.getBrylki()+" brylek rudy §bdo skarbca klanu");
											Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "
													+ "Resource generator has added "+generator.getBrylki()+" magic ores to clan "+clan.getId()+" treasury");
											clan.getLogger().logMessage("Generator surowcow dodal "+generator.getBrylki()+" brylek rudy do skarbca klanu");
										}
										if(generator.getPr() > 0) {
											treasury.addPr(generator.getPr());
											clan.broadcastMessage("§bGenerator surowcow dodal §6§o"+generator.getPr()+" punktow rozwoju §bdo skarbca klanu");
											Bukkit.getLogger().log(Level.INFO, "["+ChatColor.stripColor(Config.get().getPrefix())+"] "
													+ "Resource generator has added "+generator.getPr()+" dev poinst to clan "+clan.getId()+" treasury");
											clan.getLogger().logMessage("Generator surowcow dodal "+generator.getPr()+" punktow rozwoju do skarbca klanu");
										}
									});
							});
					});
			}
		}.runTaskAsynchronously(Main.getInst());
		Bukkit.broadcastMessage("§7["+Config.get().getPrefix()+"§7]"
				+ " §bGenerator surowcow dodal surowce do skarbcow klanow");
	}

}
