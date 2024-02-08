package me.Vark123.EpicClans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicClans.ClanSystem.ClanPermission;
import me.Vark123.EpicClans.ClanSystem.ClanRole;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementManager;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementReward;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.AchievementType;
import me.Vark123.EpicClans.ClanSystem.AchievementSystem.ClanAchievement;
import me.Vark123.EpicClans.ClanSystem.LogSystem.ClanLogger;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.ClanUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradeRequirements;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesManager;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.AttackBoostUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.ClanRotationUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.ClanSizeUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.ClanWarehouseUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.DefenseBoostUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.ExpBoostUpgrade;
import me.Vark123.EpicClans.ClanSystem.UpgradeSystem.UpgradesImpl.ResourceGeneratorUpgrade;
import me.Vark123.EpicClans.ClanSystem.WarehouseSystem.WarehouseHolder;
import me.Vark123.EpicClans.PlayerSystem.ClanPlayer;
import me.Vark123.EpicClans.PlayerSystem.PlayerManager;

public final class FileManager {

	@Getter
	private static File clanDir = new File(Main.getInst().getDataFolder(), "clans");
	
	private FileManager() { }
	
	public static void init() {
		if(!Main.getInst().getDataFolder().exists())
			Main.getInst().getDataFolder().mkdir();
		
		Main.getInst().saveResource("config.yml", false);
		Main.getInst().saveResource("clan-upgrades.yml", false);
		Main.getInst().saveResource("mobarena.yml", false);
		Main.getInst().saveResource("bossfight.yml", false);
		Main.getInst().saveResource("osiagniecia.yml", false);
		Config.get().init();
		
		if(!clanDir.exists())
			clanDir.mkdir();
		loadAchievements();
		loadClanUpgrades();
		loadClans();
	}
	
	private static void loadAchievements() {
		File file = new File(Main.getInst().getDataFolder(), "osiagniecia.yml");
		if(!file.exists())
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection achievementsSection = fYml.getConfigurationSection("osiagniecia");
		achievementsSection.getKeys(false).stream()
			.forEach(key -> {
				ConfigurationSection achievementSection = achievementsSection.getConfigurationSection(key);
				
				String id = achievementSection.getString("id");
				AchievementType type = AchievementType.valueOf(achievementSection.getString("type").toUpperCase());
				String target = ChatColor.translateAlternateColorCodes('&', achievementSection.getString("target"));
				String display = ChatColor.translateAlternateColorCodes('&', achievementSection.getString("name"));
				List<String> lore = achievementSection.getStringList("lore").stream()
						.map(line -> ChatColor.translateAlternateColorCodes('&', line))
						.collect(Collectors.toList());
				String difficulty = achievementSection.getString("difficulty");
				
				ConfigurationSection rewardSection = achievementSection.getConfigurationSection("reward");
				double money = 0;
				int stygia = 0, coins = 0, brylki = 0, pr = 0;
				if(rewardSection != null) {
					money = rewardSection.getDouble("money");
					stygia = rewardSection.getInt("stygia");
					coins = rewardSection.getInt("coins");
					brylki = rewardSection.getInt("brylki");
					pr = rewardSection.getInt("pr");
				}
				
				ClanAchievement achievement = ClanAchievement.builder()
						.id(id)
						.type(type)
						.target(target)
						.display(display)
						.lore(lore)
						.difficulty(difficulty)
						.reward(AchievementReward.builder()
								.money(money)
								.stygia(stygia)
								.coins(coins)
								.brylki(brylki)
								.pr(pr)
								.build())
						.build();
				AchievementManager.get().registerAchievement(achievement);
			});
		AchievementManager.get().getAchievements().sort((achievement1, achievement2) -> achievement2.getId().compareTo(achievement1.getId()));
	}
	
