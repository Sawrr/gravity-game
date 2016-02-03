package com.gravitygame;

import com.badlogic.gdx.InputProcessor;

public class DesktopListener implements InputProcessor {
	
	GameScreen screen;
	
	public DesktopListener(GameScreen gameScreen) {
		this.screen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screen.touchDown();
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screen.touchUp();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoomDistance = (float) 0.05 * amount;
		screen.zoom(zoomDistance);
		return true;
	}
}
