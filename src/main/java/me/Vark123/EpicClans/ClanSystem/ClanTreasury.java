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
	public void addStygia(double stygia) {this.stygia += stygia;}
	public void addCoins(double coins) {this.coins += coins;}
	public void addRuda(double ruda) {this.ruda += ruda;}
	public void addPr(double pr) {this.pr += pr;}
	
	public void removeMoney(double money) {this.money -= money;}
	public void removeStygia(double stygia) {this.stygia -= stygia;}
	public void removeCoins(double coins) {this.coins -= coins;}
	public void removeRuda(double ruda) {this.ruda -= ruda;}
	public void removePr(double pr) {this.pr -= pr;}
	
	public boolean hasEnoughMoney(double money) {return this.money >= money;}
	public boolean hasEnoughStygia(double stygia) {return this.stygia >= stygia;}
	public boolean hasEnoughCoins(double coins) {return this.coins >= coins;}
	public boolean hasEnoughRuda(double ruda) {return this.ruda >= ruda;}
	public boolean hasEnoughPr(double pr) {return this.pr >= pr;}
	
}
