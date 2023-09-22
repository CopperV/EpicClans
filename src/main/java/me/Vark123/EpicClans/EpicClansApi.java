package me.Vark123.EpicClans;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicClans.ClanSystem.ClanManager;

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
