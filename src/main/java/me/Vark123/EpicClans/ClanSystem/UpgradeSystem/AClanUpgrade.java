package me.Vark123.EpicClans.ClanSystem.UpgradeSystem;

import java.util.Collection;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class AClanUpgrade <T> {

	protected String id;
	protected Collection<UpgradeRequirements> levelRequirements;
	
	public abstract ItemStack toItem();
	public abstract T getUpgradeBonus(int level);
	public Optional<UpgradeRequirements> getLevelRequirement(int level) {
		return levelRequirements.stream()
				.filter(req -> req.getLevel() == level)
				.findAny();
	}
	
}
