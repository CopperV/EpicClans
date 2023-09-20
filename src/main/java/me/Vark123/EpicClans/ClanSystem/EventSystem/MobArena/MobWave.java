package me.Vark123.EpicClans.ClanSystem.EventSystem.MobArena;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MobWave {

	private int wave;
	private double level;
	private int amount;
	private List<String> mobs;
	
}
