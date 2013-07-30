package risk;

import java.util.ArrayList;

public class Territory {

	public String name;
	public int locX;
	public int locY;
	public int armies;
	public Player player;
	public ArrayList<Territory> adjacents = new ArrayList<Territory>();
	
	public Territory (String name, int x, int y) {
		this.name = name;
		this.locX = x;
		this.locY = y;
		this.armies = 1;
	}
	
	public String toString() {
		return name;
	}
	

}
