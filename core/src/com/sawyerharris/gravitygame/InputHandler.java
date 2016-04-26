package com.sawyerharris.gravitygame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.sawyerharris.gravitygame.GravityGame.GameState;

public class InputHandler extends GestureAdapter {
	private static final float TOUCH_TO_ZOOM = 0.0001f;
	private GameCamera camera;
	private Screen screen;
	
	public InputHandler(Screen screen, GameCamera camera) {
		this.camera = camera;
		this.screen = screen;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (screen instanceof GameScreen && GravityGame.getState() == GameState.VIEW_MOVING) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateAiming();
		}
		camera.pan(-deltaX, deltaY);
		return true;
	}
	
	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (screen instanceof GameScreen && GravityGame.getState() == GameState.VIEW_MOVING) {
			GameScreen gs = (GameScreen) screen;
			gs.setStateAiming();
		}
		float zoomDistance = TOUCH_TO_ZOOM * (initialDistance - distance);
		camera.zoom(zoomDistance);
		return true;
	}
	
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		System.out.println(count);
		if (count >= 0) {
			if (screen instanceof GameScreen && GravityGame.getState() == GameState.AIMING) {
				GameScreen gs = (GameScreen) screen;
				gs.setStateViewMoving();
			}	
		}
		return true;
	}
}