package ConnectFour;

import com.badlogic.gdx.graphics.Texture;

public class RedChip extends Chip {

	/**
	 * @param tile
	 */
	public RedChip(Tile tile) {
		super(new Texture("Connect4/RedTeam.png"), tile);
	}
	@Override
	public int getType() {
		return 1;
	}

}
