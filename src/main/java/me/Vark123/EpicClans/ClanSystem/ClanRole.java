package me.Vark123.EpicClans.ClanSystem;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClanRole {

	@Setter(value = AccessLevel.NONE)
	private String id;
	private String display;
	private int priority;
	@Setter(value = AccessLevel.NONE)
	private boolean editable;
	private List<ClanPermission> permissions;
	
}
