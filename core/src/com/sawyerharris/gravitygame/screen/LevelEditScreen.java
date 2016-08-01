package com.sawyerharris.gravitygame.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class LevelEditScreen extends LevelScreen {
	/** Level name that will be applied if the user saves */
	private String customLevelName;
	
	public LevelEditScreen(Batch batch, ShapeRenderer renderer, int worldWidth, int worldHeight) {
		super(batch, renderer, worldWidth, worldHeight);
		getCamera().setZoom(0.5f);
	}

	@Override
	public void pan(float x, float y, float deltaX, float deltaY) {
		getCamera().translate(new Vector2(-deltaX, deltaY).scl(0.75f));
		getCamera().stopAutoMove();
		getCamera().stopAutoZoom();
	}

	@Override
	public void zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyDown(int keycode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrolled(int amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		
	}
	
	public void testLevel() {
		
	}
	
	public void saveLevel() {
		
	}
	
	public void uploadLevel() {
		
	}
}
