package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class SnakeGame extends Game {
	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	private static final int GOAL = 28;
	private int score;
	private Snake snake;
	private Apple apple;
	private int turnDelay;
	private boolean isGameStopped;

	private void createGame() {
		snake = new Snake(WIDTH / 2, HEIGHT / 2);
		score = 0;
		turnDelay = 300;
		isGameStopped = false;
		setTurnTimer(turnDelay);
		setScore(score);
		createNewApple();
		drawScene();
	}

	private void gameOver() {
		stopTurnTimer();
		isGameStopped = true;
		showMessageDialog(Color.NONE, "GAME OVER", Color.RED, 72);
	}

	private void win() {
		stopTurnTimer();
		isGameStopped = true;
		showMessageDialog(Color.NONE, "YOU WIN", Color.GREEN, 72);
	}

	private void drawScene() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				setCellValueEx(x, y, Color.GRAY, "", Color.NONE);
			}
		}
		snake.draw(this);
		apple.draw(this);
	}

	private void createNewApple() {
		Apple appleTest;

		do {
			appleTest = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
		} while (snake.checkCollision(appleTest));
		this.apple = appleTest;
	}

	@Override
	public void initialize() {
		setScreenSize(WIDTH, HEIGHT);
		createGame();
	}

	@Override
	public void onTurn(int delay) {
		snake.move(apple);
		if (snake.getLength() > GOAL) {
			win();
		}
		if (!apple.isAlive) {
			score += 5;
			turnDelay -= 10;
			setTurnTimer(turnDelay);
			setScore(score);
			createNewApple();
		}
		if (!snake.isAlive) {
			gameOver();
		}
		drawScene();
	}

	@Override
	public void onKeyPress(Key key) {
		switch (key) {
			case LEFT:
				snake.setDirection(Direction.LEFT);
				break;
			case RIGHT:
				snake.setDirection(Direction.RIGHT);
				break;
			case UP:
				snake.setDirection(Direction.UP);
				break;
			case DOWN:
				snake.setDirection(Direction.DOWN);
				break;
			case SPACE:
				if (isGameStopped) {
					createGame();
				}
			default:
		}
	}
}

