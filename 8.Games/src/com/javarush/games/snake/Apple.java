package com.javarush.games.snake;

import com.javarush.engine.cell.*;
import java.lang.*;

public class Apple extends GameObject {
	private static final String APPLE_SIGN = "\uD83C\uDF4E";

	public boolean isAlive = true;

	Apple(int x, int y) {
		super(x, y);
	}

	public void draw(Game game) {
		game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.LAWNGREEN, 75);
	}

}
