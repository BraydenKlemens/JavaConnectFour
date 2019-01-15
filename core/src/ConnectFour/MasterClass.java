package ConnectFour;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MasterClass implements Screen, InputProcessor {
	
	//CONFIG.WIDTH = 800

	// variables
	public Launcher game;
	private Tile grid[][] = new Tile[6][7];
	private int x = 0;
	private int y = 0;
	private Sprite sprite;
	private boolean turn = true;
	private String winner = " ";
	// display
	private BitmapFont font = new BitmapFont();
	private BitmapFont restart = new BitmapFont();
	// Array list
	private ArrayList<Tile> lastMoves = new ArrayList<Tile>();

	public MasterClass(Launcher game) {
		this.game = game;
	}

	@Override
	public void show() {

		sprite = new Sprite(new Texture("Connect4/Connect4Board.png"));
		sprite.setSize(560, 480);

		// creates a grid of tiles starting in bottom left corner
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				Tile tile = new Tile(x, y, i, j);
				grid[i][j] = tile;
				x += 80;
			}
			x = 0;
			y += 80;
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// sets font size
		font.getData().setScale(2, 2);
		// method calls
		clearBoard();
		moveChips();
		draw();
	}

	public void draw() {
		game.batch.begin();

		// draw chips
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j].draw(game.batch);
			}
		}

		// displays which player turn it is
		if (turn) {
			font.draw(game.batch, "Blue's Turn", 580, 400);
		} else {
			font.draw(game.batch, "Red's Turn", 580, 400);
		}

		// draws the last moves
		int y = 200;
		for (Tile move : lastMoves) {
			font.draw(game.batch, "Last Move: " + move.getRow() + "," + move.getColumn(), 580, y);
			y += 30;
		}

		restart.draw(game.batch, "Press SPACE to reset game", 580, 100);
		font.draw(game.batch, "Winner: " + winner, 580, 150);

		sprite.draw(game.batch);

		game.batch.end();
	}

	public void clearBoard() {
		// remove all chips from board
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].hasChip()) {
					if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
						grid[i][j].setChip(null);
						winner = " ";
					}
				}
			}
		}
	}

	public void moveChips() {
		// moves chips down to a grid location then stops
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].hasChip()) {
					grid[i][j].getChip().translate(grid[i][j]);
				}
			}
		}
	}

	public void detectWins() {
		boolean stop = false;
		int num = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// column check
				for (int k = 1; k <= 3; k++) {
					if (grid[i][j].hasChip() && !stop) {
						if (checkChip(i + k, j, grid[i][j].getChip().getType())) {
							num++;
						} else {
							stop = true;
						}
					}
				}
				Winner(num, i, j);
				// row check
				stop = false;
				num = 0;
				for (int k = 1; k <= 3; k++) {
					if (grid[i][j].hasChip() && !stop) {
						if (checkChip(i, j + k, grid[i][j].getChip().getType())) {
							num++;
						} else {
							stop = true;
						}
					}
				}
				Winner(num, i, j);
				// diagonal up right (from left to right)
				stop = false;
				num = 0;
				for (int k = 1; k <= 3; k++) {
					if (grid[i][j].hasChip() && !stop) {
						if (checkChip(i + k, j + k, grid[i][j].getChip().getType())) {
							num++;
						} else {
							stop = true;
						}
					}
				}
				Winner(num, i, j);
				// diagonal down right (from left to right)
				stop = false;
				num = 0;
				for (int k = 1; k <= 3; k++) {
					if (grid[i][j].hasChip() && !stop) {
						if (checkChip(i + k, j - k, grid[i][j].getChip().getType())) {
							num++;
						} else {
							stop = true;
						}
					}
				}
				Winner(num, i, j);
				stop = false;
				num = 0;

			}
		}
	}

	/**
	 * @param num
	 * @param i
	 * @param j
	 */
	public void Winner(int num, int i, int j) {
		if (num > 2) {
			if (grid[i][j].getChip().getType() == 0) {
				winner = "Blue";
				return;
			} else if (grid[i][j].getChip().getType() == 1) {
				winner = "Red";
				return;
			}
		}
	}

	/**
	 * @param i
	 * @param j
	 * @param type
	 * @return
	 */
	public boolean checkChip(int i, int j, int type) {
		if (grid.length > i && grid[i].length > j && i >= 0 && j >= 0 && grid[i][j].hasChip()
				&& grid[i][j].getChip().getType() == type) {
			return true;
		}
		return false;
	}

	public void lastMoves() {
		// first in first out - keeps size at 4
		if (lastMoves.size() > 4) {
			lastMoves.remove(0);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// checks the column mouse is in
				if (grid[i][j].getX() < screenX && screenX < grid[i][j].getX() + grid[i][j].getRectangle().getWidth()) {
					// if no chip
					if (grid[i][j].hasChip() == false) {
						// blues turn
						if (turn) {
							/*
							 * places blue chip at grid location, adds that
							 * location to array list, keeps array size at 4, only
							 * detect wins when chips are placed, sets turn false
							 * so red can go next
							 */
							grid[i][j].setChip(new BlueChip(grid[i][j]));
							lastMoves.add(grid[i][j]);
							lastMoves();
							detectWins();
							turn = false;
							return true;
						} else {
							/*
							 * places red chip at grid location, adds that
							 * location to array list, keeps array size at 4, only
							 * detect wins when chips are placed, sets turn true
							 * so blue can go again
							 */
							grid[i][j].setChip(new RedChip(grid[i][j]));
							lastMoves.add(grid[i][j]);
							lastMoves();
							detectWins();
							turn = true;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
