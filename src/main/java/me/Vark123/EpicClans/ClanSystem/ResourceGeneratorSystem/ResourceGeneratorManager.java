package me.Vark123.EpicClans.ClanSystem.ResourceGeneratorSystem;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public final class ResourceGeneratorManager {

	private static final ResourceGeneratorManager inst = new ResourceGeneratorManager();
	
	private final Map<Integer, ResourceGenerator> generators;
	
	private ResourceGeneratorManager() {
		generators = new LinkedHashMap<>();
		generators.put(0, ResourceGenerator.builder()
				.money(2_500)
				.brylki(25)
				.build());
		generators.put(1, ResourceGenerator.builder()
				.money(5_000)
				.stygia(25)
				.coins(100)
				.brylki(75)
				.build());
		generators.put(2, ResourceGenerator.builder()
				.money(12_500)
				.stygia(100)
				.coins(600)
				.brylki(200)
				.build());
		generators.put(3, ResourceGenerator.builder()
				.money(25_000)
				.stygia(450)
				.coins(4_000)
				.brylki(750)
				.build());
		generators.put(4, ResourceGenerator.builder()
				.money(37_500)
				.stygia(1_250)
				.coins(7_500)
				.brylki(1_600)
				.build());
		generators.put(5, ResourceGenerator.builder()
				.money(50_000)
				.stygia(3_000)
				.coins(17_500)
				.brylki(3_500)
				.pr(1)
				.build());
		generators.put(6, ResourceGenerator.builder()
				.money(65_000)
				.stygia(5_000)
				.coins(32_500)
				.brylki(5_000)
				.pr(1)
				.build());
		generators.put(7, ResourceGenerator.builder()
				.money(82_500)
				.stygia(7_500)
				.coins(50_000)
				.brylki(7_000)
				.pr(1)
				.build());
		generators.put(8, ResourceGenerator.builder()
				.money(100_000)
				.stygia(12_500)
				.coins(67_500)
				.brylki(9_750)
				.pr(1)
				.build());
		generators.put(9, ResourceGenerator.builder()
				.money(125_000)
				.stygia(20_000)
				.coins(80_000)
				.brylki(12_500)
				.pr(1)
				.build());
		generators.put(10, ResourceGenerator.builder()
				.money(175_000)
				.stygia(30_000)
				.coins(100_000)
				.brylki(17_500)
				.pr(2)
				.build());
	}
	
	public static final ResourceGeneratorManager get() {
		return inst;
	}
	
}
