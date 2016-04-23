package com.sawyerharris.gravitygame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class GameCamera extends OrthographicCamera {

	private int worldWidth;
	private int worldHeight;
	
	public GameCamera(Level level) {
		worldWidth = level.getWidth();
		worldHeight = level.getHeight();
	}
	
	public void clamp() {
		float effectiveViewportWidth = viewportWidth * zoom;
		float effectiveViewportHeight = viewportHeight * zoom;
		position.x = MathUtils.clamp(position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
		position.y = MathUtils.clamp(position.y, effectiveViewportHeight / 2f, worldHeight - effectiveViewportHeight / 2f);
	}

}
