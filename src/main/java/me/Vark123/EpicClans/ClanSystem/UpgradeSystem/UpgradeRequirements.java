package me.Vark123.EpicClans.ClanSystem.UpgradeSystem;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.Clan;
import me.Vark123.EpicClans.ClanSystem.ClanTreasury;

@Getter
@Builder
@EqualsAndHashCode
public class UpgradeRequirements {

	private int level;
	
	private double money;
	private int stygia;
	private int coins;
	private int ruda;
	private int pr;
	
	public boolean checkClanRequirements(Clan clan) {
		ClanTreasury treasury = clan.getTreasury();
		return treasury.hasEnoughMoney(money) && treasury.hasEnoughStygia(stygia)
				&& treasury.hasEnoughCoins(coins) && treasury.hasEnoughRuda(ruda)
				&& treasury.hasEnoughPr(pr);
	}
	
	public void spendClanResources(Clan clan) {
		ClanTreasury treasury = clan.getTreasury();
		treasury.removeMoney(money);
		treasury.removeStygia(stygia);
		treasury.removeCoins(coins);
		treasury.removeRuda(ruda);
		treasury.removePr(pr);
	}
	
}
