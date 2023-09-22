package me.Vark123.EpicClans.ClanSystem.UpgradeSystem;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
@Builder
public class ClanUpgrade {

	private String id;
	@Setter
	private int level;
	
}
