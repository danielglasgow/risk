package risk;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ComputerFortifier {
	private final Player player;
	private final Continent continent;
	private final Territory fortifyFrom;
	private final List<Territory> fortifyToOptions;

	public ComputerFortifier(Player player, Continent continent) {
		this.player = player;
		this.continent = continent;
		this.fortifyFrom = makeFortifyFrom();
		this.fortifyToOptions = makefortifyToOptions(fortifyFrom);

	}

	private Territory makeFortifyFrom() {
		Territory bestFortify = new Territory("placeHolder", 0, 0);
		for (Territory territory : player.getTerritories()) {
			if (territory.armies > bestFortify.armies) {
				bestFortify = territory;
			}
		}
		return bestFortify;

	}

	private List<Territory> makefortifyToOptions(Territory territory) {
		Set<Territory> fortifyToOptions = new HashSet<Territory>();
		List<Territory> newOptions = Lists.newArrayList();
		newOptions.addAll(territory.adjacents);
		while (!newOptions.isEmpty()) {
			System.out.println("fortify options; " + fortifyToOptions);
			System.out.println(newOptions);
			fortifyToOptions.addAll(newOptions);
			newOptions.clear();
			for (Territory fortifyOption : fortifyToOptions) {
				for (Territory adjacent : fortifyOption.adjacents) {
					if (adjacent != territory
							&& !fortifyToOptions.contains(adjacent)
							&& adjacent.player == player) {
						newOptions.add(adjacent);
					}
				}
			}
		}
		List<Territory> options = Lists.newArrayList();
		options.addAll(fortifyToOptions);
		return options;

	}

	public List<Territory> getFortification() {
		Map<Territory, Integer> fortifyOptions = Maps.newHashMap();
		for (Territory territory : fortifyToOptions) {
			fortifyOptions.put(territory, enemyBorders(territory));
		}
		int fortifyScore = 0;
		Territory fortifyTo = null;
		for (Territory territory : fortifyOptions.keySet()) {
			if (fortifyOptions.get(territory) > fortifyScore) {
				fortifyScore = fortifyOptions.get(territory);
				fortifyTo = territory;
			}
		}
		List<Territory> fortification = Lists.newArrayList();
		fortification.add(fortifyFrom);
		if (fortifyTo != null) {
			fortification.add(fortifyTo);
		} else {
			fortification.add(fortifyFrom);
		}
		return fortification;
	}

	private int enemyBorders(Territory territory) {
		int enemies = 0;
		for (Territory adjacent : territory.adjacents) {
			if (adjacent.player != player) {
				enemies++;
			}
		}
		return enemies;
	}

	private void biggestCluster() {
		/*
		 * TerritoryCluster biggestCluster = continent.getClusters().get(0); for
		 * (TerritoryCluster cluster : continent.getClusters()) { if
		 * (cluster.getTerritories().size() >
		 * biggestCluster.getTerritories().size()) { biggestCluster = cluster; }
		 * }
		 */
	}
}
