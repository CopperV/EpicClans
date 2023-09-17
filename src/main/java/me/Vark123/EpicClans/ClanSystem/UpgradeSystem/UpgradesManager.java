package me.Vark123.EpicClans.ClanSystem.UpgradeSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import lombok.Getter;

@Getter
public final class UpgradesManager {

	private static final UpgradesManager inst = new UpgradesManager();
	
	private Collection<AClanUpgrade> clanUpgrades;
	
	private UpgradesManager() {
		clanUpgrades = new HashSet<>();
	}
	
	public static final UpgradesManager get() {
		return inst;
	}
	
	public Optional<AClanUpgrade> getClanUpgradeById(String id) {
		return clanUpgrades.stream()
				.filter(upgrade -> upgrade.getId().equals(id))
				.findAny();
	}
	
}
