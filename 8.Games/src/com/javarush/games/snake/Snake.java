package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class Snake {
	private static final String HEAD_SIGN = "\uD83D\uDC7E";
	private static final String BODY_SIGN = "\u26AB";
	public boolean isAlive = true;
	private List<GameObject> snakeParts = new ArrayList<>();

	private Direction direction = Direction.LEFT;

	Snake(int x, int y) {
		for (int i = 0; i < 3; i++) {
			snakeParts.add(new GameObject(x + i, y));
		}
	}

	public void setDirection(Direction direction) {
		if (this.direction == Direction.RIGHT && direction == Direction.LEFT ||
				this.direction == Direction.LEFT && direction == Direction.RIGHT ||
				this.direction == Direction.UP && direction == Direction.DOWN ||
				this.direction == Direction.DOWN && direction == Direction.UP ||
				((this.direction == Direction.LEFT || this.direction == Direction.RIGHT)
						&& snakeParts.get(0).x == snakeParts.get(1).x) ||
				((this.direction == Direction.UP || this.direction == Direction.DOWN)
						&& snakeParts.get(0).y == snakeParts.get(1).y)) {
			return;
		}
		this.direction = direction;
	}

	public void draw(Game game) {
		for (GameObject part : snakeParts) {
			if (snakeParts.lastIndexOf(part) == 0) {
				game.setCellValueEx(part.x, part.y, Color.NONE, HEAD_SIGN,
						isAlive ? Color.BLACK : Color.RED, 75);
			} else {
				game.setCellValueEx(part.x, part.y, Color.NONE, BODY_SIGN,
						isAlive ? Color.BLUE : Color.RED, 75);
			}
		}
	}

	public void move(Apple apple) {
		GameObject newTile = createNewHead();
		if (newTile.x < 0 || newTile.x >= SnakeGame.WIDTH ||
				newTile.y < 0 || newTile.y >= SnakeGame.HEIGHT ||
				checkCollision(newTile)
		) {
			this.isAlive = false;
			return;
		} else {
			snakeParts.add(0, newTile);
			if (newTile.x == apple.x && newTile.y == apple.y) {
				apple.isAlive = false;
			} else {
				removeTail();
			}
		}
	}

	public GameObject createNewHead() {
		GameObject newPart;
		int headX = snakeParts.get(0).x;
		int headY = snakeParts.get(0).y;

		switch (direction) {
			case LEFT:
				newPart = new GameObject(headX - 1, headY);
				break;
			case RIGHT:
				newPart = new GameObject(headX + 1, headY);
				break;
			case UP:
				newPart = new GameObject(headX, headY - 1);
				break;
			case DOWN:
				newPart = new GameObject(headX, headY + 1);
				break;
			default:
				newPart = null;
		}
		return (newPart);
	}

	public void removeTail() {
		snakeParts.remove(snakeParts.size() - 1);
	}

	public boolean checkCollision(GameObject head) {
		for (GameObject tile : snakeParts) {
			if (tile.y == head.y && tile.x == head.x) {
				return (true);
			}
		}
		return (false);
	}

	public int getLength() {
		return (snakeParts.size());
	}
}