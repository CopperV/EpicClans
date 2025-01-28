package me.Vark123.EpicClans;

import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.ClanManager;
import me.Vark123.EpicInventory.Pagination.InventoryManager;

@Getter
public final class EpicClansApi {

	private static final EpicClansApi api = new EpicClansApi();
	
	private final ClanManager clanManager;
	private final InventoryManager invManager;
	
	private EpicClansApi() {
		clanManager = ClanManager.get();
		invManager = Main.getInst().getInvManager();
	}
	
	public static final EpicClansApi get() {
		return api;
	}
	
}
