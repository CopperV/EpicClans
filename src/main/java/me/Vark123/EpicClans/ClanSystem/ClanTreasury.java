package me.Vark123.EpicClans.ClanSystem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ClanTreasury {

	private double money;
	private int stygia;
	private int coins;
	private int ruda;
	private int pr;
	
	public void addMoney(double money) {this.money += money;}
	public void addStygia(int stygia) {this.stygia += stygia;}
	public void addCoins(int coins) {this.coins += coins;}
	public void addRuda(int ruda) {this.ruda += ruda;}
	public void addPr(int pr) {this.pr += pr;}
	
	public void removeMoney(double money) {this.money -= money;}
	public void removeStygia(int stygia) {this.stygia -= stygia;}
	public void removeCoins(int coins) {this.coins -= coins;}
	public void removeRuda(int ruda) {this.ruda -= ruda;}
	public void removePr(int pr) {this.pr -= pr;}
	
	public boolean hasEnoughMoney(double money) {return this.money >= money;}
	public boolean hasEnoughStygia(int stygia) {return this.stygia >= stygia;}
	public boolean hasEnoughCoins(int coins) {return this.coins >= coins;}
	public boolean hasEnoughRuda(int ruda) {return this.ruda >= ruda;}
	public boolean hasEnoughPr(int pr) {return this.pr >= pr;}
	
}
