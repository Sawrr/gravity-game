package com.gravitygame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class GameCamera extends OrthographicCamera {
	public void clampPos(int worldWidth, int worldHeight) {
		float effectiveViewportWidth = viewportWidth * zoom;
		float effectiveViewportHeight = viewportHeight * zoom;
		position.x = MathUtils.clamp(position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
		position.y = MathUtils.clamp(position.y, effectiveViewportHeight / 2f, worldHeight - effectiveViewportHeight / 2f);
	}
	
	public void center(float x, float y, float zm, int worldWidth, int worldHeight) {
		position.x = x;
		position.y = y;
		zoom = zm;
		clampPos(worldWidth, worldHeight);
	}
	
	public void centerOnWorld(GameScreen screen) {
		int worldWidth = screen.worldWidth;
		int worldHeight = screen.worldHeight;
		float x = screen.worldWidth / 2;
		float y = screen.worldHeight / 2;
		float zm = Math.min(worldWidth/viewportWidth, worldHeight/viewportHeight) / 1.5f;
		center(x, y, zm, worldWidth, worldHeight);
	}
	
	public void centerOnShip(GameScreen screen) {
		int worldWidth = screen.worldWidth;
		int worldHeight = screen.worldHeight;
		float x = screen.gameMap.ship.pos.x;
		float y = screen.gameMap.ship.pos.y + 200;
		float zm = 1f;
		center(x, y, zm, worldWidth, worldHeight);
	}
}