	private static void loadClanUpgrades() {
		File file = new File(Main.getInst().getDataFolder(), "clan-upgrades.yml");
		if(!file.exists())
			return;
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		fYml.getKeys(false).stream().forEach(key -> {
			Collection<UpgradeRequirements> requirements = new ArrayList<>();
			
			ConfigurationSection upgradeSection = fYml.getConfigurationSection(key);
			upgradeSection.getKeys(false).stream().forEach(strLevel -> {
				ConfigurationSection levelSection = upgradeSection.getConfigurationSection(strLevel);
				
				int level = levelSection.getInt("level", 0);
				double balance = levelSection.getDouble("balance", 0);
				int stygia = levelSection.getInt("stygia", 0);
				int coins = levelSection.getInt("coins", 0);
				int brylki = levelSection.getInt("brylki", 0);
				int pr = levelSection.getInt("pr", 0);
				
				UpgradeRequirements req = UpgradeRequirements.builder()
						.level(level)
						.money(balance)
						.stygia(stygia)
						.coins(coins)
						.ruda(brylki)
						.pr(pr)
						.build();
				requirements.add(req);
			});
			
			switch(key.toLowerCase()) {
				case "exp":
					UpgradesManager.get().getClanUpgrades().add(new ExpBoostUpgrade(requirements));
					break;
				case "attack":
					UpgradesManager.get().getClanUpgrades().add(new AttackBoostUpgrade(requirements));
					break;
				case "defense":
					UpgradesManager.get().getClanUpgrades().add(new DefenseBoostUpgrade(requirements));
					break;
				case "players":
					UpgradesManager.get().getClanUpgrades().add(new ClanSizeUpgrade(requirements));
					break;
				case "generator":
					UpgradesManager.get().getClanUpgrades().add(new ResourceGeneratorUpgrade(requirements));
					break;
				case "rotations":
					UpgradesManager.get().getClanUpgrades().add(new ClanRotationUpgrade(requirements));
					break;
				case "warehouse":
					UpgradesManager.get().getClanUpgrades().add(new ClanWarehouseUpgrade(requirements));
					break;
			}
		});
	}
	
	private static void loadClans() {
		Arrays.asList(clanDir.listFiles()).parallelStream()
			.filter(file -> file.isDirectory())
			.forEach(dir -> {
				File info = new File(dir, "info.yml");
				YamlConfiguration fYml = YamlConfiguration.loadConfiguration(info);
				String id = fYml.getString("id");
				String display = fYml.getString("display");
				String color = fYml.getString("color");
				
				ConfigurationSection roleSection = fYml.getConfigurationSection("roles");
				Collection<ClanRole> roles = roleSection != null ? 
						roleSection.getKeys(false).stream()
							.map(key -> {
								String roleId = roleSection.getString(key+".id");
								String roleDisplay = roleSection.getString(key+".display");
								int rolePriority = roleSection.getInt(key+".priority");
								List<ClanPermission> permissions = roleSection.getStringList(key+".perm").stream()
										.map(line -> line.toUpperCase())
										.filter(line -> EnumUtils.isValidEnum(ClanPermission.class, line))
										.map(ClanPermission::valueOf)
										.collect(Collectors.toList());
								return new ClanRole(roleId, roleDisplay, rolePriority, true, permissions);
							})
							.collect(Collectors.toList()) : new ArrayList<>();
				roles.addAll(ClanManager.get().getBaseRoles());
				
				Map<ClanPlayer, ClanRole> members = new ConcurrentHashMap<>();
				ConfigurationSection memberSection = fYml.getConfigurationSection("members");
				if(memberSection != null)
					memberSection.getKeys(false).forEach(key -> {
						UUID uid = UUID.fromString(memberSection.getString(key+".uid"));
						ClanPlayer cPlayer = new ClanPlayer(uid);
						
						String roleId = memberSection.getString(key+".role");
						roles.stream()
							.filter(role -> role.getId().equals(roleId))
							.findAny()
							.ifPresentOrElse(role -> {
								members.put(cPlayer, role);
							}, () -> {
								ClanRole role = roles.stream().filter(_role -> _role.getId().equals("member")).findAny().get();
								members.put(cPlayer, role);
							});
					});
				
				double money = fYml.getDouble("treasury.money", 0);
				int stygia = fYml.getInt("treasury.stygia", 0);
				int coins = fYml.getInt("treasury.coins", 0);
				int ruda = fYml.getInt("treasury.ruda", 0);
				int pr = fYml.getInt("treasury.pr", 0);
				ClanTreasury treasury = ClanTreasury.builder()
						.money(money)
						.stygia(stygia)
						.coins(coins)
						.ruda(ruda)
						.pr(pr)
						.build();
				
				List<String> achievements = fYml.getStringList("achievements");
				
				Collection<ClanUpgrade> upgrades = new ArrayList<>();
				ConfigurationSection upgradeSection = fYml.getConfigurationSection("upgrades");
				if(upgradeSection != null) {
					UpgradesManager.get().getClanUpgrades().stream()
						.map(upgrade -> upgrade.getId())
						.forEach(upgradeId -> {
							int level = upgradeSection.getInt(upgradeId, 0);
							ClanUpgrade upgrade = ClanUpgrade.builder()
									.id(upgradeId)
									.level(level)
									.build();
							upgrades.add(upgrade);
						});
				} else {
					UpgradesManager.get().getClanUpgrades().stream()
						.map(upgrade -> upgrade.getId())
						.forEach(upgradeId -> {
							ClanUpgrade upgrade = ClanUpgrade.builder()
									.id(upgradeId)
									.level(0)
									.build();
							upgrades.add(upgrade);
						});
				}

				Map<Integer,Inventory> warehouses;
				File warehouseDir = new File(dir, "warehouse");
				if(!warehouseDir.exists()) {
					warehouseDir.mkdir();
					warehouses = ClanManager.get().generateEmptyWarehouses();
				} else {
					warehouses = new LinkedHashMap<>();
					Arrays.asList(warehouseDir.listFiles()).stream()
						.filter(file -> file.getName().endsWith(".yml"))
						.map(YamlConfiguration::loadConfiguration)
						.forEach(warehouseYml -> {
							int warehouseId = warehouseYml.getInt("id");
							Inventory inv = Bukkit.createInventory(new WarehouseHolder(warehouseId), 27, "§eMagazyn nr "+warehouseId);
							ConfigurationSection invSection = warehouseYml.getConfigurationSection("inventory");
							if(invSection != null)
								invSection.getKeys(false).stream()
									.filter(StringUtils::isNumeric)
									.map(Integer::parseInt)
									.forEach(i -> {
										ItemStack it = invSection.getItemStack(""+i);
										inv.setItem(i, it);
									});
							warehouses.put(warehouseId, inv);
						});
				}
				
				Clan clan = Clan.builder()
						.id(id)
						.name(display)
						.color(color)
						.roles(roles)
						.members(members)
						.treasury(treasury)
						.upgrades(upgrades)
						.completedAchievements(achievements)
						.logger(new ClanLogger(dir))
						.warehouses(warehouses)
						.build();
				ClanManager.get().registerClan(clan);
				
				members.keySet().forEach(cPlayer -> {
					cPlayer.setClan(clan);
					PlayerManager.get().registerPlayer(cPlayer);
				});
			});
	}
	
