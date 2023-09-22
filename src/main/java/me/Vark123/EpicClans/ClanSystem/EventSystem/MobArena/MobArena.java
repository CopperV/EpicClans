package me.Vark123.EpicClans.ClanSystem.EventSystem.MobArena;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicClans.Tools.EpicLocation;

@Getter
@AllArgsConstructor
@Builder
public class MobArena {

	private int id;
	private EpicLocation respLoc;
	private List<EpicLocation> mobRespLocs;
	
}
