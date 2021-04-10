package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
	private GameObject[][] gameField = new GameObject[SIDE][SIDE];
	private int countClosedTiles = SIDE * SIDE;
	private int countMinesOnField;
	private int countFlags;
	private int score;

	private boolean isGameStopped;

	private static final int SIDE = 9;
	private static final String MINE = "\uD83D\uDCA3";
	private static final String FLAG = "\uD83D\uDEA9";

	@Override
	public void initialize() {
		setScreenSize(SIDE, SIDE);
		createGame();
	}

	@Override
	public void onMouseLeftClick(int x, int y) {
		if (isGameStopped) {
			restart();
			return;
		}
		openTile(x, y);
	}

	@Override
	public void onMouseRightClick(int x, int y) {
		markTile(x, y);
	}

	private void createGame() {
		for (int y = 0; y < SIDE; y++) {
			for (int x = 0; x < SIDE; x++) {
				boolean isMine = getRandomNumber(10) < 1;
				if (isMine) {
					countMinesOnField++;
				}
				gameField[y][x] = new GameObject(x, y, isMine);
				setCellColor(x, y, Color.MEDIUMAQUAMARINE);
				setCellValue(x, y, "");
			}
		}
		countMineNeighbors();
		countFlags = countMinesOnField;
	}

	private void restart() {
		isGameStopped = false;
		score = 0;
		countClosedTiles = SIDE * SIDE;
		countMinesOnField = 0;
		setScore(score);
		createGame();
	}

	private List<GameObject> getNeighbors(GameObject gameObject) {
		List<GameObject> result = new ArrayList<>();
		for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
			for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
				if (y < 0 || y >= SIDE) {
					continue;
				}
				if (x < 0 || x >= SIDE) {
					continue;
				}
				if (gameField[y][x] == gameObject) {
					continue;
				}
				result.add(gameField[y][x]);
			}
		}
		return result;
	}

	private void countMineNeighbors() {
		for (int y = 0; y < SIDE; y++) {
			for (int x = 0; x < SIDE; x++) {
				GameObject gameObject = gameField[y][x];
				if (!gameObject.isMine) {
					for (GameObject neighbor : getNeighbors(gameObject)) {
						if (neighbor.isMine) {
							gameObject.countMineNeighbors++;
						}
					}
				}
			}
		}
	}

	private void openTile(int x, int y) {
		GameObject gameObject = gameField[y][x];
		if (isGameStopped || gameObject.isFlag || gameObject.isOpen) {
			return;
		}
		setCellColor(x, y, Color.GREEN);
		gameObject.isOpen = true;
		if (!gameObject.isMine) {
		    score += 5;
        }
		countClosedTiles--;
		if (countClosedTiles == countMinesOnField && !gameObject.isMine) {
			win();
		}
		if (gameObject.isMine) {
			setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);
			gameOver();
		} else if (gameObject.countMineNeighbors == 0) {
			setCellValue(gameObject.x, gameObject.y, "");
			List<GameObject> neighbors = getNeighbors(gameObject);
			for (GameObject neighbor : neighbors) {
				if (!neighbor.isOpen) {
					openTile(neighbor.x, neighbor.y);
				}
			}
		} else {
			setCellNumber(x, y, gameObject.countMineNeighbors);
		}
		setScore(score);
	}

	private void win() {
		isGameStopped = true;
		showMessageDialog(Color.GREEN, "YOU WIN!", Color.AQUAMARINE, 24);
	}

	private void markTile(int x, int y) {
		GameObject gameObject = gameField[y][x];
		if (!gameObject.isOpen && countFlags != 0 && !isGameStopped) {
			if (!gameObject.isFlag && countFlags > 0) {
				gameObject.isFlag = true;
				countFlags--;
				setCellValue(gameObject.x, gameObject.y, FLAG);
				setCellColor(gameObject.x, gameObject.y, Color.INDIGO);
			} else {
				gameObject.isFlag = false;
				countFlags++;
				setCellValue(gameObject.x, gameObject.y, "");
				setCellColor(gameObject.x, gameObject.y, Color.MEDIUMAQUAMARINE);
			}
		}
	}

	private void gameOver() {
		this.isGameStopped = true;
		showMessageDialog(Color.RED, "GameOver", Color.PALEGREEN, 24);
	}
}

