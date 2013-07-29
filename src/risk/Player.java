package risk;

import java.util.ArrayList;

public class Player {

	public String color;
	public String name;
	private final MainGame game;
	public int armiesToPlace = 0;
	public Territory territoryAttackTo;
	public Territory territoryAttackFrom;
	public Territory fortify1;
	public Territory fortify2;
	
	public Player(String name, String color, MainGame game) {
		this.game = game;
		this.name = name;
		this.color = color;
	}

	public ArrayList<Territory> getTerritories() {
		ArrayList<Territory> territories = new ArrayList<Territory>();
		for (Territory t : game.territories) {
			if (t.player.equals(this)) {
				territories.add(t);
			}
		}
		return territories;
	}
}