	public static void saveClan(Clan clan) {
		File dir = new File(clanDir, clan.getId());
		if(!dir.exists())
			dir.mkdir();
		
		File file = new File(dir, "info.yml");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(file);
		
		fYml.set("id", clan.getId());
		fYml.set("display", clan.getName());
		fYml.set("color", clan.getColor());
		
		ClanTreasury treasury = clan.getTreasury();
		fYml.set("treasury.money", treasury.getMoney());
		fYml.set("treasury.stygia", treasury.getStygia());
		fYml.set("treasury.coins", treasury.getCoins());
		fYml.set("treasury.ruda", treasury.getRuda());
		fYml.set("treasury.pr", treasury.getPr());
		
		fYml.set("roles", null);
		fYml.set("members", null);
		fYml.set("upgrades", null);
		
		clan.getRoles().stream()
			.filter(role -> !ClanManager.get().getBaseRoles().contains(role))
			.forEach(role -> {
				String roleId = role.getId();
				fYml.set("roles."+roleId+".id", roleId);
				fYml.set("roles."+roleId+".display", role.getDisplay());
				fYml.set("roles."+roleId+".priority", role.getPriority());
				fYml.set("roles."+roleId+".perm", role.getPermissions().stream()
						.map(perm -> perm.name())
						.collect(Collectors.toList()));
			});
		clan.getMembers().entrySet().stream().forEach(entry -> {
			ClanPlayer cPlayer = entry.getKey();
			ClanRole role = entry.getValue();
			String uid = cPlayer.getUid().toString();
			
			fYml.set("members."+uid+".uid", uid);
			fYml.set("members."+uid+".role", role.getId());
		});
		
		clan.getUpgrades().forEach(upgrade -> fYml.set("upgrades."+upgrade.getId(), upgrade.getLevel()));
		
		fYml.set("achievements", clan.getCompletedAchievements());
		
		try {
			fYml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File warehouseDir = new File(dir, "warehouse");
		if(!warehouseDir.exists())
			warehouseDir.mkdir();
		clan.getWarehouses().entrySet().stream()
			.forEach(entry -> {
				int num = entry.getKey();
				Inventory inv = entry.getValue();
				
				File warehouse = new File(warehouseDir, num+".yml");
				if(!warehouse.exists())
					try {
						warehouse.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				YamlConfiguration warehouseYml = YamlConfiguration.loadConfiguration(warehouse);
				warehouseYml.set("id", num);
				warehouseYml.set("inventory", null);
				for(int i = 0; i < inv.getSize(); ++i) {
					ItemStack it = inv.getItem(i);
					warehouseYml.set("inventory."+i, it);
				}
				try {
					warehouseYml.save(warehouse);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	}
	
	public static void saveClans() {
		ClanManager.get().getClans().forEach(FileManager::saveClan);
	}
	
	public static void removeClan(Clan clan) {
		File dir = new File(clanDir, clan.getId());
		if(!dir.exists() || dir.isFile())
			return;
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
