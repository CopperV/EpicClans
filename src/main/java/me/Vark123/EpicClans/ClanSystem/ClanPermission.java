package me.Vark123.EpicClans.ClanSystem;

import lombok.Getter;

@Getter
public enum ClanPermission {
	
	INVITE("Zapraszanie"),					//ZAPRASZANIE GRACZY
	KICK("Wyrzucanie"),						//WYRZUCANIE GRACZY
	LOG("Podglad dziennika"),				//DOSTEP DO PODGLADU LOGOW ZDARZEN
	TOURNAMENT("Zapis na turnieje"),		//ZAPISYWANIE KLANU NA TURNIEJE
	UPGRADE("Dostep do ulepszen"),			//ULEPSZANIE KLANU
	BACKPACK("Magazyn klanowy"),			//PLECAK KLANOWY
	PARTY("Tworzenie party"),				//DOSTEP DO KOMENDY /klan party
	SHOP("Sklep klanowy"),					//DOSTEP DO SKLEPU KLANOWEGO
	PROMOTE("Promowanie czlonkow"),			//ZARZADZANIE RANGAMI UCZESTNIKOW Z WYZSZYM PRIORYTEM
	LEADER("Lider");						//MOZLIWOSC USUWANIA KLANU I ZMIANY LIDERA ORAZ TWORZENIA ROL

	private String display;

	private ClanPermission(String display) {
		this.display = display;
	}
	
}
