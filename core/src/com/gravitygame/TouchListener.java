package com.gravitygame;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TouchListener implements GestureListener {

	GameScreen screen;
	
	public TouchListener(GameScreen gameScreen) {
		this.screen = gameScreen;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Vector3 screenPos = new Vector3(x, y, 0);
		screen.tap(screenPos);
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		screen.pan(x, y, -deltaX, deltaY);
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		screen.panStop(x, y);
		return true;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		float zoomDistance = (float) -0.0001 * (distance - initialDistance);
		screen.zoom(zoomDistance);
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
